package com.armandgray.seeme.controllers;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.armandgray.seeme.models.User;
import com.armandgray.seeme.views.DeleteAccountDialog;
import com.armandgray.seeme.views.ProfileFragment;

public class ProfileFragmentController implements ProfileFragment.ProfileController {

    private static final String DIALOG = "DELETE_ACCOUNT_DIALOG";
    private User activeUser;
    private Fragment fragment;

    public ProfileFragmentController(User activeUser, Fragment fragment) {
        this.activeUser = activeUser;
        this.fragment = fragment;
    }

    @Override
    public void postDeleteRequest() {
        new DeleteAccountDialog().show(
                fragment.getChildFragmentManager(), DIALOG);
    }

    @Override
    public void postConfirmedDeleteRequest() {
        Toast.makeText(fragment.getContext(), "Try Confirmed Delete User", Toast.LENGTH_SHORT).show();
    }
}
