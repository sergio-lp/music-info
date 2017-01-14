package com.sergio.musicinfo.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Artist implements Parcelable {
    @SerializedName("followers")
    private Followers followers;
    @SerializedName("genres")
    private List<String> genres;
    @SerializedName("id")
    private String id;
    @SerializedName("images")
    private List<Image> images;
    @SerializedName("name")
    private String name;
    @SerializedName("popularity")
    private int popularity;
    @SerializedName("uri")
    private String uri;

    private Artist(Parcel in) {
        followers = in.readParcelable(Followers.class.getClassLoader());
        genres = in.createStringArrayList();
        id = in.readString();
        images = in.createTypedArrayList(Image.CREATOR);
        name = in.readString();
        popularity = in.readInt();
        uri = in.readString();
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    public Followers getFollowers() {
        return followers;
    }

    public String getGenres() {
        if (genres.size() >= 1) {
            StringBuilder builder = new StringBuilder();

            for (String genre : genres) {
                if (genres.lastIndexOf(genre) == genres.size() - 1) {
                    builder.append(genre);
                } else {
                    builder.append(genre).append(", ");
                }
            }

            return builder.toString();
        }

        return null;
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

    public int getPopularity() {
        return popularity;
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
        dest.writeParcelable(followers, flags);
        dest.writeStringList(genres);
        dest.writeString(id);
        dest.writeTypedList(images);
        dest.writeString(name);
        dest.writeInt(popularity);
        dest.writeString(uri);
    }
}
