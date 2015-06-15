/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package sockets.servers.server;

import java.net.Socket;

/**
 * Created by forando on 15.06.15.<br>
 *     This interface must be implemented by any class that manages
 *     sockets received from {@link sockets.servers.Server.ServerAcceptor}
 */
public interface SocketManager {
    /**
     * Implement this method to use the given socket.
     * @param socket
     */
    void accept(Socket socket);

    /**
     * Implement this method to close all active sockets.
     */
    void closeAll();
}
