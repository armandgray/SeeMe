package com.armandgray.seeme.views;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.armandgray.seeme.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavBarFragment extends Fragment
        implements DiscoverFragment.NavDiscoverListener {

    private NavBarFragmentListener fragmentListener;
    private RelativeLayout navDiscover;
    private RelativeLayout navNetwork;
    private RelativeLayout navSeeMe;
    private RelativeLayout navNotes;
    private RelativeLayout navProfile;
    private RelativeLayout[] arrayNavLayouts;

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
        assignArrayNavLayouts();
        setIconColorStates(navSeeMe);

        navDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIconColorStates((RelativeLayout) view);
                fragmentListener.onNavDiscover();
            }
        });

        navNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIconColorStates((RelativeLayout) view);
                fragmentListener.onNavNetwork();
            }
        });

        navSeeMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIconColorStates((RelativeLayout) view);
                fragmentListener.onNavSeeMe();
            }
        });

        navNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIconColorStates((RelativeLayout) view);
                fragmentListener.onNavNotes();
            }
        });

        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIconColorStates((RelativeLayout) view);
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

    private void assignArrayNavLayouts() {
        arrayNavLayouts = new RelativeLayout[5];
        arrayNavLayouts[0] = navDiscover;
        arrayNavLayouts[1] = navNetwork;
        arrayNavLayouts[2] = navSeeMe;
        arrayNavLayouts[3] = navNotes;
        arrayNavLayouts[4] = navProfile;
    }

    private void setIconColorStates(RelativeLayout activeLayout) {
        setLayoutsColorInactive();
        setColorState(activeLayout);
    }

    private void setLayoutsColorInactive() {
        for (int i = 0; i < arrayNavLayouts.length; i++) {
            if (i != 2) {
                ImageView icon = (ImageView) arrayNavLayouts[i].getChildAt(0);
                icon.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorGray, null));

                TextView text = (TextView) arrayNavLayouts[i].getChildAt(1);
                text.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGray, null));
            }
        }
        arrayNavLayouts[2].getChildAt(0).setBackgroundResource(R.drawable.nav_logo_background);
    }

    private void setColorState(RelativeLayout activeLayout) {
        if (activeLayout == arrayNavLayouts[2]) {
            arrayNavLayouts[2].getChildAt(0).setBackgroundResource(R.drawable.nav_logo_back_active);
            return;
        }

        ImageView icon = (ImageView) activeLayout.getChildAt(0);
        icon.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
        TextView text = (TextView) activeLayout.getChildAt(1);
        text.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
    }

    @Override
    public void onNavDiscover() {
        setIconColorStates(navSeeMe);
    }

    public interface NavBarFragmentListener {
        void onNavDiscover();
        void onNavNetwork();
        void onNavSeeMe();
        void onNavNotes();
        void onNavProfile();
    }

}
