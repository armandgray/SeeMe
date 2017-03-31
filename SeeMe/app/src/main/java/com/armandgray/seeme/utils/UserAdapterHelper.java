package com.armandgray.seeme.utils;

import com.armandgray.seeme.models.User;

import java.util.ArrayList;

public class UserAdapterHelper implements UserRVAdapter.RecyclerViewHelper {

    private ArrayList<User> listUsers;
    private boolean isSortedByStatus;

    public UserAdapterHelper(ArrayList<User> listUsers, boolean isSortedByStatus) {
        this.listUsers = listUsers;
        this.isSortedByStatus = isSortedByStatus;
    }
}
