package com.armandgray.seeme.views;


import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

        insertNewNote("New Passed Note");

        Cursor cursor = getActivity().getContentResolver()
                .query(NotesProvider.CONTENT_URI, DatabaseHelper.ALL_COLUMNS,
                        null, null, null);

        String[] from = {DatabaseHelper.NOTE_TEXT};
        int[] to = {android.R.id.text1};
        CursorAdapter adapter = new SimpleCursorAdapter(getContext(),
                android.R.layout.simple_list_item_1, cursor, from, to, 0);

        ListView lvNotes = (ListView) rootView.findViewById(R.id.lvNotes);
        lvNotes.setAdapter(adapter);

        return rootView;
    }

    private void insertNewNote(String note) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.NOTE_TEXT, note);
        Uri noteUri = getActivity().getContentResolver()
                .insert(NotesProvider.CONTENT_URI, values);

        if (noteUri != null) {
            Log.d(TAG, "Inserted note: " + noteUri.getLastPathSegment());
        }
    }

}
