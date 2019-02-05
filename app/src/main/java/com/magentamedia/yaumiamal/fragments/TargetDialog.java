package com.magentamedia.yaumiamal.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.magentamedia.yaumiamal.R;
import com.magentamedia.yaumiamal.providers.YawmiMethodes;

import java.util.Map;

import butterknife.OnClick;

public class TargetDialog extends DialogFragment implements View.OnClickListener {

    Button yesbtn;
    ImageView closeModal;
    Spinner spinn;
    private View rootv;
    private RadioButton rb_sudah;
    private RadioButton rb_syarat;
    private LinearLayout target_picker;
    private static String SUDAH = "sudah";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootv = inflater.inflate(R.layout.dialog_target_pick, container, false);

        String[] Starget = getArguments().getStringArray("target");

        spinn = rootv.findViewById(R.id.spin_target);

        ArrayAdapter<String> spinnerArray = new ArrayAdapter<>(getContext(), R.layout.spinner_text,
                Starget);
        spinnerArray.setDropDownViewResource(R.layout.spinner_text);
        spinn.setAdapter(spinnerArray);

        yesbtn = rootv.findViewById(R.id.save_my_target);
        closeModal = rootv.findViewById(R.id.btn_dismiss_target);
        rb_sudah = rootv.findViewById(R.id.rb_sudah);
        rb_sudah.setChecked(true);
        rb_syarat = rootv.findViewById(R.id.rb_syarat);
        target_picker = rootv.findViewById(R.id.custom_target_layout);

        target_picker.setVisibility(View.GONE);

        yesbtn.setOnClickListener(this);
        closeModal.setOnClickListener(this);

        radioAction();

        return rootv;
    }

    @Override
    public void onClick(View view) {
        int btid = view.getId();

        switch (btid) {
            case R.id.save_my_target:
                getMyTarget();
                break;
            case R.id.btn_dismiss_target:
                dismiss();
                break;
        }
    }

    private void radioAction() {
        rb_sudah.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    target_picker.setVisibility(View.GONE);
                }
            }
        });

        rb_syarat.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    target_picker.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getMyTarget() {
        EditText edtarget = rootv.findViewById(R.id.edt_target);

        if (rb_syarat.isChecked()) {
            if (!edtarget.getText().toString().isEmpty()) {
                Spinner spinner = rootv.findViewById(R.id.spin_target);

                String teks = edtarget.getText().toString() + " " + spinner.getSelectedItem().toString();

                this.mListener.onCompletePickTarget(teks, true);
            }
        } else {
            this.mListener.onCompletePickTarget(SUDAH, false);
        }

        dismiss();
    }

    public interface OnCompleteListener {
        void onCompletePickTarget(String result, boolean status);
    }

    private TargetDialog.OnCompleteListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            this.mListener = (TargetDialog.OnCompleteListener) context;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCompleteListener");
        }
    }
}
