package com.armandgray.seeme.models;

public class Network {

    private String ssid;
    private String networkId;

    public Network(String ssid, String networkId) {
        this.ssid = ssid;
        this.networkId = networkId;
    }

    public String getSsid() {
        return ssid;
    }
    public String getNetworkId() {
        return networkId;
    }
}
