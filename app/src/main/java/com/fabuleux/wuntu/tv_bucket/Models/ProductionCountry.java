package com.fabuleux.wuntu.tv_bucket.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Wuntu on 23-07-2017.
 */

public class ProductionCountry {

    @SerializedName("iso_3166_1")
    @Expose
    private String iso31661;
    @SerializedName("name")
    @Expose
    private String name;

    public String getIso31661() {
        return iso31661;
    }

    public void setIso31661(String iso31661) {
        this.iso31661 = iso31661;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
