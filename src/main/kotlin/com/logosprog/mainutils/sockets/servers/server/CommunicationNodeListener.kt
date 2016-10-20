/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.sockets.servers.server

/**
 * Created by forando on 02.12.15.
 * Defines basic communication functionality between two TCP/IP sockets.
 */
interface CommunicationNodeListener {
    /**
     * Notifies that this socket has received a message from another socket.
     * @param object A message from the server. Before use, this message
     * *               must be cast to a project specific message type.
     */
    fun onInputMessage(obj: Any)

    /**
     * Notifies that communication with a remote socket has been broken.
     */
    fun onCloseSocket()
}
