package com.magentamedia.yaumiamal.models;

import android.os.Parcel;
import android.os.Parcelable;

public class dayParcel implements Parcelable {

    private int SUN;
    private int MON;
    private int TUE;
    private int WED;
    private int THU;
    private int FRI;
    private int SAT;

    private dayParcel(Parcel parcel) {
        SUN = parcel.readInt();
        MON = parcel.readInt();
        TUE = parcel.readInt();
        WED = parcel.readInt();
        THU = parcel.readInt();
        FRI = parcel.readInt();
        SAT = parcel.readInt();
    }

    public dayParcel(int SUN, int MON, int TUE, int WED, int THU, int FRI, int SAT) {
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
        parcel.writeInt(SUN);
        parcel.writeInt(MON);
        parcel.writeInt(TUE);
        parcel.writeInt(WED);
        parcel.writeInt(THU);
        parcel.writeInt(FRI);
        parcel.writeInt(SAT);
    }

    public static final Creator<dayParcel> CREATOR = new Creator<dayParcel>() {
        @Override
        public dayParcel createFromParcel(Parcel parcel) {
            return new dayParcel(parcel);
        }

        @Override
        public dayParcel[] newArray(int i) {
            return new dayParcel[i];
        }
    };

    @Override
    public String toString() {
        return "Harian {" +
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

    //set


    public void setSUN(int SUN) {
        this.SUN = SUN;
    }

    public void setMON(int MON) {
        this.MON = MON;
    }

    public void setTUE(int TUE) {
        this.TUE = TUE;
    }

    public void setWED(int WED) {
        this.WED = WED;
    }

    public void setTHU(int THU) {
        this.THU = THU;
    }

    public void setFRI(int FRI) {
        this.FRI = FRI;
    }

    public void setSAT(int SAT) {
        this.SAT = SAT;
    }
}
