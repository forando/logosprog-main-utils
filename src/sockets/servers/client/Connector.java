/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package sockets.servers.client;

import sockets.servers.Client;

import java.util.concurrent.*;

/**
 * Created by forando on 15.06.15.<br>
 *     This class provides an app with a {@link Client} object to be able to talk to HostServer
 */
public class Connector {

    private final String TAG;

    Client client;

    private volatile Thread myThread;

//    Client.ClientListener thisThreadClientListener;
    Client.ClientListener clientListener;
    String IP;
    int PORT;
    int type;
    int id;
    ConnectorListener listener;

    private static Connector connector = null;


    /**
     * The quantity of restart attempts after socket closed event.
     */
    private int restartsQuant = 0;
    /**
     * delay between two separate attempts to to obtain {@link Client} object
     */
    private int delay = 2000;

    private ExecutorService executorClientStarter;
    private Future<Boolean> futureClientStarter;


    /**
     * This constructor is just for <b>testing</b>. Here we use predefined IP an PORT
     * @param type The type of a client that wants to be connected to the server
     * @param id The clients id
     */
    public Connector(int type, int id) {
        this(type, id, "localhost", 1337);
    }

    /**
     * This constructor must be used in <b>production</b>.
     * @param type The type of a client that wants to be connected to the server
     * @param id The clients id
     * @param ip The server IP address
     * @param port The server PORT
     */
    public Connector(int type, int id, String ip, int port) {

        TAG = this.getClass().getSimpleName();


        this.type = type;
        this.id = id;
        this.IP = ip;
        this.PORT = port;

        executorClientStarter = Executors.newSingleThreadExecutor();

//        this.thisThreadClientListener = this;
//        startConnection();
//        restartsQuant++;
    }

    /**
     * This method must be used in <b>production</b>.
     * @param type The type of a client that wants to be connected to the server
     * @param id The clients id
     * @param ip The server IP
     * @param port The server PORT
     * @return A new instance of the class
     */
    public static Connector getConnector(int type, int id, String ip, int port){
        if (connector == null){
            connector = new Connector(type, id, ip, port);
        }

        return connector;
    }

    /**
     * This method is just for <b>testing</b>. Here we use predefined IP an PORT
     * @param type The type of a client that wants to be connected to the server
     * @param id The clients id
     * @return A new instance of the class
     */
    public static Connector getConnector(int type, int id){
        if (connector == null){
            connector = new Connector(type, id, "localhost", 1337);
        }

        return connector;
    }

    /**
     *
     * @param clientListener The listener to notify about general server events.
     */
    public void startConnection(Client.ClientListener clientListener){
        restartsQuant++;
        this.clientListener = clientListener;
        System.out.println(restartsQuant + " Attempt to get connected to the Server!!!");
        client = Client.getInstance(IP, PORT, type, id);
        boolean success;

        futureClientStarter = executorClientStarter.submit(new ClientStarter());

        listener.onClientConnected(client);
    }

    public void stopConnection(){
        futureClientStarter.cancel(true);
    }

    private void restartClient(){
        restartsQuant++;
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                    startInDifferentThread();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
        if (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(delay);
                startConnection(clientListener);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopThread() {
        Thread tmpThread = myThread;
        myThread = null;
        if (tmpThread != null) {
            tmpThread.interrupt();
        }
    }

    /*@Override
    public void onValidate(int id) {
        client.removeClientListener(thisThreadClientListener);
        client.addClientListener(clientListener);
        this.listener.onClientConnected(client);
    }

    @Override
    public void onInputMessage(Object object) {
        //dummy
    }

    @Override
    public void onCloseSocket() {
        restartClient();
    }*/

    public void addConnectorListener(ConnectorListener listener) throws Exception {
        if (this.listener != null) {
            throw new Exception("The ConnectorListener is already assigned!");
        }
        this.listener = listener;
    }

    public interface ConnectorListener{
        void onClientConnected(Client client);
    }

    private class ClientStarter implements Callable<Boolean>{

        @Override
        public Boolean call() throws Exception {

            if (Thread.currentThread().isInterrupted()) {
                return null;
            }

            boolean success;
            do {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Thread.currentThread().isInterrupted()) {
                    return null;
                }
                success = client.startInTheSameThread();
            }while (!success);
            return true;
        }
    }
}
