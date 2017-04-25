package com.skull.models;

/**
 * Created by Administrator on 4/5/2017.
 */

public class SectionDiscoverItems {
    private String name;
    private String url;
    private String description;


    public SectionDiscoverItems() {
    }

    public SectionDiscoverItems(String name, String url) {
        this.name = name;
        this.url = url;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
