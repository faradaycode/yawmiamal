package com.magentamedia.yaumiamal.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.magentamedia.yaumiamal.models.MyAmalModel;
import com.magentamedia.yaumiamal.services.ServIntentYaumi;

import java.util.ArrayList;

public class amalDatas extends BroadcastReceiver {

    private onAmalResultListener aListener;
    private static String TAG = amalDatas.class.getSimpleName();

    @SuppressWarnings("unused")
    public amalDatas() {}

    public amalDatas(onAmalResultListener listener) {
        aListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        ArrayList<MyAmalModel> amalLists = intent
                .getParcelableArrayListExtra(ServIntentYaumi.EXTRAS_DATA);

        Log.wtf(TAG, "CALLED FROM SERVICE");

        aListener.onAmalLoaded(amalLists);
    }

    public interface onAmalResultListener {
        void onAmalLoaded(ArrayList<MyAmalModel> amals);
    }
}
