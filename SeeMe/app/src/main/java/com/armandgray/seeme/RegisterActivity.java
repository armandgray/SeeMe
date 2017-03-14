package com.armandgray.seeme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.armandgray.seeme.controllers.RegisterActivityController;
import com.armandgray.seeme.models.User;
import com.armandgray.seeme.services.HttpService;

import java.util.HashMap;

import static com.armandgray.seeme.MainActivity.API_URI;

public class RegisterActivity extends AppCompatActivity {

    public static final String REGISTER_URI = API_URI + "/register?register=Register&";

    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String FIRST_NAME = "FIRST_NAME";
    public static final String LAST_NAME = "LAST_NAME";
    public static final String ROLE = "ROLE";

    private static final String TAG = "REGISTER_ACTIVITY";

    private RegisterActivityController controller;

    private BroadcastReceiver httpBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("BroadcastReceiver: ", "http Broadcast Received");
            User[] userList = (User[]) intent.getParcelableArrayExtra(HttpService.HTTP_SERVICE_PAYLOAD);
            if (userList.length == 0) {
                Log.e(TAG, "REGISTRATION ERROR ON RETURN");
            } else {
                Log.i(TAG, userList[0].toString());
//                Intent loginIntent = new Intent(context, MainActivity.class);
//                loginIntent.putExtra(LOGIN_PAYLOAD, userList[0]);
//                startActivity(loginIntent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        controller = new RegisterActivityController(this);
        setupButtonClickListeners();
    }

    private void setupButtonClickListeners() {
        Button btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.onAccountSubmit(getMapEditTextStrings());
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

    private HashMap<String, String> getMapEditTextStrings() {
        HashMap<String, String> mapEditTextStrings = new HashMap<>();
        mapEditTextStrings.put(USERNAME, getTextFrom((EditText) findViewById(R.id.etUsername)));
        mapEditTextStrings.put(PASSWORD, getTextFrom((EditText) findViewById(R.id.etPassword)));
        mapEditTextStrings.put(FIRST_NAME, getTextFrom((EditText) findViewById(R.id.etFirstName)));
        mapEditTextStrings.put(LAST_NAME, getTextFrom((EditText) findViewById(R.id.etLastName)));
        mapEditTextStrings.put(ROLE, getTextFrom((EditText) findViewById(R.id.etRole)));
        return mapEditTextStrings;
    }

    private String getTextFrom(EditText et) {
        return et.getText().toString();
    }

    public interface RegisterController {
        void onAccountSubmit(HashMap<String, String> mapEditTextStrings);
    }
}
