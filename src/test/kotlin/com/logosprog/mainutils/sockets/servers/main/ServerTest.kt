/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.sockets.servers.main

import com.logosprog.mainutils.sockets.servers.server.SocketManager
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.contrib.java.lang.system.SystemOutRule
import java.net.Socket
import java.util.regex.Pattern

class ServerTest{

    object socketManagerDummy : SocketManager {
        override fun accept(socket: Socket) {
            //dummy
        }

        override fun closeAll() {
            //dummy
        }

    }

    val server : Server<socketManagerDummy> = Server(socketManagerDummy)

    @Rule
    @JvmField
    val systemOutRule : SystemOutRule = SystemOutRule().enableLog()

    @Test
    fun ServerBootsAndStops(){
        server.start()
        assertTrue("Server has not been run", Pattern.compile(".+server: Running on port+.").matcher(systemOutRule.log).find())
        server.stop()
        assertTrue("Server has not been stopped",Pattern.compile(".+server: stopped+.").matcher(systemOutRule.log).find())
    }

    @Test
    fun ServerSignalizeWhenPortIsInUse(){
        server.start()
        val anotherServer : Server<socketManagerDummy> = Server(socketManagerDummy)
        assertFalse("Server did not signalize that port is already in use", anotherServer.start())
        server.stop()
        anotherServer.stop()
    }
}
