/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package sockets.servers;

import sockets.servers.server.SocketManager;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by forando on 15.06.15.<br>
 *     This class provides basic server functionality
 */
public class Server<E extends SocketManager> {

    private ServerAcceptor serverAcceptor;

    public E socketManager;

    int PORT;

    public Server(E socketManager){
        this(socketManager, 1337);
    }

    public Server(E socketManager, int port){
        this.socketManager = socketManager;
        this.PORT = port;
    }

    // Server thread accepts incoming client connections
    class ServerAcceptor extends Thread {
        int port;
        private volatile Thread myThread;
        ServerAcceptor(int port) {
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

    // Runs the sever accepter to catch incoming client connections
    public void start() {
        System.out.println("server: Running on port " + PORT);
        serverAcceptor = new ServerAcceptor(PORT);
        serverAcceptor.start();
    }

    public void stop(){
        socketManager.closeAll();
        serverAcceptor.stopThread();
        System.out.println("server: stopped");
    }

    public E getGenerelizidObject(){
        return socketManager;
    }
}
