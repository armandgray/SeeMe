package com.armandgray.seeme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    private RegisterController controller;
    private EditText[] editTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        controller = null;
        editTexts = new EditText[5];
        editTexts[0] = (EditText) findViewById(R.id.etUsername);
        editTexts[1] = (EditText) findViewById(R.id.etPassword);
        editTexts[2] = (EditText) findViewById(R.id.etFirstName);
        editTexts[3] = (EditText) findViewById(R.id.etLastName);
        editTexts[4] = (EditText) findViewById(R.id.etRole);

        Button btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.onAccountSubmit(editTexts);
            }
        });

        ImageView ivBackButton = (ImageView) findViewById(R.id.ivBackButton);
        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView tvAlreadyHaveAccount = (TextView) findViewById(R.id.tvAlreadyHaveAccount);
        tvAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public interface RegisterController {
        void onAccountSubmit(EditText[] arrayEditTextFields);
    }
}
