package com.sergio.musicinfo.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Album implements Parcelable {
    @SerializedName("artists")
    private List<SimpleArtist> artistList;
    @SerializedName("id")
    private String id;
    @SerializedName("images")
    private List<Image> images;
    @SerializedName("name")
    private String name;
    @SerializedName("uri")
    private String uri;

    protected Album(Parcel in) {
        artistList = in.createTypedArrayList(SimpleArtist.CREATOR);
        id = in.readString();
        images = in.createTypedArrayList(Image.CREATOR);
        name = in.readString();
        uri = in.readString();
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    public List<SimpleArtist> getArtistList() {
        return artistList;
    }

    public String getArtistString() {
        String artistsName;
        if (artistList.size() >= 1) {
            StringBuilder builder = new StringBuilder();

            for (SimpleArtist a : artistList) {
                if (artistList.lastIndexOf(a) == artistList.size() - 1) {
                    builder.append(a.getName());
                } else {
                    builder.append(a.getName()).append(", ");
                }
            }

            artistsName = builder.toString();
        } else {
            artistsName = artistList.get(0).getName();
        }

        return artistsName;
    }

    public String getId() {
        return id;
    }

    public List<Image> getImages() {
        return images;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(artistList);
        dest.writeString(id);
        dest.writeTypedList(images);
        dest.writeString(name);
        dest.writeString(uri);
    }
}
