/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.system

import java.net.InetAddress
import java.net.NetworkInterface

/**
 * Gets a mac address of the machine
 */

fun macAddress(): String{
    val ip: InetAddress

    ip = InetAddress.getByName("127.0.0.1")

    val network = NetworkInterface.getByInetAddress(ip)

    val mac: ByteArray = network.hardwareAddress

    var i = 0
    val sb = StringBuilder()

    for (byte in mac){
        sb.append("${byte}${if(i < mac.size - 1) "-" else ""}")
        i++
    }

    return sb.toString()
}