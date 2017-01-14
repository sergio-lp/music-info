package com.sergio.musicinfo.models.search;

import com.google.gson.annotations.SerializedName;
import com.sergio.musicinfo.models.Artist;

import java.util.List;


public class MultipleArtists {
    @SerializedName("artists")
    private List<Artist> artistList;

    public List<Artist> getArtistList() {
        return artistList;
    }
}
