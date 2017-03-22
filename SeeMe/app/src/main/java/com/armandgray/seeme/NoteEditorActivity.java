package com.armandgray.seeme;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;

import com.armandgray.seeme.db.DatabaseHelper;
import com.armandgray.seeme.db.NotesProvider;

public class NoteEditorActivity extends AppCompatActivity {

    private String action;
    private EditText etEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) { actionBar.setDisplayHomeAsUpEnabled(true); }

        etEditor = (EditText) findViewById(R.id.etEditor);

        Uri uri = getIntent().getParcelableExtra(NotesProvider.CONTENT_ITEM_TYPE);
        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle(getString(R.string.new_note));
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
        }
    }

    private void insertNote(String note) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.NOTE_TEXT, note);
        getContentResolver().insert(NotesProvider.CONTENT_URI, values);
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
