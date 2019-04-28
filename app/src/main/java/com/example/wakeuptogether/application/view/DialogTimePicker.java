package com.example.wakeuptogether.application.view;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.wakeuptogether.application.viewmodel.AlarmViewModel;
import com.example.wakeuptogether.application.viewmodel.UserViewModel;
import com.example.wakeuptogether.business.model.Time;

import java.util.Calendar;

public class DialogTimePicker extends DialogFragment {

    private UserViewModel userViewModel;
    private AlarmViewModel alarmViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        alarmViewModel = ViewModelProviders.of(getActivity(), MainActivity.viewModelFactory).get(AlarmViewModel.class);
        userViewModel = ViewModelProviders.of(getActivity(), MainActivity.viewModelFactory).get(UserViewModel.class);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getContext(), timeSetListener(), hour, minute, DateFormat.is24HourFormat(getContext()));
    }

    public TimePickerDialog.OnTimeSetListener timeSetListener(){
        TimePickerDialog.OnTimeSetListener listener = (view, hourOfDay, minute) -> {

            if(userViewModel.getCurrentCustomer().getValue() != null) {

                String alarmUid = userViewModel.getCurrentCustomer().getValue().getAlarmUid();
                alarmViewModel.updateAlarm(alarmUid, new Time(hourOfDay, minute));
                Toast.makeText(getContext(), "Hour: " + hourOfDay + " Minute: " + minute, Toast.LENGTH_SHORT).show();
            }
        };
        return listener;
    }
}
