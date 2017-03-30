package com.armandgray.seeme.controllers;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.armandgray.seeme.R;
import com.armandgray.seeme.models.User;
import com.armandgray.seeme.utils.RecyclerItemClickListener;
import com.armandgray.seeme.utils.UserRVAdapter;
import com.armandgray.seeme.views.NetworkFragment;

import java.util.Arrays;

import static com.armandgray.seeme.models.User.getSortedUserList;
import static com.armandgray.seeme.network.HttpHelper.sendRequest;
import static com.armandgray.seeme.views.NetworkFragment.NETWORK_CONNECTION_URI;

public class NetworkFragmentController implements NetworkFragment.NetworkController {

    private static final String USER_NOT_FOUND = "User Not Found!";
    private static final String CONNECTION_CONFIRMED = "Connection Confirmed";
    private static final String CONNECTION_DELETED = "Connection Deleted!";
    private static final String PREPARE_UPDATE_ERROR = "Prepare Update Error!";
    private static final String UPDATE_QUERY_ERROR = "Update Query Error!";
    private static final String INTERNAL_UPDATE_ERROR = "Internal Update Error!";
    public static final String TAG = "TAG";

    private String[] responseArray = {USER_NOT_FOUND, PREPARE_UPDATE_ERROR, CONNECTION_CONFIRMED, CONNECTION_DELETED,
            UPDATE_QUERY_ERROR, INTERNAL_UPDATE_ERROR};
    private Activity activity;
    private User activeUser;

    public NetworkFragmentController(Activity activity, User activeUser) {
        this.activity = activity;
        this.activeUser = activeUser;
    }

    @Override
    public void sendNetworkRequest() {
        String url = NETWORK_CONNECTION_URI + "username=" + activeUser.getUsername();
        sendRequest(url, activity);
    }

    @Override
    public void handleHttpResponse(String response, Parcelable[] arrayExtra, RecyclerView rvNetwork) {
        if (response != null && !response.equals("") && Arrays.asList(responseArray).contains(response)) {
            Toast.makeText(activity, response, Toast.LENGTH_SHORT).show();
        } else if (arrayExtra != null && arrayExtra.length != 0) {
            setupRvNetwork(rvNetwork, (User[]) arrayExtra);
        }
    }

    @Override
    public void setupRvNetwork(RecyclerView rvNetwork, final User[] networkArray) {
        rvNetwork.setLayoutManager(
                new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        rvNetwork.setAdapter(new UserRVAdapter(
                getSortedUserList(networkArray, User.Comparators.STATUS), true));
        rvNetwork.addOnItemTouchListener(new RecyclerItemClickListener(activity,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (networkArray.length >= position) {
                            onRecyclerItemClick(networkArray[position], view);
                        }
                    }
                }));
    }

    @Override
    public void onRecyclerItemClick(User user, View view) {
        if (user == null) { return; }
        if (!user.isRemovable()) {
            user.setRemovable(true);
            LinearLayout layout = (LinearLayout) view;
            ImageView ivStatus = (ImageView) layout.getChildAt(2);
            ivStatus.setImageResource(R.drawable.ic_account_remove_white_48dp);
        } else {
            Toast.makeText(activity, "Remove User: " + user.getUsername(), Toast.LENGTH_SHORT).show();
        }
    }
}
