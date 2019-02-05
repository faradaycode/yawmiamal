package com.magentamedia.yaumiamal.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class bootServices extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (action.equals("android.intent.action.BOOT_COMPLETED")) {


            Log.i("OnBoot", "let's start");
        } else if (action.equals("android.intent.action.TIMEZONE_CHANGED") ||
                action.equals("android.intent.action.TIME_SET") ||
                action.equals("android.intent.action.MY_PACKAGE_REPLACED")) {


            Log.i("OnTIMEZONE", "time zone start");
        }
    }
}