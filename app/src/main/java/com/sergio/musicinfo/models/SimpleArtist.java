package com.sergio.musicinfo.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SimpleArtist implements Parcelable{
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;

    private SimpleArtist(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<SimpleArtist> CREATOR = new Creator<SimpleArtist>() {
        @Override
        public SimpleArtist createFromParcel(Parcel in) {
            return new SimpleArtist(in);
        }

        @Override
        public SimpleArtist[] newArray(int size) {
            return new SimpleArtist[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }
}