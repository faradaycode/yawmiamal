package com.magentamedia.yaumiamal.models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class Items {

    @SerializedName("data")
    JsonObject datas;

    @SerializedName("status")
    String status;

    public String getStatus() {
        return status;
    }

    public JsonObject getTimings() {
        return (JsonObject) datas.get("timings");
    }
}
