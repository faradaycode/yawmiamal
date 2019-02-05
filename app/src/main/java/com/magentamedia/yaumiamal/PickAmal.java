package com.magentamedia.yaumiamal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.magentamedia.yaumiamal.fragments.PickAmalRecyclerFragment;
import com.magentamedia.yaumiamal.models.ToolbarTitle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PickAmal extends AppCompatActivity {

    @OnClick(R.id.add_custom_kegiatan)
    void addCustomKegiatan() {

        Intent intent = new Intent(this, AturAmal.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_amal);
        ToolbarTitle toolbarTitle = new ToolbarTitle();
        ButterKnife.bind(PickAmal.this);

        TextView Titles = findViewById(R.id.toolbar_title);

        toolbarTitle.setToolbarTitle("Rencana Kegiatan");
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

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.pa_parent);

        if(fragment == null) {
            fragment = new PickAmalRecyclerFragment();
            fragmentManager.beginTransaction().add(R.id.pa_parent, fragment).commit();
        }


    }

}
