package com.armandgray.seeme.utils;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    private static final int TYPE_REQUEST = 0;
    private static final int TYPE_PENDING = 1;
    private static final int TYPE_CONNECTED = 2;

    private static final String UNKNOWN = "unknown";
    private static final String CONNECTED = "connected";
    private static final String PENDING = "pending";
    private static final String REQUEST = "request";
    private static final String TAG = "USER_RV_ADAPTER";

    private List<User> listUsers;
    private boolean showRequest;


    public UserRVAdapter(List<User> listUsers, boolean showRequest) {
        this.listUsers = listUsers;
        this.showRequest = showRequest;
    }

    @Override
    public int getItemViewType(int position) {
        Log.i(TAG, String.valueOf(listUsers.get(position)));
        return super.getItemViewType(position);
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
