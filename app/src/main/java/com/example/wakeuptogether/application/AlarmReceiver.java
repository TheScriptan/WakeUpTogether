package com.example.wakeuptogether.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper.getInstance(context).deliverWakeUpNotification(context);
    }
}
