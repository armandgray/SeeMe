package com.armandgray.seeme.views;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.armandgray.seeme.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavBarFragment extends Fragment {

    private NavBarFragmentListener mListener;

    public NavBarFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (NavBarFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement NavBarFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_navbar, container, false);
        RelativeLayout navDiscover = (RelativeLayout) rootView.findViewById(R.id.navDiscover);
        navDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onNavDiscover();
            }
        });

        RelativeLayout navNetwork = (RelativeLayout) rootView.findViewById(R.id.navNetwork);
        navNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onNavNetwork();
            }
        });

        ImageView messages = (ImageView) rootView.findViewById(R.id.navSeeMe);
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onNavSeeMe();
            }
        });

        RelativeLayout navNotes = (RelativeLayout) rootView.findViewById(R.id.navNotes);
        navNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {mListener.onNavNotes();
            }
        });

        RelativeLayout navProfile = (RelativeLayout) rootView.findViewById(R.id.navProfile);
        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onNavProfile();
            }
        });
        return rootView;
    }

    public interface NavBarFragmentListener {
        void onNavDiscover();
        void onNavNetwork();
        void onNavSeeMe();
        void onNavProfile();
        void onNavNotes();
    }

}
