package com.armandgray.seeme.controllers;

import android.content.Context;
import android.widget.Toast;

import com.armandgray.seeme.LoginActivity;

import static com.armandgray.seeme.LoginActivity.LOGIN_URI;
import static com.armandgray.seeme.utils.HttpHelper.sendRequest;

public class LoginActivityController implements LoginActivity.LoginController {

    private Context context;

    public LoginActivityController(Context context) {
        this.context = context;
    }

    @Override
    public void authenticateUser(String username, String password) {
        String url = LOGIN_URI
                + "&username=" + username
                + "&password=" + password;
        sendRequest(url, context);
    }

    @Override
    public void registerUser(String username, String password) {
        Toast.makeText(context, "Register New User", Toast.LENGTH_SHORT).show();
    }
}
