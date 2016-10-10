/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.system;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by forando on 14.10.15.<br>
 * Gets a mac address of the machine
 */
public class MacAddress1 {

    /**
     *
     * @return A mac address of the adapter
     */
    public static String getMacAddress(){
        String macAdd = null;

        InetAddress ip;
        try {

//            ip = InetAddress.getLocalHost();
            ip = InetAddress.getByName("127.0.0.1");
//            System.out.println("Current IP address : " + ip.getHostAddress());

            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            byte[] mac = network.getHardwareAddress();

//            System.out.print("Current MAC address : ");

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            macAdd = sb.toString();

        } catch (UnknownHostException e) {

            e.printStackTrace();

        } catch (SocketException e){

            e.printStackTrace();

        }

        return macAdd;
    }
}
