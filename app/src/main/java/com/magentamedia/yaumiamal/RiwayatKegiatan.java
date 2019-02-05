package com.magentamedia.yaumiamal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.magentamedia.yaumiamal.fragments.RiwayatAmalRecyclerFragment;
import com.magentamedia.yaumiamal.providers.YawmiMethodes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RiwayatKegiatan extends AppCompatActivity {

    @BindView(R.id.compactcalendar_view)
    CompactCalendarView ccv;

    @BindView(R.id.calen_date)
    TextView calendate;

    private YawmiMethodes me;

    Bundle bundle;

    private String[] month_name = {"januari", "februari", "maret", "april", "mei", "juni", "juli",
            "agustus", "september", "oktober", "november", "desember"};

    FragmentManager fragmentManager;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_kegiatan);
        ButterKnife.bind(this);
        me = new YawmiMethodes();

        Toolbar toolbar = findViewById(R.id.toolbarDay);
        toolbar.setNavigationIcon(R.drawable.ic_back_ios);
        toolbar.setContentInsetStartWithNavigation(0);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        calendate.setText(cCustomFormat(System.currentTimeMillis()));

        ccv.setDayColumnNames(new String[]{"Sen","Sel","Rab","Kam","Jum","Sab","Min"});
        ccv.setCurrentDate(new Date(System.currentTimeMillis()));

        fragmentManager = getSupportFragmentManager();
        fragment = fragmentManager.findFragmentById(R.id.clistamal_container);

        if (fragment == null) {
            fragment = new RiwayatAmalRecyclerFragment();
            fragmentManager.beginTransaction().add(R.id.riwayat_recycler_parent, fragment).commit();
        }

        ccv.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

                long count = me.countSpecificData(getApplicationContext(), "tb_passed_amal",
                        "date_passed = ?", new String[]{me.toDBDate(dateClicked.getTime())});

                if (count > 0) {
                    bundle = new Bundle();
                    fragment = new RiwayatAmalRecyclerFragment();
                    bundle.putString("selectedate", me.toDBDate(dateClicked.getTime()));
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.riwayat_recycler_parent, fragment)
                            .commitAllowingStateLoss();
                } else {
                    fragmentManager.beginTransaction().remove(fragment).commit();
                }

                Log.wtf("count", ""+me.toDBDate(dateClicked.getTime()));
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

                calendate.setText(cCustomFormat(firstDayOfNewMonth.getTime()));
                long scroll_val;

                if (!isOnSameMonth(System.currentTimeMillis(), firstDayOfNewMonth.getTime())) {
                    ccv.setCurrentDate(firstDayOfNewMonth);
                    scroll_val = firstDayOfNewMonth.getTime();
                } else {
                    ccv.setCurrentDate(new Date(System.currentTimeMillis()));
                    scroll_val = System.currentTimeMillis();
                }

                long count = me.countSpecificData(getApplicationContext(), "tb_passed_amal",
                        "date_passed = ?", new String[]{me.toDBDate(scroll_val)});

                if (count > 0) {

                    bundle = new Bundle();
                    fragment = new RiwayatAmalRecyclerFragment();
                    bundle.putString("selectedate", me.toDBDate(scroll_val));
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.riwayat_recycler_parent, fragment)
                            .commitAllowingStateLoss();
                } else {
                    fragmentManager.beginTransaction().remove(fragment).commit();
                }

            }
        });
    }

    private String cCustomFormat(long milis) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milis);
        String text = month_name[cal.get(Calendar.MONTH)] + " " +
                String.valueOf(cal.get(Calendar.YEAR));

        return me.capitalizem(text).toString();
    }

    private boolean isOnSameMonth(long now_in_milis, long then_in_milis) {
        Calendar now = Calendar.getInstance();
        Calendar then = Calendar.getInstance();

        now.setTimeInMillis(now_in_milis);
        then.setTimeInMillis(then_in_milis);

        return now.get(Calendar.MONTH) == then.get(Calendar.MONTH);
    }

}
