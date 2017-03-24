package com.armandgray.seeme.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.armandgray.seeme.observers.BroadcastObserver;

public class WifiReceiver extends BroadcastReceiver {
    private boolean isWifiConnected;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            BroadcastObserver.getInstance().updateWifiState(activeNetInfo);
        } else {
            BroadcastObserver.getInstance().updateWifiState(null);
        }
    }

}
