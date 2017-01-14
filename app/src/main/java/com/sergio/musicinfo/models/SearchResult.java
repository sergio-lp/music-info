package com.sergio.musicinfo.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SearchResult<T> {
    @SerializedName("items")
    private List<T> resultList;
    @SerializedName("limit")
    private int limit;
    @SerializedName("next")
    private String nextUrl;
    @SerializedName("total")
    private int total;

    public List<T> getResultList() {
        return resultList;
    }

    public int getLimit() {
        return limit;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public int getTotal() {
        return total;
    }

}