package com.armandgray.seeme.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.armandgray.seeme.MainActivity;
import com.armandgray.seeme.RegisterActivity;
import com.armandgray.seeme.models.User;
import com.armandgray.seeme.views.DiscoverableDialog;

import java.util.HashMap;

import static com.armandgray.seeme.LoginActivity.LOGIN_PAYLOAD;
import static com.armandgray.seeme.RegisterActivity.*;
import static com.armandgray.seeme.utils.HttpHelper.sendRequest;
import static com.armandgray.seeme.utils.StringHelper.*;

public class RegisterActivityController implements RegisterActivity.RegisterController {

    private static final String TAG = "REGISTER_CONTROLLER";
    private static final String DIALOG = "DIALOG";

    private AppCompatActivity activity;
    private HashMap<String, String> mapEditTextStrings;

    public RegisterActivityController(AppCompatActivity activity) {
        this.activity = activity;
        this.mapEditTextStrings = new HashMap<>();
    }

    @Override
    public void onAccountSubmit(HashMap<String, String> mapEditTextStrings) {
        if (areValidFieldValues(mapEditTextStrings)) {
            this.mapEditTextStrings = mapEditTextStrings;
            new DiscoverableDialog().show(
                    activity.getSupportFragmentManager(), DIALOG);
        }
    }

    private boolean areValidFieldValues(HashMap<String, String> mapEditTextStrings) {
        if (!EMAIL_PTR.matcher(mapEditTextStrings.get(USERNAME)).matches()) {
            Toast.makeText(activity, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!mapEditTextStrings.get(PASSWORD).matches(PASSWORD_PTR)) {
            Toast.makeText(activity, "Password Must Contain: \n- At least 1 number\n- No whitespace\n- At least 6 characters", Toast.LENGTH_LONG).show();
            return false;
        }
        if (mapEditTextStrings.get(FIRST_NAME).equals("")
                || mapEditTextStrings.get(FIRST_NAME).indexOf(' ') != -1
                || mapEditTextStrings.get(LAST_NAME).equals("")
                || mapEditTextStrings.get(LAST_NAME).indexOf(' ') != -1) {
            Toast.makeText(activity, "Please Enter First & Last", Toast.LENGTH_LONG).show();
            return false;
        }
        if (mapEditTextStrings.get(OCCUPATION).equals("")) {
            Toast.makeText(activity, "Please Enter Role", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onSubmitDiscoverable(boolean discoverable) {
        String url = REGISTER_URI
                + "username=" + mapEditTextStrings.get(USERNAME)
                + "&password=" + mapEditTextStrings.get(PASSWORD)
                + "&firstName=" + capitalizeString(mapEditTextStrings.get(FIRST_NAME))
                + "&lastName=" + capitalizeString(mapEditTextStrings.get(LAST_NAME))
                + "&role=" + urlify(mapEditTextStrings.get(OCCUPATION))
                + "&discoverable=" + discoverable;
            sendRequest(url, activity);
    }

    @Override
    public void getUserFromResponse(User[] userList) {
        if (userList.length == 0) {
            Log.e(TAG, "REGISTRATION ERROR ON RETURN");
            Toast.makeText(activity, "Registration Failed!", Toast.LENGTH_SHORT).show();
        } else {
            Intent loginIntent = new Intent(activity, MainActivity.class);
            loginIntent.putExtra(LOGIN_PAYLOAD, userList[0]);
            activity.startActivity(loginIntent);
        }
    }
}
