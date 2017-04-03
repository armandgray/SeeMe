package com.armandgray.seeme.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Notes implements Parcelable {

    private String[] notes;

    public Notes(String[] notes) {
        this.notes = notes;
    }

    public String[] getNotes() {
        return notes;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(this.notes);
    }

    protected Notes(Parcel in) {
        this.notes = in.createStringArray();
    }

    public static final Parcelable.Creator<Notes> CREATOR = new Parcelable.Creator<Notes>() {
        @Override
        public Notes createFromParcel(Parcel source) {
            return new Notes(source);
        }

        @Override
        public Notes[] newArray(int size) {
            return new Notes[size];
        }
    };
}
