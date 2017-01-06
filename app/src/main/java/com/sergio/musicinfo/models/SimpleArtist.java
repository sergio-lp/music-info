package com.sergio.musicinfo.models;

import com.google.gson.annotations.SerializedName;

public class SimpleArtist {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}