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

import static com.armandgray.seeme.network.HttpHelper.GET;
import static com.armandgray.seeme.network.HttpHelper.POST;

public class HttpService extends IntentService {

    public static final String TAG = "HttpService";
    public static final String HTTP_SERVICE_MESSAGE = "HTTP Service Message";
    public static final String HTTP_SERVICE_JSON_PAYLOAD = "HTTP Service JSON Payload";
    public static final String HTTP_SERVICE_STRING_PAYLOAD = "HTTP Service STRING Payload";
    public static final String HTTP_SERVICE_ARRAY_PAYLOAD = "HTTP Service Array Payload";
    public static final String JSON_BODY = "JSON_BODY";

    public HttpService() { super("HttpService"); }

    @Override
    protected void onHandleIntent(Intent intent) {
        Uri uri = intent.getData();
        Log.i(TAG, "onHandleIntent: " + uri.toString());

        String body = intent.getStringExtra(JSON_BODY);
        String responseType = body != null ? POST : GET;
        String response = getResponse(uri, responseType, body);

        Intent messageIntent = new Intent(HTTP_SERVICE_MESSAGE);
        putMessageIntentExtra(messageIntent, response, responseType);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(messageIntent);
    }

    private String getResponse(Uri uri, String responseType, String body) {
        String response;
        try {
            response = HttpHelper.downloadUrl(uri.toString(), responseType, body);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

    private void putMessageIntentExtra(Intent messageIntent, String response, String responseType) {
        User[] userArray;
        if (response != null) {
            if (!response.equals("") && response.charAt(0) != '[') {
                messageIntent.putExtra(HTTP_SERVICE_STRING_PAYLOAD, response);
            } else if (responseType.equals(POST)) {
                Gson gson = new GsonBuilder().create();
                messageIntent.putExtra(HTTP_SERVICE_ARRAY_PAYLOAD,
                        gson.fromJson(response, String[].class));
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
