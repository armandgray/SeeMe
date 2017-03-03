package com.armandgray.seeme.utils;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.armandgray.seeme.R;
import com.armandgray.seeme.models.FoodItem;

import java.util.List;

public class UserRVAdapter extends
        RecyclerView.Adapter<UserRVAdapter.ViewHolder> {

    Activity activity;
    List<FoodItem> listUsers;

    public UserRVAdapter(Activity activity, List<FoodItem> listUsers) {
        this.activity = activity;
        this.listUsers = listUsers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_listitem, parent, false));
    }

    @Override
    public void onBindViewHolder(UserRVAdapter.ViewHolder viewHolder, int position) {
        FoodItem user = listUsers.get(position);

        ImageView ivUserProfile = viewHolder.ivUserProfile;
        TextView tvUserName = viewHolder.tvUserName;
        TextView tvRole = viewHolder.tvRole;

        tvUserName.setText(user.getItemName() + " - " + user.getCategory());
        tvRole.setText(String.valueOf(user.getPrice()));
    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserProfile;
        TextView tvUserName;
        TextView tvRole;

        ViewHolder(View itemView) {
            super(itemView);
            ivUserProfile = (ImageView) itemView.findViewById(R.id.ivUserProfile);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvRole = (TextView) itemView.findViewById(R.id.tvRole);
        }

    }
}
