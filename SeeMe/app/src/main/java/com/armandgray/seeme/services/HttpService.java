package com.armandgray.seeme.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.armandgray.seeme.models.User;
import com.armandgray.seeme.network.HttpHelper;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

public class HttpService extends IntentService {

    public static final String TAG = "HttpService";
    public static final String HTTP_SERVICE_MESSAGE = "HTTP Service Message";
    public static final String HTTP_SERVICE_JSON_PAYLOAD = "HTTP Service JSON Payload";
    public static final String HTTP_SERVICE_STRING_PAYLOAD = "HTTP Service STRING Payload";
    public static final String JSON_BODY = "JSON_BODY";

    public HttpService() { super("HttpService"); }

    @Override
    protected void onHandleIntent(Intent intent) {
        Uri uri = intent.getData();
        Log.i(TAG, "onHandleIntent: " + uri.toString());

        String response = getResponse(uri);

        Intent messageIntent = new Intent(HTTP_SERVICE_MESSAGE);
        putMessageIntentExtra(messageIntent, response);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(messageIntent);
    }

    private String getResponse(Uri uri) {
        String response;
        try {
            response = HttpHelper.downloadUrl(uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

    private void putMessageIntentExtra(Intent messageIntent, String response) {
        User[] userArray;
        if (response != null) {
            if (response.charAt(0) != '[') {
                messageIntent.putExtra(HTTP_SERVICE_STRING_PAYLOAD, response);
            } else {
                Gson gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                        .create();
                userArray = gson.fromJson(response, User[].class);
                messageIntent.putExtra(HTTP_SERVICE_JSON_PAYLOAD, userArray);
            }
        }
    }
}
