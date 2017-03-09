package com.armandgray.seeme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.armandgray.seeme.models.User;
import com.armandgray.seeme.services.HttpService;
import com.armandgray.seeme.utils.BroadcastObserver;
import com.armandgray.seeme.utils.NetworkHelper;
import com.armandgray.seeme.utils.UserRVAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {

    public static final String JSON_URI = "http://52.39.178.132:8080/discoverable/allusers";
    private static final String DEBUG_TAG = "DEBUG_TAG";

    private boolean networkOK;
    private boolean isWifiConnected;
    private FloatingActionButton fab;

    private BroadcastReceiver httpBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("BroadcastReceiver: ", "http Broadcast Received");
            User[] userList =
                    (User[]) intent.getParcelableArrayExtra(HttpService.HTTP_SERVICE_PAYLOAD);
            setupRvUsers(Arrays.asList(userList));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupFAB();

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        broadcastManager.registerReceiver(httpBroadcastReceiver,
                        new IntentFilter(HttpService.HTTP_SERVICE_MESSAGE));

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        isWifiConnected = networkInfo.isConnected();
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfo.isConnected();
        Log.e("Wifi connected: ", String.valueOf(isWifiConnected));
        Log.e("Mobile connected: ", String.valueOf(isMobileConn));

        Log.e("BroadcastReceiver: ", "Created");

        BroadcastObserver.getInstance().addObserver(this);

        networkOK = NetworkHelper.hasNetworkAccess(this);
        updateFAB();
    }

    private void setupFAB() {
        // TODO REMOVE THIS
        isWifiConnected = true;

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (networkOK && isWifiConnected) {
                    Intent intent = new Intent(MainActivity.this, HttpService.class);
                    intent.setData(Uri.parse(JSON_URI));
                    startService(intent);
                } else {
                    Toast.makeText(MainActivity.this, "WiFi Connection Unsuccessful!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateFAB() {
        // TODO REMOVE THIS
        isWifiConnected = true;

        if (isWifiConnected) {
            fab.setBackgroundTintList(ColorStateList.valueOf(
                    ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null)));
        } else {
            fab.setBackgroundTintList(ColorStateList.valueOf(
                    ResourcesCompat.getColor(getResources(), R.color.fabNoWifiColor, null)));
        }
    }

    private void setupRvUsers(List<User> list) {
        RecyclerView rvUsers = (RecyclerView) findViewById(R.id.rvUsers);
        rvUsers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvUsers.setAdapter(new UserRVAdapter(this, list));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(httpBroadcastReceiver);
    }

    @Override
    public void update(Observable o, Object data) {
        NetworkInfo info = (NetworkInfo) data;
        isWifiConnected = info != null;
        updateFAB();
        if (info != null) {
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService (Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid  = wifiInfo.getSSID();
            if (ssid.equals("<unknown ssid>")) {
                Log.i("ActiveNetInfo", "Wifi Network Not Found: " + String.valueOf(ssid));
            } else {
                Log.i("ActiveNetInfo", "Wifi Network: " + String.valueOf(ssid));
            }
        }
    }
}
