/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.sockets.servers.server;

/**
 * Created by forando on 02.12.15.<br>
 * Defines basic steps to be made during validation
 * process between two TCP/IP sockets.
 */
public interface CommunicationNodeValidatorListener1<B> {
    /**
     * Notifies that communication validation has been done successfully.
     * @param bean A Bean that holds specific for each project data to be
     *             used after validation process.
     */
    void onValidate(B bean);
}
