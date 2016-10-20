/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.sockets.servers.main

import com.logosprog.mainutils.sockets.servers.server.SocketManager
import java.net.ServerSocket
import java.net.Socket

/**
 * Created by forando on 15.06.15.
 * This class provides basic server functionality.
 * It's generalized by a predefined object that must
 * implement [SocketManager1] interface.
 */
class Server<E : SocketManager>
/**
 * This constructor must be used in production
 * @param socketManager An object that implements [SocketManager1] interface
 * *
 * @param port The port this server will be listening to.
 */
@JvmOverloads constructor(
        /**
         * Call this method to get [E] object.
         * @return The object of generalized, during this class
         * * construction, type
         */
        var generalizedObject: E, internal var PORT:

        Int = 1337) {

    private var serverAcceptor: ServerAcceptor? = null

    // Server thread accepts incoming client connections
    inner class ServerAcceptor internal constructor(internal var port: Int) : Thread() {
        @Volatile private var myThread: Thread? = null

        init {
            this.name = "ServerAcceptor"
            myThread = this
        }

        fun stopThread() {
            val tmpThread = myThread
            myThread = null
            tmpThread?.interrupt()
        }

        override fun run() {

            if (myThread == null) {
                return  // stopped before started.
            }
            try {
                val serverSocket = ServerSocket(port)
                while (true) {
                    val socket: Socket
                    // this blocks, waiting for a Socket to the client
                    socket = serverSocket.accept()
                    println("server: got client")
                    generalizedObject.accept(socket)
                    Thread.`yield`() // let another thread have some time perhaps to stop this one.
                    if (Thread.currentThread().isInterrupted) {
                        throw InterruptedException("Stopped by ifInterruptedStop()")
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }
    }

    /**
     * Runs the [ServerAcceptor] to catch incoming client connections
     */
    fun start() {
        println("server: Running on port " + PORT)
        serverAcceptor = ServerAcceptor(PORT)
        serverAcceptor!!.start()
    }

    /**
     * Stops the server.
     */
    fun stop() {
        generalizedObject.closeAll()
        serverAcceptor!!.stopThread()
        println("server: stopped")
    }
}
/**
 * This constructor is for test only. Here server port is set to **1337**
 * @param socketManager An object that implements [SocketManager] interface
 */