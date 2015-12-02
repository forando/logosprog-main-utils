/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package sockets.servers;

import sockets.InPut;
import sockets.servers.server.CommunicationNodeListener;
import sockets.servers.server.CommunicationNodeValidatorListener;
import system.ConsoleMessage;

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

    protected int id;

    /**
     * A TYPE of the client.<br/>
     * Defined by project that uses this class
     */
    protected final int type;

    /**
     * Defines if this client is connected to the server and ready to
     * communicate with it.
     */
    private volatile boolean isReady = false;

    private final String hostName;
    private final int port;

    protected Socket socket = null;
    protected ObjectOutputStream out;
    protected InPut inPut;
    protected final Object lock;

    /**
     * Defines if this client has been registered by the server with an id.
     */
    protected boolean registered = false;

    private L clientListener;
    /**
     * Serves this client validation process.
     */
    private ExecutorService validatorExecutor;
    /**
     * Serves output messages sending.
     */
    private ExecutorService outputMessagesExecutor;

    /**
     * An instance of this class. Singleton pattern.
     */
    private static Client client = null;

    /**
     *
     * @param hostName An IP address of the server host.
     * @param port A port to communicate with the server.
     * @param type A Client TYPE to be registered with.
     * @param id An ID to be registered with.
     */
    public CommunicationNode(String hostName, int port, int type, int id){
        TAG = this.getClass().getSimpleName();

        lock = new Object();
        this.hostName = hostName;
        this.port = port;
        this.type = type;
        this.id = id;
        validatorExecutor = Executors.newSingleThreadExecutor();
        outputMessagesExecutor = Executors.newSingleThreadExecutor();
    }

    public int getId() {
        return id;
    }

    /**
     * Registers this client with a given id.
     * @param id An id to be registered with.
     */
    public void setId(int id){
        registered = true;
        if (this.id != id){
            this.id = id;
        }
        ConsoleMessage.printInfoMessage(TAG + ".setId(): Client registered with ID = " + id);
    }

    public int getType() {
        return type;
    }

    /**
     * Starts validation process in a new thread.
     * @param listener A listener to notify about validation.
     */
    public void startInDifferentThread(V listener) {
        if (clientReady()) {
            ConsoleMessage.printInfoMessage(TAG + ".startInDifferentThread(): The client has already " +
                    "been started. This start is ignored");
        }else{
            if (validatorExecutor != null && validatorExecutor.isShutdown())
                validatorExecutor = Executors.newSingleThreadExecutor();
            validatorExecutor.submit(new Validator(listener));
        }
    }

    /**
     * Starts validation process in the same thread.
     * @return TRUE - if validation is successful, and FALSE - if not.
     */
    public boolean startInTheSameThread(){
        if (clientReady()) {
            ConsoleMessage.printInfoMessage(TAG + ".startInTheSameThread(): The client has already" +
                    " been started. This start is ignored");
            return true;
        }else{
            return validate(getSocket());
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
                Socket socket = getSocket();
                ConsoleMessage.printInfoMessage(TAG + ".getSocket(): Got new socket.");
                return makeBean();
            } catch (Exception ex) {
                ex.printStackTrace();
                notifyClose();
                return null;
            }
        }
    }

    protected abstract B makeBean();

    protected abstract Socket getSocket();

    /**
     * Checks if the client is ready for use.
     * @return TRUE - if ready, FALSE - if not.
     */
    protected boolean clientReady(){
        return isReady && socket != null;
    }

    /**
     * During validation process some exceptions might occur.
     * In this case NULL will be returned.<br/>
     * This method ensures that we've received the real object.
     * If it's true, it also sets the {@link #isReady} flag to TRUE.
     * @param soc A socket to be checked for NULL.
     * @return TRUE - if the socket is not NULL and FALSE - otherwise.
     */
    /**
     *
     * @param bean
     * @return
     */
    public abstract boolean validate(B bean);

    /**
     * Opens the sockets input and output streams in order to
     * communicate with the server.
     * @param listener A listener to notify about received messages.
     * @return TRUE - if all streams have been opened successfully.
     */
    public boolean openSocketStreams(ClientListener listener) {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
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
            public void onClose() {
                close();
            }
        });
        inPut.start();
        return true;
    }

    /**
     * Closes the sockets input and output streams.
     */
    private void closeSocketStreams(){
        try {
            if (inPut != null) inPut.stopThread();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Breaks the communication with server.
     */
    public void stopClient(){
        removeClientListener();
        close();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        /*
        IMPORTANT!!!
        We have to remove this listener.
        This listener will be reassigned again after the client is reconnected.
         */
        closeSocketStreams();
    }

    /**
     * Notifies listener about socket close event.
     */
    protected void notifyClose(){
        ConsoleMessage.printErrorMessage(TAG + ".notifyClose(): Socket with ID = " + id + " has been closed");
        if (clientListener != null){
            clientListener.onCloseSocket();
            /*
            IMPORTANT!!!
            After onCloseSocket() event is fired, we have to remove this listener.
            This listener will be reassigned again after the client is reconnected.
             */
            removeClientListener();
        }
    }

    private void close(){
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
//                if (in != null) in.close();
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

            if(validate(getSocket())) listener.onValidate(id);

            return null;
        }
    }
}
