package com.magentamedia.yaumiamal.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.azan.PrayerTimes;
import com.azan.types.PrayersType;
import com.magentamedia.yaumiamal.models.Alarm;
import com.magentamedia.yaumiamal.models.JadwalSholat;
import com.magentamedia.yaumiamal.models.MyAmalModel;
import com.magentamedia.yaumiamal.providers.YawmiMethodes;
import com.magentamedia.yaumiamal.receivers.AlarmsReceiver;
import com.magentamedia.yaumiamal.utils.sharedPreferenceSingleton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

public class ServIntentYaumi extends IntentService {

    private YawmiMethodes me;

    public static final String TAG = ServIntentYaumi.class.getSimpleName();
    public static final String LIST_AMAL = "com.receiver.loadamal";
    public static final String EXTRAS_DATA = "amal_extras";
    public static final String FRAGMENT_DATA = "fragment_data";
    public static final String ACTION_COMPLETE = TAG + ".ACTION_COMPLETE";

    private ArrayList<Alarm> alarms;
    private ArrayList<MyAmalModel> parcelAmal;

    public ServIntentYaumi() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        alarms = new ArrayList<>();
        parcelAmal = new ArrayList<>();

        Log.wtf(TAG, "IntentService On Line");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(@NonNull Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        getAzan();
        getAlarm();

        Intent sender = new Intent(ACTION_COMPLETE);
        sender.putParcelableArrayListExtra(EXTRAS_DATA, alarms);
        sender.putParcelableArrayListExtra(FRAGMENT_DATA, parcelAmal);

        LocalBroadcastManager.getInstance(this).sendBroadcast(sender);
    }

    //static methode for calling or recall service
    public static void LauncherService(Context context) {
        final Intent launcher = new Intent(context, ServIntentYaumi.class);

        context.startService(launcher);
    }

    //get all amalan time, status and label
    public void getAlarm() {

        me = new YawmiMethodes();
        Calendar calendar = Calendar.getInstance();
        String[] col_name = new String[]{"col_sun", "col_mon", "col_tue", "col_wed", "col_thu",
                "col_fri", "col_sat"};

        String sql = "SELECT a.id_a, b.id_la, b.amalan, a.my_target, a.notification_time, a.notification_enabled " +
                "FROM tb_amalanku a JOIN tb_list_amalan b ON a.id_la = b.id_la JOIN tb_passed_amal c " +
                "ON a.id_a = c.id_a WHERE " + col_name[calendar.get(Calendar.DAY_OF_WEEK) - 1] + " = 1 " +
                "AND a.notification_enabled = 1 AND c.status_passed = 0";

        String reg = ":";
        Cursor cursor = me.joinData(ServIntentYaumi.this, sql);

        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                String[] split = splitter(cursor.getString(4), reg);

                alarms.add(new Alarm(
                        me.capitalizem(cursor.getString(2)).toString(),
                        me.capitalizem("jangan lupa " + cursor.getString(2) + " hari ini")
                        .toString(),
                        toCalendarFormat(split).getTimeInMillis()));

                parcelAmal.add(new MyAmalModel(cursor.getInt(0), cursor.getInt(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getInt(5)));

                Log.wtf(TAG + ".sqlreturn", cursor.getString(0));
            }
        }
    }

    //get azan times, save it into arraylist<model>
    public void getAzan() {

        me = new YawmiMethodes();

        double latitude = sharedPreferenceSingleton.getInstance(ServIntentYaumi.this)
                .getDouble(sharedPreferenceSingleton.Key.USER_LATITUDE_KEY, -1);
        double longtitude = sharedPreferenceSingleton.getInstance(ServIntentYaumi.this)
                .getDouble(sharedPreferenceSingleton.Key.USER_LONGTITUDE_KEY, 1);
        double altitude = 0;
        double timezone = 0;
        String SUBUH = "subuh";
        String ZUHR = "zuhur";
        String ASR = "ashar";
        String MAGHRIB = "maghrib";
        String ISYA = "isya";
        String teks = "jangan lupa salat ";
        String prefix = "azan ";

        PrayerTimes prayerTimes = me.getAzan(latitude, longtitude, altitude, timezone);

        if (sharedPreferenceSingleton.getInstance(getApplicationContext())
                .getBoolean(sharedPreferenceSingleton.Key.IS_FAJR_ACTIVE, false)) {

            alarms.add(new Alarm(me.capitalizem(prefix + SUBUH).toString(),
                    teks + SUBUH, prayerTimes
                    .getPrayTime(PrayersType.FAJR).getTime()));
        }

        if (sharedPreferenceSingleton.getInstance(getApplicationContext())
                .getBoolean(sharedPreferenceSingleton.Key.IS_ZUHR_ACTIVE, false)) {

            alarms.add(new Alarm(me.capitalizem(prefix + ZUHR).toString(),
                    teks + ZUHR, prayerTimes
                    .getPrayTime(PrayersType.ZUHR).getTime()));
        }

        if (sharedPreferenceSingleton.getInstance(getApplicationContext())
                .getBoolean(sharedPreferenceSingleton.Key.IS_ASR_ACTIVE, false)) {

            alarms.add(new Alarm(me.capitalizem(prefix + ASR).toString(),
                    teks + ASR, prayerTimes
                    .getPrayTime(PrayersType.ASR).getTime()));
        }

        if (sharedPreferenceSingleton.getInstance(getApplicationContext())
                .getBoolean(sharedPreferenceSingleton.Key.IS_MAGHRIB_ACTIVE, false)) {

            alarms.add(new Alarm(me.capitalizem(prefix + MAGHRIB).toString(),
                    teks + MAGHRIB, prayerTimes
                    .getPrayTime(PrayersType.MAGHRIB).getTime()));
        }

        if (sharedPreferenceSingleton.getInstance(getApplicationContext())
                .getBoolean(sharedPreferenceSingleton.Key.IS_ISHA_ACTIVE, false)) {

            alarms.add(new Alarm(me.capitalizem(prefix + ISYA).toString(),
                    teks + ISYA, prayerTimes
                    .getPrayTime(PrayersType.ISHA).getTime()));
        }

    }

    private String[] splitter(String word, String regex) {

        String[] result = word.split(regex);

        return result;
    }

    private Calendar toCalendarFormat(String[] clock) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(clock[0].replaceAll("\\s", "")));
        calendar.set(Calendar.MINUTE, Integer.parseInt(clock[1].replaceAll("\\s", "")));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }
}
