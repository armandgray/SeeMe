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

import java.util.ArrayList;
import java.util.List;

public class UserRVAdapter extends
        RecyclerView.Adapter<UserRVAdapter.ItemViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_REQUEST_HEADER = 1;
    private static final int TYPE_PENDING_HEADER = 2;
    private static final int TYPE_CONNECTED_HEADER = 3;

    private static final String UNKNOWN = "unknown";
    private static final String CONNECTED = "connected";
    private static final String PENDING = "pending";
    private static final String REQUEST = "request";
    private static final String TAG = "USER_RV_ADAPTER";
    private List<User> listUsers;

    private boolean showRequest;
    private List<String> statusList;
    private final boolean isfirstUserRequest;
    private final int firstPendingIndex;
    private final int firstConnectedIndex;

    public UserRVAdapter(List<User> listUsers, boolean showRequest) {
        this.listUsers = listUsers;
        this.showRequest = showRequest;
        this.statusList = new ArrayList<>();
        for (int i = 0; i < listUsers.size(); i++) {
            statusList.add(listUsers.get(i).getStatus());
            Log.i(TAG, statusList.get(i));
        }
        isfirstUserRequest = statusList.indexOf("request") == 0;
        firstPendingIndex = statusList.indexOf("pending");
        firstConnectedIndex = statusList.indexOf("connected");
    }

    @Override
    public int getItemViewType(int position) {
        if (isfirstUserRequest && position == 0) {
            return TYPE_REQUEST_HEADER;
        } else if (position == firstPendingIndex) {
            return TYPE_PENDING_HEADER;
        } else if (position == firstConnectedIndex) {
            return TYPE_CONNECTED_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM:
                return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_listitem, parent, false));
            case TYPE_REQUEST_HEADER:
                return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_listitem, parent, false));
            case TYPE_PENDING_HEADER:
                return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_listitem, parent, false));
            case TYPE_CONNECTED_HEADER:
                return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_listitem, parent, false));
        }

        throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(ItemViewHolder viewHolder, int position) {
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

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserProfile;
        TextView tvUserName;
        TextView tvRole;
        ImageView ivStatus;

        ItemViewHolder(View itemView) {
            super(itemView);
            ivUserProfile = (ImageView) itemView.findViewById(R.id.ivUserProfile);
            tvUserName = (TextView) itemView.findViewById(R.id.tvFullName);
            tvRole = (TextView) itemView.findViewById(R.id.tvOccupation);
            ivStatus = (ImageView) itemView.findViewById(R.id.ivStatus);
        }

    }
}
