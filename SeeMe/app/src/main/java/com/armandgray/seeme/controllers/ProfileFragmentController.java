package com.armandgray.seeme.controllers;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.armandgray.seeme.models.User;
import com.armandgray.seeme.views.DeleteAccountDialog;
import com.armandgray.seeme.views.ProfileFragment;

import java.util.Arrays;
import java.util.HashMap;

import static com.armandgray.seeme.utils.HttpHelper.sendRequest;
import static com.armandgray.seeme.views.ProfileFragment.DELETE_URL;
import static com.armandgray.seeme.views.ProfileFragment.DISCOVERABLE;
import static com.armandgray.seeme.views.ProfileFragment.ET_EDIT;
import static com.armandgray.seeme.views.ProfileFragment.ITEM_DISCOVERABLE;
import static com.armandgray.seeme.views.ProfileFragment.ITEM_FULL_NAME;
import static com.armandgray.seeme.views.ProfileFragment.ITEM_PASSWORD;
import static com.armandgray.seeme.views.ProfileFragment.ITEM_ROLE;
import static com.armandgray.seeme.views.ProfileFragment.TV_CONTENT;
import static com.armandgray.seeme.views.ProfileFragment.UDPATE_URL;

public class ProfileFragmentController implements ProfileFragment.ProfileController {

    private static final String DIALOG = "DELETE_ACCOUNT_DIALOG";
    private static final String PASSWORD_INCORRECT = "Password Incorrect!";
    private static final String USER_NOT_FOUND = "User Not Found!";
    private static final String UPDATE_FAILED = "Update Failed!";
    private static final String ACCOUNT_DELETED = "Account Deleted!";
    private static final String TAG = "PROFILE_CONTROLLER";

    private String[] responseArray = {USER_NOT_FOUND, PASSWORD_INCORRECT, UPDATE_FAILED, ACCOUNT_DELETED};
    private User activeUser;
    private Fragment fragment;
    private ProfileUpdateListener updateListener;

    public ProfileFragmentController(User activeUser, Fragment fragment, ProfileUpdateListener listener) {
        this.activeUser = activeUser;
        this.fragment = fragment;
        this.updateListener = listener;
    }

    @Override
    public void postUpdateRequest(HashMap<String, HashMap> itemsMap) {
        EditText etEdit = (EditText) itemsMap.get(ITEM_FULL_NAME).get(ET_EDIT);
        if (!verifyFullName(etEdit.getText().toString())) { return; }

        StringBuilder url = new StringBuilder();
        url.append(UDPATE_URL);
        url.append("username=").append(activeUser.getUsername());
        url.append("&oldSecret=").append("111111");

        for (String itemTitle : itemsMap.keySet()) {
            etEdit = (EditText) itemsMap.get(itemTitle).get(ET_EDIT);
            if (!itemTitle.equals(ITEM_DISCOVERABLE)) {
                addUrlParameter(itemTitle,
                    etEdit.getText().toString(), url);
            }
        }
        addUrlDiscoverableParam(itemsMap, url);

        sendRequest(url.toString(), fragment.getContext());
    }

    private boolean verifyFullName(String text) {
        int indexOfSpaceDelimeter = text.indexOf(' ');
        if (indexOfSpaceDelimeter == -1) {
            Toast.makeText(fragment.getContext(), "Please Enter Full Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (text.substring(indexOfSpaceDelimeter + 1, text.length()).indexOf(' ') != -1) {
            Toast.makeText(fragment.getContext(), "Please Hyphenate Additional Names", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void addUrlParameter(String itemTitle, String text, StringBuilder url) {
        switch (itemTitle) {
            case ITEM_FULL_NAME:
                int indexOfSpaceDelimeter = text.indexOf(' ');
                String firstName = text.substring(0, indexOfSpaceDelimeter);
                String lastName = text.substring(indexOfSpaceDelimeter + 1, text.length());

                url.append("&firstName=").append(firstName);
                url.append("&lastName=").append(lastName);
                return;
            case ITEM_PASSWORD:
                url.append("&password=").append(text);
                return;
            case ITEM_ROLE:
                url.append("&role=").append(text);
                return;
            case ITEM_DISCOVERABLE:
                url.append("&discoverable=").append(text);
        }
    }

    private void addUrlDiscoverableParam(HashMap<String, HashMap> itemsMap, StringBuilder url) {
        TextView tvDiscoverable = (TextView) itemsMap.get(ITEM_DISCOVERABLE).get(TV_CONTENT);
        if (tvDiscoverable.getText().equals(DISCOVERABLE)) {
            addUrlParameter(ITEM_DISCOVERABLE, "true", url);
        } else {
            addUrlParameter(ITEM_DISCOVERABLE, "false", url);
        }
    }

    @Override
    public void postFeedBack() {
        Toast.makeText(fragment.getActivity(), "Post Feedback", Toast.LENGTH_SHORT).show();
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
            if (response.equals(ACCOUNT_DELETED)) {
                updateListener.onAccountDelete();
            }
        } else if (parcelableArrayExtra != null && parcelableArrayExtra.length != 0) {
            updateListener.onAccountUpdate((User) parcelableArrayExtra[0]);
        } else {
            Log.i(TAG, response);
        }
    }

    public interface ProfileUpdateListener {
        void onAccountUpdate(User updatedUser);
        void onAccountDelete();
    }
}

