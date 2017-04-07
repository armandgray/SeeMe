package com.armandgray.seeme.utils;

import android.widget.ImageView;

import com.armandgray.seeme.R;
import com.armandgray.seeme.models.User;

import java.util.ArrayList;

import static com.armandgray.seeme.utils.UserRVAdapter.CONNECTED;
import static com.armandgray.seeme.utils.UserRVAdapter.PENDING;
import static com.armandgray.seeme.utils.UserRVAdapter.REQUEST;
import static com.armandgray.seeme.utils.UserRVAdapter.UNKNOWN;

public class UserAdapterHelper implements UserRVAdapter.RecyclerViewHelper {

    private final boolean isSortedByStatus;

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

    @Override
    public void addHeaderPlaceholder(int index, ArrayList<User> listUsers, ArrayList<String> statusList) {
        if (index != -1) {
            listUsers.add(index, listUsers.get(index));
            statusList.add(index, "");
        }
    }

    @Override
    public void setImageFromStatus(User user, ImageView ivStatus) {
        switch (user.getStatus()) {
            case PENDING:
                ivStatus.setImageResource(R.drawable.ic_account_convert_white_48dp);
                return;
            case CONNECTED:
                ivStatus.setImageResource(R.drawable.ic_account_check_white_48dp);
                return;
            case REQUEST:
                if (isSortedByStatus) {
                    ivStatus.setImageResource(R.drawable.ic_account_plus_white_48dp);
                    return;
                }
                ivStatus.setImageResource(R.drawable.ic_account_check_white_48dp);
                return;
            case UNKNOWN:
                ivStatus.setImageResource(R.drawable.ic_account_plus_white_48dp);
        }
    }
}
