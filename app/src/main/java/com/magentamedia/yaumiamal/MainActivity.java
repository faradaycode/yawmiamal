package com.magentamedia.yaumiamal;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.azan.PrayerTimes;
import com.azan.types.PrayersType;
import com.magentamedia.yaumiamal.fragments.AboutDialog;
import com.magentamedia.yaumiamal.models.JadwalSholat;
import com.magentamedia.yaumiamal.models.ToolbarTitle;
import com.magentamedia.yaumiamal.providers.YawmiMethodes;
import com.magentamedia.yaumiamal.receivers.amalDatas;
import com.magentamedia.yaumiamal.services.ServIntentYaumi;
import com.magentamedia.yaumiamal.utils.sharedPreferenceSingleton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST_LOCATION = 211;
    private static String TAG = MainActivity.class.getSimpleName() + ".";
    private static String TAG_RECEIVER = "LOADAZANDATA";
    private YawmiMethodes me;
    private boolean clickable;
    private static int LOCATION_CHANGED_FEEDBACK_CODE = 432;
    private Map<String, Date> azan;
    private static String SUBUH = "subuh";
    private static String ZUHUR = "zuhur";
    private static String ASHAR = "ashar";
    private static String MAGHRIB = "maghrib";
    private static String ISYA = "isya";
    private amalDatas amalDatas;
    private ArrayList<JadwalSholat> azanParam;
    private String azan_label = null;
    private int azan_background = -1;

    @BindView(R.id.tx_waktusholat)
    TextView tx_salatTimes;

    @BindView(R.id.tx_adzan)
    TextView tx_azan;

    @BindView(R.id.bt_amalan)
    CardView btamal;

    @OnClick(R.id.bt_profil)
    void cl_profil() {
        Intent intent = new Intent(getApplicationContext(), Profil.class);
        startActivityForResult(intent, LOCATION_CHANGED_FEEDBACK_CODE);
    }

    @BindView(R.id.bg_header)
    RelativeLayout bgheader;

    @OnClick(R.id.btn_jadwal_adzan)
    void cl_jadwal_adzan() {

        if (clickable) {
            Intent intent = new Intent(this, ListJadwalAdzan.class);
            intent.putParcelableArrayListExtra("azan_time_data", azanParam);
            intent.putExtra("next_azan", azan_label);
            intent.putExtra("azan_bg", azan_background);

            startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Jadwal Belum Bisa Dilihat Karena Lokasi Belum Di atur")
                    .setCancelable(false).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @OnClick(R.id.bt_amalan)
    void cl_amalan() {
        changePage(MyListAmal.class);
    }

    @OnClick(R.id.bt_artikel)
    void cl_artikel(View view) {
        changePage(Artikel.class);
    }

    @OnClick(R.id.btn_about_me)
    void cl_about() {
        showAbout();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startPermissions();

        me = new YawmiMethodes(); //apply providers
        ButterKnife.bind(this); //apply butterknife

        ToolbarTitle toolbarTitle = new ToolbarTitle();
        toolbarTitle.setToolbarTitle(getTitle().toString());
        TextView titles = findViewById(R.id.toolbar_title);
        titles.setText(toolbarTitle.getToolbarTitle());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppStarted();

        Log.v("MyApp", "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();

        nearestAdzan();

        Log.v("MyApp", "onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.v("MyApp", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.v("MyApp", "onPause");
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(amalDatas);

        Log.v("MyApp", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.v("MyApp", "onDestroy");

    }

    //request permission for location (needed for api > 25)
    private void startPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    private void AppStarted() {

        //first initial check
        if (sharedPreferenceSingleton.getInstance(MainActivity.this)
                .getBoolean(sharedPreferenceSingleton.Key.FIRST_INIT_KEY, true)) {

            String waktu_salat = "--:--";
            tx_salatTimes.setText(waktu_salat);
            String nama_salat = "-----";
            tx_azan.setText(me.capitalizem(nama_salat));
            clickable = false;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Lokasi Anda Belum Di atur, Klik Lanjut Untuk Beralih Ke Pengaturan Lokasi")
                    .setCancelable(false)
                    .setPositiveButton("Lanjut", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getApplicationContext(), Profil.class);
                            startActivityForResult(intent, LOCATION_CHANGED_FEEDBACK_CODE);
                        }
                    });
            AlertDialog alert = builder.create();

            alert.show();
        } else {

            clickable = true;
        }
    }

    private void nearestAdzan() {

        Date current = new Date();
        Date nextAzan = new Date();

        Calendar twelve_midnight = Calendar.getInstance();
        twelve_midnight.set(Calendar.HOUR_OF_DAY, 23);
        twelve_midnight.set(Calendar.MINUTE, 59);
        twelve_midnight.set(Calendar.SECOND, 0);
        twelve_midnight.set(Calendar.MILLISECOND, 0);

        double latitude = sharedPreferenceSingleton.getInstance(MainActivity.this)
                .getDouble(sharedPreferenceSingleton.Key.USER_LATITUDE_KEY, -1);
        double longtitude = sharedPreferenceSingleton.getInstance(MainActivity.this)
                .getDouble(sharedPreferenceSingleton.Key.USER_LONGTITUDE_KEY, 1);
        double altitude = 0;
        double timezone = 0;

        PrayerTimes prayerTimes = me.getAzan(latitude, longtitude, altitude, timezone);

        Date time_fajr = prayerTimes.getPrayTime(PrayersType.FAJR);
        Date time_zuhr = prayerTimes.getPrayTime(PrayersType.ZUHR);
        Date time_asr = prayerTimes.getPrayTime(PrayersType.ASR);
        Date time_maghrib = prayerTimes.getPrayTime(PrayersType.MAGHRIB);
        Date time_isha = prayerTimes.getPrayTime(PrayersType.ISHA);

        //add data to arraylist for parcelable to destination activity
        azanParam = new ArrayList<>();

        azanParam.add(new JadwalSholat(
                "subuh",
                prayerTimes.getPrayTime(PrayersType.FAJR),
                sharedPreferenceSingleton.getInstance(MainActivity.this)
                        .getBoolean(sharedPreferenceSingleton.Key.IS_FAJR_ACTIVE)));

        azanParam.add(new JadwalSholat("zuhur",
                prayerTimes.getPrayTime(PrayersType.ZUHR),
                sharedPreferenceSingleton.getInstance(MainActivity.this)
                        .getBoolean(sharedPreferenceSingleton.Key.IS_ZUHR_ACTIVE)));

        azanParam.add(new JadwalSholat("ashar",
                prayerTimes.getPrayTime(PrayersType.ASR),
                sharedPreferenceSingleton.getInstance(MainActivity.this)
                        .getBoolean(sharedPreferenceSingleton.Key.IS_ASR_ACTIVE)));

        azanParam.add(new JadwalSholat("maghrib",
                prayerTimes.getPrayTime(PrayersType.MAGHRIB),
                sharedPreferenceSingleton.getInstance(MainActivity.this)
                        .getBoolean(sharedPreferenceSingleton.Key.IS_MAGHRIB_ACTIVE)));

        azanParam.add(new JadwalSholat("isya",
                prayerTimes.getPrayTime(PrayersType.ISHA),
                sharedPreferenceSingleton.getInstance(MainActivity.this)
                        .getBoolean(sharedPreferenceSingleton.Key.IS_ISHA_ACTIVE)));

        //check for nearest azan time
        if (current.getTime() > time_fajr.getTime() && current.getTime() <= time_zuhr.getTime()) {
            azan_label = ZUHUR;
            nextAzan = time_zuhr;
            azan_background = R.drawable.zuhur;
        }

        if (current.getTime() > time_zuhr.getTime() && current.getTime() <= time_asr.getTime()) {
            azan_label = ASHAR;
            nextAzan = time_asr;
            azan_background = R.drawable.ashar;
        }

        if (current.getTime() > time_asr.getTime() && current.getTime() <= time_maghrib.getTime()) {

            azan_label = MAGHRIB;
            nextAzan = time_maghrib;
            azan_background = R.drawable.maghrib;
        }

        if (current.getTime() > time_maghrib.getTime() && current.getTime() <= time_isha.getTime()) {

            azan_label = ISYA;
            nextAzan = time_isha;
            azan_background = R.drawable.isya;
        }

        if (current.getTime() > time_isha.getTime()) {

            if (current.getTime() > twelve_midnight.getTimeInMillis()) {
                if (current.getTime() < time_fajr.getTime()) {
                    azan_label = SUBUH;
                    nextAzan = time_fajr;
                    azan_background = R.drawable.subuh;
                }
            }
        }

        tx_salatTimes.setText(me.toTimes(nextAzan));
        tx_azan.setText(me.capitalizem(azan_label));
        bgheader.setBackground(ContextCompat.getDrawable(this, azan_background));

    }

    private void changePage(Class classname) {
        Intent intent = new Intent(this, classname);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        new AlertDialog.Builder(this).setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("No", null).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOCATION_CHANGED_FEEDBACK_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                clickable = true;
                Intent intent = new Intent(TAG_RECEIVER);
                LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent);
            }
        }
    }

    private void showAbout() {
        DialogFragment df = new AboutDialog();

        df.show(getSupportFragmentManager(), TAG + ".ABOUT_DIALOG");
    }

}
