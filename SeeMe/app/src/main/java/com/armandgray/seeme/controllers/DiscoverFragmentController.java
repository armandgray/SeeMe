package com.armandgray.seeme.controllers;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.armandgray.seeme.models.User;
import com.armandgray.seeme.utils.RecyclerItemClickListener;
import com.armandgray.seeme.utils.UserRVAdapter;
import com.armandgray.seeme.views.DiscoverFragment.DiscoverClickListener;
import com.armandgray.seeme.views.DiscoverFragment.DiscoverController;

import java.util.Arrays;

import static com.armandgray.seeme.models.User.getSortedUserList;
import static com.armandgray.seeme.network.HttpHelper.sendRequest;
import static com.armandgray.seeme.views.DiscoverFragment.NEW_CONNECTION_URI;

public class DiscoverFragmentController implements DiscoverController {

    private static final String TAG = "DISCOVER_CONTROLLER";
    private static final String USER_NOT_FOUND = "User Not Found!";
    private static final String CONNECTION_CONFIRMED = "Connection Confirmed";
    private static final String CONNECTION_DELETED = "Connection Deleted!";
    private static final String REQUEST_SENT = "Request Sent";
    private static final String PREPARE_UPDATE_ERROR = "Prepare Update Error!";
    private static final String UPDATE_QUERY_ERROR = "Update Query Error!";
    private static final String INTERNAL_UPDATE_ERROR = "Internal Update Error!";
    public static final String UNKNOWN = "unknown";

    private String[] responseArray = {USER_NOT_FOUND, PREPARE_UPDATE_ERROR, CONNECTION_CONFIRMED, CONNECTION_DELETED,
            REQUEST_SENT, UPDATE_QUERY_ERROR, INTERNAL_UPDATE_ERROR};

    private Activity activity;
    private DiscoverClickListener listener;
    private User activeUser;

    public DiscoverFragmentController(Activity activity, DiscoverClickListener listener, User activeUser) {
        this.activity = activity;
        this.listener = listener;
        this.activeUser = activeUser;
    }

    @Override
    public void handleHttpResponse(String response, Parcelable[] arrayExtra, RecyclerView rvUsers) {
        if (response != null && !response.equals("") && Arrays.asList(responseArray).contains(response)) {
            Toast.makeText(activity, response, Toast.LENGTH_SHORT).show();
        } else if (arrayExtra != null && arrayExtra.length != 0) {
            setupRvUsers(rvUsers, (User[]) arrayExtra);
            listener.onUserArrayUpdate((User[]) arrayExtra);
        }
    }

    @Override
    public void setupRvUsers(RecyclerView rvUsers, final User[] userArray) {
        rvUsers.setLayoutManager(
                new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        rvUsers.setAdapter(new UserRVAdapter(activity,
                getSortedUserList(userArray, User.Comparators.FIRST_NAME), false));
        rvUsers.addOnItemTouchListener(new RecyclerItemClickListener(activity,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (userArray.length >= position) {
                            onRecyclerItemClick(userArray[position]);
                        }
                    }
                }));
    }

    @Override
    public void onRecyclerItemClick(User user) {
        if (user != null && user.getStatus().equals(UNKNOWN)) {
            String url = NEW_CONNECTION_URI
                    + "username=" + activeUser.getUsername()
                    + "&connection=" + user.getUsername();
            sendRequest(url, activity);
            listener.onUserClick(user);
        }
    }
}
