/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.sockets.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by forando on 15.06.15.<br>
 *     This class uses socket to listen for input messages
 */
public class InPut extends Thread {
    public static final String THREAD_NAME = "SocketInPut";
    private volatile Thread myThread;
    private Socket socket;
//    private int id;
    ObjectInputStream in;

//    ExecutorService executor = Executors.newFixedThreadPool(5);

    List<InputListener> listeners;

    public InPut(Socket socket, int id) {
        this.setName(THREAD_NAME);
        myThread = this;
        this.socket = socket;
//        this.id = id;
        listeners = new ArrayList<>();
        this.in = null;
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
            //this.in = new ObjectInputStream(this.socket.getInputStream());
            ReadableByteChannel channel = Channels.newChannel(socket.getInputStream());
            in = new ObjectInputStream(Channels.newInputStream(channel));
            while (true) {
                //get object from server, will block until object arrives.
                Object messageObject = in.readObject();
                for (InputListener l : listeners){
                    l.onMessage(messageObject);
                }
                //executor.submit(new MessageTransmitter(messageObject));

                Thread.yield(); // let another thread have some time perhaps to stop this one.
                if (Thread.currentThread().isInterrupted()) {
//                    executor.shutdown();
                    if (in != null) try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    throw new InterruptedException("Socket: Stopped by ifInterruptedStop()");
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();

            if (in != null) try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (InputListener l : listeners){
                l.onClose(socket);
            }
        }
    }

    public void addInputListener(InputListener listener){
        listeners.add(listener);
    }

    public interface InputListener{
        void onMessage(Object messageObject);
        void onClose(Socket socket);
    }


}
