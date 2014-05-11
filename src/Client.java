package com.example.comp4985androidgpsassignment3;

/**
 * Created by User on 3/1/14.
 */
public class Client {

    private static String ip_address;
    private static long latitude;
    private static long longitude;

    public Client(String ip, long lat, long lon){
        ip_address = ip;
        latitude   = lat;
        longitude  = lon;
    }

    public static String getIp_address() {
        return ip_address;
    }

    public static long getLatitude() {
        return latitude;
    }

    public static long getLongitude() {
        return longitude;
    }
}
