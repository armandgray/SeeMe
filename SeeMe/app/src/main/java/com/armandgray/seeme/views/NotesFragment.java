package com.armandgray.seeme.views;


import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.armandgray.seeme.R;
import com.armandgray.seeme.db.DatabaseHelper;
import com.armandgray.seeme.db.NotesProvider;
import com.armandgray.seeme.models.User;

import static com.armandgray.seeme.MainActivity.ACTIVE_USER;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {


    private static final String TAG = "NOTES_FRAGMENT";

    public NotesFragment() {
        // Required empty public constructor
    }

    public static NotesFragment newInstance(User activeUser) {
        Bundle args = new Bundle();
        args.putParcelable(ACTIVE_USER, activeUser);

        NotesFragment fragment = new NotesFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.NOTE_TEXT, "New Note");
        Uri noteUri = getActivity().getContentResolver()
                .insert(NotesProvider.CONTENT_URI, values);

        if (noteUri != null) {
            Log.d(TAG, "Inserted note: " + noteUri.getLastPathSegment());
        }

        return rootView;
    }

}
