package com.magentamedia.yaumiamal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magentamedia.yaumiamal.fragments.ListAdzanRecyclerFragment;
import com.magentamedia.yaumiamal.models.JadwalSholat;
import com.magentamedia.yaumiamal.models.ToolbarTitle;
import com.magentamedia.yaumiamal.providers.YawmiMethodes;
import com.magentamedia.yaumiamal.utils.sharedPreferenceSingleton;

import net.time4j.SystemClock;
import net.time4j.android.ApplicationStarter;
import net.time4j.calendar.HijriCalendar;
import net.time4j.format.expert.ChronoFormatter;
import net.time4j.format.expert.PatternType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListJadwalAdzan extends AppCompatActivity {

    YawmiMethodes me;

    @BindView(R.id.tx_date_mix)
    TextView tx_hjr;

    @BindView(R.id.lj_wp)
    RelativeLayout wallpaper_header;

    private String[] day_name = new String[]{"minggu", "senin", "selasa", "rabu", "kamis", "jumat",
    "sabtu"};

    private String[] month_name = {"januari", "februari", "maret", "april", "mei", "juni", "juli",
    "agustus", "september", "oktober", "november", "desember"};

    private ArrayList<JadwalSholat> jadwalSholats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationStarter.initialize(this, true);
        setContentView(R.layout.activity_list_jadwal_adzan);

        ButterKnife.bind(this);

        me = new YawmiMethodes();

        Intent intent = this.getIntent();
        Calendar current = Calendar.getInstance();
        String day = me.capitalizem(day_name[current.get(Calendar.DAY_OF_WEEK) - 1]).toString();
        String dates = me.capitalizem(current.get(Calendar.DATE) + " " +
                month_name[current.get(Calendar.MONTH)] + " " + current.get(Calendar.YEAR)).toString();
        String next_azan = intent.getStringExtra("next_azan");
        int drawable_asset = intent.getIntExtra("azan_bg", -1);

        jadwalSholats = intent.getParcelableArrayListExtra("azan_time_data");

        ToolbarTitle toolbarTitle = new ToolbarTitle();
        TextView Titles = findViewById(R.id.toolbar_title);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarTitle.setToolbarTitle("Jadwal Salat");
        toolbar.setNavigationIcon(R.drawable.ic_back_ios);
        Titles.setText(toolbarTitle.getToolbarTitle());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.adzan_list_parent);

        tx_hjr.setText(day + ", " + dates + " / " + getHijri());
        wallpaper_header.setBackground(ContextCompat.getDrawable(ListJadwalAdzan.this,
                drawable_asset));

        Bundle passing = new Bundle();

        if(fragment == null) {
            fragment = new ListAdzanRecyclerFragment();
            passing.putParcelableArrayList("fragment_data", jadwalSholats);
            passing.putString("next_azan", next_azan);
            fragment.setArguments(passing);

            fragmentManager.beginTransaction().add(R.id.adzan_list_parent, fragment).commit();
        }

    }

    private String getHijri() {

        ChronoFormatter<HijriCalendar> hijriFormat =
                ChronoFormatter.setUp(HijriCalendar.family(), Locale.ENGLISH)
                        .addPattern("d MMMM yyyy", PatternType.CLDR)
                        .build()
                        .withCalendarVariant(HijriCalendar.VARIANT_UMALQURA);

        HijriCalendar today =
                SystemClock.inLocalView().today().transform(
                        HijriCalendar.class,
                        HijriCalendar.VARIANT_UMALQURA
                );

        return hijriFormat.format(today);
    }
}
