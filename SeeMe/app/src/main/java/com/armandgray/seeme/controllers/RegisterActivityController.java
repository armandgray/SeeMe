package com.armandgray.seeme.controllers;

import android.content.Context;
import android.widget.EditText;

import com.armandgray.seeme.RegisterActivity;

public class RegisterActivityController implements RegisterActivity.RegisterController {

    private Context context;

    public RegisterActivityController(Context context) {
        this.context = context;
    }

    @Override
    public void onAccountSubmit(EditText[] arrayEditTextFields) {
        if (areValidFieldValues()) {

        }
    }

    private boolean areValidFieldValues() {
        return true;
    }
}
