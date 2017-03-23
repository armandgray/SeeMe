package com.armandgray.seeme.controllers;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.armandgray.seeme.models.Network;
import com.armandgray.seeme.models.User;
import com.armandgray.seeme.views.SeeMeFragment;
import com.armandgray.seeme.views.SeeMeFragment.SeeMeTouchListener;

import static com.armandgray.seeme.MainActivity.UPDATE_NETWORK_URI;
import static com.armandgray.seeme.utils.HttpHelper.sendRequest;
import static com.armandgray.seeme.utils.NetworkHelper.getWifiConnectionState;
import static com.armandgray.seeme.utils.NetworkHelper.getWifiNetwork;
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

    @Override
    public void requestLocalUsers() {
        if (!getWifiConnectionState(context)) {
            Toast.makeText(context, "Wifi Connection Unsuccessful!", Toast.LENGTH_SHORT).show();
            return;
        }
        updateUserNetwork();
        sendLocalUsersRequest();
        seeMeTouchListener.onTouchSeeMe();
    }

    private void updateUserNetwork() {
        Network network = getWifiNetwork(context);
        ssid = network.getSsid();
        networkId = network.getNetworkId();
        sendRequest(UPDATE_NETWORK_URI + networkId, context);
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
        if (!getWifiConnectionState(context)) {
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
                if (getWifiConnectionState(context)) {
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
