package com.armandgray.seeme.controllers;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.armandgray.seeme.models.Network;
import com.armandgray.seeme.models.User;
import com.armandgray.seeme.network.HttpHelper;
import com.armandgray.seeme.views.SeeMeFragment;
import com.armandgray.seeme.views.SeeMeFragment.SeeMeTouchListener;

import static com.armandgray.seeme.network.HttpHelper.sendPostRequest;
import static com.armandgray.seeme.network.NetworkHelper.getWifiConnectionState;
import static com.armandgray.seeme.network.NetworkHelper.getWifiNetwork;
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
        sendLocalUsersRequest();
        seeMeTouchListener.onTouchSeeMe();
    }

    private void sendLocalUsersRequest() {
        Network network = getWifiNetwork(context);
        ssid = network.getSsid();
        networkId = network.getNetworkId();
        String url = LOCAL_USERS_URI
                + networkId
                + "&ssid="+ ssid.substring(1, ssid.length() - 1).replaceAll(" ", "%20")
                + "&username=" + activeUser.getUsername();
        HttpHelper.sendGetRequest(url, context);
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
