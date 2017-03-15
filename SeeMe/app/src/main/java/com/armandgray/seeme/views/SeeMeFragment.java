package com.armandgray.seeme.views;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.armandgray.seeme.MainActivity;
import com.armandgray.seeme.R;
import com.armandgray.seeme.controllers.SeeMeFragmentController;
import com.armandgray.seeme.models.User;
import com.armandgray.seeme.utils.BroadcastObserver;
import com.armandgray.seeme.utils.NetworkHelper;

import java.util.Observable;
import java.util.Observer;

import static com.armandgray.seeme.MainActivity.ACTIVE_USER;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeeMeFragment extends Fragment
        implements Observer {

    public static final String LOCAL_USERS_URI = MainActivity.API_URI + "/discoverable/localusers?networkId=";
    private static final String TAG = "TAG";

    private boolean isWifiConnected;
    private ImageView ivWifi;
    private boolean networkOK;
    private SeeMeTouchListener seeMeTouchListener;
    private User activeUser;
    private SeeMeFragmentController controller;

    public SeeMeFragment() {
    }

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

        ivWifi = (ImageView) rootView.findViewById(R.id.ivWifi);
        activeUser = getArguments().getParcelable(ACTIVE_USER);

        controller = new SeeMeFragmentController(activeUser, seeMeTouchListener, getContext());

        ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null) {
            isWifiConnected = networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        }
        if (isWifiConnected) {
            controller.getWifiNetworkId(ivWifi);
        }


        networkOK = NetworkHelper.hasNetworkAccess(getContext());
        BroadcastObserver.getInstance().addObserver(this);

        ImageView ivSeeMe = (ImageView) rootView.findViewById(R.id.ivSeeMe);
        ivSeeMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.requestLocalUsers();
            }
        });
        return rootView;
    }

    @Override
    public void update(Observable o, Object data) {
        isWifiConnected = data != null;
        if (data != null) {
            controller.getWifiNetworkId(ivWifi);
        }
    }

    public interface SeeMeTouchListener {
        void onTouchSeeMe();
    }

    public interface SeeMeContoller {
        void getWifiNetworkId(ImageView ivWifi);
        void requestLocalUsers();
    }
}
