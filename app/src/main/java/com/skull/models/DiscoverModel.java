package com.skull.models;

import com.webservices.models.MovieByCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 4/5/2017.
 */

public class DiscoverModel {

    private int id;
    private String name;
    private String url;
    private List<MovieByCategory> allItemsInSection;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<MovieByCategory> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(List<MovieByCategory> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }
}