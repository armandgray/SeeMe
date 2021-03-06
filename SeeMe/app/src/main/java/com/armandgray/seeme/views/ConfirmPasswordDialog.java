package com.armandgray.seeme.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.armandgray.seeme.R;

import static com.armandgray.seeme.utils.StringHelper.getBoldStringBuilder;

public class ConfirmPasswordDialog extends DialogFragment {

    private static final String CONFIRM_HEADER = "Confirm Account Update";
    private static final String CONFIRM_CONTENT = "Please enter your password below.";
    private ConfirmPasswordListener confirmPasswordListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            confirmPasswordListener = (ConfirmPasswordListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getParentFragment().toString()
                    + " must implement ConfirmPasswordListener");
        }
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View dialogLayout = LayoutInflater.from(getContext())
                .inflate(R.layout.confirm_dialog_layout, null);
        builder.setView(dialogLayout);
        builder.setMessage(getBoldStringBuilder(CONFIRM_HEADER, CONFIRM_CONTENT))
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText etPassword = (EditText) dialogLayout.findViewById(R.id.etPassword);
                        confirmPasswordListener.onConfirmPassword(etPassword.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        confirmPasswordListener.onCancel();
                    }
                });
        return builder.create();
    }


    public interface ConfirmPasswordListener {
        void onConfirmPassword(String password);
        void onCancel();
    }
}
