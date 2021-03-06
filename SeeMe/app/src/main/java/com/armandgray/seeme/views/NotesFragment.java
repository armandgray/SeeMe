package com.armandgray.seeme.views;


import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.armandgray.seeme.NoteEditorActivity;
import com.armandgray.seeme.R;
import com.armandgray.seeme.db.DatabaseHelper;
import com.armandgray.seeme.db.NotesProvider;
import com.armandgray.seeme.models.User;
import com.armandgray.seeme.services.HttpService;
import com.armandgray.seeme.utils.NotesLvAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;
import static com.armandgray.seeme.MainActivity.ACTIVE_USER;
import static com.armandgray.seeme.MainActivity.API_URI;
import static com.armandgray.seeme.network.HttpHelper.sendNotesRequest;
import static com.armandgray.seeme.network.HttpHelper.sendPostRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String POST_NOTES_URI = API_URI + "/notes/post?";
    private static final String GET_NOTES_URI = API_URI + "/notes/get?";
    private static final String TAG = "NOTES_FRAGMENT";
    private static final int EDITOR_REQUEST_CODE = 1001;

    private static final String USER_NOT_FOUND = "User Not Found!";

    private CursorAdapter adapter;
    private User activeUser;

    private final BroadcastReceiver httpBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleHttpResponse(intent.getStringExtra(HttpService.HTTP_SERVICE_STRING_PAYLOAD),
                    intent.getStringArrayExtra(HttpService.HTTP_SERVICE_NOTES_PAYLOAD));
        }
    };

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
        activeUser = getArguments().getParcelable(ACTIVE_USER);
        adapter = new NotesLvAdapter(getContext());

        ListView lvNotes = (ListView) rootView.findViewById(R.id.lvNotes);
        lvNotes.setAdapter(adapter);
        getLoaderManager().initLoader(0, null, this);
        lvNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), NoteEditorActivity.class);
                Uri uri = Uri.parse(NotesProvider.CONTENT_URI + "/" + id);
                intent.putExtra(NotesProvider.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

        FloatingActionButton fabDelete = (FloatingActionButton) rootView.findViewById(R.id.fabDelete);
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), NoteEditorActivity.class), EDITOR_REQUEST_CODE);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            LocalBroadcastManager.getInstance(getActivity().getApplicationContext())
                    .registerReceiver(httpBroadcastReceiver,
                            new IntentFilter(HttpService.HTTP_SERVICE_MESSAGE));

            sendGetNotesRequest();
        }
    }

    private void sendGetNotesRequest() {
        String url = GET_NOTES_URI
                + "username=" + activeUser.getUsername();
        sendNotesRequest(url, getContext());
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    private void handleHttpResponse(String response, String[] arrayExtra) {
        if (response != null && response.equals(USER_NOT_FOUND)) {
            getActivity().getContentResolver().delete(NotesProvider.CONTENT_URI, null, null);
            restartLoader();
            return;
        }
        if (arrayExtra != null) {
            updateSqliteDatabase(arrayExtra);
        }
    }

    private void updateSqliteDatabase(String[] arrayExtra) {
        getActivity().getContentResolver().delete(NotesProvider.CONTENT_URI, null, null);
        for (String note : arrayExtra) {
            insertNote(note);
        }
        restartLoader();
    }

    private void insertNote(String note) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.NOTE_TEXT, note);
        getActivity().getContentResolver().insert(NotesProvider.CONTENT_URI, values);
        restartLoader();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            sendPostNotesRequest();
            restartLoader();
        }
    }

    private void sendPostNotesRequest() {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Cursor cursor = getActivity().getContentResolver()
                .query(NotesProvider.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            String url = POST_NOTES_URI
                    + "username=" + activeUser.getUsername();
            sendPostRequest(url, getNotesJson(cursor, json, jsonArray), getContext());
        }
    }

    private String getNotesJson(Cursor cursor, JSONObject json, JSONArray jsonArray) {
        String noteText;
        try {
            do {
                noteText = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOTE_TEXT));
                jsonArray.put(cursor.getPosition(), noteText);
            } while (cursor.moveToNext());
            json = new JSONObject();
            json.put("notes", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!getUserVisibleHint()) {
            LocalBroadcastManager.getInstance(getActivity().getApplicationContext())
                    .unregisterReceiver(httpBroadcastReceiver);
        }
    }
}
