/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package sockets.servers.client;

import sockets.servers.Client;
import system.ConsoleMessage;

import java.util.concurrent.*;

/**
 * Created by forando on 15.06.15.<br>
 *     This class provides an app with a {@link Client} object to be able to talk to HostServer
 */
public class Connector {

    private final String TAG;

    Client client;

    String IP;
    int PORT;
    /**
     * A TYPE of the client.<br/>
     * Defined by project that uses this class
     */
    int type;
    int id;
    ConnectorListener listener;

    /**
     * An instance of this class. Realizes singleton pattern.
     */
    private static Connector connector = null;


    /**
     * The quantity of restart attempts after socket closed event.
     */
    private int restartsQuant = 0;
    /**
     * delay between two separate attempts to to obtain {@link Client} object
     */
    private static final int delay = 2000;

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

    }

    /**
     * This method must be used in <b>production</b>.
     * @param type The type of a client that wants to be connected to the server
     * @param id The clients id
     * @param ip The server IP
     * @param port The server PORT
     * @return A new instance of the class. Realizes singleton pattern.
     */
    public static Connector getInstance(int type, int id, String ip, int port){
        if (connector == null){
            connector = new Connector(type, id, ip, port);
        }
        return connector;
    }

    /**
     * This method is just for <b>testing</b>. Here we use predefined IP an PORT
     * @param type The type of a client that wants to be connected to the server
     * @param id The clients id
     * @return A new instance of the class. Realizes singleton pattern.
     */
    public static Connector getInstance(int type, int id){
        if (connector == null){
            connector = new Connector(type, id, "localhost", 1337);
        }
        return connector;
    }


    public void startConnection(){
        stopConnection();
        client = Client.getInstance(IP, PORT, type, id);
        futureClientStarter = executorClientStarter.submit(new ClientStarter());
    }

    public void stopConnection(){
        if (futureClientStarter != null) futureClientStarter.cancel(true);
    }

    private void restartClient(){
        stopConnection();
        startConnection();
    }

    public void addConnectorListener(ConnectorListener listener) {
        this.listener = listener;
    }

    public interface ConnectorListener{
        void onClientConnected(Client client);
    }

    private class ClientStarter implements Callable<Boolean>{

        @Override
        public Boolean call() throws Exception {

            boolean success;
            // let another thread have some time perhaps to stop this one:
            Thread.yield();
            if (Thread.currentThread().isInterrupted()) {
                return false;
            }
            restartsQuant++;
            ConsoleMessage.printInfoMessage(TAG + ".ClientStarter.call(): " + restartsQuant +
                    " Attempt to get connected to the Server!!!");
            success = client.startInTheSameThread();
            while (!success){
                // let another thread have some time perhaps to stop this one:
                Thread.yield();
                if (Thread.currentThread().isInterrupted()) {
                    return false;
                }
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // let another thread have some time perhaps to stop this one:
                Thread.yield();
                if (Thread.currentThread().isInterrupted()) {
                    return false;
                }
                restartsQuant++;
                ConsoleMessage.printInfoMessage(TAG + ".ClientStarter.call(): " + restartsQuant +
                        " Attempt to get connected to the Server!!!");
                success = client.startInTheSameThread();
            }
            listener.onClientConnected(client);
            return true;
        }
    }
}
