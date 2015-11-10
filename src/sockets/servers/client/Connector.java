/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package sockets.servers.client;

import sockets.servers.Client;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

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


    /**
     * This constructor is just for <b>testing</b>. Here we use predefined IP an PORT
     * @param clientListener The listener to notify about general server events.
     * @param type The type of a client that wants to be connected to the server
     * @param id The clients id
     */
    public Connector(Client.ClientListener clientListener, int type, int id) {
        this(clientListener, type, id, "localhost", 1337);
    }

    /**
     * This constructor must be used in <b>production</b>.
     * @param clientListener The listener to notify about general server events.
     * @param type The type of a client that wants to be connected to the server
     * @param id The clients id
     * @param ip The server IP address
     * @param port The server PORT
     */
    public Connector(Client.ClientListener clientListener, int type, int id, String ip, int port) {

        TAG = this.getClass().getSimpleName();

        this.clientListener = clientListener;
        this.type = type;
        this.id = id;
        this.IP = ip;
        this.PORT = port;
//        this.thisThreadClientListener = this;
        startClient();
        restartsQuant++;
    }

    /**
     * This method must be used in <b>production</b>.
     * @param clientListener The listener to notify about general server events.
     * @param type The type of a client that wants to be connected to the server
     * @param id The clients id
     * @param ip The server IP
     * @param port The server PORT
     * @return A new instance of the class
     */
    public static Connector getConnector(Client.ClientListener clientListener, int type, int id, String ip, int port){
        if (connector == null){
            connector = new Connector(clientListener, type, id, ip, port);
        }

        return connector;
    }

    /**
     * This method is just for <b>testing</b>. Here we use predefined IP an PORT
     * @param clientListener The listener to notify about general server events.
     * @param type The type of a client that wants to be connected to the server
     * @param id The clients id
     * @return A new instance of the class
     */
    public static Connector getConnector(Client.ClientListener clientListener, int type, int id){
        if (connector == null){
            connector = new Connector(clientListener, type, id, "localhost", 1337);
        }

        return connector;
    }

    private void startClient(){
        System.out.println(restartsQuant + " Attempt to get connected to the Server!!!");
        client = Client.getInstance(IP, PORT, type, id);
        client.addClientListener(clientListener);

        boolean success;

        do {
            success = client.startClient();
        }while (!success);

        listener.onClientConnected(client);
    }

    private void restartClient(){
        restartsQuant++;
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                    startClient();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
        if (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(delay);
                startClient();
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
}
