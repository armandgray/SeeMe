package com.armandgray.seeme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.armandgray.seeme.services.HttpService;

public class LoginActivity extends AppCompatActivity {

    public static final String LOGIN_URI = "http://52.39.178.132:8080/login?login=Log+In";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, HttpService.class);
                intent.setData(Uri.parse(LOGIN_URI
                        + "&username=" + etUsername.getText().toString()
                        + "&password=" + etPassword.getText().toString()));
                startService(intent);
            }
        });

        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Register New User", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
