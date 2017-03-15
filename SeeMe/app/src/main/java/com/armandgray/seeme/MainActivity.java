package com.armandgray.seeme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.armandgray.seeme.models.User;
import com.armandgray.seeme.views.DiscoverFragment;
import com.armandgray.seeme.views.NavBarFragment;
import com.armandgray.seeme.views.NetworkFragment;
import com.armandgray.seeme.views.NotesFragment;
import com.armandgray.seeme.views.ProfileFragment;
import com.armandgray.seeme.views.SeeMeFragment;

import static com.armandgray.seeme.LoginActivity.LOGIN_PAYLOAD;

public class MainActivity extends AppCompatActivity
        implements NavBarFragment.NavBarFragmentListener {

    public static final String API_URI = "http://52.39.178.132:8080";
    private static final String DEBUG_TAG = "DEBUG_TAG";
    private static final String TAG = "MAIN_ACTIVITY";
    public static final String ACTIVE_USER = "ACTIVE_USER";

    private User activeUser;
    private ViewPager viewPager;
    private NavBarFragment navbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activeUser = getIntent().getParcelableExtra(LOGIN_PAYLOAD);
        if (activeUser == null) {
//            startActivity(new Intent(this, LoginActivity.class));
            activeUser = new User("Armand", "Gray", "Creator", "armand@test.com", "1234567890", true, "");
        } else {
            Toast.makeText(this, "Welcome Back " + activeUser.getFirstName(), Toast.LENGTH_SHORT).show();
        }

        navbar = new NavBarFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.navContainer, navbar)
                .commit();

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ViewPagerAdapter adapterViewPager = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        viewPager.setCurrentItem(2);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                navbar.onPageChange(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private static final int NUM_PAGES = 5;

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return DiscoverFragment.newInstance(activeUser);
                case 1:
                    return NetworkFragment.newInstance(activeUser);
                case 2:
                    return SeeMeFragment.newInstance(activeUser);
                case 3:
                    return NotesFragment.newInstance(activeUser);
                case 4:
                    return ProfileFragment.newInstance(activeUser);
                default:
                    return null;
            }
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

}
