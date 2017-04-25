package com.webservices.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.List;

/**
 * Created by Administrator on 4/17/2017.
 */

public class ApiResponse {
    @SerializedName("data")
    @Expose
    private List<Banner> bannerList = null;

    @SerializedName("category_data")
    @Expose
    private List<Category> categoryList = null;


    @SerializedName("movies_by_category")
    @Expose
    private List<MovieByCategory> moviesByCategory = null;

    @SerializedName("movies_data")
    @Expose
    private List<MovieByCategory> mMoviesList = null;




    public List<MovieByCategory> getmMoviesList() {
        return mMoviesList;
    }

    public void setmMoviesList(List<MovieByCategory> mMoviesList) {
        this.mMoviesList = mMoviesList;
    }

    public List<MovieByCategory> getMoviesByCategory() {
        return moviesByCategory;
    }

    public void setMoviesByCategory(List<MovieByCategory> moviesByCategory) {
        this.moviesByCategory = moviesByCategory;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Banner> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<Banner> bannerList) {
        this.bannerList = bannerList;
    }
}
