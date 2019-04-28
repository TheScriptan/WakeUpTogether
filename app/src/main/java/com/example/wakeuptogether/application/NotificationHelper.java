package com.example.wakeuptogether.application;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.wakeuptogether.R;
import com.example.wakeuptogether.application.view.MainActivity;

import java.util.Calendar;

public class NotificationHelper {

    private static NotificationHelper sInstance;

    private NotificationManager notificationManager;

    public static final String ACTION_WAKE_UP = "com.example.wakeuptogether.ACTION_WAKE_UP";

    public static final int NOTIFICATION_WAKE_UP = 0;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private PendingIntent deliverWakeUpPendingIntent;

    private NotificationHelper(Context context){
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();

        //Create AlarmReceiver
        Intent notifyIntent = new Intent(context, AlarmReceiver.class);
        deliverWakeUpPendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_WAKE_UP, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static NotificationHelper getInstance(Context context){
        if(sInstance == null){
            synchronized (NotificationHelper.class){
                if(sInstance == null){
                    sInstance = new NotificationHelper(context);
                }
            }
        }
        return sInstance;
    }

    //Testing notification
    public void deliverWakeUpNotification(Context context){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);

        //Create WakeUpReceiver
        Intent wakeUpIntent = new Intent(context, WakeUpReceiver.class);
        wakeUpIntent.setAction(ACTION_WAKE_UP);
        PendingIntent wakeUpActionPendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_WAKE_UP, wakeUpIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notification = getNotificationBuilder(context,
                "It is time to wake up!", "Notify your friends you woke up in time!", NOTIFICATION_WAKE_UP);
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.mipmap.ic_launcher, "Wake Up", wakeUpActionPendingIntent).build();
        notification.addAction(action);
        notificationManager.notify(NOTIFICATION_WAKE_UP, notification.build());
    }

    //NotificationCompatBuilder where default notification is defined, but not yet built
    public NotificationCompat.Builder getNotificationBuilder(Context context, String contentTitle, String contentText, int notificationId){
        Intent contentIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingContentIntent = PendingIntent.getActivity(context, notificationId, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_search_white_24dp)
                .setContentIntent(pendingContentIntent)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        return builder;
    }

    //Notification channel for API 26+
    public void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID, "Wake Up Together Notification", NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setDescription("Notifies when to wake up at a chosen time by you or your friends.");
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public PendingIntent getDeliverWakeUpPendingIntent(){
        return deliverWakeUpPendingIntent;
    }
}
