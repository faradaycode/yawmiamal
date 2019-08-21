/*
 * Created by Ferdi Reza on 8/20/19 9:53 AM
 * Copyright (c) 2019 . All rights reserved
 * Last modified 8/20/19 9:53 AM
 */

package com.bangfer.assistask.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.bangfer.assistask.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PengaturanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan);
        ButterKnife.bind(this);
    }
}
