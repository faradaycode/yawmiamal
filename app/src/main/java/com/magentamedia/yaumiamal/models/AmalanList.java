package com.magentamedia.yaumiamal.models;

public class AmalanList {
    private int id;
    private String amal;
    private String type;
    private int grouping;


    public int getId() {
        return id;
    }

    public String getAmal() {
        return amal;
    }

    public String getType() {
        return type;
    }

    public int getGrouping() {
        return grouping;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAmal(String amal) {
        this.amal = amal;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setGrouping(int grouping) {
        this.grouping = grouping;
    }
}
