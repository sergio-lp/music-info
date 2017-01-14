package com.sergio.musicinfo.models.search;

import com.google.gson.annotations.SerializedName;
import com.sergio.musicinfo.models.Artist;
import com.sergio.musicinfo.models.SearchResult;

import java.util.List;

public class ArtistSearch {
    @SerializedName("artists")
    private SearchResult<Artist> searchResult;

    public SearchResult<Artist> getSearchResult() {
        return searchResult;
    }
}
