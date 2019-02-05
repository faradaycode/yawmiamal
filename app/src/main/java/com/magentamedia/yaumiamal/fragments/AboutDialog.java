package com.magentamedia.yaumiamal.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.magentamedia.yaumiamal.BuildConfig;
import com.magentamedia.yaumiamal.R;

public class AboutDialog extends DialogFragment {

    private View rootView;
    private TextView t_buildVer;
    private static String versionme = "Versi " + BuildConfig.VERSION_NAME;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_about, container,false);

        t_buildVer = rootView.findViewById(R.id.tx_build_version);
        t_buildVer.setText(versionme);

        return rootView;
    }
}
