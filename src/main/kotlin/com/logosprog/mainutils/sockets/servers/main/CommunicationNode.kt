/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

@file:Suppress("MemberVisibilityCanPrivate", "unused")

package com.logosprog.mainutils.sockets.servers.main

import com.logosprog.mainutils.sockets.main.InPut
import com.logosprog.mainutils.sockets.main.OutPut
import com.logosprog.mainutils.sockets.servers.server.CommunicationNodeListener
import com.logosprog.mainutils.sockets.servers.server.CommunicationNodeValidatorListener
import com.logosprog.mainutils.system.printInfoMessage
import java.io.IOException
import java.io.ObjectOutputStream
import java.net.Socket
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by forando on 02.12.15.
 * Provides communication between two TCP/IP sockets.
 */
abstract class CommunicationNode<B, in L : CommunicationNodeListener, V : CommunicationNodeValidatorListener<B>> {

    private val tag: String = this.javaClass.simpleName

    /**
     * Defines if this client is connected to the server and ready to
     * communicate with it.
     */
    @Volatile protected var ready = false

    protected var out: ObjectOutputStream? = null
    protected var inPut: InPut? = null
    protected val lock: Any = Any()

    private var socketListener: L? = null
    /**
     * Serves this client validation process.
     */
    private var validatorExecutor: ExecutorService? = null
    /**
     * Serves output messages sending.
     */
    private val outputMessagesExecutor: ExecutorService

    init {

        validatorExecutor = Executors.newSingleThreadExecutor()
        outputMessagesExecutor = Executors.newSingleThreadExecutor()
    }

    /**
     * Starts validation process in a new thread.
     * @param listener A listener to notify about validation.
     */
    fun startInDifferentThread(listener: V) {
        if (ready()) {
            printInfoMessage(tag + ".startInDifferentThread(): The client has already " +
                    "been started. This start is ignored")
        } else {
            if (validatorExecutor == null || validatorExecutor!!.isShutdown)
                validatorExecutor = Executors.newSingleThreadExecutor()
            validatorExecutor!!.submit(Validator(listener))
        }
    }

    /**
     * Starts validation process in the same thread.
     * @return data BEAN - if validation is successful, and NULL - if not.
     */
    open fun startInTheSameThread(): B? {
        if (ready()) {
            printInfoMessage(tag + ".startInTheSameThread(): The client has already" +
                    " been started. This start is ignored")
            return null
        } else {
            val bean = bean
            if (beanIsValid(bean))
                return bean
            return null
        }
    }

    /**
     * Tries to connect to the remote socket.
     * If it fails - NULL is returned.
     * @return data bean or NULL.
     */
    protected val bean: B?
        get() = synchronized(lock) {
            return try {
                val socket = makeSocket()
                if (null != socket) {
                    printInfoMessage(tag + ".getBean: Got new socket.")
                    makeBean(socket)
                } else {
                    null
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                notifyClose()
                null
            }

        }


    /**
     * Tries to connect to the remote socket.
     * If it fails - NULL is returned.
     * @param socket A socket to make a bean
     * *
     * @return Bean or NULL.
     * *
     * @throws IOException If the operation was not successful
     */
    @Throws(IOException::class)
    protected abstract fun makeBean(socket: Socket?): B?

    @Throws(IOException::class)
    protected abstract fun makeSocket(): Socket?

    /**
     * Checks if the node is ready for use.
     * @return TRUE - if ready, FALSE - if not.
     */
    open fun ready(): Boolean {
        return ready
    }

    /**
     * During validation process some exceptions might occur.
     * In this case NULL will be returned.
     * This method ensures that we've received the valid results.
     * It, also, can init some additional objects that are necessary
     * for communication between sockets.
     * @param bean A data bean (specific for each project).
     * *
     * @return TRUE - if it's valid or FALSE - if not.
     */
    abstract fun beanIsValid(bean: B?): Boolean


    /**
     * Opens the sockets input and output streams in order to
     * communicate with the server.
     * @param listener A listener to notify about received messages.
     * *
     * @param socket A socket to open stream from
     * *
     * @param id An id to be assigned for a new connection
     * *
     * @return TRUE - if all streams have been opened successfully.
     */
    fun openSocketStreams(listener: L, socket: Socket, id: Int): Boolean {
        try {
            out = ObjectOutputStream(socket.outputStream)
            /*if (out == null){
                ready = false;
                return false;
            }*/
        } catch (e: IOException) {
            e.printStackTrace()
            ready = false
            return false
        }

        addClientListener(listener)

        inPut = InPut(socket, id)
        inPut!!.addInputListener(object : InPut.InputListener {
            override fun onMessage(messageObject: Any) {
                transferMessage(messageObject)
            }

            override fun onClose(socket: Socket) {
                close(socket)
            }
        })
        inPut!!.start()
        return true
    }


    /**
     * Closes the sockets input and output streams.
     * @param socket A socket to be closed
     */
    protected fun closeSocketStreams(socket: Socket?) {
        try {
            if (inPut != null) inPut!!.stopThread()
            if (out != null) out!!.close()
            socket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    /**
     * Breaks the communication with remote socket.
     * @param socket Local socket.
     */
    fun shutDownNode(socket: Socket) {
        /*
        IMPORTANT!!!
        After onCloseSocket() event is fired, we have to remove this listener.
        This listener will be reassigned again after the client is reconnected.
         */
        removeClientListener()
        close(socket)
    }

    /**
     * Notifies listener about socket close event.
     */
    protected fun notifyClose() {
        if (socketListener != null) {
            socketListener!!.onCloseSocket()
            /*
            IMPORTANT!!!
            After onCloseSocket() event is fired, we have to remove this listener.
            This listener will be reassigned again after the client is reconnected.
             */
            removeClientListener()
        }
    }

    fun close(socket: Socket?) {
        /*
        * We need the lock here to be sure that the next Client.getInstance() will
        * return a new Client object.
        * That's why we have to guarantee that this
        * method is finished before the next Client.getInstance() call.
        * */
        synchronized(lock) {
            if (inPut != null) inPut!!.stopThread()
            if (validatorExecutor != null) validatorExecutor!!.shutdownNow()
            try {
                //bug: if this block kicks out a NullPointer exception this shuts down android app
                if (out != null) out!!.close()
                socket?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                ready = false
                notifyClose()
            }
        }
    }

    private fun transferMessage(obj: Any) {

        if (socketListener != null) socketListener!!.onInputMessage(obj)
    }

    fun send(messageObject: Any) {
        /*
        Not sure if we need this lock here.
        Did it just to have 100% guarantee
         */
        synchronized(lock) {
            //bug: Sometimes display availability message is sent when printer socket outPut = NULL
            try {
                val outPut = OutPut(out, messageObject)
                outputMessagesExecutor.submit(outPut)
            } catch (ex: NullPointerException) {
                ex.printStackTrace()
            }

        }
    }

    private fun addClientListener(listener: L) {
        socketListener = listener
    }

    fun removeClientListener() {
        socketListener = null
    }

    /**
     * Is used by [.validatorExecutor]
     */
    private inner class Validator(internal var listener: V) : Callable<Void> {

        @Throws(Exception::class)
        override fun call(): Void? {
            // let another thread have some time perhaps to stop this one:
            Thread.yield()
            //interruption check:
            if (Thread.currentThread().isInterrupted) {
                return null
            }
            val bean = bean
            if (beanIsValid(bean)) listener.onValidate(bean)

            return null
        }
    }
}
