package com.armandgray.seeme.views;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.armandgray.seeme.R;
import com.armandgray.seeme.controllers.NetworkFragmentController;
import com.armandgray.seeme.models.User;
import com.armandgray.seeme.services.HttpService;

import static com.armandgray.seeme.MainActivity.ACTIVE_USER;
import static com.armandgray.seeme.MainActivity.API_URI;
import static com.armandgray.seeme.utils.StringHelper.getBoldStringBuilder;

/**
 * A simple {@link Fragment} subclass.
 */
public class NetworkFragment extends Fragment {

    public static final String NETWORK_CONNECTION_URI = API_URI + "/connection/network?";
    public static final String UPDATE_CONNECTION_URI = API_URI + "/connection/update-status?";
    public static final String DELETE_CONNECTION_URI = API_URI + "/connection/delete?";
    
    private static final String NO_NETWORK_HEADER = "No Network Found";
    private static final String NO_NETWORK_CONTENT = "New SeeMe Users can build their network using SeeMe Touch. On the Discover screen, press connect on available users to build your network.";
    public static final String TAG = "NETWORK FRAGMENT";

    private RecyclerView rvNetwork;
    private TextView tvNoNetwork;
    private LinearLayout networkContainer;

    private User[] networkArray;
    private User activeUser;
    private NetworkController controller;

    private BroadcastReceiver httpBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "http Broadcast Received");
            Parcelable[] arrayExtra = intent.getParcelableArrayExtra(HttpService.HTTP_SERVICE_JSON_PAYLOAD);
            networkArray = (User[]) arrayExtra;
            toggleShowNetwork();
            controller.handleHttpResponse(
                    intent.getStringExtra(HttpService.HTTP_SERVICE_STRING_PAYLOAD), arrayExtra, rvNetwork);
        }
    };

    public NetworkFragment() {}

    public static NetworkFragment newInstance(User activeUser) {
        Bundle args = new Bundle();
        args.putParcelable(ACTIVE_USER, activeUser);

        NetworkFragment fragment = new NetworkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_network, container, false);

        assignFields(rootView);
        tvNoNetwork.setText(getBoldStringBuilder(NO_NETWORK_HEADER, NO_NETWORK_CONTENT));
        toggleShowNetwork();

        return rootView;
    }

    private void assignFields(View rootView) {
        rvNetwork = (RecyclerView) rootView.findViewById(R.id.rvNetwork);
        tvNoNetwork = (TextView) rootView.findViewById(R.id.tvNoNetwork);
        networkContainer = (LinearLayout) rootView.findViewById(R.id.networkContainer);
        activeUser = getArguments().getParcelable(ACTIVE_USER);
        controller = new NetworkFragmentController(getActivity(), activeUser);
    }

    private void toggleShowNetwork() {
        if (networkArray == null || networkArray.length == 0) {
            tvNoNetwork.setVisibility(View.VISIBLE);
            networkContainer.setVisibility(View.INVISIBLE);
            return;
        }
        tvNoNetwork.setVisibility(View.INVISIBLE);
        networkContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            controller.sendNetworkRequest();
            LocalBroadcastManager.getInstance(getActivity().getApplicationContext())
                    .registerReceiver(httpBroadcastReceiver,
                            new IntentFilter(HttpService.HTTP_SERVICE_MESSAGE));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!getUserVisibleHint()) {
            LocalBroadcastManager.getInstance(getActivity().getApplicationContext())
                    .unregisterReceiver(httpBroadcastReceiver);
        }
    }

    public interface NetworkController {
        void sendNetworkRequest();
        void handleHttpResponse(String response, Parcelable[] arrayExtra, RecyclerView rvNetwork);
        void setupRvNetwork(RecyclerView rvNetwork, final User[] userArray);
        void onRecyclerItemClick(User user, View view);
    }
}
