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
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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

import java.util.HashMap;

import static com.armandgray.seeme.MainActivity.ACTIVE_USER;
import static com.armandgray.seeme.MainActivity.API_URI;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements DeleteAccountDialog.DeleteAccountListener {

    public static final String UDPATE_URL = API_URI + "/profile/update?";
    public static final String DELETE_URL = API_URI + "/profile/delete?";

    public static final String ITEM_FULL_NAME = "itemFullName";
    public static final String ITEM_PASSWORD = "itemPassword";
    public static final String ITEM_ROLE = "itemRole";
    public static final String ITEM_DISCOVERABLE = "itemDiscoverable";
    public static final String IV_ICON = "ivIcon";
    public static final String TV_CONTENT = "tvContent";
    public static final String ET_EDIT = "etEdit";
    public static final String IV_CLOUD = "ivCloud";

    public static final String DISCOVERABLE = "Discoverable";
    public static final String HIDDEN = "Hidden";

    private static final String TAG = "PROFILE_FRAGMENT";

    private User activeUser;
    private ProfileController controller;

    private TextView tvFullName;
    private TextView tvUsername;
    private ImageView ivProfile;

    private FloatingActionButton fabCamera;
    private ImageView ivEdit;

    private boolean editable;
    private boolean profileEdited;

    private HashMap<String, HashMap> itemsMap;
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
        setupClickListeners();

        return rootView;
    }

    private void assignFields(View rootView) {
        activeUser = getArguments().getParcelable(ACTIVE_USER);
        controller = new ProfileFragmentController(activeUser, this,
                (ProfileFragmentController.ProfileUpdateListener) getActivity());

        tvFullName = (TextView) rootView.findViewById(R.id.tvFullName);
        tvUsername = (TextView) rootView.findViewById(R.id.tvUsername);
        ivProfile = (ImageView) rootView.findViewById(R.id.ivProfile);
        fabCamera = (FloatingActionButton) rootView.findViewById(R.id.fabCamera);
        ivEdit = (ImageView) rootView.findViewById(R.id.ivEdit);

        itemsMap = new HashMap<>();
        itemsMap.put(ITEM_FULL_NAME, getMapFromLayout((LinearLayout) rootView.findViewById(R.id.itemFullName)));
        itemsMap.put(ITEM_PASSWORD, getMapFromLayout((LinearLayout) rootView.findViewById(R.id.itemPassword)));
        itemsMap.put(ITEM_ROLE, getMapFromLayout((LinearLayout) rootView.findViewById(R.id.itemRole)));
        itemsMap.put(ITEM_DISCOVERABLE, getMapFromLayout((LinearLayout) rootView.findViewById(R.id.itemDiscoverable)));

        feedbackContainer = (LinearLayout) rootView.findViewById(R.id.feedbackContainer);
        btnDeleteAccount = (Button) rootView.findViewById(R.id.btnDeleteAccount);
    }

    private HashMap<String, View> getMapFromLayout(LinearLayout layout) {
        HashMap<String, View> map = new HashMap<>();
        map.put(IV_ICON, layout.getChildAt(0));
        map.put(TV_CONTENT, layout.getChildAt(1));
        map.put(ET_EDIT, layout.getChildAt(2));
        map.put(IV_CLOUD, layout.getChildAt(3));
        return map;
    }

    private void setupHeaderContent() {
        tvFullName.setText(activeUser.getFirstName() + " " + activeUser.getLastName());
        tvUsername.setText("--> " + activeUser.getUsername() + " <--");
        ivProfile.setImageResource(R.drawable.ic_account_circle_white_48dp);
    }

    private void setupItemContent() {
        setupItem(itemsMap.get(ITEM_FULL_NAME), R.drawable.ic_account_outline_white_48dp,
                activeUser.getFirstName() + " " + activeUser.getLastName());
        setupItem(itemsMap.get(ITEM_PASSWORD), R.drawable.ic_lock_open_outline_white_48dp, "0000000");
        setupItem(itemsMap.get(ITEM_ROLE), R.drawable.ic_tools_resources, activeUser.getRole());
        setupItem(itemsMap.get(ITEM_DISCOVERABLE), R.drawable.ic_earth_white_48dp,
                activeUser.isDiscoverable() ? DISCOVERABLE : HIDDEN);

        TextView tvPassword = (TextView) itemsMap.get(ITEM_PASSWORD).get(TV_CONTENT);
        tvPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        EditText etPassword = (EditText) itemsMap.get(ITEM_PASSWORD).get(ET_EDIT);
        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private void setupItem(HashMap item, int drawable, String content) {
        ImageView ivIcon = (ImageView) item.get(IV_ICON);
        ivIcon.setImageResource(drawable);
        TextView tvContent = (TextView) item.get(TV_CONTENT);
        tvContent.setText(content);
    }

    private void setupClickListeners() {
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Take or Select Photo From Gallery", Toast.LENGTH_SHORT).show();
            }
        });

        setupEditClickListener();
        final TextView tvDiscoverable = (TextView) itemsMap.get(ITEM_DISCOVERABLE).get(TV_CONTENT);
        tvDiscoverable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView ivDiscCloud = (ImageView) itemsMap.get(ITEM_DISCOVERABLE).get(IV_CLOUD);
                ivDiscCloud.setImageResource(R.drawable.ic_cloud_white_48dp);
                profileEdited = true;
                if (editable && tvDiscoverable.getText().equals(DISCOVERABLE)) {
                    tvDiscoverable.setText(HIDDEN);
                } else if (editable) {
                    tvDiscoverable.setText(DISCOVERABLE);
                }
            }
        });
        feedbackContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.postFeedBack();
            }
        });
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.postDeleteRequest();
            }
        });
    }

    private void setupEditClickListener() {
        toggleEditable();
        for (HashMap item : itemsMap.values()) { setupEditTextChangeListener(item); }

        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editable = !editable;
                if (profileEdited) {
                    controller.postUpdateRequest(itemsMap);
                    profileEdited = false;
                }
                toggleEditable();
            }
        });
    }

    private void toggleEditable() {
        ivEdit.setImageResource(editable ? R.drawable.ic_cloud_check_white_48dp : R.drawable.ic_pencil_white_48dp);

        TextView tvContent;
        EditText etEdit;
        for (String itemTitle : itemsMap.keySet()) {
            tvContent = (TextView) itemsMap.get(itemTitle).get(TV_CONTENT);
            etEdit = (EditText) itemsMap.get(itemTitle).get(ET_EDIT);

            if (editable && !itemTitle.equals(ITEM_DISCOVERABLE)) {
                tvContent.setVisibility(View.GONE);
                etEdit.setVisibility(View.VISIBLE);
                continue;
            }
            tvContent.setVisibility(View.VISIBLE);
            etEdit.setVisibility(View.GONE);
        }
        tvContent = (TextView) itemsMap.get(ITEM_DISCOVERABLE).get(TV_CONTENT);
        if (editable) {
            tvContent.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGray, null));
        } else {
            tvContent.setTextColor(ResourcesCompat.getColor(getResources(), android.R.color.white, null));
        }
    }

    private void setupEditTextChangeListener(final HashMap item) {
        EditText etEdit = (EditText) item.get(ET_EDIT);
        final ImageView ivCloud = (ImageView) item.get(IV_CLOUD);
        etEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ivCloud.setImageResource(R.drawable.ic_cloud_white_48dp);
                profileEdited = true;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });
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
        void postUpdateRequest(HashMap<String, HashMap> itemsMap);
        void postFeedBack();
    }
}
