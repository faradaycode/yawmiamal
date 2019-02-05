package com.magentamedia.yaumiamal.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArtikelMessagesModel {
    @SerializedName("id_ar")
    @Expose
    private int idAr;

    @SerializedName("ar_author")
    @Expose
    private String arAuthor;

    @SerializedName("ar_title")
    @Expose
    private String arTitle;

    @SerializedName("id_ca")
    @Expose
    private String idCa;

    @SerializedName("ar_post")
    @Expose
    private String arPost;

    @SerializedName("ar_datepost")
    @Expose
    private String arDatepost;

    @SerializedName("ar_viewed")
    @Expose
    private String arViewed;

    @SerializedName("ar_likes")
    @Expose
    private String arLikes;

    @SerializedName("ar_dislikes")
    @Expose
    private String arDislikes;

    @SerializedName("ar_neutral")
    @Expose
    private String arNeutral;

    public int getIdAr() {
        return idAr;
    }

    public void setIdAr(int idAr) {
        this.idAr = idAr;
    }

    public String getArAuthor() {
        return arAuthor;
    }

    public void setArAuthor(String arAuthor) {
        this.arAuthor = arAuthor;
    }

    public String getArTitle() {
        return arTitle;
    }

    public void setArTitle(String arTitle) {
        this.arTitle = arTitle;
    }

    public String getIdCa() {
        return idCa;
    }

    public void setIdCa(String idCa) {
        this.idCa = idCa;
    }

    public String getArPost() {
        return arPost;
    }

    public void setArPost(String arPost) {
        this.arPost = arPost;
    }

    public String getArDatepost() {
        return arDatepost;
    }

    public void setArDatepost(String arDatepost) {
        this.arDatepost = arDatepost;
    }

    public String getArViewed() {
        return arViewed;
    }

    public void setArViewed(String arViewed) {
        this.arViewed = arViewed;
    }

    public String getArLikes() {
        return arLikes;
    }

    public void setArLikes(String arLikes) {
        this.arLikes = arLikes;
    }

    public String getArDislikes() {
        return arDislikes;
    }

    public void setArDislikes(String arDislikes) {
        this.arDislikes = arDislikes;
    }

    public String getArNeutral() {
        return arNeutral;
    }

    public void setArNeutral(String arNeutral) {
        this.arNeutral = arNeutral;
    }
}
