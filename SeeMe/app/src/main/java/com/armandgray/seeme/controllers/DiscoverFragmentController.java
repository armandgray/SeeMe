package com.armandgray.seeme.controllers;

import android.content.Context;
import android.widget.Toast;

import com.armandgray.seeme.models.User;
import com.armandgray.seeme.views.DiscoverFragment.*;

import static com.armandgray.seeme.network.HttpHelper.sendRequest;
import static com.armandgray.seeme.views.DiscoverFragment.NEW_CONNECTION_URI;

public class DiscoverFragmentController implements DiscoverController {

    private Context context;
    private DiscoverClickListener listener;
    private User activeUser;

    public DiscoverFragmentController(Context context, DiscoverClickListener listener, User activeUser) {
        this.context = context;
        this.listener = listener;
        this.activeUser = activeUser;
    }

    @Override
    public void onRecyclerItemClick(User user) {
        if (user != null) {
            Toast.makeText(context,
                    "Request Sent to " + user.getFirstName() + " " + user.getLastName(),
                    Toast.LENGTH_SHORT).show();
            String url = NEW_CONNECTION_URI
                    + "username=" + activeUser.getUsername()
                    + "&connection=" + user.getUsername();
            sendRequest(url, context);
            listener.onUserClick(user);
        }
    }
}
