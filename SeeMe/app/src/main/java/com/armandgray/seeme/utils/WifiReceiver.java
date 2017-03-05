package com.armandgray.seeme.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class WifiReceiver extends BroadcastReceiver {
    public boolean isWifiConnected = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            Toast.makeText(context, "Wifi Connected!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Wifi Not Connected!", Toast.LENGTH_SHORT).show();
        }
    }
}
