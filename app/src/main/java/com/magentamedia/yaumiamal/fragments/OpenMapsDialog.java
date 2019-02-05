package com.magentamedia.yaumiamal.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.magentamedia.yaumiamal.R;
import com.magentamedia.yaumiamal.providers.YawmiMethodes;

import java.util.ArrayList;
import java.util.List;

public class OpenMapsDialog extends DialogFragment implements View.OnClickListener {

    private List<ToggleButton> dayToggles = new ArrayList<>();

    private View rootv;
    private YawmiMethodes me = new YawmiMethodes();

    private TextView sbutton;
    private TextView nbutton;
    private Handler han;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootv = inflater.inflate(R.layout.dialog_open_maps, container, false);
        sbutton = rootv.findViewById(R.id.w_yesbtn);
        sbutton.setOnClickListener(this);
        nbutton = rootv.findViewById(R.id.w_nobtn);
        nbutton.setOnClickListener(this);

        return rootv;
    }

    @Override
    public void onClick(View view) {
        int btid = view.getId();

        switch (btid) {
            case R.id.w_yesbtn:
                getOpenMaps();
                break;
            case R.id.w_nobtn:
                dismiss();
                break;
        }
    }

    private void getOpenMaps() {
        han = new Handler();

        han.postDelayed(new Runnable() {
            @Override
            public void run() {
                Uri geos = Uri.parse("geo:0,0?q=");
                Intent ine = new Intent(Intent.ACTION_VIEW, geos);
                ine.setPackage("com.google.android.apps.maps");
                startActivity(ine);
            }
        }, 1000);
    }
}
