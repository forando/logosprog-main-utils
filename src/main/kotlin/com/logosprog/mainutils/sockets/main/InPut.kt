/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.sockets.main

import java.io.IOException
import java.io.ObjectInputStream
import java.net.Socket
import java.nio.channels.Channels
import java.util.*

/**
 * Created by forando on 15.06.15.
 * This class uses socket to listen for input messages
 */
class InPut(private val socket: Socket, id: Int) : Thread() {
    @Volatile private var myThread: Thread? = null
    //    private int id;
    internal var input: ObjectInputStream? = null

    //    ExecutorService executor = Executors.newFixedThreadPool(5);

    internal var listeners: MutableList<InputListener>

    init {
        this.name = THREAD_NAME
        myThread = this
        //        this.id = id;
        listeners = ArrayList<InputListener>()
        this.input = null
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
            //this.in = new ObjectInputStream(this.socket.getInputStream());
            val channel = Channels.newChannel(socket.inputStream)
            input = ObjectInputStream(Channels.newInputStream(channel))
            while (true) {
                //get object from server, will block until object arrives.
                val messageObject = input!!.readObject()
                for (l in listeners) {
                    l.onMessage(messageObject)
                }
                //executor.submit(new MessageTransmitter(messageObject));

                Thread.`yield`() // let another thread have some time perhaps to stop this one.
                if (Thread.currentThread().isInterrupted) {
                    //                    executor.shutdown();
                    if (input != null)
                        try {
                            input!!.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    throw InterruptedException("Socket: Stopped by ifInterruptedStop()")
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()

            if (input != null)
                try {
                    input!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            for (l in listeners) {
                l.onClose(socket)
            }
        }

    }

    fun addInputListener(listener: InputListener) {
        listeners.add(listener)
    }

    interface InputListener {
        fun onMessage(messageObject: Any)
        fun onClose(socket: Socket)
    }

    companion object {
        val THREAD_NAME = "SocketInPut"
    }


}
