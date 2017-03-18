package com.armandgray.seeme.controllers;

import android.app.Activity;
import android.widget.Toast;

import com.armandgray.seeme.models.User;
import com.armandgray.seeme.views.ProfileFragment;

public class ProfileFragmentController implements ProfileFragment.ProfileController {

    private User activeUser;
    private Activity activity;

    public ProfileFragmentController(User activeUser, Activity activity) {
        this.activeUser = activeUser;
        this.activity = activity;
    }

    @Override
    public void postDeleteRequest() {
        Toast.makeText(activity, "Try Delete User", Toast.LENGTH_SHORT).show();
    }
}
