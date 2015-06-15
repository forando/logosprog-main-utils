/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package sockets.servers;

import sockets.servers.server.SocketManager;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by forando on 15.06.15.<br>
 *     This class provides basic server functionality.<br>
 *     It's generalized by a predefined object that must
 *     implement {@link SocketManager} interface.
 */
public class Server<E extends SocketManager> {

    private ServerAcceptor serverAcceptor;

    public E socketManager;

    int PORT;

    /**
     * This constructor is for test only.<br> Here server port is set to <b>1337</b>
     * @param socketManager An object that implements {@link SocketManager} interface
     */
    public Server(E socketManager){
        this(socketManager, 1337);
    }

    /**
     * This constructor must be used in production
     * @param socketManager An object that implements {@link SocketManager} interface
     * @param port The port this server will be listening to.
     */
    public Server(E socketManager, int port){
        this.socketManager = socketManager;
        this.PORT = port;
    }

    // Server thread accepts incoming client connections
    class ServerAcceptor extends Thread {
        public static final String THREAD_NAME = "ServerAcceptor";
        int port;
        private volatile Thread myThread;
        ServerAcceptor(int port) {
            this.setName(THREAD_NAME);
            this.port = port;
            myThread = this;
        }

        public void stopThread() {
            Thread tmpThread = myThread;
            myThread = null;
            if (tmpThread != null) {
                tmpThread.interrupt();
            }
        }

        public void run() {
            if (myThread == null) {
                return; // stopped before started.
            }
            try{
                ServerSocket serverSocket = new ServerSocket(port);
                while (true){
                    Socket socket = null;
                    // this blocks, waiting for a Socket to the client
                    socket = serverSocket.accept();
                    System.out.println("server: got client");

                    socketManager.accept(socket);

                    Thread.yield(); // let another thread have some time perhaps to stop this one.
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException("Stopped by ifInterruptedStop()");
                    }
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    /**
     * Runs the {@link ServerAcceptor} to catch incoming client connections
     */
    public void start() {
        System.out.println("server: Running on port " + PORT);
        serverAcceptor = new ServerAcceptor(PORT);
        serverAcceptor.start();
    }

    /**
     * Stops the server.
     */
    public void stop(){
        socketManager.closeAll();
        serverAcceptor.stopThread();
        System.out.println("server: stopped");
    }

    /**
     * Call this method to get {@link E} object.
     * @return The object of generalized, during this class
     * construction, type
     */
    public E getGenerelizidObject(){
        return socketManager;
    }
}
