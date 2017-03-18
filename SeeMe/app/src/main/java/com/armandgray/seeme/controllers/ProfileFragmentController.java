package com.armandgray.seeme.controllers;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.armandgray.seeme.models.User;
import com.armandgray.seeme.views.DeleteAccountDialog;
import com.armandgray.seeme.views.ProfileFragment;

import java.util.Arrays;

import static com.armandgray.seeme.utils.HttpHelper.sendRequest;
import static com.armandgray.seeme.views.ProfileFragment.DELETE_URL;

public class ProfileFragmentController implements ProfileFragment.ProfileController {

    private static final String DIALOG = "DELETE_ACCOUNT_DIALOG";
    private static final String PASSWORD_INCORRECT = "Password Incorrect!";
    private static final String USER_NOT_FOUND = "User Not Found!";
    private static final String UPDATE_FAILED = "Update Failed!";
    private static final String ACCOUNT_DELETED = "Account Deleted!";

    private String[] responseArray = {USER_NOT_FOUND, PASSWORD_INCORRECT, UPDATE_FAILED, ACCOUNT_DELETED};
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

    @Override
    public void handleHttpResponse(String response, Parcelable[] parcelableArrayExtra) {
        if (response != null && !response.equals("")) {
            if (Arrays.asList(responseArray).contains(response)) {
                Toast.makeText(fragment.getContext(), response, Toast.LENGTH_SHORT).show();
            }
        } else {

        }
    }
}
