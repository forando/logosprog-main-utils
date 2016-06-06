/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.sockets.servers.main;

import com.logosprog.mainutils.sockets.main.InPut;
import com.logosprog.mainutils.sockets.servers.server.CommunicationNodeListener;
import com.logosprog.mainutils.sockets.main.OutPut;
import com.logosprog.mainutils.sockets.servers.server.CommunicationNodeValidatorListener;
import com.logosprog.mainutils.system.ConsoleMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by forando on 02.12.15.<br/>
 * Provides communication between two TCP/IP sockets.
 */
public abstract class CommunicationNode<B, L extends CommunicationNodeListener, V extends CommunicationNodeValidatorListener<B>> {

    private final String TAG;

    /**
     * Defines if this client is connected to the server and ready to
     * communicate with it.
     */
    protected volatile boolean isReady = false;

    protected ObjectOutputStream out;
    protected InPut inPut;
    protected final Object lock;

    private L socketListener;
    /**
     * Serves this client validation process.
     */
    private ExecutorService validatorExecutor;
    /**
     * Serves output messages sending.
     */
    private ExecutorService outputMessagesExecutor;

    public CommunicationNode(){
        TAG = this.getClass().getSimpleName();

        lock = new Object();
        validatorExecutor = Executors.newSingleThreadExecutor();
        outputMessagesExecutor = Executors.newSingleThreadExecutor();
    }

    /**
     * Starts validation process in a new thread.
     * @param listener A listener to notify about validation.
     */
    public void startInDifferentThread(V listener) {
        if (ready()) {
            ConsoleMessage.printInfoMessage(TAG + ".startInDifferentThread(): The client has already " +
                    "been started. This start is ignored");
        }else{
            if (validatorExecutor == null || validatorExecutor.isShutdown())
                validatorExecutor = Executors.newSingleThreadExecutor();
            validatorExecutor.submit(new Validator(listener));
        }
    }

    /**
     * Starts validation process in the same thread.
     * @return data BEAN - if validation is successful, and NULL - if not.
     */
    public B startInTheSameThread(){
        if (ready()) {
            ConsoleMessage.printInfoMessage(TAG + ".startInTheSameThread(): The client has already" +
                    " been started. This start is ignored");
            return null;
        }else{
            B bean = getBean();
            if (beanIsValid(bean))
            return bean;
            return null;
        }
    }

    /**
     * Tries to connect to the remote socket.<br/>
     * If it fails - NULL is returned.
     * @return data bean or NULL.
     */
    protected B getBean() {
        synchronized (lock) {
            try {
                Socket socket = makeSocket();
                if (null != socket) {
                    ConsoleMessage.printInfoMessage(TAG + ".getBean: Got new socket.");
                    return makeBean(socket);
                }else {
                    return null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                notifyClose();
                return null;
            }
        }
    }

    /**
     * Tries to connect to the remote socket.<br/>
     * If it fails - NULL is returned.
     * @return Bean or NULL.
     */
    protected abstract B makeBean(Socket socket) throws IOException;

    protected abstract Socket makeSocket() throws IOException;

    /**
     * Checks if the node is ready for use.
     * @return TRUE - if ready, FALSE - if not.
     */
    public boolean ready(){
        return isReady;
    }

    /**
     * During validation process some exceptions might occur.
     * In this case NULL will be returned.<br/>
     * This method ensures that we've received the valid results.<br/>
     * It, also, can init some additional objects that are necessary
     * for communication between sockets.
     * @param bean A data bean (specific for each project).
     * @return TRUE - if it's valid or FALSE - if not.
     */
    public abstract boolean beanIsValid(B bean);

    /**
     * Opens the sockets input and output streams in order to
     * communicate with the server.
     * @param listener A listener to notify about received messages.
     * @return TRUE - if all streams have been opened successfully.
     */
    public boolean openSocketStreams(L listener, Socket socket, int id) {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            /*if (out == null){
                isReady = false;
                return false;
            }*/
        } catch (IOException e) {
            e.printStackTrace();
            isReady = false;
            return false;
        }

        addClientListener(listener);

        inPut = new InPut(socket, id);
        inPut.addInputListener(new InPut.InputListener() {
            @Override
            public void onMessage(Object messageObject) {
                transferMessage(messageObject);
            }

            @Override
            public void onClose(Socket socket) {
                close(socket);
            }
        });
        inPut.start();
        return true;
    }

    /**
     * Closes the sockets input and output streams.
     */
    protected void closeSocketStreams(Socket socket){
        try {
            if (inPut != null) inPut.stopThread();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Breaks the communication with remote socket.
     * @param socket Local socket.
     */
    public void shutDownNode(Socket socket){
        /*
        IMPORTANT!!!
        After onCloseSocket() event is fired, we have to remove this listener.
        This listener will be reassigned again after the client is reconnected.
         */
        removeClientListener();
        close(socket);
    }

    /**
     * Notifies listener about socket close event.
     */
    protected void notifyClose(){
        if (socketListener != null){
            socketListener.onCloseSocket();
            /*
            IMPORTANT!!!
            After onCloseSocket() event is fired, we have to remove this listener.
            This listener will be reassigned again after the client is reconnected.
             */
            removeClientListener();
        }
    }

    public void close(Socket socket){
        /*
        * We need the lock here to be sure that the next Client.getInstance() will
        * return a new Client object.
        * That's why we have to guarantee that this
        * method is finished before the next Client.getInstance() call.
        * */
        synchronized (lock) {
            if (inPut != null) inPut.stopThread();
            if (validatorExecutor != null) validatorExecutor.shutdownNow();
            try {
                //bug: if this block kicks out a NullPointer exception this shuts down android app
                if (out != null) out.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                isReady = false;
                notifyClose();
            }
        }
    }

    private void transferMessage(Object object){

        if (socketListener != null) socketListener.onInputMessage(object);
    }

    public void send(Object messageObject){
        /*
        Not sure if we need this lock here.
        Did it just to have 100% guarantee
         */
        synchronized (lock) {
            //bug: Sometimes display availability message is sent when printer socket outPut = NULL
            try {
                OutPut outPut = new OutPut(out, messageObject);
                outputMessagesExecutor.submit(outPut);
            }catch (NullPointerException ex){
                ex.printStackTrace();
            }
        }
    }

    private void addClientListener(L listener){
        socketListener = listener;
    }

    public void removeClientListener(){
        socketListener = null;
    }

    /**
     * Is used by {@link #validatorExecutor}
     */
    private class Validator implements Callable<Void> {

        V listener;

        public Validator(V listener){
            this.listener = listener;
        }

        @Override
        public Void call() throws Exception {
            // let another thread have some time perhaps to stop this one:
            Thread.yield();
            //interruption check:
            if (Thread.currentThread().isInterrupted()) {
                return null;
            }
            B bean = getBean();
            if(beanIsValid(bean)) listener.onValidate(bean);

            return null;
        }
    }
}
