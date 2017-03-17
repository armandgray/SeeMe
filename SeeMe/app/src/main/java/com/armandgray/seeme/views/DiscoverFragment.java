package com.armandgray.seeme.views;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.armandgray.seeme.R;
import com.armandgray.seeme.models.User;
import com.armandgray.seeme.services.HttpService;
import com.armandgray.seeme.utils.UserRVAdapter;

import java.util.Arrays;
import java.util.List;

import static com.armandgray.seeme.MainActivity.ACTIVE_USER;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment {

    private static final String TAG = "DISCOVER_FRAGMENT";

    private RecyclerView rvUsers;
    private TextView tvNoUsers;
    private LinearLayout usersContainer;
    private User[] userList;
    private User activeUser;

    private BroadcastReceiver httpBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("BroadcastReceiver: ", "http Broadcast Received");
            userList = (User[]) intent.getParcelableArrayExtra(HttpService.HTTP_SERVICE_PAYLOAD);
            if (userList != null) {
                setupRvUsers(Arrays.asList(userList));
                toggleShowUsers();
            } else {
                Log.i("USER_LIST", "LIST IS NULL");
            }
        }
    };

    public DiscoverFragment() {
        // Required empty public constructor
    }

    public static DiscoverFragment newInstance(User activeUser) {
        Bundle args = new Bundle();
        args.putParcelable(ACTIVE_USER, activeUser);

        DiscoverFragment fragment = new DiscoverFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discover, container, false);

        activeUser = getArguments().getParcelable(ACTIVE_USER);

        rvUsers = (RecyclerView) rootView.findViewById(R.id.rvUsers);
        tvNoUsers = (TextView) rootView.findViewById(R.id.tvNoUsers);
        tvNoUsers.setText("No Available Users");
        usersContainer = (LinearLayout) rootView.findViewById(R.id.usersContainer);
        toggleShowUsers();

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(
                getActivity().getApplicationContext());
        broadcastManager.registerReceiver(httpBroadcastReceiver,
                new IntentFilter(HttpService.HTTP_SERVICE_MESSAGE));

        return rootView;
    }

    private void toggleShowUsers() {
        if (userList == null || userList.length == 0) {
            tvNoUsers.setVisibility(View.VISIBLE);
            usersContainer.setVisibility(View.INVISIBLE);
            return;
        }
        Log.i(TAG, userList[0].getFirstName());
        tvNoUsers.setVisibility(View.INVISIBLE);
        usersContainer.setVisibility(View.VISIBLE);
    }

    private void setupRvUsers(List<User> list) {
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvUsers.setAdapter(new UserRVAdapter(getActivity(), list));

    }

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(getActivity().getApplicationContext())
                .registerReceiver(httpBroadcastReceiver,
                        new IntentFilter(HttpService.HTTP_SERVICE_MESSAGE));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext())
                .unregisterReceiver(httpBroadcastReceiver);
    }

}
