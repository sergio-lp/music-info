package com.sergio.musicinfo.models.search;

import com.google.gson.annotations.SerializedName;
import com.sergio.musicinfo.models.Album;
import com.sergio.musicinfo.models.SearchResult;

public class AlbumSearch {
    @SerializedName("albums")
    private SearchResult<Album> searchResult;

    public SearchResult<Album> getSearchResult() {
        return searchResult;
    }
}
