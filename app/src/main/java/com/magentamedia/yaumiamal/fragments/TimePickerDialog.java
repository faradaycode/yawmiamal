package com.magentamedia.yaumiamal.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.magentamedia.yaumiamal.R;
import com.magentamedia.yaumiamal.providers.YawmiMethodes;

import java.util.Calendar;

public class TimePickerDialog extends DialogFragment implements android.app.TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new android.app.TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        YawmiMethodes me = new YawmiMethodes();

        String mHour = (i < 10)?"0"+i:""+i;
        String mMinute = (i1 < 10)?"0"+i1:""+i1;
        TextView textView = getActivity().findViewById(R.id.tx_notify_time);
        ImageView bt_reset_timePick = getActivity().findViewById(R.id.reset_notify_time_text);

        me.setTextview(textView, mHour+":"+mMinute);
        bt_reset_timePick.setVisibility(View.VISIBLE);
    }
}
