package com.armandgray.seeme.controllers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.armandgray.seeme.models.User;
import com.armandgray.seeme.views.SeeMeFragment;
import com.armandgray.seeme.views.SeeMeFragment.SeeMeTouchListener;

import static com.armandgray.seeme.utils.HttpHelper.sendRequest;
import static com.armandgray.seeme.views.SeeMeFragment.LOCAL_USERS_URI;

public class SeeMeFragmentController implements SeeMeFragment.SeeMeController {

    private static final String TAG = "SEEME_CONTROLLER";
    private User activeUser;
    private SeeMeTouchListener seeMeTouchListener;
    private Context context;

    private String ssid = "";
    private String networkId = "";

    private int requestInterval = 3000;
    private Handler handler;

    public SeeMeFragmentController(User activeUser, SeeMeTouchListener seeMeTouchListener, Context context) {
        this.activeUser = activeUser;
        this.seeMeTouchListener = seeMeTouchListener;
        this.context = context;
        this.handler = new Handler();
    }

    public boolean getWifiState() {
        if (getWifiConnectionState()) {
            getWifiNetworkId();
            return true;
        }
        return false;
    }

    private boolean getWifiConnectionState() {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
             return networkInfo != null
                     && networkInfo.isConnected()
                     && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    private void getWifiNetworkId() {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService (Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        ssid  = wifiInfo.getSSID();
        networkId = wifiInfo.getBSSID();
        if (ssid.equals("<unknown ssid>")) {
            Log.i("ActiveNetInfo", "Wifi Network Not Found: " + String.valueOf(ssid));
        } else {
            Log.i("ActiveNetInfo", "Wifi Network " + String.valueOf(ssid) + ": " + networkId);
        }
    }

    @Override
    public void requestLocalUsers() {
        if (!getWifiConnectionState()) {
            Toast.makeText(context, "Wifi Connection Unsuccessful!", Toast.LENGTH_SHORT).show();
            return;
        }
        sendLocalUsersRequest();
        seeMeTouchListener.onTouchSeeMe();
    }

    private void sendLocalUsersRequest() {
        String url = LOCAL_USERS_URI
                + networkId
                + "&ssid="+ ssid.substring(1, ssid.length() - 1).replaceAll(" ", "%20")
                + "&username=" + activeUser.getUsername();
        sendRequest(url, context);
    }

    @Override
    public void startAutoRequests() {
        if (!getWifiConnectionState()) {
            Toast.makeText(context, "Wifi Connection Unsuccessful!", Toast.LENGTH_SHORT).show();
            return;
        }
        statusChecker.run();
    }

    @Override
    public void stopAutoRequests() {
        handler.removeCallbacks(statusChecker);
    }

    private Runnable statusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                if (getWifiConnectionState()) {
//                    sendLocalUsersRequest();
                    Log.i(TAG, "Requesting...");
                }
                updateStatusOrRequestInterval();
            } finally {
                handler.postDelayed(statusChecker, requestInterval);
            }
        }
    };

    private void updateStatusOrRequestInterval() {}
}