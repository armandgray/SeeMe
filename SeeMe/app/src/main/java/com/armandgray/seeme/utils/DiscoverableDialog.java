package com.armandgray.seeme.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;

public class DiscoverableDialog extends DialogFragment {

    private DiscoverableListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (DiscoverableListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement DiscoverableListener");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getBoldStringBuilder())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onSubmitDiscoverable(true);
                    }
                })
                .setNegativeButton("Hidden", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onSubmitDiscoverable(false);
                    }
                });
        return builder.create();
    }

    @NonNull
    private SpannableStringBuilder getBoldStringBuilder() {
        final String dialogTextHeader = "Discoverable is Turned On for \"See Me\"\n\n";
        String dialogTextContent = "Users are discoverable by default. You can hide your account by clicking below, but this may restrict your experience.";
        final SpannableStringBuilder stringBuilder = new SpannableStringBuilder(dialogTextHeader + dialogTextContent);
        final StyleSpan boldStyleSpan = new StyleSpan(android.graphics.Typeface.BOLD);
        stringBuilder.setSpan(boldStyleSpan, 0, dialogTextHeader.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return stringBuilder;
    }

    public interface DiscoverableListener {
        void onSubmitDiscoverable(boolean b);
    }
}
