package sockets;/*
 * Copyright (c) 2014. This code is a LogosProg property. All Rights Reserved.
 */

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by forando on 01.10.14.<br>
 *     This class uses socket to send messages.
 */
public class OutPut  extends Thread {
    public static final String THREAD_NAME = "SocketOutPut";
    private volatile Thread myThread;
    ObjectOutputStream out;
    private int id;
    private Object messageObject;

    public OutPut(ObjectOutputStream out, int id, Object messageObject){
        this.setName(THREAD_NAME);
        myThread = this;
        this.out = out;
        this.id = id;
        this.messageObject = messageObject;
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
            out.writeUnshared(messageObject);
            out.flush();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
