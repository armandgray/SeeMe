package com.armandgray.seeme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.armandgray.seeme.controllers.LoginActivityController;
import com.armandgray.seeme.models.User;
import com.armandgray.seeme.services.HttpService;

import static com.armandgray.seeme.MainActivity.API_URI;

public class LoginActivity extends AppCompatActivity {

    public static final String LOGIN_URI = API_URI + "/login?login=Log+In";
    public static final String LOGIN_PAYLOAD = "LOGIN_PAYLOAD";
    private static final String TAG = "LOGIN_ACTIVITY";

    private EditText etUsername;
    private EditText etPassword;

    private LoginActivityController controller;

    private BroadcastReceiver httpBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("BroadcastReceiver: ", "http Broadcast Received");
            User[] userList = (User[]) intent.getParcelableArrayExtra(HttpService.HTTP_SERVICE_JSON_PAYLOAD);
            if (userList.length == 0) {
                Toast.makeText(LoginActivity.this,
                        "Username or Password Incorrect",
                        Toast.LENGTH_SHORT).show();
            } else {
                Intent loginIntent = new Intent(context, MainActivity.class);
                loginIntent.putExtra(LOGIN_PAYLOAD, userList[0]);
                startActivity(loginIntent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        controller = new LoginActivityController(this);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.authenticateUser(
                        etUsername.getText().toString(),
                        etPassword.getText().toString());
            }
        });

        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.registerUser(
                        etUsername.getText().toString(),
                        etPassword.getText().toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        broadcastManager.registerReceiver(httpBroadcastReceiver,
                new IntentFilter(HttpService.HTTP_SERVICE_MESSAGE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(httpBroadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public interface LoginController {
        void authenticateUser(String username, String password);
        void registerUser(String username, String password);
    }
}
