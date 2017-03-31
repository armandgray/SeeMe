package com.armandgray.seeme.utils;

import android.support.v7.widget.RecyclerView.ViewHolder;
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
        RecyclerView.Adapter<ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_REQUEST_HEADER = 1;
    private static final int TYPE_PENDING_HEADER = 2;
    private static final int TYPE_CONNECTED_HEADER = 3;

    private static final String UNKNOWN = "unknown";
    private static final String CONNECTED = "connected";
    private static final String PENDING = "pending";
    private static final String REQUEST = "request";

    private static final String CONNECTION_REQUESTS = "Connection Requests";
    private static final String PENDING_RESPONSE = "Pending Response";
    private static final String CURRENT_NETWORK = "Current Network";

    private static final String TAG = "USER_RV_ADAPTER";

    private ArrayList<User> listUsers;
    private boolean showRequest;
    private final boolean isfirstUserRequest;
    private final int firstPendingIndex;
    private final int firstConnectedIndex;

    public UserRVAdapter(ArrayList<User> listUsers, boolean showRequest) {
        this.listUsers = listUsers;
        this.showRequest = showRequest;
        List<String> statusList = new ArrayList<>();
        for (int i = 0; i < listUsers.size(); i++) {
            statusList.add(listUsers.get(i).getStatus());
            Log.i(TAG, statusList.get(i));
        }
        isfirstUserRequest = statusList.indexOf("request") == 0;
        if (isfirstUserRequest) {
            listUsers.add(0, listUsers.get(0));
            statusList.add(0, "");
        }
        firstPendingIndex = statusList.indexOf("pending");
        if (firstPendingIndex != -1) {
            listUsers.add(firstPendingIndex, listUsers.get(firstPendingIndex));
            statusList.add(firstPendingIndex, "");
        }
        firstConnectedIndex = statusList.indexOf("connected");
        if (firstConnectedIndex != -1) {
            listUsers.add(firstConnectedIndex, listUsers.get(firstConnectedIndex));
            statusList.add(firstConnectedIndex, "");
        }
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM:
                return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_listitem, parent, false));
            case TYPE_REQUEST_HEADER:
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.header_layout, parent, false), TYPE_REQUEST_HEADER);
            case TYPE_PENDING_HEADER:
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.header_layout, parent, false), TYPE_PENDING_HEADER);
            case TYPE_CONNECTED_HEADER:
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.header_layout, parent, false), TYPE_CONNECTED_HEADER);
        }

        throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        User user = listUsers.get(position);

        if (holder instanceof ItemViewHolder) {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            ImageView ivUserProfile = viewHolder.ivUserProfile;
            TextView tvUserName = viewHolder.tvUserName;
            TextView tvRole = viewHolder.tvOccupation;
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
        } else if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder viewHolder = (HeaderViewHolder) holder;
            TextView tvHeader = viewHolder.tvHeader;
            switch (viewHolder.type) {
                case TYPE_REQUEST_HEADER:
                    tvHeader.setText(CONNECTION_REQUESTS);
                    return;
                case TYPE_PENDING_HEADER:
                    tvHeader.setText(PENDING_RESPONSE);
                    return;
                case TYPE_CONNECTED_HEADER:
                    tvHeader.setText(CURRENT_NETWORK);
                    return;
            }
        }
    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserProfile;
        TextView tvUserName;
        TextView tvOccupation;
        ImageView ivStatus;

        ItemViewHolder(View itemView) {
            super(itemView);
            ivUserProfile = (ImageView) itemView.findViewById(R.id.ivUserProfile);
            tvUserName = (TextView) itemView.findViewById(R.id.tvFullName);
            tvOccupation = (TextView) itemView.findViewById(R.id.tvOccupation);
            ivStatus = (ImageView) itemView.findViewById(R.id.ivStatus);
        }

    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeader;
        int type;

        HeaderViewHolder(View itemView, int headerType) {
            super(itemView);
            tvHeader = (TextView) itemView.findViewById(R.id.tvHeader);
            type = headerType;
        }

    }
}
