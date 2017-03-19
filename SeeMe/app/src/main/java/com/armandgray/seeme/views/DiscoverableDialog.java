package com.armandgray.seeme.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import static com.armandgray.seeme.utils.StringHelper.getBoldStringBuilder;

public class DiscoverableDialog extends DialogFragment {

    private static final String DISCOVERABLE_HEADER = "Discoverable is Turned On for \"See Me\"";
    private static final String DISCOVERABLE_CONTENT = "Users are discoverable by default. You can hide your account by clicking below, but this may restrict your experience.";
    private DiscoverableListener discoverableListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            discoverableListener = (DiscoverableListener) context;
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
        builder.setMessage(getBoldStringBuilder(DISCOVERABLE_HEADER, DISCOVERABLE_CONTENT))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        discoverableListener.onSubmitDiscoverable(true);
                    }
                })
                .setNegativeButton("Hidden", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        discoverableListener.onSubmitDiscoverable(false);
                    }
                });
        return builder.create();
    }

    public interface DiscoverableListener {
        void onSubmitDiscoverable(boolean b);
    }
}
