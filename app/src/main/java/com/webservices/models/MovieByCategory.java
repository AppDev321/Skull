package com.webservices.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 4/17/2017.
 */

public class MovieByCategory implements Serializable {


    @SerializedName("id")
    @Expose
    private int id;



    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("year")
    @Expose
    private int year;
    @SerializedName("rate")
    @Expose
    private int rate;
    @SerializedName("category_id")
    @Expose
    private int categoryId;
    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("video_link")
    @Expose
    private String videoLink;
    @SerializedName("is_youtube")
    @Expose
    private int isYoutube;
    @SerializedName("director")
    @Expose
    private String director;
    @SerializedName("writer")
    @Expose
    private String writer;
    @SerializedName("prod")
    @Expose
    private String prod;
    @SerializedName("countries")
    @Expose
    private String countries;
    @SerializedName("dur")
    @Expose
    private int dur;
    @SerializedName("rate2")
    @Expose
    private String rate2;
    @SerializedName("cov")
    @Expose
    private String cov;
    @SerializedName("plot")
    @Expose
    private String plot;
    @SerializedName("plotout")
    @Expose
    private String plotout;
    @SerializedName("cast")
    @Expose
    private String cast;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public int getIsYoutube() {
        return isYoutube;
    }

    public void setIsYoutube(int isYoutube) {
        this.isYoutube = isYoutube;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getProd() {
        return prod;
    }

    public void setProd(String prod) {
        this.prod = prod;
    }

    public String getCountries() {
        return countries;
    }

    public void setCountries(String countries) {
        this.countries = countries;
    }

    public int getDur() {
        return dur;
    }

    public void setDur(int dur) {
        this.dur = dur;
    }

    public String getRate2() {
        return rate2;
    }

    public void setRate2(String rate2) {
        this.rate2 = rate2;
    }

    public String getCov() {
        return cov;
    }

    public void setCov(String cov) {
        this.cov = cov;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getPlotout() {
        return plotout;
    }

    public void setPlotout(String plotout) {
        this.plotout = plotout;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }


}