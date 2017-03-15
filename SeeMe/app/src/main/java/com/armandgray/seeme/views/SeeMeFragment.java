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

import com.armandgray.seeme.MainActivity;
import com.armandgray.seeme.R;
import com.armandgray.seeme.utils.BroadcastObserver;
import com.armandgray.seeme.utils.NetworkHelper;

import java.util.Observable;
import java.util.Observer;

import static com.armandgray.seeme.utils.HttpHelper.sendRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeeMeFragment extends Fragment
        implements Observer {

    private static final String TAG = "TAG";
    private static final String LOCAL_USERS_URI = MainActivity.API_URI + "/discoverable/localusers?networkId=";

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
        BroadcastObserver.getInstance().addObserver(this);

        ImageView ivSeeMe = (ImageView) rootView.findViewById(R.id.ivSeeMe);
        ivSeeMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkOK && isWifiConnected) {
                    String url = LOCAL_USERS_URI
                            + networkId
                            + "&ssid="+ ssid.substring(1, ssid.length() - 1).replaceAll(" ", "%20")
                            // TODO Replace dummy user with Active User
                            + "&username=danimeza@gmail.com";

                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_container, new DiscoverFragment())
                            .addToBackStack(TAG)
                            .commit();

                    sendRequest(url, getContext());
                } else {
                    Toast.makeText(getContext(), "WiFi Connection Unsuccessful!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }

    @Override
    public void update(Observable o, Object data) {
        isWifiConnected = data != null;
        if (data != null) {
            getWifiNetworkId();
        }
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
