package com.example.wakeuptogether.application.view;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.wakeuptogether.application.NotificationHelper;
import com.example.wakeuptogether.application.viewmodel.AlarmViewModel;
import com.example.wakeuptogether.application.viewmodel.UserViewModel;
import com.example.wakeuptogether.business.model.Time;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DialogTimePicker extends DialogFragment {

    private UserViewModel userViewModel;
    private AlarmViewModel alarmViewModel;
    private Calendar calendar;
    private Calendar time;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        alarmViewModel = ViewModelProviders.of(getActivity(), MainActivity.viewModelFactory).get(AlarmViewModel.class);
        userViewModel = ViewModelProviders.of(getActivity(), MainActivity.viewModelFactory).get(UserViewModel.class);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        time = new GregorianCalendar(year, month, day, minute, hour);
        return new TimePickerDialog(getContext(), timeSetListener(), hour, minute, DateFormat.is24HourFormat(getContext()));
    }

    public TimePickerDialog.OnTimeSetListener timeSetListener(){
        TimePickerDialog.OnTimeSetListener listener = (view, hourOfDay, minute) -> {

            if(userViewModel.getCurrentCustomer().getValue() != null) {
                //Todo: Extract AlarmManager from here to a better place
                AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                time.set(Calendar.MINUTE, minute);
                time.set(Calendar.HOUR, hourOfDay);
                long triggerTime = time.getTimeInMillis();
                PendingIntent pd = NotificationHelper.getInstance(getContext()).getWakeUpPendingIntent();
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pd);

                String alarmUid = userViewModel.getCurrentCustomer().getValue().getAlarmUid();
                alarmViewModel.updateAlarm(alarmUid, new Time(hourOfDay, minute));
                Toast.makeText(getContext(), "Hour: " + hourOfDay + " Minute: " + minute, Toast.LENGTH_SHORT).show();
            }
        };
        return listener;
    }
}
