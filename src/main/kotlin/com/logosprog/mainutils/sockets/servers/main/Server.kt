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
 * implement [SocketManager] interface.
 */
class Server<E : SocketManager>
/**
 * This constructor must be used in production
 * @param generalizedObject An object that implements [SocketManager] interface
 * *
 * @param PORT The port this server will be listening to.
 */
@JvmOverloads constructor(var generalizedObject: E, internal var PORT: Int = 1337) {

    private var serverAcceptor: ServerAcceptor? = null

    // Server thread accepts incoming client connections
    inner class ServerAcceptor internal constructor(internal var serverSocket: ServerSocket) : Thread() {
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
                println("server: stopped before running")
                return  // stopped before started.
            }

            try {
                println("server: Running on port " + serverSocket.localPort)
                while (true) {
                    val socket: Socket
                    // this blocks, waiting for a Socket to the client
                    socket = serverSocket.accept()
                    println("server: got client")
                    generalizedObject.accept(socket)
                    Thread.`yield`() // let another thread have some time perhaps to stop this one.
                    if (Thread.currentThread().isInterrupted) {
//                        throw InterruptedException("Stopped by ifInterruptedStop()")
                        println("server: stopped")
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
    fun start(): Boolean {
        try{
            serverAcceptor = ServerAcceptor(ServerSocket(PORT))
            serverAcceptor!!.start()
            return true
        }catch (ex: Exception){
//            ex.printStackTrace()
            return false
        }
    }

    /**
     * Stops the server.
     */
    fun stop() {
        generalizedObject.closeAll()
        serverAcceptor?.stopThread()
    }
}