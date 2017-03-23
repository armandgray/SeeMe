package com.armandgray.seeme.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.armandgray.seeme.models.Network;

public class NetworkHelper {

    public static final String WIFI_BROADCAST_MESSAGE = "android.net.wifi.STATE_CHANGE";

    public static boolean hasNetworkAccess(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    };

    private static NetworkInfo getNetworkInfo(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean getWifiConnectionState(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null
                && networkInfo.isConnected()
                && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static Network getWifiNetwork(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService (Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        String networkId = wifiInfo.getBSSID();
        if (ssid.equals("<unknown ssid>")) {
            Log.i("ActiveNetInfo", "Wifi Network Not Found: " + String.valueOf(ssid));
        } else {
            Log.i("ActiveNetInfo", "Wifi Network " + String.valueOf(ssid) + ": " + networkId);
        }

        return new Network(ssid, networkId);
    }
}
