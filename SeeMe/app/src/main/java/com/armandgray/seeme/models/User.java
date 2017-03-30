package com.armandgray.seeme.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

public class User implements Parcelable, Comparator<User> {

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

    public int compareTo(User u) {
        return Comparators.FIRST_NAME.compare(this, u);
    }

    @Override
    public int compare(User u1, User u2) {
        return Comparators.FIRST_NAME.compare(u1, u2);
    }

    public static class Comparators {

        public static Comparator<User> FIRST_NAME = new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                return u1.firstName.compareTo(u2.firstName);
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.occupation);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.username);
        dest.writeString(this.secret);
        dest.writeByte(this.discoverable ? (byte) 1 : (byte) 0);
        dest.writeString(this.network);
        dest.writeString(this.status);
    }

    protected User(Parcel in) {
        this.occupation = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.username = in.readString();
        this.secret = in.readString();
        this.discoverable = in.readByte() != 0;
        this.network = in.readString();
        this.status = in.readString();
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
