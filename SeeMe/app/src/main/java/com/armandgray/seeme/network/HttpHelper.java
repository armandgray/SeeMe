package com.armandgray.seeme.network;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

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
import static com.armandgray.seeme.services.HttpService.DATA_TYPE;

/**
 * Helper class for working with a remote server
 */
public class HttpHelper {

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String NOTES = "NOTES";
    private static final String TAG = "HTTP_HELPER";

    public static void sendPostRequest(String url, String body, Context context) {
        if (!isNetworkOk(context)) { return; }
        Intent intent = new Intent(context, HttpService.class);
        intent.setData(Uri.parse(url));
        intent.putExtra(JSON_BODY, body);
        context.startService(intent);
    }

    public static void sendGetRequest(String url, Context context) {
        if (!isNetworkOk(context)) { return; }
        Intent intent = new Intent(context, HttpService.class);
        intent.setData(Uri.parse(url));
        context.startService(intent);
    }

    public static void sendNotesRequest(String url, Context context) {
        if (!isNetworkOk(context)) { return; }
        Intent intent = new Intent(context, HttpService.class);
        intent.setData(Uri.parse(url));
        intent.putExtra(DATA_TYPE, NOTES);
        context.startService(intent);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean isNetworkOk(Context context) {
        if (!NetworkHelper.hasNetworkAccess(context)) {
            Toast.makeText(context, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * @return
     * Returns text from a URL on a web server
     * @throws IOException
     * otherwise
     */
    public static String downloadUrl(String address, String requestType, String body) throws IOException {

        InputStream is = null;
        try {

            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(requestType);
            conn.setRequestProperty( "Content-Type", "application/json" );
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoInput(true);
            conn.connect();

            if (requestType.equals(POST)) {
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
     * @return
     * Reads an InputStream and converts it to a String.
     * @throws IOException
     * otherwise
     */
    private static String readStream(InputStream stream) throws IOException {

        byte[] buffer = new byte[1024];
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        BufferedOutputStream out = null;
        try {
            int length;
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
