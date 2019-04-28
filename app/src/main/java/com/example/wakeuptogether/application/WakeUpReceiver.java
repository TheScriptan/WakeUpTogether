package com.example.wakeuptogether.application;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.wakeuptogether.business.firebase.FirestoreHelper;

import java.util.Calendar;

public class WakeUpReceiver extends BroadcastReceiver {

    public WakeUpReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        FirestoreHelper.getInstance().setCustomerWakeUp(hour, minute);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
