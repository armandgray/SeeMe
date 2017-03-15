package com.armandgray.seeme.views;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.armandgray.seeme.R;
import com.armandgray.seeme.utils.NetworkHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeeMeFragment extends Fragment {


    private boolean isWifiConnected;
    private String ssid;
    private String networkId;
    private ImageView ivWifi;
    private boolean networkOK;

    public SeeMeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_see_me, container, false);

        ivWifi = (ImageView) rootView.findViewById(R.id.ivWifi);

        ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null) {
            isWifiConnected = networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        }
        if (isWifiConnected) {
            getWifiNetworkId();
        }

        networkOK = NetworkHelper.hasNetworkAccess(getContext());

        ImageView ivSeeMe = (ImageView) rootView.findViewById(R.id.ivSeeMe);
        ivSeeMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkOK && isWifiConnected) {
                    Toast.makeText(getContext(), "WiFi Connection Good!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "WiFi Connection Unsuccessful!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }

    private void getWifiNetworkId() {
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext()
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
