package com.example.wakeuptogether.application.view;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.wakeuptogether.application.viewmodel.AlarmViewModel;
import com.example.wakeuptogether.application.viewmodel.UserViewModel;
import com.example.wakeuptogether.business.model.Time;
import com.google.firebase.firestore.auth.User;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

public class DialogTimePicker extends DialogFragment {

    private UserViewModel userViewModel;
    private AlarmViewModel alarmViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        alarmViewModel = ViewModelProviders.of(this).get(AlarmViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        Calendar c = Calendar.getInstance();
        //Todo: change hour and minute to Firestore hour and minute
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), timeSetListener(), hour, minute
                , DateFormat.is24HourFormat(getContext()));
        return timePickerDialog;
    }

    public TimePickerDialog.OnTimeSetListener timeSetListener(){
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String alarmUid = userViewModel.getCurrentCustomer().getValue().getAlarmUid();
                alarmViewModel.updateAlarm(alarmUid, new Time(hourOfDay, minute));
                Toast.makeText(getContext(), "Hour: " + hourOfDay + " Minute: " + minute, Toast.LENGTH_SHORT).show();
            }
        };
        return listener;
    }
}
