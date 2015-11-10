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

    ExecutorService executor;
    Future<Socket> future = null;

    private static Client client = null;

    /*public static void main(String[] args) {
        new ClientServer(APP.IP, APP.PORT, SocketMessage.DISPLAY, 0).startClient();
    }*/

    protected Client(String hostName, int port, int type, int id){
        TAG = this.getClass().getSimpleName();

        lock = new Object();
        this.hostName = hostName;
        this.port = port;
        this.type = type;
        this.id = id;
        listeners = new ArrayList<>();
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
    public boolean startClient() {
//        validator.start();
        if (isReady && socket != null) {
            ConsoleMessage.printDebugMessage(TAG + ".startClient(): The client has already been started. This start is ignored");
            return true;
        }
        this.future = executor.submit(new Validator());
        try {
            /*
            here the thread is going for sleep:
             */
            this.socket = future.get(TIME, TimeUnit.SECONDS);
            if (socket == null) return false;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            isReady = false;
            return false;
        }
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            isReady = false;
            return false;
        }
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
        isReady = true;
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
        System.out.println("Socket with ID = " + id + " has been closed");
        for (ClientListener l : listeners) {
            l.onCloseSocket();
        }
    }

    private void close(){
        if (registered) {
            if (inPut != null) inPut.stopThread();
            if (output != null)output.stopThread();
        }else{
//            validator.stopThread();
            if (future != null)future.cancel(true);
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


    public void register(int id){
        registered = true;
        if (this.id != id){
            this.id = id;
        }
    }

    public void validate(Socket soc) {
        this.socket = soc;
        /*ReadableByteChannel channel = Channels.newChannel(this.socket.getInputStream());
        this.in = new ObjectInputStream(Channels.newInputStream(channel));*/
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            inPut = new InPut(socket, id);
            inPut.addInputListener(new InPut.InputListener() {
                @Override
                public void onMessage(Object messageObject) {
                    transferMessage(messageObject);
                }

                @Override
                public void onClose() {
                    close();
                    executor.shutdown();
                }
            });
            inPut.start();
            /*for (ClientListener l : listeners) {
                l.onValidate(id);
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Validator implements Callable<Socket>{
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
        public Socket call() throws Exception {
            //interruption check:
            if (Thread.currentThread().isInterrupted()) {
                return null;
            }

            try {
                // make connection to the server hostName/port
                /*InetAddress hostIP = APP.getHostIP(hostName);
                Socket socket = new Socket(hostIP, port);*/
                Socket socket = new Socket(hostName, port);

                System.out.println("Client: connected!");

                /*
                outBuffer[0]: 1 - this client talks using serializable Objects, 0 - talks with bytes only
                outBuffer[1]: type of a client (printer, terminal, display etc.)
                outBuffer[2]: client id
                 */
                byte[] outBuffer = {0x01, (byte)type, (byte) id};

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
                if (val > 0 && inputBuffer[0]==0x01 && inputBuffer[2]>=0){
                    register(inputBuffer[2]);
                    System.out.println("Validator: client registered with ID = " + inputBuffer[2]);
//                    validate(socket);
                    return socket;
                }else{
                    close();
                    return null;
                }
            }catch (Exception ex){
                ex.printStackTrace();
                closeServer();
                return null;
            }
        }
    }

    private void transferMessage(Object object){

        for (ClientListener l : listeners){
            l.onInputMessage(object);
        }
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

    private List<ClientListener> listeners;

    public void addClientListener(ClientListener listener){
        listeners.add(listener);
    }
    public void removeClientListener(ClientListener listener){
        listeners.remove(listener);
    }

    public interface ClientListener {
        void onInputMessage(Object object);
        void onCloseSocket();
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
