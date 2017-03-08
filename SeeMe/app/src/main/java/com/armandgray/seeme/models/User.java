package com.armandgray.seeme.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String firstName;
    private String lastName;
    private String username;
    private String secret;
    private boolean discoverable;
    private String network;

    public User(String firstName, String lastName, String username, String secret, boolean discoverable, String network) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.secret = secret;
        this.discoverable = discoverable;
        this.network = network;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
    }

    protected User(Parcel in) {
        this.firstName = in.readString();
        this.lastName = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
