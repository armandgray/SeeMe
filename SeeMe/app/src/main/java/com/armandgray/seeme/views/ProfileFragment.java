package com.armandgray.seeme.views;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.armandgray.seeme.R;
import com.armandgray.seeme.controllers.ProfileFragmentController;
import com.armandgray.seeme.models.User;
import com.armandgray.seeme.services.HttpService;

import static com.armandgray.seeme.MainActivity.ACTIVE_USER;
import static com.armandgray.seeme.MainActivity.API_URI;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements DeleteAccountDialog.DeleteAccountListener {

    private static final String DISCOVERABLE = "Discoverable";
    private static final String HIDDEN = "Hidden";
    private static final String TAG = "PROFILE_FRAGMENT";
    public static final String UDPATE_URL = API_URI + "/profile/update?";
    public static final String DELETE_URL = API_URI + "/profile/delete?";

    private User activeUser;
    private ProfileController controller;

    private TextView tvFullName;
    private TextView tvUsername;
    private ImageView ivProfile;

    private FloatingActionButton fabCamera;
    private ImageView ivEdit;

    private boolean editable;
    private LinearLayout itemFullName;
    private LinearLayout itemPassword;
    private LinearLayout itemRole;
    private LinearLayout itemDiscoverable;

    private LinearLayout[] itemsArray;
    private LinearLayout feedbackContainer;
    private Button btnDeleteAccount;


    private BroadcastReceiver httpBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "http Broadcast Received");
            controller.handleHttpResponse(
                    intent.getStringExtra(HttpService.HTTP_SERVICE_STRING_PAYLOAD),
                    intent.getParcelableArrayExtra(HttpService.HTTP_SERVICE_JSON_PAYLOAD));
        }
    };

    public ProfileFragment() {}

    public static ProfileFragment newInstance(User activeUser) {
        Bundle args = new Bundle();
        args.putParcelable(ACTIVE_USER, activeUser);

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        assignFields(rootView);
        setupHeaderContent();
        setupItemContent();
        setupEditClickListener();

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.postDeleteRequest();
            }
        });

        return rootView;
    }

    private void assignFields(View rootView) {
        activeUser = getArguments().getParcelable(ACTIVE_USER);
        controller = new ProfileFragmentController(activeUser, this);

        tvFullName = (TextView) rootView.findViewById(R.id.tvFullName);
        tvUsername = (TextView) rootView.findViewById(R.id.tvUsername);
        ivProfile = (ImageView) rootView.findViewById(R.id.ivProfile);
        fabCamera = (FloatingActionButton) rootView.findViewById(R.id.fabCamera);
        ivEdit = (ImageView) rootView.findViewById(R.id.ivEdit);

        itemsArray = new LinearLayout[4];
        itemsArray[0] = itemFullName = (LinearLayout) rootView.findViewById(R.id.itemFullName);
        itemsArray[1] = itemPassword = (LinearLayout) rootView.findViewById(R.id.itemPassword);
        itemsArray[2] = itemRole = (LinearLayout) rootView.findViewById(R.id.itemRole);
        itemsArray[3] = itemDiscoverable = (LinearLayout) rootView.findViewById(R.id.itemDiscoverable);

        feedbackContainer = (LinearLayout) rootView.findViewById(R.id.feedbackContainer);
        btnDeleteAccount = (Button) rootView.findViewById(R.id.btnDeleteAccount);
    }

    private void setupHeaderContent() {
        tvFullName.setText(activeUser.getFirstName() + " " + activeUser.getLastName());
        tvUsername.setText("--> " + activeUser.getUsername() + " <--");
        ivProfile.setImageResource(R.drawable.ic_account_circle_white_48dp);
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Take or Select Photo From Gallery", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupItemContent() {
        setupItem(itemFullName, R.drawable.ic_account_outline_white_48dp,
                activeUser.getFirstName() + " " + activeUser.getLastName());
        setupItem(itemPassword, R.drawable.ic_lock_open_outline_white_48dp, "0000000");
        setupItem(itemRole, R.drawable.ic_tools_resources, activeUser.getRole());
        setupItem(itemDiscoverable, R.drawable.ic_earth_white_48dp,
                activeUser.isDiscoverable() ? DISCOVERABLE : HIDDEN);

        TextView tvPassword = (TextView) itemPassword.getChildAt(1);
        tvPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private void setupItem(LinearLayout item, int drawable, String title) {
        ImageView itemImageView = (ImageView) item.getChildAt(0);
        itemImageView.setImageResource(drawable);
        TextView itemTextView = (TextView) item.getChildAt(1);
        itemTextView.setText(title);
    }

    private void setupEditClickListener() {
        toggleEditable();
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editable = !editable;
                toggleEditable();
            }
        });
    }

        private void toggleEditable() {
        ivEdit.setImageResource(editable ? R.drawable.ic_cloud_check_white_48dp : R.drawable.ic_pencil_white_48dp);

        TextView tvItemTitle;
        EditText etItemEdit;
        for (LinearLayout item : itemsArray) {
            tvItemTitle = (TextView) item.getChildAt(1);
            etItemEdit = (EditText) item.getChildAt(2);

            if (editable) {
                tvItemTitle.setVisibility(View.GONE);
                etItemEdit.setVisibility(View.VISIBLE);
                continue;
            }
            tvItemTitle.setVisibility(View.VISIBLE);
            etItemEdit.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(getActivity().getApplicationContext())
                .registerReceiver(httpBroadcastReceiver,
                        new IntentFilter(HttpService.HTTP_SERVICE_MESSAGE));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext())
                .unregisterReceiver(httpBroadcastReceiver);
    }

    @Override
    public void postConfirmedDeleteRequest(String username, String password) {
        controller.postConfirmedDeleteRequest(username, password);
    }

    public interface ProfileController {
        void postDeleteRequest();
        void postConfirmedDeleteRequest(String username, String password);
        void handleHttpResponse(String response, Parcelable[] parcelableArrayExtra);
    }
}
