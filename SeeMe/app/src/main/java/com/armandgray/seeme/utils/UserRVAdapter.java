package com.armandgray.seeme.utils;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.armandgray.seeme.R;
import com.armandgray.seeme.models.User;

import java.util.List;

public class UserRVAdapter extends
        RecyclerView.Adapter<UserRVAdapter.ViewHolder> {

    private static final String UNKNOWN = "unknown";
    private static final String CONNECTED = "connected";
    private static final String PENDING = "pending";
    public static final String REQUEST = "request";
    private Activity activity;
    private List<User> listUsers;
    private boolean showRequest;


    public UserRVAdapter(Activity activity, List<User> listUsers, boolean showRequest) {
        this.activity = activity;
        this.listUsers = listUsers;
        this.showRequest = showRequest;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_listitem, parent, false));
    }

    @Override
    public void onBindViewHolder(UserRVAdapter.ViewHolder viewHolder, int position) {
        User user = listUsers.get(position);

        ImageView ivUserProfile = viewHolder.ivUserProfile;
        TextView tvUserName = viewHolder.tvUserName;
        TextView tvRole = viewHolder.tvRole;
        ImageView ivStatus = viewHolder.ivStatus;

        tvUserName.setText(user.getFirstName() + " " + user.getLastName());
        tvRole.setText("Work: " + user.getOccupation());

        switch (user.getStatus()) {
            case PENDING:
                ivStatus.setImageResource(R.drawable.ic_account_convert_white_48dp);
                return;
            case CONNECTED:
                ivStatus.setImageResource(R.drawable.ic_account_check_white_48dp);
                return;
            case REQUEST:
                if (showRequest) {
                    ivStatus.setImageResource(R.drawable.ic_account_plus_white_48dp);
                    return;
                }
                ivStatus.setImageResource(R.drawable.ic_account_check_white_48dp);
                return;
            case UNKNOWN:
                ivStatus.setImageResource(R.drawable.ic_account_plus_white_48dp);
        }
    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserProfile;
        TextView tvUserName;
        TextView tvRole;
        ImageView ivStatus;

        ViewHolder(View itemView) {
            super(itemView);
            ivUserProfile = (ImageView) itemView.findViewById(R.id.ivUserProfile);
            tvUserName = (TextView) itemView.findViewById(R.id.tvFullName);
            tvRole = (TextView) itemView.findViewById(R.id.tvOccupation);
            ivStatus = (ImageView) itemView.findViewById(R.id.ivStatus);
        }

    }
}
