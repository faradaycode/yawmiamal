package com.magentamedia.yaumiamal.providers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.magentamedia.yaumiamal.R;

public class theNotification {

    private NotificationManager nmanager;
    private NotificationCompat.Builder mNotifyBuilder;
    private YawmiMethodes me = new YawmiMethodes();

    public theNotification(Context context, int ID, String title, String message, Class classname,
                           int alarmID) {

        String NOTIFICATION_CHANNEL_ID = context.getPackageName()+ ".ADHAN";
        int reqCode = ID;

        Intent notificationIntent = new Intent(context, classname);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(),
                alarmID, notificationIntent, 0);

        Uri alarmSound = Uri.parse("android.resource://" +
                context.getPackageName() + "/" + R.raw.knock_door);

        mNotifyBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);

        mNotifyBuilder.setSmallIcon(R.drawable.ic_small_yawmi)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_yawmi))
                .setContentTitle(me.capitalizem(title).toString())
                .setContentText(me.capitalizem(message).toString())
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                .setContentIntent(pendingIntent);

        nmanager = (NotificationManager) context.getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        assert nmanager != null;
        nmanager.notify(reqCode, mNotifyBuilder.build());
    }
}
