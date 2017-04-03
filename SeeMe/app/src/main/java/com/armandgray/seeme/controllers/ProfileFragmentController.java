package com.armandgray.seeme.controllers;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.armandgray.seeme.R;
import com.armandgray.seeme.models.User;
import com.armandgray.seeme.views.ConfirmPasswordDialog;
import com.armandgray.seeme.views.DeleteAccountDialog;
import com.armandgray.seeme.views.PostFeedbackDialog;
import com.armandgray.seeme.views.ProfileFragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;

import static com.armandgray.seeme.network.HttpHelper.sendPostRequest;
import static com.armandgray.seeme.utils.StringHelper.PASSWORD_PTR;
import static com.armandgray.seeme.utils.StringHelper.capitalizeString;
import static com.armandgray.seeme.utils.StringHelper.urlify;
import static com.armandgray.seeme.views.ProfileFragment.DELETE_URL;
import static com.armandgray.seeme.views.ProfileFragment.DISCOVERABLE;
import static com.armandgray.seeme.views.ProfileFragment.ET_EDIT;
import static com.armandgray.seeme.views.ProfileFragment.FEEDBACK_URL;
import static com.armandgray.seeme.views.ProfileFragment.HIDDEN;
import static com.armandgray.seeme.views.ProfileFragment.ITEM_DISCOVERABLE;
import static com.armandgray.seeme.views.ProfileFragment.ITEM_FULL_NAME;
import static com.armandgray.seeme.views.ProfileFragment.ITEM_PASSWORD;
import static com.armandgray.seeme.views.ProfileFragment.ITEM_OCCUPATION;
import static com.armandgray.seeme.views.ProfileFragment.IV_CLOUD;
import static com.armandgray.seeme.views.ProfileFragment.IV_ICON;
import static com.armandgray.seeme.views.ProfileFragment.TV_CONTENT;
import static com.armandgray.seeme.views.ProfileFragment.UDPATE_URL;

public class ProfileFragmentController implements ProfileFragment.ProfileController {

    private static final String DIALOG = "DELETE_ACCOUNT_DIALOG";
    private static final String PASSWORD_INCORRECT = "Password Incorrect!";
    private static final String USER_NOT_FOUND = "User Not Found!";
    private static final String UPDATE_FAILED = "Update Failed!";
    private static final String ACCOUNT_DELETED = "Account Deleted!";
    private static final String TAG = "PROFILE_CONTROLLER";
    private static final String MESSAGE_SENT = "Message Sent!";
    private static final String MESSAGE_UPLOAD_FAILED = "Message Upload Failed!";

    private String[] responseArray = {USER_NOT_FOUND, PASSWORD_INCORRECT, UPDATE_FAILED, ACCOUNT_DELETED,
            MESSAGE_UPLOAD_FAILED, MESSAGE_SENT};
    private User activeUser;
    private Fragment fragment;
    private ProfileUpdateListener updateListener;
    private HashMap<String, HashMap> itemsMap;
    private String url;

    public ProfileFragmentController(User activeUser,
                                     Fragment fragment,
                                     HashMap<String, HashMap> itemsMap,
                                     ProfileUpdateListener listener) {
        this.activeUser = activeUser;
        this.fragment = fragment;
        this.itemsMap = itemsMap;
        this.updateListener = listener;
        this.url = "";
    }

    @Override
    public void setupItemContent(User user) {
        setupItem(itemsMap.get(ITEM_FULL_NAME), R.drawable.ic_account_outline_white_48dp,
                user.getFirstName() + " " + user.getLastName());
        setupItem(itemsMap.get(ITEM_PASSWORD), R.drawable.ic_lock_open_outline_white_48dp, "0000000");
        setupItem(itemsMap.get(ITEM_OCCUPATION), R.drawable.ic_tools_resources, user.getOccupation());
        setupItem(itemsMap.get(ITEM_DISCOVERABLE), R.drawable.ic_earth_white_48dp,
                user.isDiscoverable() ? DISCOVERABLE : HIDDEN);

    }

    private void setupItem(HashMap item, int drawable, String content) {
        ImageView ivIcon = (ImageView) item.get(IV_ICON);
        ivIcon.setImageResource(drawable);
        TextView tvContent = (TextView) item.get(TV_CONTENT);
        tvContent.setText(content);
    }

    @Override
    public void postUpdateRequest(HashMap<String, HashMap> itemsMap) {
        String defaultUrl = UDPATE_URL + "username=" + activeUser.getUsername();
        StringBuilder urlBuilder = new StringBuilder(defaultUrl);

        for (String itemTitle : itemsMap.keySet()) {
            EditText etEdit = (EditText) itemsMap.get(itemTitle).get(ET_EDIT);
            if (!itemTitle.equals(ITEM_DISCOVERABLE)) {
                String text = etEdit.getText().toString();
                if (!verifyFields(itemTitle, text)) { return; }
                if (!text.equals("")) { addUrlParameter(itemTitle, text, urlBuilder); }
            }
        }
        addUrlDiscoverableParam(itemsMap, urlBuilder);
        url = urlBuilder.toString();
        new ConfirmPasswordDialog().show(
                fragment.getChildFragmentManager(), DIALOG);
    }

    private boolean verifyFields(String itemTitle, String text) {
        if (text.equals("")) { return true; }

        switch (itemTitle) {
            case ITEM_FULL_NAME:
                return verifyFullName(text);
            case ITEM_PASSWORD:
                if (!text.matches(PASSWORD_PTR)) {
                    Toast.makeText(fragment.getContext(), "Password Must Contain: \n- At least 1 number\n- No whitespace\n- At least 6 characters", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
        }
        return true;
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

                url.append("&firstName=").append(capitalizeString(firstName));
                url.append("&lastName=").append(capitalizeString(lastName));
                return;
            case ITEM_PASSWORD:
                url.append("&password=").append(text);
                return;
            case ITEM_OCCUPATION:
                url.append("&role=").append(urlify(capitalizeString(text)));
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
        new PostFeedbackDialog().show(
                fragment.getChildFragmentManager(), DIALOG);
    }

    @Override
    public void postFeedBackMessage(String message) {
        String url = "";
        try {
            url = FEEDBACK_URL
                    + "username=" + activeUser.getUsername()
                    + "&message=" + URLEncoder.encode(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        sendPostRequest(url, fragment.getContext());
    }

    @Override
    public void onConfirmPassword(String password) {
        url += "&oldSecret=" + password;
        sendPostRequest(url, fragment.getContext());
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
        url = DELETE_URL
                + "username=" + username
                + "&password=" + password;
        sendPostRequest(url, fragment.getContext());
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
            User newUser = (User) parcelableArrayExtra[0];
            updateListener.onAccountUpdate(newUser);
            Toast.makeText(fragment.getContext(), "Account Updated!", Toast.LENGTH_SHORT).show();
            resetUI(newUser);
        }
    }

    @Override
    public void resetUI(User user) {
        setupItemContent(user);

        for (String itemTitle : itemsMap.keySet()) {
            EditText etEdit = (EditText) itemsMap.get(itemTitle).get(ET_EDIT);
            etEdit.setText("");
            ImageView ivCloud = (ImageView) itemsMap.get(itemTitle).get(IV_CLOUD);
            ivCloud.setImageResource(R.drawable.ic_cloud_check_white_48dp);
        }
    }

    public interface ProfileUpdateListener {
        void onAccountUpdate(User updatedUser);
        void onAccountDelete();
    }
}

