package com.armandgray.seeme.controllers;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.armandgray.seeme.models.User;
import com.armandgray.seeme.views.DeleteAccountDialog;
import com.armandgray.seeme.views.ProfileFragment;

import static com.armandgray.seeme.utils.HttpHelper.sendRequest;
import static com.armandgray.seeme.views.ProfileFragment.DELETE_URL;

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
    public void postConfirmedDeleteRequest(String username, String password) {
        if (!username.equals(activeUser.getUsername())) {
            Toast.makeText(fragment.getContext(), "Update Cancelled: Non Active User!", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = DELETE_URL
                + "username=" + username
                + "&password=" + password;
        sendRequest(url, fragment.getContext());
    }
}
