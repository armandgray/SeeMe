package com.armandgray.seeme.views;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.armandgray.seeme.R;
import com.armandgray.seeme.models.User;

import static com.armandgray.seeme.MainActivity.ACTIVE_USER;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private static final String DISCOVERABLE = "Discoverable";
    private static final String HIDDEN = "Hidden";
    private User activeUser;

    private TextView tvFullName;
    private TextView tvUsername;
    private ImageView ivProfile;
    private FloatingActionButton fabCamera;

    private LinearLayout itemUsername;
    private LinearLayout itemPassword;
    private LinearLayout itemRole;
    private LinearLayout itemDiscoverable;

    private LinearLayout feedbackContainer;
    private Button btnDeleteAccount;

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

        return rootView;
    }

    private void assignFields(View rootView) {
        activeUser = getArguments().getParcelable(ACTIVE_USER);

        tvFullName = (TextView) rootView.findViewById(R.id.tvFullName);
        tvUsername = (TextView) rootView.findViewById(R.id.tvUsername);
        ivProfile = (ImageView) rootView.findViewById(R.id.ivProfile);
        fabCamera = (FloatingActionButton) rootView.findViewById(R.id.fabCamera);

        itemUsername = (LinearLayout) rootView.findViewById(R.id.itemUsername);
        itemPassword = (LinearLayout) rootView.findViewById(R.id.itemPassword);
        itemRole = (LinearLayout) rootView.findViewById(R.id.itemRole);
        itemDiscoverable = (LinearLayout) rootView.findViewById(R.id.itemDiscoverable);

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
        setupItem(itemUsername, R.drawable.ic_account_outline_white_48dp, activeUser.getUsername());
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

}
