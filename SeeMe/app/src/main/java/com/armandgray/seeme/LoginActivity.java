package com.armandgray.seeme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.armandgray.seeme.models.User;
import com.armandgray.seeme.services.HttpService;

import static com.armandgray.seeme.MainActivity.API_URI;

public class LoginActivity extends AppCompatActivity {

    public static final String LOGIN_URI = API_URI + "/login?login=Log+In";
    public static final String LOGIN_PAYLOAD = "LOGIN_PAYLOAD";
    private static final String TAG = "LOGIN_ACTIVITY";
    private EditText etUsername;
    private EditText etPassword;

    private BroadcastReceiver httpBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("BroadcastReceiver: ", "http Broadcast Received");
            User[] userList = (User[]) intent.getParcelableArrayExtra(HttpService.HTTP_SERVICE_PAYLOAD);
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
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
}
