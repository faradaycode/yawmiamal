package com.magentamedia.yaumiamal;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.magentamedia.yaumiamal.fragments.TargetDialog;
import com.magentamedia.yaumiamal.fragments.TimePickerDialog;
import com.magentamedia.yaumiamal.fragments.WeekdayDialog;
import com.magentamedia.yaumiamal.models.ToolbarTitle;
import com.magentamedia.yaumiamal.models.amalParcel;
import com.magentamedia.yaumiamal.models.dayParcel;
import com.magentamedia.yaumiamal.providers.YawmiMethodes;
import com.magentamedia.yaumiamal.services.ServIntentYaumi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class AturAmal extends AppCompatActivity implements WeekdayDialog.OnCompleteListener,
TargetDialog.OnCompleteListener {

    private String kegiatan = "";
    private String periodenya = "setiap hari";
    private String targetnya = "sudah";
    private String waktunya = "08:00";
    private static String[] surah = new String[]{"Surat","Juz","Ayat","Kali"};
    private static String[] buku = new String[]{"Halaman","Buku","Kali"};
    private static String[] murojaah = new String[]{"Surat","Juz","Ayat","Halaman","Buku","Kali"};
    private static String[] shalat = new String[]{"Rakaat"};
    private String[] lainnya;
    private static String[] dayOfWeek = new String[]{"minggu", "senin", "selasa", "rabu", "kamis",
            "jumat", "sabtu"};
    private boolean status_edited;
    private int id_listAmal;
    private int idx;
    private int group = 4;
    private YawmiMethodes me;
    private boolean swState;
    private int SUN = 0;
    private int MON = 0;
    private int TUE = 0;
    private int WED = 0;
    private int THU = 0;
    private int FRI = 0;
    private int SAT = 0;
    private ContentValues updateSet;
    private ContentValues insertSet;
    private static String TB_AMAL = "tb_amalanku";
    private List<String> kustomHari;

    @BindView(R.id.reset_my_target)
    ImageView bcTarget;

    @BindView(R.id.reset_my_period)
    ImageView bcPeriod;

    @BindView(R.id.reset_notify_time_text)
    ImageView bcWaktu;

    @BindView(R.id.sw_notif)
    Switch aSwitch;

    @OnCheckedChanged(R.id.sw_notif)
    void switchNotif(CompoundButton button, boolean checked) {

        swState = checked;

        if(!checked) {
            txTimeNotif.setTextColor(Color.GRAY);
        } else {
            txTimeNotif.setTextColor(getResources().getColor(R.color.matcha_latte));
        }
    }

    @BindView(R.id.tx_nama_kegiatan)
    TextView txnkegiatan;

    @BindView(R.id.tx_notify_time)
    TextView txTimeNotif;

    @OnClick(R.id.tx_notify_time)
    void setNotify() {
        if (swState) {
            showTimePickerDialog();
            bcWaktu.setVisibility(View.VISIBLE);
        }
    }

    @BindView(R.id.tx_periode)
    TextView txperiode;

    @OnClick(R.id.tx_periode)
    void setMyPeriod() {
        showDayPicker();
    }

    @BindView(R.id.tx_list_hari)
    TextView txlisthari;

    @BindView(R.id.tx_mytarget)
    TextView txTarget;

    @OnClick(R.id.tx_mytarget)
    void setMyTarget() {
        showTargetDialog();
    }

    @OnClick(R.id.simpan_atur_amal)
    void saveMyAmal() {
        saveAllSetting();
    }

    @BindView(R.id.in_nama_kegiatan)
    EditText in_namakegiatan;

    @OnClick(R.id.reset_my_target)
    void reset_target() {
        resetAll(txTarget, targetnya);
        bcTarget.setVisibility(View.GONE);
    }

    @OnClick(R.id.reset_my_period)
    void reset_period() {
        resetAll(txperiode, periodenya);
        bcPeriod.setVisibility(View.GONE);
        txlisthari.setText("");
        txlisthari.setVisibility(View.GONE);
    }

    @OnClick(R.id.reset_notify_time_text)
    void resetMyTime() {
        resetAll(txTimeNotif, waktunya);
        bcWaktu.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atur_amal);

        ButterKnife.bind(this); //apply butterknife
        me = new YawmiMethodes();
        updateSet = new ContentValues();
        insertSet = new ContentValues();
        ToolbarTitle toolbarTitle = new ToolbarTitle();

        TextView Titles = findViewById(R.id.toolbar_title);

        in_namakegiatan.setVisibility(View.GONE);
        txnkegiatan.setVisibility(View.GONE);

        txlisthari.setVisibility(View.GONE);
        aSwitch.setChecked(false);
        txTimeNotif.setTextColor(Color.GRAY);

        bcPeriod.setVisibility(View.GONE);
        bcTarget.setVisibility(View.GONE);
        bcWaktu.setVisibility(View.GONE);

        //toolbar setting
        toolbarTitle.setToolbarTitle("Atur Kegiatan");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_ios);
        Titles.setText(toolbarTitle.getToolbarTitle());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //set text
        txTimeNotif.setText(waktunya);
        txperiode.setText(me.capitalizem(periodenya));
        txTarget.setText(me.capitalizem(targetnya));

        Intent intent = getIntent();

        if(intent != null) {

            //check if user edit amal or insert new amal
            if (intent.hasExtra("edited")) {

                txnkegiatan.setVisibility(View.VISIBLE);
                status_edited = intent.getBooleanExtra("edited", false);
                amalParcel parcela = intent.getExtras().getParcelable("PARCEL");

                if (status_edited) {
                    kustomHari = new ArrayList<>();

                    assert parcela != null;

                    if (parcela.getSUN() == 1) {
                        kustomHari.add(dayOfWeek[0]);
                        SUN = 1;
                    }

                    if (parcela.getMON() == 1) {
                        kustomHari.add(dayOfWeek[1]);
                        MON = 1;
                    }

                    if (parcela.getTUE() == 1) {
                        kustomHari.add(dayOfWeek[2]);
                        TUE = 1;
                    }

                    if (parcela.getWED() == 1) {
                        kustomHari.add(dayOfWeek[3]);
                        WED = 1;
                    }

                    if (parcela.getTHU() == 1) {
                        kustomHari.add(dayOfWeek[4]);
                        THU = 1;
                    }

                    if (parcela.getFRI() == 1) {
                        kustomHari.add(dayOfWeek[5]);
                        FRI = 1;
                    }

                    if (parcela.getSAT() == 1) {
                        kustomHari.add(dayOfWeek[6]);
                        SAT = 1;
                    }

                    txnkegiatan.setText(me.capitalizem(parcela.getAmalan()));
                    targetnya = (parcela.getmTarget().equals("sudah") ? "sudah" : parcela
                            .getmTarget());
                    waktunya = parcela.getNotif_time();
                    periodenya = (kustomHari.size() > 6) ? "setiap hari" : "saat hari";
                    txlisthari.setVisibility((kustomHari.size() > 0 && kustomHari.size() <= 6) ?
                    View.VISIBLE : View.GONE);
                    txlisthari.setText((kustomHari.size() < 7) ? me.capitalizem(TextUtils
                            .join(", ", kustomHari)) : "");
                    aSwitch.setChecked(parcela.getNotif_enabled() == 1);
                    id_listAmal = parcela.getId_list_amal();
                    group = parcela.getaGroup();

                    Log.wtf("parcel", parcela.toString());
                } else {

                    kegiatan = intent.getStringExtra("amalan");
                    id_listAmal = intent.getIntExtra("ida", -1);
                    idx = intent.getIntExtra("index", -1);
                    group = intent.getIntExtra("group", -1);

                    txnkegiatan.setText(kegiatan);
                }
            } else {
                id_listAmal = -1;
                in_namakegiatan.setVisibility(View.VISIBLE);
            }
        }

        //merging two array
        List<String> temp = new ArrayList<>();
        Collections.addAll(temp, murojaah);
        Collections.addAll(temp, shalat);

        lainnya = temp.toArray(new String[temp.size()]);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerDialog();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void showDayPicker() {
        DialogFragment df = new WeekdayDialog();
        String word_boundary = ".*?\\b(setiap)\\b\\s.*?";

        //passing
        Bundle param = new Bundle();
        dayParcel dParcel = new dayParcel(SUN, MON, TUE, WED, THU, FRI, SAT);

        if (txperiode.getText().toString().toLowerCase().matches(word_boundary)) {
            param.putInt("daily_status", 1);
            param.putParcelable("hariParcel", dParcel);
        } else {
            param.putInt("daily_status", 0);
            param.putParcelable("hariParcel", dParcel);
        }

        df.setArguments(param);
        df.show(getSupportFragmentManager(), "dayPicker");
    }

    private void showTargetDialog() {
        DialogFragment df = new TargetDialog();

        //passing
        Bundle param = new Bundle();

        if (group == 1) {
            param.putStringArray("target", shalat);
        }

        if (group == 2) {
            param.putStringArray("target", surah);
        }

        if (group == 3) {
            param.putStringArray("target", buku);
        }

        if (group == 4) {
            param.putStringArray("target", lainnya);
        }

        if (group == 5) {
            param.putStringArray("target", murojaah);
        }

        df.setArguments(param);
        df.show(getSupportFragmentManager(), "targetPicker");
    }

    private void saveAllSetting() {

        String notify_time; // = txTimeNotif.getText().toString();
        String theTarget = txTarget.getText().toString();
        int alarm_on;
        int currentDayWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        if (swState) {
            notify_time = txTimeNotif.getText().toString();
            alarm_on = 1;
        } else {
            notify_time = waktunya;
            alarm_on = 0;
        }

        if (status_edited) {
            //todo update
            updateSet.put("id_la", id_listAmal);
            updateSet.put("my_target", theTarget.toLowerCase());
            updateSet.put("notification_time", notify_time);
            updateSet.put("notification_enabled", alarm_on);

            String whereis = "id_la = ?";

            long updateStatus = me.cvUpdate(AturAmal.this, TB_AMAL, updateSet, whereis,
                    new String[]{String.valueOf(id_listAmal)});

            if (updateStatus != -1) {

                String searched = "col_mon, col_tue, col_wed, col_thu, col_fri, col_sat, col_sun";

                Cursor cursor = me.getSpecificData(AturAmal.this, TB_AMAL,
                        searched, "id_la = " + id_listAmal);

                if (cursor.getCount() > 0) {

                    boolean delete_this = false;

                    String where = "id_la = " + id_listAmal + " AND status_passed = 0";

                    if (currentDayWeek == 2) {
                        if (cursor.getInt(0) != 1) {
                            where += " AND date_passed = (date('now','localtime'))";
                            delete_this = true;
                        }
                    }

                    if (currentDayWeek == 3) {
                        if (cursor.getInt(1) != 1) {
                            where += " AND date_passed = (date('now','localtime'))";
                            delete_this = true;
                        }
                    }

                    if (currentDayWeek == 4) {
                        if (cursor.getInt(2) != 1) {
                            where += " AND date_passed = (date('now','localtime'))";
                            delete_this = true;
                        }
                    }

                    if (currentDayWeek == 5) {
                        if (cursor.getInt(3) != 1) {
                            where += " AND date_passed = (date('now','localtime'))";
                            delete_this = true;
                        }
                    }

                    if (currentDayWeek == 6) {
                        if (cursor.getInt(4) != 1) {
                            where += " AND date_passed = (date('now','localtime'))";
                            delete_this = true;
                        }
                    }

                    if (currentDayWeek == 7) {
                        if (cursor.getInt(5) != 1) {
                            where += " AND date_passed = (date('now','localtime'))";
                            delete_this = true;
                        }
                    }

                    if (currentDayWeek == 1) {
                        if (cursor.getInt(6) != 1) {
                            where += " AND date_passed = (date('now','localtime'))";
                            delete_this = true;
                        }
                    }

                    String sql = "DELETE FROM tb_passed_amal WHERE " + where;

                    if (delete_this) {
                        int del_status = me.SQLWriteMode(AturAmal.this, sql);

                        Log.wtf("passed_deleted", "" + del_status);
                    }
                }

                successUpdate();
            }
        } else {
            //todo insert

            //for custom amal
            if (id_listAmal < 0) {

                String custom_amal = in_namakegiatan.getText().toString().toLowerCase();

                boolean la_exist = me.checkExist(this, "tb_list_amalan",
                        "amalan = '" + custom_amal + "'");

                if (la_exist) {

                    me.onToast(this, me.capitalizem(custom_amal + "sudah ada " +
                            "dalam daftar kegiatanmu").toString());

                } else {
                    //if amalan not exist in list (user create custom amal) do this

                    id_listAmal = ((int) me.countingRow(this, "tb_list_amalan",
                            null) + 1);
                    ContentValues contentValues = new ContentValues();

                    contentValues.put("amalan", custom_amal);
                    contentValues.put("tipe", "c");
                    contentValues.put("group_amal", 0);

                    long insertCustom = me.cvInsert(AturAmal.this, "tb_list_amalan",
                            contentValues);

                    //after inserting new custom, insert to tb_amalanku
                    if (insertCustom != -1) {
                        insertSet.put("id_la", id_listAmal);
                        insertSet.put("my_target", theTarget.toLowerCase());
                        insertSet.put("notification_time", notify_time);
                        insertSet.put("notification_enabled", alarm_on);

                        long insertToAmalanku = me.cvInsert(AturAmal.this, TB_AMAL, insertSet);

                        if (insertToAmalanku != -1) {

                            String where = "id_la = " + insertToAmalanku;

                            if (currentDayWeek == 2) {
                                where = "col_mon = 1";
                            }

                            if (currentDayWeek == 3) {
                                where = "col_tue = 1";
                            }

                            if (currentDayWeek == 4) {
                                where = "col_wed = 1";
                            }

                            if (currentDayWeek == 5) {
                                where = "col_thu = 1";
                            }

                            if (currentDayWeek == 6) {
                                where = "col_fri = 1";
                            }

                            if (currentDayWeek == 7) {
                                where = "col_sat = 1";
                            }

                            if (currentDayWeek == 1) {
                                where = "col_sun = 1";
                            }

                            Cursor cursor = me.getSpecificData(AturAmal.this, TB_AMAL, "*",
                                    where);

                            if (cursor.getCount() > 0) {
                                ContentValues insertToPassedAmal = new ContentValues();

                                insertToPassedAmal.put("id_a", insertToAmalanku);
                                insertToPassedAmal.put("id_la", id_listAmal);
                                insertToPassedAmal.put("id_la", id_listAmal);

                                long intoPassed = me.cvInsert(AturAmal.this,
                                        "tb_passed_amal", insertToPassedAmal);

                                Log.wtf("into_passed", "" + intoPassed);
                            }

                            successSaving();
                        }
                    }
                }
            } else {

                //if user pick from list
                //check if data has been insert in table
                boolean chk = me.checkExist(this, "tb_amalanku", "id_la=" +
                        id_listAmal);

                if (chk) {
                    me.onToast(this, "Kegiatan Ini Sudah Kamu Pilih");

                } else {
                    insertSet.put("id_la", id_listAmal);
                    insertSet.put("my_target", theTarget);
                    insertSet.put("notification_time", notify_time);
                    insertSet.put("notification_enabled", alarm_on);

                    long insertToAmalanku = me.cvInsert(AturAmal.this, TB_AMAL, insertSet);

                    if (insertToAmalanku != -1) {

                        String where = "id_a = " + insertToAmalanku;

                        if (currentDayWeek == 2) {
                            where += " AND col_mon = 1";
                        }

                        if (currentDayWeek == 3) {
                            where += " AND col_tue = 1";
                        }

                        if (currentDayWeek == 4) {
                            where += " AND col_wed = 1";
                        }

                        if (currentDayWeek == 5) {
                            where += " AND col_thu = 1";
                        }

                        if (currentDayWeek == 6) {
                            where += " AND col_fri = 1";
                        }

                        if (currentDayWeek == 7) {
                            where += " AND col_sat = 1";
                        }

                        if (currentDayWeek == 1) {
                            where += " AND col_sun = 1";
                        }

                        Cursor cursor = me.getSpecificData(AturAmal.this, TB_AMAL, "*",
                                where);

                        if (cursor.getCount() > 0) {
                            ContentValues insertToPassedAmal = new ContentValues();

                            insertToPassedAmal.put("id_a", insertToAmalanku);
                            insertToPassedAmal.put("id_la", id_listAmal);
                            insertToPassedAmal.put("id_la", id_listAmal);

                            long intoPassed = me.cvInsert(AturAmal.this,
                                    "tb_passed_amal", insertToPassedAmal);

                            Log.wtf("into_passed", "" + intoPassed);
                        }

                        successSaving();
                    }
                }
            }
        }
    }

    private void successSaving() {

        final Intent newPage = new Intent(this, MyListAmal.class);

        me.onToast(this, "Berhasil Menambah Kegiatan");

        newPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        ServIntentYaumi.LauncherService(AturAmal.this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(newPage);
            }
        }, Toast.LENGTH_SHORT);
    }

    private void successUpdate() {

        final Intent newPage = new Intent(this, MyListAmal.class);

        me.onToast(this, "Update Berhasil");

        newPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        ServIntentYaumi.LauncherService(AturAmal.this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(newPage);
            }
        }, Toast.LENGTH_SHORT);

    }

    private void resetAll(TextView components, String word) {
        me.setTextview(components, word);
    }

    //event after daypicker dismiss()
    @Override
    public void onCompletePickWeekday(Map<String,Integer> map) {

        if (!status_edited) {
            insertSet.put("col_mon", map.get("mon"));
            insertSet.put("col_tue", map.get("tue"));
            insertSet.put("col_wed", map.get("wed"));
            insertSet.put("col_thu", map.get("thu"));
            insertSet.put("col_fri", map.get("fri"));
            insertSet.put("col_sat", map.get("sat"));
            insertSet.put("col_sun", map.get("sun"));
        } else {
            updateSet.put("col_mon", map.get("mon"));
            updateSet.put("col_tue", map.get("tue"));
            updateSet.put("col_wed", map.get("wed"));
            updateSet.put("col_thu", map.get("thu"));
            updateSet.put("col_fri", map.get("fri"));
            updateSet.put("col_sat", map.get("sat"));
            updateSet.put("col_sun", map.get("sun"));
        }

        kustomHari = new ArrayList<>();

        if (map.get("mon") == 1) {
            kustomHari.add(dayOfWeek[1]);
        }

        if (map.get("tue") == 1) {
            kustomHari.add(dayOfWeek[2]);
        }

        if (map.get("wed") == 1) {
            kustomHari.add(dayOfWeek[3]);
        }

        if (map.get("thu") == 1) {
            kustomHari.add(dayOfWeek[4]);
        }

        if (map.get("fri") == 1) {
            kustomHari.add(dayOfWeek[5]);
        }

        if (map.get("sat") == 1) {
            kustomHari.add(dayOfWeek[6]);
        }

        if (map.get("sun") == 1) {
            kustomHari.add(dayOfWeek[0]);
        }

        if (kustomHari.size() >  0) {
            txlisthari.setVisibility(View.VISIBLE);
            txlisthari.setText((kustomHari.size() < 7) ? me.capitalizem(TextUtils
                    .join(", ", kustomHari)) : "");

            bcPeriod.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCompletePickTarget(String result, boolean b) {
        txTarget.setText(me.capitalizem(result).toString());

        if (b) {
            bcTarget.setVisibility(View.VISIBLE);
        }
    }
}