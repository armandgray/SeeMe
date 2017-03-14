package com.armandgray.seeme.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.armandgray.seeme.models.User;
import com.armandgray.seeme.utils.HttpHelper;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

public class HttpService extends IntentService {

    public static final String TAG = "HttpService";
    public static final String HTTP_SERVICE_MESSAGE = "HTTP Service Message";
    public static final String HTTP_SERVICE_PAYLOAD = "HTTP Service Payload";

    public HttpService() { super("HttpService"); }

    @Override
    protected void onHandleIntent(Intent intent) {
        Uri uri = intent.getData();
        Log.i(TAG, "onHandleIntent: " + uri.toString());

        String response;
        try {
            response = HttpHelper.downloadUrl(uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        User[] userArray = null;

        if (response != null) {
            Log.i(TAG, response);
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                    .create();
            userArray = gson.fromJson(response, User[].class);
        }

        Intent messageIntent = new Intent(HTTP_SERVICE_MESSAGE);
        messageIntent.putExtra(HTTP_SERVICE_PAYLOAD, userArray);
        LocalBroadcastManager broadcastManager =
                LocalBroadcastManager.getInstance(getApplicationContext());
        broadcastManager.sendBroadcast(messageIntent);
    }
}
