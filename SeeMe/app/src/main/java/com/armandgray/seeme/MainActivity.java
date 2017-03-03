package com.armandgray.seeme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.armandgray.seeme.models.FoodItem;
import com.armandgray.seeme.services.HttpService;
import com.armandgray.seeme.utils.NetworkHelper;
import com.armandgray.seeme.utils.UserRVAdapter;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String JSON_URI = "http://560057.youcanlearnit.net/services/json/itemsfeed.php";
    private boolean networkOK;
    RecyclerView rvUsers;

    private BroadcastReceiver httpBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            FoodItem[] foodItems =
                    (FoodItem[]) intent.getParcelableArrayExtra(HttpService.HTTP_SERVICE_PAYLOAD);
            setupRvUsers(Arrays.asList(foodItems));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupFAB();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(httpBroadcastReceiver,
                        new IntentFilter(HttpService.HTTP_SERVICE_MESSAGE));

        networkOK = NetworkHelper.hasNetworkAccess(this);
    }

    private void setupFAB() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (networkOK) {
                    Intent intent = new Intent(MainActivity.this, HttpService.class);
                    intent.setData(Uri.parse(JSON_URI));
                    startService(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Network Unavailable!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupRvUsers(List<FoodItem> list) {
//        ArrayList<User> listUsers = new ArrayList<>();
//        User u1 = new User.Builder().firstName("Armand").lastName("Gray").role("Mobile Developer").build();
//        User u2 = new User.Builder().firstName("Daniela").lastName("Gray").role("Program Coordinator").build();
//        User u3 = new User.Builder().firstName("Penny").lastName("Luke").role("Dog").build();
//
//        listUsers.add(u1);
//        listUsers.add(u2);
//        listUsers.add(u3);

        rvUsers = (RecyclerView) findViewById(R.id.rvUsers);
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
}
