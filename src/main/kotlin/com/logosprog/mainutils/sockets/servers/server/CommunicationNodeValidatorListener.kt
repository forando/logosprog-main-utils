/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.sockets.servers.server

/**
 * Created by forando on 02.12.15.
 * Defines basic steps to be made during validation
 * process between two TCP/IP sockets.
 */
interface CommunicationNodeValidatorListener<B> {
    /**
     * Notifies that communication validation has been done successfully.
     * @param bean A Bean that holds specific for each project data to be
     * *             used after validation process.
     */
    fun onValidate(bean: B?)
}
