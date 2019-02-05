package com.magentamedia.yaumiamal.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArtikelModel {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private ArtikelMessagesModel message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArtikelMessagesModel getMessage() {
        return message;
    }

    public void setMessage(ArtikelMessagesModel message) {
        this.message = message;
    }
}
