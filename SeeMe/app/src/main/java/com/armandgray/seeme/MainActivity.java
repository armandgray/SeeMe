package com.armandgray.seeme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.armandgray.seeme.controllers.ProfileFragmentController;
import com.armandgray.seeme.models.Network;
import com.armandgray.seeme.models.User;
import com.armandgray.seeme.network.HttpHelper;
import com.armandgray.seeme.utils.ViewPagerAdapter;
import com.armandgray.seeme.views.DiscoverFragment;
import com.armandgray.seeme.views.NavBarFragment;
import com.armandgray.seeme.views.SeeMeFragment;

import java.util.Observable;
import java.util.Observer;

import static com.armandgray.seeme.LoginActivity.LOGIN_PAYLOAD;
import static com.armandgray.seeme.network.NetworkHelper.getWifiConnectionState;
import static com.armandgray.seeme.network.NetworkHelper.getWifiNetwork;

public class MainActivity extends AppCompatActivity
        implements NavBarFragment.NavBarFragmentListener,
        SeeMeFragment.SeeMeTouchListener,
        DiscoverFragment.DiscoverClickListener,
        ProfileFragmentController.ProfileUpdateListener,
        Observer {

    public static final String API_URI = "http://armandgray.com/seeme/api";
    private static final String UPDATE_NETWORK_URI = API_URI + "/discoverable/update-network?networkId=";
    public static final String ACTIVE_USER = "ACTIVE_USER";
    private static final String TAG = "MAIN_ACTIVITY";
    private static final String ARMANDGRAY_COM = "http://armandgray.com";
    private static final String DEV_MISSION_COM = "https://dev-mission.github.io/devmission.org/";

    private User activeUser;
    private ViewPager viewPager;
    private NavBarFragment navbar;
    private User[] discoverArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setActiveUser();
        setUpNavBarFragment();
        setupViewPager();
    }

    private void setActiveUser() {
        activeUser = getIntent().getParcelableExtra(LOGIN_PAYLOAD);
        if (activeUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Welcome Back " + activeUser.getFirstName(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpNavBarFragment() {
        navbar = new NavBarFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.navContainer, navbar)
                .commit();
    }

    private void setupViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), activeUser));
        viewPager.setCurrentItem(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int oldPosition = 2;

            @Override
            public void onPageSelected(int newPosition) {
                navbar.onPageChange(newPosition);
                Fragment oldFragment = getActiveFragment(oldPosition);
                Fragment newFragment = getActiveFragment(newPosition);
                oldFragment.setUserVisibleHint(false);
                oldFragment.onPause();
                newFragment.setUserVisibleHint(true);
                newFragment.onResume();
                oldPosition = newPosition;
            }

            private Fragment getActiveFragment(int newPosition) {
                return getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + newPosition);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    public void onTouchCycle() {
        viewPager.setCurrentItem(2);
    }

    @Override
    public void onUserClick() {
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onUserArrayUpdate(User[] userArray) {
        discoverArray = userArray;
    }

    @Override
    public void onTouchSeeMe() {
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onAccountUpdate(User updatedUser) {
        activeUser = updatedUser;
    }

    @Override
    public void onAccountDelete() {
        activeUser = null;
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onNavDiscover() {
        viewPager.setCurrentItem(0, true);
    }

    @Override
    public void onNavNetwork() {
        viewPager.setCurrentItem(1, true);
    }

    @Override
    public void onNavSeeMe() {
        viewPager.setCurrentItem(2, true);
    }

    @Override
    public void onNavNotes() {
        viewPager.setCurrentItem(3, true);
    }

    @Override
    public void onNavProfile() {
        viewPager.setCurrentItem(4, true);
    }

    @Override
    public void update(Observable o, Object data) {
        if (data != null && getWifiConnectionState(this)) {
            Network network = getWifiNetwork(this);
            String url = UPDATE_NETWORK_URI
                    + network.getNetworkId()
                    + "&ssid=" + network.getSsid()
                    + "&username=" + activeUser.getUsername();
            HttpHelper.sendGetRequest(url, this);
        }
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
            case R.id.the_developer:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ARMANDGRAY_COM)));
                return true;
            case R.id.dev_mission:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(DEV_MISSION_COM)));
                return true;
            case R.id.action_settings:
            viewPager.setCurrentItem(4);
            return true;
            case R.id.sign_out_menu:
                activeUser = null;
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public User[] getDiscoverArray() {
        return discoverArray;
    }
}
