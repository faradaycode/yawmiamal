package com.magentamedia.yaumiamal.models;

public class daily_activities {

    String amalan;
    String waktu_saved;
    int hari_saved;
    int the_ID;
    int notif_enabled;

    public daily_activities(String amalan, int hari_saved, String waktu_saved, int the_ID,
                            int notif_enabled) {
        this.the_ID = the_ID;
        this.amalan = amalan;
        this.waktu_saved = waktu_saved;
        this.hari_saved = hari_saved;
        this.notif_enabled = notif_enabled;
    }

    public String getWaktu_saved() {
        return waktu_saved;
    }

    public String getAmalan() {
        return amalan;
    }

    public int getThe_ID() {
        return the_ID;
    }

    public int getHari_saved() {
        return hari_saved;
    }

    public int getNotif_enabled() {
        return notif_enabled;
    }
}
