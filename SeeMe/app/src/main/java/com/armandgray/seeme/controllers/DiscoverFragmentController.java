package com.armandgray.seeme.controllers;

import android.content.Context;
import android.widget.Toast;

import com.armandgray.seeme.models.User;
import com.armandgray.seeme.views.DiscoverFragment.*;

public class DiscoverFragmentController implements DiscoverController {

    private Context context;
    private DiscoverClickListener listener;

    public DiscoverFragmentController(Context context, DiscoverClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void onRecyclerItemClick(User user) {
        if (user != null) {
            Toast.makeText(context,
                    "Request Sent to " + user.getFirstName() + " " + user.getLastName(),
                    Toast.LENGTH_SHORT).show();
            listener.onUserClick(user);
        }
    }
}
