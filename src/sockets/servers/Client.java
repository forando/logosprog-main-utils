/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package sockets.servers;

import sockets.InPut;
import sockets.OutPut;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by forando on 15.06.15.<br>
 *     This class provides communication with Server for any client
 *     that uses it.
 */
public class Client {
    public int id;

    private final String hostName;
    private final int port;
    private int type;

    private Validator validator;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    InPut inPut;
    OutPut output;

    int counter = 0;

    //public sockets.SocketMessage message;

    //public Object object;

    private boolean registered = false;

    /*public static void main(String[] args) {
        new ClientServer(APP.IP, APP.PORT, SocketMessage.DISPLAY, 0).startClient();
    }*/

    public Client(String hostName, int port, int type, int id){
        this.hostName = hostName;
        this.port = port;
        this.type = type;
        this.id = id;
        listeners = new ArrayList<>();
        validator = new Validator();
    }

    // Runs a client handler to connect to a server
    public void startClient() {
        validator.start();
    }

    public void stopClient(){close();}

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
            inPut.stopThread();
            output.stopThread();
        }else{
            validator.stopThread();
        }
        try {
            //bug: if this block kicks out a NullPointer exception this shuts down android app
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            closeServer();
        }
    }


    public void register(int id){
        registered = true;
        if (this.id != id){
            this.id = id;
        }
    }

    private void validate(Socket soc){
        this.socket = soc;
        try {
            /*ReadableByteChannel channel = Channels.newChannel(this.socket.getInputStream());
            this.in = new ObjectInputStream(Channels.newInputStream(channel));*/
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
            out = new ObjectOutputStream(socket.getOutputStream());
            for (ClientListener l : listeners) {
                l.onRegister(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class Validator extends Thread{
        public static final String THREAD_NAME = "ClientValidator";
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
        }

        @Override
        public void run() {
            if (myThread == null) {
                return; // stopped before started.
            }
            try {
                // make connection to the server hostName/port
                /*InetAddress hostIP = APP.getHostIP(hostName);
                Socket socket = new Socket(hostIP, port);*/
                Socket socket = new Socket(hostName, port);

                System.out.println("client: connected!");

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

                /*
                inputBuffer[0]: 1 - this client talks using serializable Objects, 0 - talks with bytes only
                inputBuffer[1]: type of a client (printer, terminal, display etc.)
                inputBuffer[2]: client id
                 */
                byte[] inputBuffer = new byte[2];
                int val = input.read(inputBuffer);
                if (val > 0 && inputBuffer[0]==0x01 && inputBuffer[1]>=0){
                    register(inputBuffer[1]);
                    System.out.println("Validator: client registered with ID = " + inputBuffer[1]);
                    validate(socket);
                }else{
                    close();
                }
            }catch (Exception ex){
                ex.printStackTrace();
                closeServer();
            }
        }
    }

    private synchronized void transferMessage(Object object){

        for (ClientListener l : listeners){
            l.onInputMessage(object);
        }
    }

    public synchronized void send(Object messageObject){
        if (output != null){
            output.stopThread();
            output = null;
        }
        output = new OutPut(out, id, messageObject);
        output.start();
    }

    private List<ClientListener> listeners;

    public void addClientListener(ClientListener listener){
        listeners.add(listener);
    }
    public void removeClientListener(ClientListener listener){
        listeners.remove(listener);
    }

    public interface ClientListener {
        void onRegister(int id);
        void onInputMessage(Object object);
        void onCloseSocket();
    }
}
