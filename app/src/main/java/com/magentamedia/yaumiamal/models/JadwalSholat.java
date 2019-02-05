package com.magentamedia.yaumiamal.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.magentamedia.yaumiamal.providers.YawmiMethodes;

import java.util.Date;

public class JadwalSholat implements Parcelable {

    private String azan_label;
    private Date azan_time;
    private boolean azan_state;

    public JadwalSholat(String azan_label, Date azan_time, boolean azan_state) {

        this.azan_label = azan_label;
        this.azan_time = azan_time;
        this.azan_state = azan_state;
    }

    public void setAzan_label(String azan_label) {
        this.azan_label = azan_label;
    }

    public void setAzan_state(boolean azan_state) {
        this.azan_state = azan_state;
    }

    public void setAzan_time(Date azan_time) {
        this.azan_time = azan_time;
    }

    public String getAzan_label() {
        return azan_label;
    }

    public Date getAzan_time() {
        return azan_time;
    }

    public boolean isAzan_state() {
        return azan_state;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.azan_label);
        dest.writeLong(this.azan_time != null ? this.azan_time.getTime() : -1);
        dest.writeByte(this.azan_state ? (byte) 1 : (byte) 0);
    }

    protected JadwalSholat(Parcel in) {
        this.azan_label = in.readString();
        long tmpAzan_time = in.readLong();
        this.azan_time = tmpAzan_time == -1 ? null : new Date(tmpAzan_time);
        this.azan_state = in.readByte() != 0;
    }

    public static final Parcelable.Creator<JadwalSholat> CREATOR = new Parcelable.Creator<JadwalSholat>() {
        @Override
        public JadwalSholat createFromParcel(Parcel source) {
            return new JadwalSholat(source);
        }

        @Override
        public JadwalSholat[] newArray(int size) {
            return new JadwalSholat[size];
        }
    };

    @Override
    public String toString() {
        YawmiMethodes me = new YawmiMethodes();

        return "Azan {" + azan_label + ", " + me.toTimes(azan_time) + ", " + azan_state + "}";
    }
}
