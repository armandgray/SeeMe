package com.armandgray.seeme.controllers;

import android.content.Context;
import android.widget.Toast;

import com.armandgray.seeme.models.User;
import com.armandgray.seeme.views.DiscoverFragment;

public class DiscoverFragmentController implements DiscoverFragment.DiscoverController {

    private Context context;

    public DiscoverFragmentController(Context context) {
        this.context = context;
    }

    @Override
    public void onRecyclerItemClick(User user) {
        if (user != null) {
            Toast.makeText(context,
                    "Request Sent to " + user.getFirstName() + " " + user.getLastName(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
