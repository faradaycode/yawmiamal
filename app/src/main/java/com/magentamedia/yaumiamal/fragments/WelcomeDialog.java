package com.magentamedia.yaumiamal.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.magentamedia.yaumiamal.MainActivity;
import com.magentamedia.yaumiamal.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeDialog extends DialogFragment {

    private View rootv;

    private String param = "";

    @BindView(R.id.welcome_teks)
    TextView welcometext;

    @OnClick(R.id.welcome_ok)
    public void wel_ok (View view) {

        if (param.equals("")) {
            dismiss();
        } else {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        Log.i("WELCOME", "ok");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootv = inflater.inflate(R.layout.dialog_welcome, container, false);
        ButterKnife.bind(this, rootv);

        param = getArguments().getString("text_val");

        welcometext.setText(param);

        return rootv;
    }


}
