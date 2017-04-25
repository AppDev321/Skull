package com.webservices.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 4/17/2017.
 */

public class Banner {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("video_id")
    @Expose
    private String videoId;
    @SerializedName("is_youtube")
    @Expose
    private String isYoutube;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getIsYoutube() {
        return isYoutube;
    }

    public void setIsYoutube(String isYoutube) {
        this.isYoutube = isYoutube;
    }

}


