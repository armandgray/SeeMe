package com.armandgray.seeme.controllers;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.ImageView;

import com.armandgray.seeme.R;
import com.armandgray.seeme.models.User;
import com.armandgray.seeme.views.SeeMeFragment;
import com.armandgray.seeme.views.SeeMeFragment.SeeMeTouchListener;

public class SeeMeFragmentController implements SeeMeFragment.SeeMeContoller {

    private User activeUser;
    private SeeMeTouchListener seeMeTouchListener;
    private Context context;
    private String ssid;
    private String networkId;

    public SeeMeFragmentController(User activeUser, SeeMeTouchListener seeMeTouchListener, Context context) {
        this.activeUser = activeUser;
        this.seeMeTouchListener = seeMeTouchListener;
        this.context = context;
    }

    @Override
    public void getWifiNetworkId(ImageView ivWifi) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService (Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        ssid  = wifiInfo.getSSID();
        networkId = wifiInfo.getBSSID();
        if (ssid.equals("<unknown ssid>")) {
            Log.i("ActiveNetInfo", "Wifi Network Not Found: " + String.valueOf(ssid));
            ivWifi.setImageResource(R.drawable.ic_wifi_off_white_48dp);
        } else {
            Log.i("ActiveNetInfo", "Wifi Network " + String.valueOf(ssid) + ": " + networkId);
            ivWifi.setImageResource(R.drawable.ic_wifi_white_48dp);
        }
    }
}
