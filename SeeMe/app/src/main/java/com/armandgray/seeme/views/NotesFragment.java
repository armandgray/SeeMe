package com.armandgray.seeme.views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.armandgray.seeme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {


    public NotesFragment() {
        // Required empty public constructor
    }

    public static NotesFragment newInstance() {
        Bundle args = new Bundle();
        NotesFragment fragment = new NotesFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

}
