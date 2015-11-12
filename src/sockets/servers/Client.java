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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by forando on 15.06.15.<br>
 *     This class provides communication with Server for any client
 *     that uses it.
 */
public class Client {

    private final String TAG;

    public int id;

    private boolean isReady = false;

    private final String hostName;
    private final int port;
    private int type;

    /**
     * Time (in seconds) to wait for the server response during connection validation.
     */
    private static final int TIME = 3;

//    private Validator validator;
    private Socket socket = null;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    InPut inPut;
    OutPut output;
    private final Object lock;

    int counter = 0;

    //public sockets.SocketMessage message;

    //public Object object;

    private boolean registered = false;

    private ClientListener clientListener;
    private List<ClientValidatorListener> validatorListeners;


    ExecutorService executor;

    private static Client client = null;

    /*public static void main(String[] args) {
        new ClientServer(APP.IP, APP.PORT, SocketMessage.DISPLAY, 0).startInDifferentThread();
    }*/

    protected Client(String hostName, int port, int type, int id){
        TAG = this.getClass().getSimpleName();

        lock = new Object();
        this.hostName = hostName;
        this.port = port;
        this.type = type;
        this.id = id;
//        validator = new Validator();
        executor = Executors.newSingleThreadExecutor();
    }

    public static Client getInstance(String hostName, int port, int type, int id){
        if (client == null){
            client = new Client(hostName, port, type, id);
        }
        return client;
    }

    // Runs a client handler to connect to a server
    public void startInDifferentThread(ClientValidatorListener listener) {
        if (isReady && socket != null) {
            ConsoleMessage.printDebugMessage(TAG + ".startInDifferentThread(): The client has already " +
                    "been started. This start is ignored");
        }else{
            if (executor != null && executor.isShutdown())
            executor = Executors.newSingleThreadExecutor();
            executor.submit(new Validator(listener));
        }
    }

    public boolean startInTheSameThread(){
        if (isReady && socket != null) {
            ConsoleMessage.printDebugMessage(TAG + ".startInTheSameThread(): The client has already" +
                    " been started. This start is ignored");
            return true;
        }else{
            return validate(getSocket());
        }
    }

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

    public boolean openInoutOutputStreams(ClientListener listener) {
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

    public void stopClient(){
        close();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        closeSocketStreams();
    }

    void closeSocketStreams(){
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void closeServer(){
        ConsoleMessage.printErrorMessage(TAG + ".closeServer(): Socket with ID = " + id + " has been closed");
        if (clientListener != null){
            clientListener.onCloseSocket();
            /*
            After onCloseSocket() event is fired, we have to remove this listener.
            This listener has to be reassigned again after the client is reconnected.
             */
            removeClientListener();
        }
    }

    private void close(){
        if (registered) {
            if (inPut != null) inPut.stopThread();
            if (output != null)output.stopThread();
        }else{
//            validator.stopThread();
            if (executor != null)executor.shutdownNow();
        }
        try {
            //bug: if this block kicks out a NullPointer exception this shuts down android app
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            isReady = false;
            closeServer();
        }
    }

    private class Validator implements Callable<Void>{

        ClientValidatorListener listener;

        public Validator(ClientValidatorListener listener){
            this.listener = listener;
        }
        /*public static final String THREAD_NAME = "ClientValidator";
        private volatile Thread myThread;

        public Validator(){
            this.setName(THREAD_NAME);
            myThread = this;
        }

        public void stopThread() {
            Thread tmpThread = myThread;
            myThread = null;
            if (tmpThread != null) {
                tmpThread.interrupt();
            }
        }*/


        /*@Override
        public void run() {
            if (myThread == null) {
                return; // stopped before started.
            }
            try {
                // make connection to the server hostName/port
                InetAddress hostIP = APP.getHostIP(hostName);
                Socket socket = new Socket(hostIP, port);
                Socket socket = new Socket(hostName, port);

                System.out.println("Client: connected!");


                outBuffer[0]: 1 - this client talks using serializable Objects, 0 - talks with bytes only
                outBuffer[1]: type of a client (printer, terminal, display etc.)
                outBuffer[2]: client id

                byte[] outBuffer = {0x01, (byte)type, (byte) id};

                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                output.write(outBuffer);
                output.flush();

                ReadableByteChannel channel = Channels.newChannel(socket.getInputStream());
                ObjectInputStream input = new ObjectInputStream(Channels.newInputStream(channel));


                inputBuffer[0]: 1 - this client talks using serializable Objects, 0 - talks with bytes only
                inputBuffer[1]: type of a client (printer, terminal, display etc.)
                inputBuffer[2]: client id

                byte[] inputBuffer = new byte[3];
                int val = input.read(inputBuffer);
                if (val > 0 && inputBuffer[0]==0x01 && inputBuffer[2]>=0){
                    register(inputBuffer[2]);
                    System.out.println("Validator: client registered with ID = " + inputBuffer[2]);
                    validate(socket);
                }else{
                    close();
                }
            }catch (Exception ex){
                ex.printStackTrace();
                closeServer();
            }
        }*/
        @Override
        public Void call() throws Exception {
            //interruption check:
            if (Thread.currentThread().isInterrupted()) {
                return null;
            }

            if(validate(getSocket())) listener.onValidate(id);

            return null;
        }
    }

    private Socket getSocket() {
        synchronized (lock) {
            try {
                // make connection to the server hostName/port
            /*InetAddress hostIP = APP.getHostIP(hostName);
            Socket socket = new Socket(hostIP, port);*/
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
//                    validate(socket);
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
            if (output != null) {
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
        void onInputMessage(Object object);
        void onCloseSocket();
    }

    public interface ClientValidatorListener {
        void onValidate(int id);
    }

    class MessageTransmitter implements  Runnable{

        private Object messageObject;

        public MessageTransmitter(Object messageObject){
            this.messageObject = messageObject;
        }

        @Override
        public void run() {
            transferMessage(messageObject);
        }
    }
}
