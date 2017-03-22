package com.armandgray.seeme;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.armandgray.seeme.db.DatabaseHelper;
import com.armandgray.seeme.db.NotesProvider;

public class NoteEditorActivity extends AppCompatActivity {

    private String action;
    private EditText etEditor;
    private String noteFilter;
    private String oldText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) { actionBar.setDisplayHomeAsUpEnabled(true); }

        etEditor = (EditText) findViewById(R.id.etEditor);
        action = Intent.ACTION_DEFAULT;

        Uri uri = getIntent().getParcelableExtra(NotesProvider.CONTENT_ITEM_TYPE);
        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle(getString(R.string.new_note));
        } else {
            action = Intent.ACTION_EDIT;
            noteFilter = DatabaseHelper.NOTE_ID + " = " + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri,
                    DatabaseHelper.ALL_COLUMNS, noteFilter, null, null);
            cursor.moveToFirst();
            oldText = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOTE_TEXT));
            etEditor.setText(oldText);
            etEditor.requestFocus();
        }
    }

    private void finishedEditing() {
        String newText = etEditor.getText().toString().trim();
        switch (action) {
            case Intent.ACTION_INSERT:
                if (newText.length() == 0) {
                    setResult(RESULT_CANCELED);
                    return;
                }
                insertNote(newText);
                return;
            case Intent.ACTION_EDIT:
                if (newText.length() == 0) {
//                    deleteNote();
                } else if (oldText.equals(newText)) {
                    setResult(RESULT_CANCELED);
                } else {
                    updateNote(newText);
                }
        }
    }

    private void insertNote(String note) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.NOTE_TEXT, note);
        getContentResolver().insert(NotesProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    private void updateNote(String note) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.NOTE_TEXT, note);
        getContentResolver().update(NotesProvider.CONTENT_URI, values, noteFilter, null);
        Toast.makeText(this, "Note Updated!", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed() {
        finishedEditing();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
