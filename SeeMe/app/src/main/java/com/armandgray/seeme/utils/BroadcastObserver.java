package com.armandgray.seeme.utils;

import android.net.NetworkInfo;

import java.util.Observable;

public class BroadcastObserver extends Observable {

    private static BroadcastObserver instance = new BroadcastObserver();

    public static BroadcastObserver getInstance() { return instance; }

    private BroadcastObserver() {}

    void updateWifiState(NetworkInfo networkInfo) {
        synchronized (this) {
            setChanged();
            notifyObservers(networkInfo);
        }
    }
}
