package com.armandgray.seeme.views;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.armandgray.seeme.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavBarFragment extends Fragment {

    private NavBarFragmentListener fragmentListener;
    private RelativeLayout navDiscover;
    private RelativeLayout navNetwork;
    private RelativeLayout navSeeMe;
    private RelativeLayout navNotes;
    private RelativeLayout navProfile;
    private View[] arrayNavViews;

    public NavBarFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragmentListener = (NavBarFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement NavBarFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_navbar, container, false);

        getViewReferences(rootView);
        assignArrayNavViews(rootView);

        navDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentListener.onNavDiscover();
            }
        });

        navNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentListener.onNavNetwork();
            }
        });

        navSeeMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIconColorStates(navSeeMe);
                fragmentListener.onNavSeeMe();
            }
        });

        navNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentListener.onNavNotes();
            }
        });

        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentListener.onNavProfile();
            }
        });



        return rootView;
    }

    private void getViewReferences(View rootView) {
        navDiscover = (RelativeLayout) rootView.findViewById(R.id.navDiscover);
        navNetwork = (RelativeLayout) rootView.findViewById(R.id.navNetwork);
        navSeeMe = (RelativeLayout) rootView.findViewById(R.id.navSeeMe);
        navNotes = (RelativeLayout) rootView.findViewById(R.id.navNotes);
        navProfile = (RelativeLayout) rootView.findViewById(R.id.navProfile);
    }

    private void assignArrayNavViews(View rootView) {
        arrayNavViews = new View[5];
        arrayNavViews[0] = navDiscover;
        arrayNavViews[1] = navNetwork;
        arrayNavViews[2] = navSeeMe;
        arrayNavViews[3] = navNotes;
        arrayNavViews[4] = navProfile;
    }

    private void setIconColorStates(View activeView) {
        for (int i = 0; i < arrayNavViews.length; i++) {
        }
    }

    public interface NavBarFragmentListener {
        void onNavDiscover();
        void onNavNetwork();
        void onNavSeeMe();
        void onNavProfile();
        void onNavNotes();
    }

}
