package com.armandgray.seeme.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

import com.armandgray.seeme.models.User;
import com.armandgray.seeme.network.HttpHelper;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import static com.armandgray.seeme.network.HttpHelper.GET;
import static com.armandgray.seeme.network.HttpHelper.NOTES;
import static com.armandgray.seeme.network.HttpHelper.POST;

public class HttpService extends IntentService {

    private static final String TAG = "HttpService";
    public static final String HTTP_SERVICE_MESSAGE = "HTTP Service Message";
    public static final String HTTP_SERVICE_JSON_PAYLOAD = "HTTP Service JSON Payload";
    public static final String HTTP_SERVICE_STRING_PAYLOAD = "HTTP Service STRING Payload";
    public static final String HTTP_SERVICE_NOTES_PAYLOAD = "HTTP Service NOTES Payload";
    public static final String JSON_BODY = "JSON_BODY";
    public static final String DATA_TYPE = "DATA_TYPE";
    private String dataType;

    public HttpService() { super("HttpService"); }

    @Override
    protected void onHandleIntent(Intent intent) {
        Uri uri = intent.getData();

        String body = intent.getStringExtra(JSON_BODY);
        String responseType = body != null ? POST : GET;
        dataType = intent.getStringExtra(DATA_TYPE) == null ?
                "" : intent.getStringExtra(DATA_TYPE);

        String response = getResponse(uri, responseType, body);
        if (response == null) { return; }

        Intent messageIntent = new Intent(HTTP_SERVICE_MESSAGE);
        putMessageIntentExtra(messageIntent, response);
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

    private void putMessageIntentExtra(Intent messageIntent, String response) {
        User[] userArray;
        if (response != null) {
            if (dataType.equals(NOTES)) {
                try {
                    JSONArray jsonArray = new JSONArray(response).getJSONObject(0).getJSONArray("Notes");
                    String[] array = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        array[i] = jsonArray.get(i).toString();
                    }
                    messageIntent.putExtra(HTTP_SERVICE_NOTES_PAYLOAD, array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (!response.equals("") && response.charAt(0) != '[') {
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
