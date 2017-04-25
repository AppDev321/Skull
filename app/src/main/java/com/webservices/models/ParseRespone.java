
package com.webservices.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParseRespone {
    @SerializedName("data")
    @Expose
    private ParseData data;

    public ParseData getData() {
        return data;
    }

    public void setData(ParseData data) {
        this.data = data;
    }





}