/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package sockets.servers;

import sockets.InPut;
import sockets.OutPut;
import sockets.servers.server.CommunicationNodeListener;
import sockets.servers.server.CommunicationNodeValidatorListener;
import system.ConsoleMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.*;

/**
 * Created by forando on 15.06.15.<br>
 *     This class provides communication with Server for any client
 *     that uses it.
 */
public class Client extends CommunicationNode<Client.ClientBean, CommunicationNodeListener, CommunicationNodeValidatorListener<Client.ClientBean>> {

    private final String TAG;

    protected int id;

    /**
     * A TYPE of the client.<br/>
     * Defined by project that uses this class
     */
    protected final int type;

    private final String hostName;
    private final int port;

    protected Socket socket = null;

    /**
     * Defines if this client has been registered by the server with an id.
     */
    protected boolean registered = false;

    /**
     * An instance of this class. Singleton pattern.
     */
    private static Client client = null;

    /**
     * This class realizes singleton pattern. That's why we have the Constructor
     * protected.
     * @param hostName An IP address of the server host.
     * @param port A port to communicate with the server.
     * @param type A Client TYPE to be registered with.
     * @param id An ID to be registered with.
     */
    protected Client(String hostName, int port, int type, int id){
        super();

        TAG = this.getClass().getSimpleName();

        this.hostName = hostName;
        this.port = port;
        this.type = type;
        this.id = id;
    }

    /**
     * Realizes singlton pattern.
     * @param hostName An IP address of the server host.
     * @param port A port to communicate with the server.
     * @param type A Client TYPE to be registered with.
     * @param id An ID to be registered with.
     * @return An instance of {@link Client} class.
     */
    public static Client getInstance(String hostName, int port, int type, int id){
        if (client != null && client.ready()){
            ConsoleMessage.printInfoMessage("Client.getInstance(): The client has already " +
                    "been created. Returning the existing one.");
            return client;
        }else{
            client = new Client(hostName, port, type, id);
            return client;
        }
    }

    public int getId() {
        return id;
    }

    /**
     * Registers this client with a given id.
     * @param id An id to be registered with.
     */
    public void setId(int id){
        registered = true;
        if (this.id != id){
            this.id = id;
        }
        ConsoleMessage.printInfoMessage(TAG + ".setId(): Client registered with ID = " + id);
    }

    public int getType() {
        return type;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    protected ClientBean makeBean(Socket socket) throws IOException {
        /*
        outBuffer[0]: 1 - this client talks using serializable Objects, 0 - talks with bytes only
        outBuffer[1]: type of a client (printer, terminal, display etc.)
        outBuffer[2]: client id
         */
        byte[] outBuffer = {0x01, (byte) type, (byte) id};

        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        output.write(outBuffer);
        output.flush();

        ReadableByteChannel channel = Channels.newChannel(socket.getInputStream());
        ObjectInputStream input = new ObjectInputStream(Channels.newInputStream(channel));

        // let another thread have some time perhaps to stop this one:
        Thread.yield();
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
        if (val > 0 && inputBuffer[0] == 0x01 && inputBuffer[2] >= 0) {
            setId(inputBuffer[2]);
            return new ClientBean(socket);
        } else {
            close(socket);
            return null;
        }
    }

    @Override
    protected Socket makeSocket() throws IOException {
        return new Socket(hostName, port);
    }

    @Override
    protected boolean ready() {
        return isReady && socket != null;
    }

    @Override
    public boolean beanIsValid(ClientBean bean) {
        this.socket = bean.getSocket();
        if (socket == null){
            isReady = false;
            return false;
        }else {
            isReady = true;
            return true;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        closeSocketStreams(socket);
    }

    /*public interface ClientValidatorListener {
        *//*
         * Notifies that validation has been done successfully.
         * @param id The id this client has been registered with.
         * @param clientTalksWithObject Optional. Necessary only on the server side.
         *                              Can be <b>1</b> - if a client talks to this
         *                              socket using JAVA Objects object,
         *                              or <b>0</b> - if it doesn't.
         *//*
        void onValidate(int id, byte... clientTalksWithObject);
    }*/

    class ClientBean{
        private Socket socket;

        public ClientBean(Socket socket) {
            this.socket = socket;
        }

        public Socket getSocket() {
            return socket;
        }
    }
}
