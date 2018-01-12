/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

@file:Suppress("unused")

package com.logosprog.mainutils.system

import java.net.InetAddress
import java.net.NetworkInterface

/**
 * Gets a mac address of the machine
 */

fun macAddress(): String{
    val ip: InetAddress = InetAddress.getByName("127.0.0.1")

    val network = NetworkInterface.getByInetAddress(ip)

    val mac: ByteArray = network.hardwareAddress

    val sb = StringBuilder()

    for ((i, byte) in mac.withIndex()){
        sb.append("$byte${if(i < mac.size - 1) "-" else ""}")
    }

    return sb.toString()
}