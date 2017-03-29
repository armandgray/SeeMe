package com.armandgray.seeme.controllers;

import android.content.Context;
import android.os.Parcelable;
import android.widget.Toast;

import com.armandgray.seeme.models.User;
import com.armandgray.seeme.views.NetworkFragment;

import static com.armandgray.seeme.network.HttpHelper.sendRequest;
import static com.armandgray.seeme.views.NetworkFragment.NETWORK_CONNECTION_URI;

/**
 * Created by armandgray on 3/28/17.
 */

public class NetworkFragmentController implements NetworkFragment.NetworkController {

    private Context context;
    private User activeUser;

    public NetworkFragmentController(Context context, User activeUser) {
        this.context = context;
        this.activeUser = activeUser;
    }

    @Override
    public void sendNetworkRequest() {
        String url = NETWORK_CONNECTION_URI + "username=" + activeUser.getUsername();
        sendRequest(url, context);
    }

    @Override
    public void handleHttpResponse(String response, Parcelable[] arrayExtra) {
        Toast.makeText(context, "Response Received", Toast.LENGTH_SHORT).show();
    }
}
