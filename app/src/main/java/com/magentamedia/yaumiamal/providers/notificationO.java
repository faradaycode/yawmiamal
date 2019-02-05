package com.magentamedia.yaumiamal.providers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.magentamedia.yaumiamal.R;

public class notificationO {

    private NotificationManager manager;
    private NotificationCompat.Builder NotifyBuilder;
    private String NOTIFICATION_CHANNEL_ID;
    private String NOTIFICATION_CHANNEL_NAME = "Adhan Channel";
    private YawmiMethodes me = new YawmiMethodes();

    public notificationO (Context context, int ID, String title, String message, Class classname,
                          int alarmID) {

        NOTIFICATION_CHANNEL_ID = context.getPackageName()+".ADHAN";

        Intent intent = new Intent(context, classname);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, alarmID, intent, 0);

        Uri soun = Uri.parse("android.resource://" +
                context.getPackageName() + "/" + R.raw.knock_door);

        NotifyBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        NotifyBuilder.setSmallIcon(R.drawable.ic_small_yawmi)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_yawmi));
        NotifyBuilder.setContentTitle(me.capitalizem(title).toString())
                .setContentText(me.capitalizem(message).toString())
                .setAutoCancel(false)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );

            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            notificationChannel.setSound(soun, att);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.setVibrationPattern(new long[]{0, 0, 0, 0,0, 0, 0, 0, 0});
            notificationChannel.enableVibration(true);

            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            assert manager != null;
            manager.createNotificationChannel(notificationChannel);

            Log.d("URI", notificationChannel.getSound().getPath());
        }

        assert manager != null;
        manager.notify(ID, NotifyBuilder.build());
    }
}
