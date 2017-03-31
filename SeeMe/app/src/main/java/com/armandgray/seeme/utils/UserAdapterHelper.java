package com.armandgray.seeme.utils;

import com.armandgray.seeme.models.User;

import java.util.ArrayList;

public class UserAdapterHelper implements UserRVAdapter.RecyclerViewHelper {

    private boolean isSortedByStatus;

    public UserAdapterHelper(boolean isSortedByStatus) {
        this.isSortedByStatus = isSortedByStatus;
    }

    @Override
    public ArrayList<String> getUserStatusList(ArrayList<User> listUsers) {
        ArrayList<String> statusList = new ArrayList<>();
        for (int i = 0; i < listUsers.size(); i++) {
            statusList.add(listUsers.get(i).getStatus());
        }
        return statusList;
    }
}
