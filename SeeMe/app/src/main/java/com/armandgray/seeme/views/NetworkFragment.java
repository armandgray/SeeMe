package com.armandgray.seeme.views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.armandgray.seeme.R;
import com.armandgray.seeme.models.User;
import com.armandgray.seeme.utils.UserRVAdapter;

import java.util.List;

import static com.armandgray.seeme.MainActivity.ACTIVE_USER;
import static com.armandgray.seeme.utils.StringHelper.getBoldStringBuilder;

/**
 * A simple {@link Fragment} subclass.
 */
public class NetworkFragment extends Fragment {


    private static final String NO_NETWORK_HEADER = "No Network Found";
    private static final String NO_NETWORK_CONTENT = "New SeeMe Users can build their network using SeeMe Touch. On the Discover screen, press connect on available users to build your network.";
    private RecyclerView rvNetwork;
    private TextView tvNoNetwork;
    private LinearLayout networkContainer;
    private User[] networkArray;

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
        // Inflate the layout for this fragment
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

}
