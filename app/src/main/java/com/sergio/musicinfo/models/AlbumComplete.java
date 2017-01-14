package com.sergio.musicinfo.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AlbumComplete implements Parcelable {
    @SerializedName("release_date")
    private String releaseDate;

    private AlbumComplete(Parcel in) {
        releaseDate = in.readString();
    }

    public static final Creator<AlbumComplete> CREATOR = new Creator<AlbumComplete>() {
        @Override
        public AlbumComplete createFromParcel(Parcel in) {
            return new AlbumComplete(in);
        }

        @Override
        public AlbumComplete[] newArray(int size) {
            return new AlbumComplete[size];
        }
    };

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(releaseDate);
    }
}
