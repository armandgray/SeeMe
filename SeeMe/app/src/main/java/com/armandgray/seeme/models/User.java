package com.armandgray.seeme.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private final String occupation;
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String secret;
    private final boolean discoverable;
    private final String network;
    private String status;

    public User(String firstName, String lastName, String occupation, String username, String secret, boolean discoverable, String network, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.occupation = occupation;
        this.username = username;
        this.secret = secret;
        this.discoverable = discoverable;
        this.network = network;
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getSecret() {
        return secret;
    }

    public boolean isDiscoverable() {
        return discoverable;
    }

    public String getNetwork() {
        return network;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
