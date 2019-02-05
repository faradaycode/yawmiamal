package com.magentamedia.yaumiamal.models;

import android.os.Parcel;
import android.os.Parcelable;

public class amalParcel implements Parcelable {

    private int id_list_amal;
    private String amalan;
    private String mTarget;
    private String notif_time;
    private int notif_enabled;
    private int aGroup;
    private int SUN;
    private int MON;
    private int TUE;
    private int WED;
    private int THU;
    private int FRI;
    private int SAT;

    private amalParcel(Parcel parcel) {
        id_list_amal = parcel.readInt();
        amalan = parcel.readString();
        mTarget = parcel.readString();
        notif_time = parcel.readString();
        notif_enabled = parcel.readInt();
        aGroup = parcel.readInt();
        SUN = parcel.readInt();
        MON = parcel.readInt();
        TUE = parcel.readInt();
        WED = parcel.readInt();
        THU = parcel.readInt();
        FRI = parcel.readInt();
        SAT = parcel.readInt();
    }

    public amalParcel(int id_list_amal, String amalan, String mTarget, String notif_time,
                      int notif_enabled, int aGroup, int SUN, int MON, int TUE, int WED, int THU,
                      int FRI, int SAT) {

        this.id_list_amal = id_list_amal;
        this.amalan = amalan;
        this.mTarget = mTarget;
        this.notif_time = notif_time;
        this.notif_enabled = notif_enabled;
        this.aGroup = aGroup;
        this.SUN = SUN;
        this.MON = MON;
        this.TUE = TUE;
        this.WED = WED;
        this.THU = THU;
        this.FRI = FRI;
        this.SAT = SAT;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id_list_amal);
        parcel.writeString(amalan);
        parcel.writeString(mTarget);
        parcel.writeString(notif_time);
        parcel.writeInt(notif_enabled);
        parcel.writeInt(aGroup);
        parcel.writeInt(SUN);
        parcel.writeInt(MON);
        parcel.writeInt(TUE);
        parcel.writeInt(WED);
        parcel.writeInt(THU);
        parcel.writeInt(FRI);
        parcel.writeInt(SAT);
    }

    public static final Creator<amalParcel> CREATOR = new Creator<amalParcel>() {
        @Override
        public amalParcel createFromParcel(Parcel parcel) {
            return new amalParcel(parcel);
        }

        @Override
        public amalParcel[] newArray(int i) {
            return new amalParcel[i];
        }
    };

    @Override
    public String toString() {
        return "My Amal {" +
                "id_la = '" + id_list_amal + '\'' +
                ", amalan = '" + amalan + '\'' +
                ", mTarget = " + mTarget  + '\'' +
                ", notif_time = " + notif_time  + '\'' +
                ", notif_enabled = " + notif_enabled  + '\'' +
                ", aGroup = " + aGroup  + '\'' +
                ", SUN = " + SUN + '\'' +
                ", MON = " + MON  + '\'' +
                ", TUE = " + TUE  + '\'' +
                ", WED = " + WED  + '\'' +
                ", THU = " + THU  + '\'' +
                ", FRI = " + FRI   + '\'' +
                ", SAT = "+ SAT  + '\'' +
                '}';
    }

    //get

    public int getId_list_amal() {
        return id_list_amal;
    }

    public String getAmalan() {
        return amalan;
    }

    public String getmTarget() {
        return mTarget;
    }

    public int getNotif_enabled() {
        return notif_enabled;
    }

    public String getNotif_time() {
        return notif_time;
    }

    public int getaGroup() {
        return aGroup;
    }

    public int getSUN() {
        return SUN;
    }

    public int getMON() {
        return MON;
    }

    public int getTUE() {
        return TUE;
    }

    public int getWED() {
        return WED;
    }

    public int getTHU() {
        return THU;
    }

    public int getFRI() {
        return FRI;
    }

    public int getSAT() {
        return SAT;
    }
}
