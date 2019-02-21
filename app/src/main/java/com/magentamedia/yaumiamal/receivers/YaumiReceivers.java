package com.magentamedia.yaumiamal.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.magentamedia.yaumiamal.models.Alarm;
import com.magentamedia.yaumiamal.models.MyAmalModel;
import com.magentamedia.yaumiamal.services.ServIntentYaumi;

import java.util.ArrayList;

public class YaumiReceivers extends BroadcastReceiver {

    private onAmalResultListener aListener;
    private static String TAG = YaumiReceivers.class.getSimpleName();

    @SuppressWarnings("unused")
    public YaumiReceivers() {}

    public YaumiReceivers(onAmalResultListener listener) {
        aListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        ArrayList<Alarm> myAlarms = intent
                .getParcelableArrayListExtra(ServIntentYaumi.EXTRAS_DATA);
        ArrayList<MyAmalModel> amalModels = intent
                .getParcelableArrayListExtra(ServIntentYaumi.FRAGMENT_DATA);

        AlarmsReceiver.setMyAlarm(context, myAlarms);

        Log.wtf(TAG, "CALLED FROM SERVICE");
        Log.v(ServIntentYaumi.FRAGMENT_DATA, amalModels.toString());
        Log.d(ServIntentYaumi.EXTRAS_DATA, myAlarms.toString());
    }

    public interface onAmalResultListener {
        void onAmalLoaded(ArrayList<Alarm> amals);
    }
}
