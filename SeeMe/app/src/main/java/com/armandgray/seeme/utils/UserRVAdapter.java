package com.armandgray.seeme.utils;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.armandgray.seeme.R;
import com.armandgray.seeme.models.User;

import java.util.ArrayList;
import java.util.HashMap;

public class UserRVAdapter extends
        RecyclerView.Adapter<ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_REQUEST_HEADER = 1;
    private static final int TYPE_PENDING_HEADER = 2;
    private static final int TYPE_CONNECTED_HEADER = 3;

    static final String UNKNOWN = "unknown";
    static final String CONNECTED = "connected";
    static final String PENDING = "pending";
    static final String REQUEST = "request";

    private static final String TAG = "USER_RV_ADAPTER";

    private final ArrayList<User> listUsers;
    private final RecyclerViewHelper helper;
    private final HashMap<String, Integer> mapPivotIndices;

    public UserRVAdapter(ArrayList<User> listUsers, boolean isSortedByStatus) {
        this.listUsers = listUsers;
        this.helper = new UserAdapterHelper(isSortedByStatus);
        this.mapPivotIndices = new HashMap<>();

        assignPivotIndices(isSortedByStatus);
    }

    private void assignPivotIndices(boolean isSortedByStatus) {
        ArrayList<String> statusList = new ArrayList<>();
        if (isSortedByStatus) {
            statusList = helper.getUserStatusList(listUsers);
        }

        mapPivotIndices.put(REQUEST, statusList.indexOf(REQUEST));
        helper.addHeaderPlaceholder(mapPivotIndices.get(REQUEST), listUsers, statusList);
        mapPivotIndices.put(PENDING, statusList.indexOf(PENDING));
        helper.addHeaderPlaceholder(mapPivotIndices.get(PENDING), listUsers, statusList);
        mapPivotIndices.put(CONNECTED, statusList.indexOf(CONNECTED));
        helper.addHeaderPlaceholder(mapPivotIndices.get(CONNECTED), listUsers, statusList);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mapPivotIndices.get(REQUEST)) {
            return TYPE_REQUEST_HEADER;
        } else if (position == mapPivotIndices.get(PENDING)) {
            return TYPE_PENDING_HEADER;
        } else if (position == mapPivotIndices.get(CONNECTED)) {
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
            TextView tvUserName = viewHolder.tvUserName;
            TextView tvRole = viewHolder.tvOccupation;
            ImageView ivStatus = viewHolder.ivStatus;

            tvUserName.setText(user.getFirstName() + " " + user.getLastName());
            tvRole.setText("Title: " + user.getOccupation());
            helper.setImageFromStatus(user, ivStatus);

        } else if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder viewHolder = (HeaderViewHolder) holder;
            TextView tvHeader = viewHolder.tvHeader;
            switch (viewHolder.type) {
                case TYPE_REQUEST_HEADER:
                    tvHeader.setText(R.string.connection_requests);
                    return;
                case TYPE_PENDING_HEADER:
                    tvHeader.setText(R.string.pending_response);
                    return;
                case TYPE_CONNECTED_HEADER:
                    tvHeader.setText(R.string.current_network);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    public ArrayList<User> getListUsers() {
        return listUsers;
    }

    public HashMap<String, Integer> getMapPivotIndices() {
        return mapPivotIndices;
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivUserProfile;
        final TextView tvUserName;
        final TextView tvOccupation;
        final ImageView ivStatus;

        ItemViewHolder(View itemView) {
            super(itemView);
            ivUserProfile = (ImageView) itemView.findViewById(R.id.ivUserProfile);
            tvUserName = (TextView) itemView.findViewById(R.id.tvFullName);
            tvOccupation = (TextView) itemView.findViewById(R.id.tvOccupation);
            ivStatus = (ImageView) itemView.findViewById(R.id.ivStatus);
        }

    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        final TextView tvHeader;
        final int type;

        HeaderViewHolder(View itemView, int headerType) {
            super(itemView);
            tvHeader = (TextView) itemView.findViewById(R.id.tvHeader);
            type = headerType;
        }
    }

    interface RecyclerViewHelper {
        ArrayList<String> getUserStatusList(ArrayList<User> listUsers);
        void addHeaderPlaceholder(int index, ArrayList<User> listUsers, ArrayList<String> statusList);
        void setImageFromStatus(User user, ImageView ivStatus);
    }
}
