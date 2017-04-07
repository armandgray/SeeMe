package com.armandgray.seeme.network;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.armandgray.seeme.services.HttpService;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.armandgray.seeme.services.HttpService.JSON_BODY;
import static com.armandgray.seeme.services.HttpService.RESPONSE_TYPE;

/**
 * Helper class for working with a remote server
 */
public class HttpHelper {

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String NOTES = "NOTES";
    public static final String TAG = "HTTP_HELPER";

    public static void sendPostRequest(String url, String body, Context context) {
        Intent intent = new Intent(context, HttpService.class);
        intent.setData(Uri.parse(url));
        intent.putExtra(JSON_BODY, body);
        context.startService(intent);
    }

    public static void sendGetRequest(String url, Context context) {
        Intent intent = new Intent(context, HttpService.class);
        intent.setData(Uri.parse(url));
        context.startService(intent);
    }

    public static void sendGetRequest(String url, String responseType, Context context) {
        Intent intent = new Intent(context, HttpService.class);
        intent.setData(Uri.parse(url));
        intent.putExtra(RESPONSE_TYPE, responseType);
        context.startService(intent);
    }

    /**
     * Returns text from a URL on a web server
     * @return
     * @throws IOException
     */
    public static String downloadUrl(String address, String requestType, String body) throws IOException {

        InputStream is = null;
        try {

            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(POST);
            conn.setRequestProperty( "Content-Type", "application/json" );
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoInput(true);
            conn.connect();
            Log.i(TAG, "downloadUrl()");

            if (requestType.equals(POST)) {
                Log.i(TAG, POST + ": " + body);
                OutputStream os = conn.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
                writer.write(body);
                writer.flush();
                writer.close();
                os.close();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new IOException("Got response code " + responseCode);
            }
            is = conn.getInputStream();
            return readStream(is);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return null;
    }



    /**
     * Reads an InputStream and converts it to a String.
     * @return
     * @throws IOException
     */
    private static String readStream(InputStream stream) throws IOException {

        byte[] buffer = new byte[1024];
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        BufferedOutputStream out = null;
        try {
            int length = 0;
            out = new BufferedOutputStream(byteArray);
            while ((length = stream.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
            return byteArray.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

}
