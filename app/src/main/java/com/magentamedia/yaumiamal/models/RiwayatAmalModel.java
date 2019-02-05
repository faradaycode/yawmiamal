package com.magentamedia.yaumiamal.models;

public class RiwayatAmalModel {
    int idx;
    String amalan;
    int status;

    public RiwayatAmalModel(int idx, String amalan, int status) {
        this.idx = idx;
        this.amalan = amalan;
        this.status = status;
    }

    public int getIdx() {
        return idx;
    }

    public String getAmalan() {
        return amalan;
    }

    public int getStatus() {
        return status;
    }
}
