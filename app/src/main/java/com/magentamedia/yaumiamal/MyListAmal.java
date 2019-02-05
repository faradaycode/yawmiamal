package com.magentamedia.yaumiamal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.magentamedia.yaumiamal.fragments.MyAmalRecyclerFragment;
import com.magentamedia.yaumiamal.models.ToolbarTitle;
import com.magentamedia.yaumiamal.providers.YawmiMethodes;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyListAmal extends AppCompatActivity {

    YawmiMethodes me;

    @BindView(R.id.textMeter)
    TextView meteran;

    @BindView(R.id.progressBar2)
    ProgressBar pb;

    @OnClick(R.id.m_add_kegiatan)
    void addKegiatan() {
        changePage(PickAmal.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list_amal);
        ButterKnife.bind(this);
        ToolbarTitle toolbarTitle = new ToolbarTitle();

        TextView Titles = findViewById(R.id.toolbar_title);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarTitle.setToolbarTitle("Kegiatan Saya");
        toolbar.setNavigationIcon(R.drawable.ic_back_ios);
        Titles.setText(toolbarTitle.getToolbarTitle());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

//        TextView tx_noamal = findViewById(R.id.tx_nolistamal);

        me = new YawmiMethodes();

        pb.setProgress((int) Math.round(intData()));
        meteran.setText(String.valueOf(Math.round(intData())+"%"));

        Log.d("intData()", ""+intData());
    }

    @Override
    protected void onStart() {
        super.onStart();

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.clistamal_container);

        if (fragment == null) {
            fragment = new MyAmalRecyclerFragment();
            fragmentManager.beginTransaction().add(R.id.clistamal_container, fragment).commit();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();

        pb.setProgress((int) intData());
        meteran.setText(String.valueOf((int)intData()) + "%");
    }

    private double intData() {

        String where = "date_today = '"+me.toDBDate(System.currentTimeMillis())+"'";
        String keys = "target_today, done_today";
        Cursor cursor = me.getSpecificData(this, "tb_mypoint", keys, where);

//        Log.d("da", ""+cursor.getCount());

        if (cursor.getCount() > 0) {

            return (cursor.getDouble(1) / cursor.getDouble(0)) * 100;
        } else {
            return  0;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 222 || requestCode == 223) {
            if(resultCode == RESULT_OK) {
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//                    iin.setAction(AdzanAlarmService.ACTION_START_SERVICE);
//                    startService(iin);
//                }
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    iin.setAction(AdzanAlarmService.ACTION_START_FOREGROUND_SERVICE);
//                    startForegroundService(iin);
//                }
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
                Log.e("req28", "no request code");
            }
        }
    }

    private void changePage(Class classname) {
        Intent intent = new Intent(this, classname);
        startActivity(intent);
    }
}
