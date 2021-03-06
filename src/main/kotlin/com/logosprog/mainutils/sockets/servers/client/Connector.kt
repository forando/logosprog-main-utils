/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

@file:Suppress("unused", "MemberVisibilityCanPrivate")

package com.logosprog.mainutils.sockets.servers.client
import com.logosprog.mainutils.sockets.servers.main.Client
import com.logosprog.mainutils.sockets.servers.main.ClientBean
import com.logosprog.mainutils.system.printInfoMessage
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * Created by forando on 15.06.15.
 * This class provides an app with a [Client] object to be able to talk to HostServer
 */
class Connector
/**
 * This constructor must be used in **production**.
 * @param type The type of a client that wants to be connected to the server
 * @param id The clients id
 * @param IP The server IP address
 * @param PORT The server PORT
 */
@JvmOverloads constructor(
        /**
         * A TYPE of the client.
         * Defined by project that uses this class
         */
        internal var type: Int, internal var id: Int, internal var IP:

        String = "localhost", internal var PORT: Int = 1337) {

    private val tag: String = this.javaClass.simpleName

    internal var listener: ConnectorListener? = null


    /**
     * The quantity of restart attempts after socket closed event.
     */
    private var restartsQuant = 0

    private val executorClientStarter: ExecutorService = Executors.newSingleThreadExecutor()
    private var futureClientStarter: Future<Boolean>? = null


    fun startConnection() {
        stopConnection()
        Client.id = id
        Client.hostName = IP
        Client.port = PORT
        Client.type = type
//        client = Client.getInstance(IP, PORT, type, id)
        futureClientStarter = executorClientStarter.submit(ClientStarter())
    }

    fun stopConnection() {
        if (futureClientStarter != null) futureClientStarter!!.cancel(true)
    }

    private fun restartClient() {
        stopConnection()
        startConnection()
    }

    fun addConnectorListener(listener: ConnectorListener) {
        this.listener = listener
    }

    interface ConnectorListener {
        fun onClientConnected(serverClient: Client)
    }

    private inner class ClientStarter : Callable<Boolean> {

        @Throws(Exception::class)
        override fun call(): Boolean? {
            var bean: ClientBean?
            // let another thread have some time perhaps to stop this one:
            Thread.`yield`()
            if (Thread.currentThread().isInterrupted) {
                return false
            }
            restartsQuant++
            printInfoMessage(tag + ".ClientStarter.call: " + restartsQuant +
                    " Attempt to get connected to the Server!!!")
            bean = Client.startInTheSameThread()
            while (null == bean) {
                // let another thread have some time perhaps to stop this one:
                Thread.`yield`()
                if (Thread.currentThread().isInterrupted) {
                    return false
                }
                try {
                    Thread.sleep(delay.toLong())
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                // let another thread have some time perhaps to stop this one:
                Thread.`yield`()
                if (Thread.currentThread().isInterrupted) {
                    return false
                }
                restartsQuant++
                printInfoMessage(tag + ".ClientStarter.call: " + restartsQuant +
                        " Attempt to get connected to the Server!!!")
                bean = Client.startInTheSameThread()
            }
            listener?.onClientConnected(Client)
            return true
        }
    }

    companion object {

        /**
         * An instance of this class. Realizes singleton pattern.
         */
        private var connector: Connector? = null
        /**
         * delay between two separate attempts to to obtain [Client] object
         */
        private val delay = 2000

        /**
         * This method must be used in **production**.
         * Realizes singleton pattern.
         * @param type The type of a client that wants to be connected to the server
         * *
         * @param id The clients id
         * *
         * @param ip The server IP
         * *
         * @param port The server PORT
         * *
         * @return A new instance of the class. Realizes singleton pattern.
         */
        fun getInstance(type: Int, id: Int, ip: String, port: Int): Connector {
            if (connector == null) {
                connector = Connector(type, id, ip, port)
            }
            return connector as Connector
        }

        /**
         * This method is just for **testing**. Here we use predefined IP an PORT
         * @param type The type of a client that wants to be connected to the server
         * *
         * @param id The clients id
         * *
         * @return A new instance of the class. Realizes singleton pattern.
         */
        fun getInstance(type: Int, id: Int): Connector {
            if (connector == null) {
                connector = Connector(type, id, "localhost", 1337)
            }
            return connector as Connector
        }
    }
}
