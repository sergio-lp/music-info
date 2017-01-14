package com.sergio.musicinfo.models.search;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.sergio.musicinfo.models.Music;
import com.sergio.musicinfo.models.SearchResult;

public class MusicSearch {
    @SerializedName("tracks")
    private SearchResult<Music> searchResult;

    public SearchResult<Music> getSearchResult() {
        return searchResult;
    }

}
