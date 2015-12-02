/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package sockets.servers;

import sockets.InPut;
import sockets.servers.server.CommunicationNodeListener;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by forando on 02.12.15.<br/>
 * Provides communication between two TCP/IP sockets.
 */
public abstract class CommunicationNode<L extends CommunicationNodeListener> {
    private final String TAG;

    protected int id;

    /**
     * A TYPE of the client.<br/>
     * Defined by project that uses this class
     */
    protected final int type;

    /**
     * Defines if this client is connected to the server and ready to
     * communicate with it.
     */
    private volatile boolean isReady = false;

    private final String hostName;
    private final int port;

    protected Socket socket = null;
    protected ObjectOutputStream out;
    protected InPut inPut;
    protected final Object lock;

    /**
     * Defines if this client has been registered by the server with an id.
     */
    protected boolean registered = false;

    private L clientListener;
    /**
     * Serves this client validation process.
     */
    private ExecutorService validatorExecutor;
    /**
     * Serves output messages sending.
     */
    private ExecutorService outputMessagesExecutor;

    /**
     * An instance of this class. Singleton pattern.
     */
    private static Client client = null;

    /**
     *
     * @param hostName An IP address of the server host.
     * @param port A port to communicate with the server.
     * @param type A Client TYPE to be registered with.
     * @param id An ID to be registered with.
     */
    public CommunicationNode(String hostName, int port, int type, int id){
        TAG = this.getClass().getSimpleName();

        lock = new Object();
        this.hostName = hostName;
        this.port = port;
        this.type = type;
        this.id = id;
        validatorExecutor = Executors.newSingleThreadExecutor();
        outputMessagesExecutor = Executors.newSingleThreadExecutor();
    }
}
