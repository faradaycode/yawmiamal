
/*
 * Created by Ferdi Reza on 8/20/19 9:52 AM
 * Copyright (c) 2019 . All rights reserved
 * Last modified 8/20/19 9:23 AM
 */

package com.bangfer.assistask.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bangfer.assistask.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @OnClick(R.id.bt_setting)
    void toSetting()
    {
        startActivity(new Intent(MainActivity.this, PengaturanActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
    }
}
