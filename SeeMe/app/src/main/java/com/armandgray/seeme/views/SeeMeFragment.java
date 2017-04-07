package com.armandgray.seeme.views;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.armandgray.seeme.MainActivity;
import com.armandgray.seeme.R;
import com.armandgray.seeme.controllers.SeeMeFragmentController;
import com.armandgray.seeme.models.User;
import com.armandgray.seeme.observers.BroadcastObserver;

import java.util.Observable;
import java.util.Observer;

import static com.armandgray.seeme.MainActivity.ACTIVE_USER;
import static com.armandgray.seeme.network.NetworkHelper.getWifiConnectionState;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeeMeFragment extends Fragment
        implements Observer {

    public static final String LOCAL_USERS_URI = MainActivity.API_URI + "/discoverable/localusers?networkId=";
    private static final String TAG = "TAG";

    private ImageView ivWifi;
    private TextView tvAuto;
    private ImageView ivSeeMe;

    private boolean networkOK;
    private boolean autoUpdate;

    private SeeMeTouchListener seeMeTouchListener;
    private SeeMeFragmentController controller;

    public SeeMeFragment() {}

    public static SeeMeFragment newInstance(User activeUser) {
        Bundle args = new Bundle();
        args.putParcelable(ACTIVE_USER, activeUser);

        SeeMeFragment fragment = new SeeMeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            seeMeTouchListener = (SeeMeTouchListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement SeeMeTouchListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_see_me, container, false);

        assignFields(rootView);
        updateUI(getWifiConnectionState(getContext()));
        setupClickListeners();
        BroadcastObserver.getInstance().addObserver(this);

        return rootView;
    }

    private void assignFields(View rootView) {
        ivWifi = (ImageView) rootView.findViewById(R.id.ivWifi);
        ivSeeMe = (ImageView) rootView.findViewById(R.id.ivSeeMe);
        tvAuto = (TextView) rootView.findViewById(R.id.tvAuto);
        User activeUser = getArguments().getParcelable(ACTIVE_USER);
        controller = new SeeMeFragmentController(activeUser, seeMeTouchListener, getContext());
        autoUpdate = false;
    }

    private void updateUI(boolean isWifiConnected) {
        if (isWifiConnected) {
            ivWifi.setImageResource(R.drawable.ic_wifi_white_48dp);
            return;
        }
        autoUpdate = false;
        toggleAutoRequests();
        ivWifi.setImageResource(R.drawable.ic_wifi_off_white_48dp);
    }

    private void setupClickListeners() {
        tvAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getWifiConnectionState(getContext())) {
                    autoUpdate = !autoUpdate;
                } else {
                    Toast.makeText(getContext(), "Wifi Connection Unsuccessful!", Toast.LENGTH_SHORT).show();
                }
                toggleAutoRequests();
            }
        });
        ivSeeMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.requestLocalUsers();
            }
        });
    }

    private void toggleAutoRequests() {
        if (autoUpdate) {
            tvAuto.setBackgroundResource(R.drawable.main_auto_back_active);
            controller.startAutoRequests();
        } else {
            tvAuto.setBackgroundResource(R.drawable.main_auto_back);
            controller.stopAutoRequests();
        }
    }

    @Override
    public void update(Observable o, Object data) {
        updateUI(getWifiConnectionState(getContext()));
    }

    public interface SeeMeTouchListener {
        void onTouchSeeMe();
    }

    public interface SeeMeController {
        void requestLocalUsers();
        void startAutoRequests();
        void stopAutoRequests();
    }
}
