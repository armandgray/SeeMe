package com.armandgray.seeme.views;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
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

import java.util.List;

import static com.armandgray.seeme.MainActivity.ACTIVE_USER;
import static com.armandgray.seeme.MainActivity.API_URI;
import static com.armandgray.seeme.network.HttpHelper.sendRequest;
import static com.armandgray.seeme.utils.StringHelper.getBoldStringBuilder;

/**
 * A simple {@link Fragment} subclass.
 */
public class NetworkFragment extends Fragment {

    private static final String NETWORK_CONNECTION_URI = API_URI + "/connection/network?";
    private static final String NO_NETWORK_HEADER = "No Network Found";
    private static final String NO_NETWORK_CONTENT = "New SeeMe Users can build their network using SeeMe Touch. On the Discover screen, press connect on available users to build your network.";

    private RecyclerView rvNetwork;
    private TextView tvNoNetwork;
    private LinearLayout networkContainer;

    private User[] networkArray;
    private User activeUser;
    private NetworkController controller;

    private BroadcastReceiver httpBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("BroadcastReceiver: ", "http Broadcast Received");
            Parcelable[] arrayExtra = intent.getParcelableArrayExtra(HttpService.HTTP_SERVICE_JSON_PAYLOAD);
            controller.handleHttpResponse(
                    intent.getStringExtra(HttpService.HTTP_SERVICE_STRING_PAYLOAD), arrayExtra);
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

    private void setupRvNetwork(List<User> list) {
        rvNetwork.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvNetwork.setAdapter(new UserRVAdapter(getActivity(), list));
    }

    @Override
    public void onResume() {
        super.onResume();
        String url = NETWORK_CONNECTION_URI + "username=" + activeUser.getUsername();
        sendRequest(url, getContext());
        controller.sendNetworkRequest();
    }

    public interface NetworkController {
        void sendNetworkRequest();
        void handleHttpResponse(String response, Parcelable[] arrayExtra);
    }
}
