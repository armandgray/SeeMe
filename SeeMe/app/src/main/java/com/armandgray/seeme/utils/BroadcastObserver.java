package com.armandgray.seeme.utils;

import java.util.Observable;

public class BroadcastObserver extends Observable {

    private static BroadcastObserver instance = new BroadcastObserver();

    public static BroadcastObserver getInstance() { return instance; }

    private BroadcastObserver() {}

    public void updateWifiState(boolean isWifiConnected) {
        synchronized (this) {
            setChanged();
            notifyObservers(isWifiConnected);
        }
    }
}
