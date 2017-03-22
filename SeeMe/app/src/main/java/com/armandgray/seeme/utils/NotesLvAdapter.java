package com.armandgray.seeme.utils;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.armandgray.seeme.R;
import com.armandgray.seeme.db.DatabaseHelper;

public class NotesLvAdapter extends CursorAdapter {

    public NotesLvAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.note_listitem, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String noteText = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOTE_TEXT));

        int pos = noteText.indexOf(10);
        if (pos != -1) {
            noteText = noteText.substring(0, pos) + "...";
        }

        TextView tvText = (TextView) view.findViewById(R.id.tvText);
        tvText.setText(noteText);

    }
}
