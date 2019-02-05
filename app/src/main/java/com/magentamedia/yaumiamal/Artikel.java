package com.magentamedia.yaumiamal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.magentamedia.yaumiamal.fragments.ArtikelRecyclerFragment;
import com.magentamedia.yaumiamal.fragments.MyAmalRecyclerFragment;

public class Artikel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artikel);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.artikel_recycler_parent);

        if (fragment == null) {
            fragment = new ArtikelRecyclerFragment();
            fragmentManager.beginTransaction().add(R.id.artikel_recycler_parent, fragment).commit();
        }
    }
}
