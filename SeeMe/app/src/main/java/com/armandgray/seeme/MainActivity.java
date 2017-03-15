package com.armandgray.seeme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.armandgray.seeme.models.User;
import com.armandgray.seeme.views.NavBarFragment;
import com.armandgray.seeme.views.SeeMeFragment;

import static com.armandgray.seeme.LoginActivity.LOGIN_PAYLOAD;

public class MainActivity extends AppCompatActivity
        implements NavBarFragment.NavBarFragmentListener {

    public static final String API_URI = "http://52.39.178.132:8080";
    private static final String DEBUG_TAG = "DEBUG_TAG";

    private User activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activeUser = getIntent().getParcelableExtra(LOGIN_PAYLOAD);
        if (activeUser == null) {
//            startActivity(new Intent(this, LoginActivity.class));
        } else {
            Toast.makeText(this, "Welcome Back " + activeUser.getFirstName(), Toast.LENGTH_SHORT).show();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_container, new SeeMeFragment())
                .commit();

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
