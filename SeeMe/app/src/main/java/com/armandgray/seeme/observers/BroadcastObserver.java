package com.armandgray.seeme.observers;

import android.net.NetworkInfo;

import java.util.Observable;

public class BroadcastObserver extends Observable {

    private static final BroadcastObserver instance = new BroadcastObserver();

    public static BroadcastObserver getInstance() { return instance; }

    private BroadcastObserver() {}

    public void updateWifiState(NetworkInfo networkInfo) {
        synchronized (this) {
            setChanged();
            notifyObservers(networkInfo);
        }
    }
}
