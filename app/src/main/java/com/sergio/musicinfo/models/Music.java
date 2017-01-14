package com.sergio.musicinfo.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Music implements Parcelable {
    @SerializedName("album")
    private Album album;
    @SerializedName("artists")
    private List<SimpleArtist> artistList;
    @SerializedName("disc_number")
    private int discNumber;
    @SerializedName("duration_ms")
    private int durationMS;
    @SerializedName("explicit")
    private boolean explicit;
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("popularity")
    private int popularity;
    @SerializedName("preview_url")
    private String previewURL;
    @SerializedName("uri")
    private String uri;

    private Music(Parcel in) {
        album = in.readParcelable(Album.class.getClassLoader());
        artistList = in.createTypedArrayList(SimpleArtist.CREATOR);
        discNumber = in.readInt();
        durationMS = in.readInt();
        explicit = in.readByte() != 0;
        id = in.readString();
        name = in.readString();
        popularity = in.readInt();
        previewURL = in.readString();
        uri = in.readString();
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

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

    public int getDiscNumber() {
        return discNumber;
    }

    public int getDurationMS() {
        return durationMS;
    }

    public boolean isExplicit() {
        return explicit;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPopularity() {
        return popularity;
    }

    public String getPreviewURL() {
        return previewURL;
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
        dest.writeParcelable(album, flags);
        dest.writeTypedList(artistList);
        dest.writeInt(discNumber);
        dest.writeInt(durationMS);
        dest.writeByte((byte) (explicit ? 1 : 0));
        dest.writeString(id);
        dest.writeString(name);
        dest.writeInt(popularity);
        dest.writeString(previewURL);
        dest.writeString(uri);
    }
}