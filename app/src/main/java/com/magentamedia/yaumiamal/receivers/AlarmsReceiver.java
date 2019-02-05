package com.magentamedia.yaumiamal.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.magentamedia.yaumiamal.MainActivity;
import com.magentamedia.yaumiamal.R;

public class AlarmsReceiver extends BroadcastReceiver {

    private static int REQUEST_ID = 880;
    private static String TAG = AlarmsReceiver.class.getSimpleName();
    private static String ALARMS_EXTRA = "alarms_extra";

    @Override
    public void onReceive(Context context, Intent intent) {

//        ArrayList<Alarm> alarm = intent.getParcelableArrayListExtra(ServIntentYaumi.ALARMS_EXTRA);

        String label = intent.getStringExtra("TITLE");
        String teks = intent.getStringExtra("CONTENT");
        int a_id = intent.getIntExtra("A_ID", REQUEST_ID);

        Log.wtf(TAG, label);

        Intent iin = new Intent(context, MainActivity.class);

        context.startActivity(iin);

        Toast.makeText(context, label, Toast.LENGTH_LONG);

    }

    private void notification(Context context, int alarmID, String title, String content) {

        String NOTIFICATION_CHANNEL_ID = context.getPackageName()+ ".AZAN";

        Intent notificationIntent = new Intent(context, MainActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(),
                alarmID, notificationIntent, 0);

        Uri alarmSound = Uri.parse("android.resource://" +
                context.getPackageName() + "/" + R.raw.knock_door);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(context,
                NOTIFICATION_CHANNEL_ID);

        mNotifyBuilder.setSmallIcon(R.drawable.ic_small_yawmi)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_yawmi))
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                .setContentIntent(pendingIntent);

        NotificationManager nmanager = (NotificationManager) context.getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        assert nmanager != null;
        nmanager.notify(alarmID, mNotifyBuilder.build());
    }
}
