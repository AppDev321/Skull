package com.webservices.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 4/20/2017.
 */

public class ParseData {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("urls")
    @Expose
    private List<YoutubePareUrl> urls = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    public List<YoutubePareUrl> getUrls() {
        return urls;
    }

    public void setUrls(List<YoutubePareUrl> urls) {
        this.urls = urls;
    }
}
