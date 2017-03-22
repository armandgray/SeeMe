package com.armandgray.seeme.views;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.armandgray.seeme.NoteEditorActivity;
import com.armandgray.seeme.R;
import com.armandgray.seeme.db.DatabaseHelper;
import com.armandgray.seeme.db.NotesProvider;
import com.armandgray.seeme.models.User;
import com.armandgray.seeme.utils.NotesLvAdapter;

import static com.armandgray.seeme.MainActivity.ACTIVE_USER;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final String TAG = "NOTES_FRAGMENT";
    private static final int EDITOR_REQUEST_CODE = 1001;
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

        adapter = new NotesLvAdapter(getContext(), null, 0);

        ListView lvNotes = (ListView) rootView.findViewById(R.id.lvNotes);
        lvNotes.setAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);

        FloatingActionButton fabDelete = (FloatingActionButton) rootView.findViewById(R.id.fabDelete);
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), NoteEditorActivity.class), EDITOR_REQUEST_CODE);
            }
        });

        return rootView;
    }

    private Loader<Cursor> restartLoader() {
        return getLoaderManager().restartLoader(0, null, this);
    }

    private void deleteAllNotes() {
        getActivity().getContentResolver()
                .delete(NotesProvider.CONTENT_URI, null, null);
        restartLoader();
        Toast.makeText(getContext(), "Notes Deleted", Toast.LENGTH_SHORT).show();
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
