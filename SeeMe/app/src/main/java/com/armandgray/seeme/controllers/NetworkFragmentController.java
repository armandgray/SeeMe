package com.armandgray.seeme.controllers;

import android.content.Context;
import android.os.Parcelable;
import android.widget.Toast;

import com.armandgray.seeme.models.User;
import com.armandgray.seeme.views.NetworkFragment;

import java.util.Arrays;

import static com.armandgray.seeme.network.HttpHelper.sendRequest;
import static com.armandgray.seeme.views.NetworkFragment.NETWORK_CONNECTION_URI;

public class NetworkFragmentController implements NetworkFragment.NetworkController {

    private static final String USER_NOT_FOUND = "User Not Found!";
    private static final String CONNECTION_CONFIRMED = "Connection Confirmed";
    private static final String CONNECTION_DELETED = "Connection Deleted!";
    private static final String REQUEST_SENT = "Request Sent";
    private static final String PREPARE_UPDATE_ERROR = "Prepare Update Error!";
    private static final String UPDATE_QUERY_ERROR = "Update Query Error!";
    private static final String INTERNAL_UPDATE_ERROR = "Internal Update Error!";

    private String[] responseArray = {USER_NOT_FOUND, PREPARE_UPDATE_ERROR, CONNECTION_CONFIRMED, CONNECTION_DELETED,
            REQUEST_SENT, UPDATE_QUERY_ERROR, INTERNAL_UPDATE_ERROR};
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
        if (response != null && !response.equals("") && Arrays.asList(responseArray).contains(response)) {
            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
        }
    }
}
