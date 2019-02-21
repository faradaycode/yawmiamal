package com.magentamedia.yaumiamal;

import android.app.Application;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.FirebaseApp;
import com.magentamedia.yaumiamal.receivers.AlarmsReceiver;
import com.magentamedia.yaumiamal.receivers.YaumiReceivers;
import com.magentamedia.yaumiamal.services.ServIntentYaumi;
import com.magentamedia.yaumiamal.utils.sharedPreferenceSingleton;

public class AppYawmi extends Application {

    private static String TAG = AppYawmi.class.getSimpleName();
    private static String FIRST_INIT = TAG + ".FIRST_INIT";
    private YaumiReceivers yaumiReceivers;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(getApplicationContext());
        sharedPreferenceSingleton prefton = sharedPreferenceSingleton.getInstance(AppYawmi.this);
        yaumiReceivers = new YaumiReceivers();

        //if not first install, start service
        if (!prefton.getBoolean(sharedPreferenceSingleton.Key.FIRST_INIT_KEY, true)) {

            IntentFilter filter = new IntentFilter(ServIntentYaumi.ACTION_COMPLETE);
            LocalBroadcastManager.getInstance(this).registerReceiver(yaumiReceivers, filter);
            ServIntentYaumi.LauncherService(AppYawmi.this);

        } else {
            prefton.put(sharedPreferenceSingleton.Key.FIRST_INIT_KEY, true);
        }
    }
}
