package com.magentamedia.yaumiamal.receivers;

import android.app.AlarmManager;
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
import com.magentamedia.yaumiamal.models.Alarm;
import com.magentamedia.yaumiamal.services.ServIntentYaumi;

import java.util.ArrayList;

public class AlarmsReceiver extends BroadcastReceiver {

    private static String TAG = AlarmsReceiver.class.getSimpleName();
    private static String ALARMS_EXTRA = "alarms_extra";

    @Override
    public void onReceive(Context context, Intent intent) {

        ArrayList<Alarm> alarm = intent.getParcelableArrayListExtra(ServIntentYaumi.EXTRAS_DATA);

        String label = intent.getStringExtra("TITLE");
        String teks = intent.getStringExtra("CONTENT");
        int a_id = intent.getIntExtra("A_ID", -1);


        Intent iin = new Intent(context, MainActivity.class);

        context.startActivity(iin);

        setMyAlarm(context, alarm);

        Toast.makeText(context, label, Toast.LENGTH_LONG).show();

        Log.wtf(TAG, alarm.toString());

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

    public static void setMyAlarm(Context context, ArrayList<Alarm> alarms) {

        if (alarms.size() > 0) {

            AlarmManager mgrAlarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            //set alarm each data in arraylist
            for (int i = 0; i < alarms.size(); i++) {
                Intent alarmintent = new Intent(context, AlarmsReceiver.class);
                alarmintent.putExtra("TITLE", alarms.get(i).getAlarmLabel());
                alarmintent.putExtra("CONTENT", alarms.get(i).getAlarmText());
                alarmintent.putExtra("A_ID", i);

                PendingIntent pendingIntent = PendingIntent
                        .getBroadcast(context, i, alarmintent, 0);

                assert mgrAlarm != null;

                if (System.currentTimeMillis() < alarms.get(i).getTime()) {
                    mgrAlarm.setExact(AlarmManager.RTC_WAKEUP, alarms.get(i).getTime(), pendingIntent);
                }
            }

            Log.wtf(TAG, alarms.toString());
        }
    }

    public static void cancelAlarm(Context context, ArrayList<Alarm> alarms) {

        Intent alarmintent = new Intent(context, AlarmsReceiver.class);

        for (int i = 0; i < alarms.size(); i++) {
            PendingIntent pendingIntent = PendingIntent
                    .getBroadcast(context, i, alarmintent, 0);

            AlarmManager mgrAlarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            assert mgrAlarm != null;
            mgrAlarm.cancel(pendingIntent);
        }
    }
}
