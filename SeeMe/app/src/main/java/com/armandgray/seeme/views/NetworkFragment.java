package com.armandgray.seeme.views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.armandgray.seeme.R;
import com.armandgray.seeme.models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class NetworkFragment extends Fragment {


    public NetworkFragment() {
        // Required empty public constructor
    }

    public static NetworkFragment newInstance(User activeUser) {
        Bundle args = new Bundle();
        NetworkFragment fragment = new NetworkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_network, container, false);
    }

}
