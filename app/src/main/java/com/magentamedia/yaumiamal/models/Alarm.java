package com.magentamedia.yaumiamal.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Alarm {

    private String AlarmLabel;
    private String AlarmText;
    private long time;

    public String getAlarmLabel() {
        return AlarmLabel;
    }

    public String getAlarmText() {
        return AlarmText;
    }

    public long getTime() {
        return time;
    }


    public Alarm(String AlarmLabel, String AlarmText, long time) {
        this.AlarmLabel = AlarmLabel;
        this.AlarmText = AlarmText;
        this.time = time;
    }

    protected Alarm(Parcel in) {
        this.AlarmLabel = in.readString();
        this.AlarmText = in.readString();
        this.time = in.readLong();
    }

    @Override
    public String toString() {
        return "alarm {" +
                "label = '" + AlarmLabel + '\'' +
                ", text = '" + AlarmText + '\'' +
                ", waktu = '" + time + '\'' +
                '}';
    }
}
