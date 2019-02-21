package com.magentamedia.yaumiamal.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Alarm implements Parcelable {

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

    @Override
    public String toString() {
        return "alarm {" +
                "label = '" + AlarmLabel + '\'' +
                ", text = '" + AlarmText + '\'' +
                ", waktu = '" + time + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.AlarmLabel);
        dest.writeString(this.AlarmText);
        dest.writeLong(this.time);
    }

    protected Alarm(Parcel in) {
        this.AlarmLabel = in.readString();
        this.AlarmText = in.readString();
        this.time = in.readLong();
    }

    public static final Parcelable.Creator<Alarm> CREATOR = new Parcelable.Creator<Alarm>() {
        @Override
        public Alarm createFromParcel(Parcel source) {
            return new Alarm(source);
        }

        @Override
        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };
}
