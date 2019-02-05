package com.magentamedia.yaumiamal.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MyAmalModel implements Parcelable {
    private int id_a;
    private String amalan;
    private String mTarget;
    private String notif_time;
    private int notif_enabled;
    private int id_la;


    public MyAmalModel(int id_a, int id_la, String amalan, String mTarget, String notif_time,
                       int notif_enabled) {
        this.id_a = id_a;
        this.id_la = id_la;
        this.amalan = amalan;
        this.mTarget = mTarget;
        this.notif_time = notif_time;
        this.notif_enabled = notif_enabled;
    }

    public int getId_a() {
        return id_a;
    }

    public String getAmalan() {
        return amalan;
    }

    public String getmTarget() {
        return mTarget;
    }

    public String getNotif_time() {
        return notif_time;
    }

    public int getNotif_enabled() {
        return notif_enabled;
    }

    public int getId_la() {
        return id_la;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id_a);
        dest.writeString(this.amalan);
        dest.writeString(this.mTarget);
        dest.writeString(this.notif_time);
        dest.writeInt(this.notif_enabled);
        dest.writeInt(this.id_la);
    }

    protected MyAmalModel(Parcel in) {
        this.id_a = in.readInt();
        this.amalan = in.readString();
        this.mTarget = in.readString();
        this.notif_time = in.readString();
        this.notif_enabled = in.readInt();
        this.id_la = in.readInt();
    }

    public static final Parcelable.Creator<MyAmalModel> CREATOR = new Parcelable.Creator<MyAmalModel>() {
        @Override
        public MyAmalModel createFromParcel(Parcel source) {
            return new MyAmalModel(source);
        }

        @Override
        public MyAmalModel[] newArray(int size) {
            return new MyAmalModel[size];
        }
    };
}
