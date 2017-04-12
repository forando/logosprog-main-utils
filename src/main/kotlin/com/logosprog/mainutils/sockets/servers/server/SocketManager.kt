/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.sockets.servers.server

import java.net.Socket

/**
 * Created by forando on 15.06.15.
 * This interface must be implemented by any class that manages
 * sockets received from [Server.ServerAcceptor]
 */
interface SocketManager {
    /**
     * Implement this method to use the given socket.
     * @param socket A socket to be accepted by the server
     */
    fun accept(socket: Socket)

    /**
     * Implement this method to close all active sockets.
     */
    fun closeAll()
}
