/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package sockets.servers;

import sockets.InPut;
import sockets.OutPut;
import system.ConsoleMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.*;

/**
 * Created by forando on 15.06.15.<br>
 *     This class provides communication with Server for any client
 *     that uses it.
 */
public class Client {

    private final String TAG;

    public int id;
    /**
     * Defines if this client is connected to the server and ready to
     * communicate with it.
     */
    private volatile boolean isReady = false;

    private final String hostName;
    private final int port;
    /**
     * A TYPE of the client.<br/>
     * Defined by project that uses this class
     */
    private int type;

    private Socket socket = null;
//    private ObjectInputStream in;
    private ObjectOutputStream out;
    InPut inPut;
    private final Object lock;

    /**
     * Defines if this client has been registered by the server with an id.
     */
    private boolean registered = false;

    private ClientListener clientListener;
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
     * This class realizes singleton pattern. That's why we have the Constructor
     * protected.
     * @param hostName An IP address of the server host.
     * @param port A port to communicate with the server.
     * @param type A Client TYPE to be registered with.
     * @param id An ID to be registered with.
     */
    protected Client(String hostName, int port, int type, int id){
        TAG = this.getClass().getSimpleName();

        lock = new Object();
        this.hostName = hostName;
        this.port = port;
        this.type = type;
        this.id = id;
        validatorExecutor = Executors.newSingleThreadExecutor();
        outputMessagesExecutor = Executors.newSingleThreadExecutor();
    }

    /**
     * Realizes singlton pattern.
     * @param hostName An IP address of the server host.
     * @param port A port to communicate with the server.
     * @param type A Client TYPE to be registered with.
     * @param id An ID to be registered with.
     * @return An instance of {@link Client} class.
     */
    public static Client getInstance(String hostName, int port, int type, int id){
        if (client != null && client.clientReady()){
            ConsoleMessage.printInfoMessage("Client.getInstance(): The client has already " +
                    "been created. Returning the existing one.");
            return client;
        }else{
            client = new Client(hostName, port, type, id);
            return client;
        }
    }

    /**
     * Starts validation process in a new thread.
     * @param listener A listener to notify about validation.
     */
    public void startInDifferentThread(ClientValidatorListener listener) {
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
    public boolean validate(Socket soc) {
        this.socket = soc;
        if (socket == null){
            isReady = false;
            return false;
        }else {
            isReady = true;
            return true;
        }
    }

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

    private void closeServer(){
        ConsoleMessage.printErrorMessage(TAG + ".closeServer(): Socket with ID = " + id + " has been closed");
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
                closeServer();
            }
        }
    }

    /**
     * Is used by {@link #validatorExecutor}
     */
    private class Validator implements Callable<Void>{

        ClientValidatorListener listener;

        public Validator(ClientValidatorListener listener){
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

    /**
     * Tries to connect to the server.<br/>
     * If it fails - NULL is returned.
     * @return Socket or NULL.
     */
    private Socket getSocket() {
        synchronized (lock) {
            try {
                Socket socket = new Socket(hostName, port);

                ConsoleMessage.printInfoMessage(TAG + ".getSocket(): Got new socket.");

                /*
                outBuffer[0]: 1 - this client talks using serializable Objects, 0 - talks with bytes only
                outBuffer[1]: type of a client (printer, terminal, display etc.)
                outBuffer[2]: client id
                 */
                byte[] outBuffer = {0x01, (byte) type, (byte) id};

                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                output.write(outBuffer);
                output.flush();

                ReadableByteChannel channel = Channels.newChannel(socket.getInputStream());
                ObjectInputStream input = new ObjectInputStream(Channels.newInputStream(channel));

                // let another thread have some time perhaps to stop this one:
                Thread.yield();
                //interruption check:
                if (Thread.currentThread().isInterrupted()) {
                    return null;
                }

                /*
                inputBuffer[0]: 1 - this client talks using serializable Objects, 0 - talks with bytes only
                inputBuffer[1]: type of a client (printer, terminal, display etc.)
                inputBuffer[2]: client id
                 */
                byte[] inputBuffer = new byte[3];
                int val = input.read(inputBuffer);
                if (val > 0 && inputBuffer[0] == 0x01 && inputBuffer[2] >= 0) {
                    register(inputBuffer[2]);
                    return socket;
                } else {
                    close();
                    return null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                closeServer();
                return null;
            }
        }
    }

    /**
     * Registers this client with a given id.
     * @param id An id to be registered with.
     */
    public void register(int id){
        registered = true;
        if (this.id != id){
            this.id = id;
        }
        ConsoleMessage.printInfoMessage(TAG + ".register(): Client registered with ID = " + id);
    }

    private void transferMessage(Object object){

        if (clientListener != null) clientListener.onInputMessage(object);
    }

    public void send(Object messageObject){
        /*
        Not sure if we need this lock here.
        Did it just to have 100% guarantee
         */
        synchronized (lock) {
            /*if (output != null) {
                output.stopThread();
                output = null;
            }
            output = new OutPut(out, id, messageObject);
            output.start();

            //waiting until the message is sent
            try {
                output.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            //bug: Sometimes display availability message is sent when printer socket outPut = NULL
            try {
                OutPut outPut = new OutPut(out, id, messageObject);
                outputMessagesExecutor.submit(outPut);
            }catch (NullPointerException ex){
                ex.printStackTrace();
            }
        }
    }

    private void addClientListener(ClientListener listener){
        clientListener = listener;
    }

    public void removeClientListener(){
        clientListener = null;
    }

    public interface ClientListener {
        /**
         * Notifies that the client has received a message from the server
         * @param object A message from the server. Before use, this message
         *               must be cast to a client specific message type.
         */
        void onInputMessage(Object object);

        /**
         * Notifies tha communication with the server has been broken.
         */
        void onCloseSocket();
    }

    /**
     * See {@link ClientValidatorListener#onValidate(int)}
     */
    public interface ClientValidatorListener {
        /**
         * Notifies that validation has been done successfully.
         * @param id The id this client has been registered with.
         */
        void onValidate(int id);
    }
}
