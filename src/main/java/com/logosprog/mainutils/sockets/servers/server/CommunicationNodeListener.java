/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.sockets.servers.server;

/**
 * Created by forando on 02.12.15.<br>
 * Defines basic communication functionality between two TCP/IP sockets.
 */
public interface CommunicationNodeListener {
    /**
     * Notifies that this socket has received a message from another socket.
     * @param object A message from the server. Before use, this message
     *               must be cast to a project specific message type.
     */
    void onInputMessage(Object object);

    /**
     * Notifies that communication with a remote socket has been broken.
     */
    void onCloseSocket();
}
