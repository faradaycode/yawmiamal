package com.magentamedia.yaumiamal.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.magentamedia.yaumiamal.R;
import com.magentamedia.yaumiamal.models.dayParcel;
import com.magentamedia.yaumiamal.providers.YawmiMethodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeekdayDialog extends DialogFragment implements View.OnClickListener {

    private List<ToggleButton> dayToggles = new ArrayList<>();
    Map<String, Integer> selectedDay;
    private View rootv;
    private YawmiMethodes me = new YawmiMethodes();

    private Button sbutton;
    private ImageView dismissDay;
    private RadioButton rb_daily;
    private RadioButton rb_custom;
    private LinearLayout picker_layout;
    private dayParcel parcels;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootv = inflater.inflate(R.layout.dialog_dayweek_pick, container, false);

        sbutton = rootv.findViewById(R.id.save_day_week_dialog);
        sbutton.setOnClickListener(this);
        dismissDay = rootv.findViewById(R.id.btn_dismiss_dayweek);
        dismissDay.setOnClickListener(this);
        rb_daily = rootv.findViewById(R.id.rb_daily);
        rb_custom = rootv.findViewById(R.id.rb_kustom);
        picker_layout = rootv.findViewById(R.id.linlay_daypicker);
        picker_layout.setVisibility(View.GONE);

        selectedDay = new HashMap<>();

        Bundle passedParam = getArguments();

        bindingView();
        onStartToggle();
        listenToToggle();

        if (passedParam.getInt("daily_status") == 1) {
            rb_daily.setChecked(true);
        } else {
            rb_custom.setChecked(true);
        }

        if (rb_custom.isChecked()) {
            picker_layout.setVisibility(View.VISIBLE);
        }

        bindingToggleState();
        radioAction();

        return rootv;
    }

    public interface OnCompleteListener {
        void onCompletePickWeekday(Map<String, Integer> maps);
    }

    private OnCompleteListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            this.mListener = (OnCompleteListener) context;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public void onClick(View view) {
        int btid = view.getId();

        switch (btid) {
            case R.id.save_day_week_dialog:
                getWeekDay();
                break;
            case R.id.btn_dismiss_dayweek:
                dismiss();
                break;
        }
    }

    private void radioAction() {
        rb_custom.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    picker_layout.setVisibility(View.VISIBLE);
                }
            }
        });

        rb_daily.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    picker_layout.setVisibility(View.GONE);
                }
            }
        });
    }

    private void bindingView() {
        dayToggles.add((ToggleButton) rootv.findViewById(R.id.monday_toggle));
        dayToggles.add((ToggleButton) rootv.findViewById(R.id.tuesday_toggle));
        dayToggles.add((ToggleButton) rootv.findViewById(R.id.wednesday_toggle));
        dayToggles.add((ToggleButton) rootv.findViewById(R.id.thursday_toggle));
        dayToggles.add((ToggleButton) rootv.findViewById(R.id.friday_toggle));
        dayToggles.add((ToggleButton) rootv.findViewById(R.id.saturday_toggle));
        dayToggles.add((ToggleButton) rootv.findViewById(R.id.sunday_toggle));
    }

    private void bindingToggleState() {
        for (int i = 0; i < dayToggles.size(); i++) {
            Weekday weekday = Weekday.values()[i];
            if (dayToggles.get(i).isChecked()) {
                selectedDay.put(String.valueOf(weekday), 1);
            } else {
                selectedDay.put(String.valueOf(weekday), 0);
            }
        }
    }

    private void onStartToggle() {
        parcels = getArguments().getParcelable("hariParcel");

        assert parcels != null;
        if (parcels.getSUN() == 1) {
            dayToggles.get(6).setChecked(true);
        }

        if (parcels.getMON() == 1) {
            dayToggles.get(0).setChecked(true);
        }

        if (parcels.getTUE() == 1) {
            dayToggles.get(1).setChecked(true);
        }

        if (parcels.getWED() == 1) {
            dayToggles.get(2).setChecked(true);
        }

        if (parcels.getTHU() == 1) {
            dayToggles.get(3).setChecked(true);
        }

        if (parcels.getFRI() == 1) {
            dayToggles.get(4).setChecked(true);
        }

        if (parcels.getSAT() == 1) {
            dayToggles.get(5).setChecked(true);
        }
    }

    private void listenToToggle() {
        for (int i = 0; i < dayToggles.size(); i++) {
            final Weekday weekday = Weekday.values()[i];

            dayToggles.get(i).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        selectedDay.put(String.valueOf(weekday), 1);
                    } else {
                        selectedDay.put(String.valueOf(weekday), 0);
                    }
                }
            });
        }
    }

    private enum Weekday {
        mon,
        tue,
        wed,
        thu,
        fri,
        sat,
        sun;

        public static List<Weekday> getAllDays() {
            return Arrays.asList(Weekday.values());
        }
    }

    private void getWeekDay() {

        this.mListener.onCompletePickWeekday(selectedDay);

        if (rb_custom.isChecked()) {
            TextView textView2 = getActivity().findViewById(R.id.tx_periode);
            me.setTextview(textView2, rb_custom.getText().toString());
            getActivity().findViewById(R.id.reset_my_period).setVisibility(View.VISIBLE);
        }

        dismiss();
    }

    @Override
    public String toString() {
        String ret = "Map Value ";

        for (Map.Entry<String, Integer> datas: selectedDay.entrySet()) {
            ret += datas.getKey() + ": " + datas.getValue();
        }

        return ret;
    }
}
