package com.armandgray.seeme.views;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.armandgray.seeme.R;
import com.armandgray.seeme.db.DatabaseHelper;
import com.armandgray.seeme.db.NotesProvider;
import com.armandgray.seeme.models.User;

import static com.armandgray.seeme.MainActivity.ACTIVE_USER;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final String TAG = "NOTES_FRAGMENT";
    private CursorAdapter adapter;

    public NotesFragment() {}

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

        String[] from = {DatabaseHelper.NOTE_TEXT};
        int[] to = {android.R.id.text1};
        adapter = new SimpleCursorAdapter(getContext(),
                android.R.layout.simple_list_item_1, null, from, to, 0);

        ListView lvNotes = (ListView) rootView.findViewById(R.id.lvNotes);
        lvNotes.setAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);

        insertDummyData();

        FloatingActionButton fabDelete = (FloatingActionButton) rootView.findViewById(R.id.fabDelete);
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int button) {
                                if (button == DialogInterface.BUTTON_POSITIVE) {
                                    getActivity().getContentResolver()
                                            .delete(NotesProvider.CONTENT_URI, null, null);
                                    restartLoader();
                                    Toast.makeText(getContext(), "Notes Deleted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure?")
                        .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                        .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                        .show();
            }
        });

        return rootView;
    }

    private void insertDummyData() {
        insertNewNote("New Passed Note");
        insertNewNote("New MultiLine Note\n More text");
        insertNewNote("New Long Note Long Long Long Long Long Long Long Long Long Long Long Long Long Long Long Long Long Long");

        restartLoader();
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

    private Loader<Cursor> restartLoader() {
        return getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), NotesProvider.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
