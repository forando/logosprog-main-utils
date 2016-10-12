/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.sockets.servers.server;

import com.logosprog.mainutils.sockets.servers.main.Server;

import java.net.Socket;

/**
 * Created by forando on 15.06.15.<br>
 *     This interface must be implemented by any class that manages
 *     sockets received from {@link com.logosprog.mainutils.sockets.servers.main.Server.ServerAcceptor}
 */
public interface SocketManager {
    /**
     * Implement this method to use the given socket.
     * @param socket A socket to be accepted by the server
     */
    void accept(Socket socket);

    /**
     * Implement this method to close all active sockets.
     */
    void closeAll();
}