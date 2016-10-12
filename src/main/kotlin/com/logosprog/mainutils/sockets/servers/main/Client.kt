/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.sockets.servers.main

import com.logosprog.mainutils.sockets.servers.server.CommunicationNodeListener
import com.logosprog.mainutils.sockets.servers.server.CommunicationNodeValidatorListener
import com.logosprog.mainutils.system.printInfoMessage
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket
import java.nio.channels.Channels

/**
 * Provides communication with Server for any client
 * that uses it.
 */
object client: CommunicationNode<client.ClientBean, CommunicationNodeListener, CommunicationNodeValidatorListener<client.ClientBean>>(){

    /**
     * An IP address of the server host.
     */
    var hostName: String = ""
    /**
     * A port to communicate with the server.
     */
    var port: Int = 0
    /**
     * A Client TYPE to be registered with.
     */
    var type: Int = 0
    var _id: Int = 0

    private val TAG: String = "Client"
    var socket: Socket? = null
        private set

    /**
     * An ID to be registered with.
     */
    var id: Int
        get() = _id
        set(value){
            registered = true
            if (_id != value)
                _id = value
            printInfoMessage(TAG + ".setId(): Client registered with ID = " + id)
        }

    class ClientBean(val socket: Socket?)

    /**
     * Defines if this client has been registered by the server with an id.
     */
    private var registered = false

    override fun makeBean(socket: Socket?): ClientBean? {
        /*
        outBuffer[0]: 1 - this client talks using serializable Objects, 0 - talks with bytes only
        outBuffer[1]: type of a client (printer, terminal, display etc.)
        outBuffer[2]: client id
         */
        val outBuffer = byteArrayOf(0x01, type.toByte(), id.toByte())

        val output = ObjectOutputStream(socket?.outputStream)
        output.write(outBuffer)
        output.flush()

        val channel = Channels.newChannel(socket?.inputStream)
        val input = ObjectInputStream(Channels.newInputStream(channel))

        // let another thread have some time perhaps to stop this one:
        Thread.`yield`()
        //interruption check:
        if (Thread.currentThread().isInterrupted) {
            return null
        }

        /*
        inputBuffer[0]: 1 - this client talks using serializable Objects, 0 - talks with bytes only
        inputBuffer[1]: type of a client (printer, terminal, display etc.)
        inputBuffer[2]: client id
         */
        val inputBuffer = ByteArray(3)
        val value = input.read(inputBuffer)
        if (value > 0 && inputBuffer[0].toInt() == 0x01 && inputBuffer[2] >= 0) {
            id = inputBuffer[2].toInt()
            return ClientBean(socket)
        } else {
            close(socket)
            return null
        }
    }

    override fun makeSocket(): Socket {
        return Socket(hostName, port)
    }

    override fun ready(): Boolean {
        return isReady && socket != null
    }

    override fun beanIsValid(bean: ClientBean?): Boolean {
        if (null != bean && null != bean.socket) {
            this.socket = bean.socket
            isReady = true
            return true
        } else {
            isReady = false
            return false
        }
    }

    fun finalize() {
        close(socket)
    }

}
