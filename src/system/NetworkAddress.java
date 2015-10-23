/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package system;

import java.net.*;
import java.util.Enumeration;

/**
 * Created by forando on 20.10.15.<br/>
 * Gets an address of the machine
 */
public class NetworkAddress {

    /**
     *
     * @param addressType Can be: <ul>
     *                    <li><b>ip</b> - if you want to get current ip</li>
     *                    <li><b>mac</b> - if you want to get working adapter mac address</li>
     *                    </ul>
     * @return An address depending on the addressType.
     */
    public static String GetAddress(String addressType){
        String address = "";
        InetAddress lanIp = null;
        try {

            String ipAddress = null;
            Enumeration<NetworkInterface> net = null;
            net = NetworkInterface.getNetworkInterfaces();
            while(net.hasMoreElements()){
                NetworkInterface element = net.nextElement();
                Enumeration<InetAddress> addresses = element.getInetAddresses();
                while (addresses.hasMoreElements()){
                    InetAddress ip = addresses.nextElement();
                    if (ip instanceof Inet4Address){

                        if (ip.isSiteLocalAddress()){

                            ipAddress = ip.getHostAddress();
                            lanIp = InetAddress.getByName(ipAddress);
                        }

                    }

                }
            }

            if(lanIp == null) return null;

            if(addressType.equals("ip")){

                address = lanIp.toString().replaceAll("^/+", "");

            }else if(addressType.equals("mac")){

                address = GetMacAddress(lanIp);

            }else{

                throw new Exception("Specify \"ip\" or \"mac\"");

            }

        } catch (UnknownHostException e) {

            e.printStackTrace();

        } catch (SocketException e){

            e.printStackTrace();

        } catch (Exception e){

            e.printStackTrace();

        }

        return address;

    }

    private static String GetMacAddress(InetAddress ip){
        String address = null;
        try {

            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            address = sb.toString();

        } catch (SocketException e) {

            e.printStackTrace();

        }

        return address;
    }
}
