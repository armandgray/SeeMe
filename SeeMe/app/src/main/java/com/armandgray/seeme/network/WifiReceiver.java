package com.armandgray.seeme.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.armandgray.seeme.observers.BroadcastObserver;

public class WifiReceiver extends BroadcastReceiver {

    public static final String TAG = "WifiReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)
                || intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            BroadcastObserver.getInstance().updateWifiState(activeNetInfo);
        }
    }

}
