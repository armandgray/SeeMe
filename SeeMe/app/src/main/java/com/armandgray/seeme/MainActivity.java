package com.armandgray.seeme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.armandgray.seeme.views.DiscoverFragment;
import com.armandgray.seeme.views.NavBarFragment;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static com.armandgray.seeme.LoginActivity.LOGIN_PAYLOAD;
import static com.armandgray.seeme.utils.HttpHelper.sendRequest;

public class MainActivity extends AppCompatActivity
        implements Observer, NavBarFragment.NavBarFragmentListener {

    public static final String API_URI = "http://52.39.178.132:8080";
    private static final String LOCAL_USERS_URI = API_URI + "/discoverable/localusers?networkId=";
    private static final String DEBUG_TAG = "DEBUG_TAG";

    private boolean networkOK;
    private boolean isWifiConnected;
    private FloatingActionButton fab;

    private User activeUser;
    private String ssid;
    private String networkId;

    private BroadcastReceiver httpBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("BroadcastReceiver: ", "http Broadcast Received");
            User[] userList =
                    (User[]) intent.getParcelableArrayExtra(HttpService.HTTP_SERVICE_PAYLOAD);
            if (userList != null) {
                setupRvUsers(Arrays.asList(userList));
            } else {
                Log.i("USER_LIST", "LIST IS NULL");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupFAB();

        activeUser = getIntent().getParcelableExtra(LOGIN_PAYLOAD);
        if (activeUser == null) {
//            startActivity(new Intent(this, LoginActivity.class));
        } else {
            Toast.makeText(this, "Welcome Back " + activeUser.getFirstName(), Toast.LENGTH_SHORT).show();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_container, new DiscoverFragment())
                .commit();

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        broadcastManager.registerReceiver(httpBroadcastReceiver,
                        new IntentFilter(HttpService.HTTP_SERVICE_MESSAGE));

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null) {
            isWifiConnected = networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        }
        if (isWifiConnected) {
            getWifiNetworkId();
        }

        BroadcastObserver.getInstance().addObserver(this);

        networkOK = NetworkHelper.hasNetworkAccess(this);
        updateFAB();
    }

    private void setupFAB() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (networkOK && isWifiConnected) {
                    String url = LOCAL_USERS_URI
                            + networkId
                            + "&ssid="+ ssid.substring(1, ssid.length() - 1).replaceAll(" ", "%20")
                            + "&username=" + activeUser.getUsername();
                    sendRequest(url, getApplicationContext());
                } else {
                    Toast.makeText(MainActivity.this, "WiFi Connection Unsuccessful!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateFAB() {
        if (isWifiConnected) {
            fab.setBackgroundTintList(ColorStateList.valueOf(
                    ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null)));
        } else {
            fab.setBackgroundTintList(ColorStateList.valueOf(
                    ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null)));
        }
    }

    private void getWifiNetworkId() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService (Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        ssid  = wifiInfo.getSSID();
        networkId = wifiInfo.getBSSID();
        if (ssid.equals("<unknown ssid>")) {
            Log.i("ActiveNetInfo", "Wifi Network Not Found: " + String.valueOf(ssid));
        } else {
            Log.i("ActiveNetInfo", "Wifi Network " + String.valueOf(ssid) + ": " + networkId);
        }
    }

    @Override
    public void update(Observable o, Object data) {
        NetworkInfo info = (NetworkInfo) data;
        isWifiConnected = info != null;
        updateFAB();
        if (info != null) {
            getWifiNetworkId();
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
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                activeUser = null;
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(httpBroadcastReceiver);
    }

    @Override
    public void onNavDiscover() {

    }

    @Override
    public void onNavNetwork() {

    }

    @Override
    public void onNavSeeMe() {

    }

    @Override
    public void onNavProfile() {

    }

    @Override
    public void onNavNotes() {

    }
}
