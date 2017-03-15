package com.armandgray.seeme.controllers;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.armandgray.seeme.R;
import com.armandgray.seeme.models.User;
import com.armandgray.seeme.views.SeeMeFragment;
import com.armandgray.seeme.views.SeeMeFragment.SeeMeTouchListener;

import static com.armandgray.seeme.utils.HttpHelper.sendRequest;
import static com.armandgray.seeme.views.SeeMeFragment.LOCAL_USERS_URI;

public class SeeMeFragmentController implements SeeMeFragment.SeeMeContoller {

    private User activeUser;
    private SeeMeTouchListener seeMeTouchListener;
    private Context context;

    private boolean isWifiConnected;
    private boolean networkOK;
    private String ssid;
    private String networkId;


    public SeeMeFragmentController(User activeUser, SeeMeTouchListener seeMeTouchListener, Context context) {
        this.activeUser = activeUser;
        this.seeMeTouchListener = seeMeTouchListener;
        this.context = context;
        this.ssid = "";
        this.networkId = "";
    }

    @Override
    public void requestLocalUsers() {
        if (networkOK && isWifiConnected) {
            String url = LOCAL_USERS_URI
                    + networkId
                    + "&ssid="+ ssid.substring(1, ssid.length() - 1).replaceAll(" ", "%20")
                    + "&username=" + activeUser.getUsername();
            sendRequest(url, context);
            seeMeTouchListener.onTouchSeeMe();
        } else {
            Toast.makeText(context, "WiFi Connection Unsuccessful!", Toast.LENGTH_SHORT).show();
        }
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
