package com.armandgray.seeme.controllers;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.armandgray.seeme.models.User;
import com.armandgray.seeme.utils.RecyclerItemClickListener;
import com.armandgray.seeme.utils.UserRVAdapter;
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
    private Activity activity;
    private User activeUser;

    public NetworkFragmentController(Activity activity, User activeUser) {
        this.activity = activity;
        this.activeUser = activeUser;
    }

    @Override
    public void sendNetworkRequest() {
        String url = NETWORK_CONNECTION_URI + "username=" + activeUser.getUsername();
        sendRequest(url, activity);
    }

    @Override
    public void handleHttpResponse(String response, Parcelable[] arrayExtra) {
        if (response != null && !response.equals("") && Arrays.asList(responseArray).contains(response)) {
            Toast.makeText(activity, response, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setupRvUsers(RecyclerView rvUsers, final User[] networkArray) {
        rvUsers.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        rvUsers.setAdapter(new UserRVAdapter(activity, Arrays.asList(networkArray)));
        rvUsers.addOnItemTouchListener(new RecyclerItemClickListener(activity,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (networkArray.length >= position) {
                            onRecyclerItemClick(networkArray[position]);
                        }
                    }
                }));
    }

    @Override
    public void onRecyclerItemClick(User user) {
        if (user != null) {
            Toast.makeText(activity, user.getUsername() + " Click", Toast.LENGTH_SHORT).show();
        }
    }
}
