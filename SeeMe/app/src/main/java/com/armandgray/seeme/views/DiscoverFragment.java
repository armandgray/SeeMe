package com.armandgray.seeme.views;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.armandgray.seeme.R;
import com.armandgray.seeme.controllers.DiscoverFragmentController;
import com.armandgray.seeme.models.User;
import com.armandgray.seeme.services.HttpService;
import com.armandgray.seeme.utils.RecyclerItemClickListener;
import com.armandgray.seeme.utils.UserRVAdapter;

import java.util.Arrays;
import java.util.List;

import static com.armandgray.seeme.MainActivity.ACTIVE_USER;
import static com.armandgray.seeme.MainActivity.API_URI;
import static com.armandgray.seeme.utils.StringHelper.getBoldStringBuilder;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment {

    public static final String UPDATE_CONNECTION_URI = API_URI + "/connection/update-status?";
    public static final String NEW_CONNECTION_URI = API_URI + "/connection/new?";
    public static final String DELETE_CONNECTION_URI = API_URI + "/connection/delete?";

    private static final String TAG = "DISCOVER_FRAGMENT";
    private static final String NO_USERS_HEADER = "No Current Available Users";
    private static final String NO_USERS_CONTENT = "Users are discoverable through SeeMe Touch. On the main screen, press the touch button or set SeeMe Touch to auto.";

    private TextView tvNoUsers;
    private ImageView ivCycle;
    private LinearLayout usersContainer;
    private LinearLayout noUsersContainer;

    private RecyclerView rvUsers;
    private User[] userArray;
    private User activeUser;

    private DiscoverClickListener discoverClickListener;
    private DiscoverFragmentController controller;

    private BroadcastReceiver httpBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("BroadcastReceiver: ", "http Broadcast Received");
            Parcelable[] arrayExtra = intent.getParcelableArrayExtra(HttpService.HTTP_SERVICE_JSON_PAYLOAD);
            controller.handleHttpResponse(
                    intent.getStringExtra(HttpService.HTTP_SERVICE_STRING_PAYLOAD), arrayExtra);
        }
    };

    public DiscoverFragment() {}

    public static DiscoverFragment newInstance(User activeUser) {
        Bundle args = new Bundle();
        args.putParcelable(ACTIVE_USER, activeUser);

        DiscoverFragment fragment = new DiscoverFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            discoverClickListener = (DiscoverClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement DiscoverClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discover, container, false);

        assignFields(rootView);
        tvNoUsers.setText(getBoldStringBuilder(NO_USERS_HEADER, NO_USERS_CONTENT));
        toggleShowUsers();
        setupIvClickListener();

        setupDummyUsers();

        return rootView;
    }

    private void assignFields(View rootView) {
        rvUsers = (RecyclerView) rootView.findViewById(R.id.rvUsers);
        tvNoUsers = (TextView) rootView.findViewById(R.id.tvNoUsers);
        ivCycle = (ImageView) rootView.findViewById(R.id.ivCycle);
        noUsersContainer = (LinearLayout) rootView.findViewById(R.id.noUsersContainer);
        usersContainer = (LinearLayout) rootView.findViewById(R.id.usersContainer);
        activeUser = getArguments().getParcelable(ACTIVE_USER);
        controller = new DiscoverFragmentController(getContext(), discoverClickListener, activeUser);
    }

    private void toggleShowUsers() {
        if (userArray == null || userArray.length == 0) {
            noUsersContainer.setVisibility(View.VISIBLE);
            usersContainer.setVisibility(View.INVISIBLE);
            return;
        }
        noUsersContainer.setVisibility(View.INVISIBLE);
        usersContainer.setVisibility(View.VISIBLE);
    }

    private void setupIvClickListener() {
        ivCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discoverClickListener.onTouchCycle();
            }
        });
    }

    private void setupRvUsers(List<User> list) {
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvUsers.setAdapter(new UserRVAdapter(getActivity(), list));
        rvUsers.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (userArray != null && userArray.length >= position) {
                            controller.onRecyclerItemClick(userArray[position]);
                        }
                    }
                }));
    }

    private void setupDummyUsers() {
        tvNoUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userArray = new User[5];
                userArray[0] = new User("Armand", "Gray", "Creator", "danimeza@gmail.com", "1234567890", true, "");
                userArray[1] = new User("Michael", "Mei", "Unemployed", "test@gmail.com", "1234567890", true, "");
                userArray[2] = new User("Dylan", "Goodman", "Contract Reader", "genius@gmail.com", "1234567890", true, "");
                userArray[3] = new User("Amazing", "Gray", "Creator", "amazing@gmail.com", "1234567890", true, "");
                userArray[4] = new User("Blue", "Gray", "Creator", "blue@gmail.com", "1234567890", true, "");
                setupRvUsers(Arrays.asList(userArray));
                toggleShowUsers();
            }
        });
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

    public interface DiscoverClickListener {
        void onTouchCycle();
        void onUserClick(User user);
    }

    public interface DiscoverController {
        void onRecyclerItemClick(User user);
        void handleHttpResponse(String stringExtra, Parcelable[] arrayExtra);
    }

}
