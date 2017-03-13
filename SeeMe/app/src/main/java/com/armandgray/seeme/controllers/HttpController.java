package com.armandgray.seeme.controllers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.armandgray.seeme.LoginActivity;
import com.armandgray.seeme.services.HttpService;

import static com.armandgray.seeme.LoginActivity.LOGIN_URI;

public class HttpController implements LoginActivity.LoginActivityController {

    private Context context;

    public HttpController(Context context) {
        this.context = context;
    }

    @Override
    public void authenticateUser(String username, String password) {
        Intent intent = new Intent(context, HttpService.class);
        intent.setData(Uri.parse(LOGIN_URI
                + "&username=" + username
                + "&password=" + password));
        context.startService(intent);
    }
}
