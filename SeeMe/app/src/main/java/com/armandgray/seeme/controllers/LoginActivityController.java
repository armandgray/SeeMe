package com.armandgray.seeme.controllers;

import android.content.Context;
import android.content.Intent;

import com.armandgray.seeme.LoginActivity;
import com.armandgray.seeme.RegisterActivity;

import static com.armandgray.seeme.LoginActivity.LOGIN_PAYLOAD;
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
        String[] loginPayload = {username, password};
        context.startActivity(new Intent(context, RegisterActivity.class)
                .putExtra(LOGIN_PAYLOAD, loginPayload));
    }
}