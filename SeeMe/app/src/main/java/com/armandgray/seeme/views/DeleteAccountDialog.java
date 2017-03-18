package com.armandgray.seeme.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.armandgray.seeme.R;

import static com.armandgray.seeme.utils.StringHelper.getBoldStringBuilder;

public class DeleteAccountDialog extends DialogFragment {

    private static final String DELETE_HEADER = "Warning: Delete \"See Me\" Account";
    private static final String DELETE_CONTENT = "Deleting your account is permanent. After this step, all account information and data will be irretrievable. Please enter your username and password below to confirm.";
    private DeleteAccountListener deleteAccountListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            deleteAccountListener = (DeleteAccountListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getParentFragment().toString()
                    + " must implement DeleteAccountListener");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.delete_dialog_layout);
        builder.setMessage(getBoldStringBuilder(DELETE_HEADER, DELETE_CONTENT))
                .setPositiveButton("Delete Account", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteAccountListener.postConfirmedDeleteRequest();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // DO NOTHING
                    }
                });
        return builder.create();
    }

    public interface DeleteAccountListener {
        void postConfirmedDeleteRequest();
    }
}
