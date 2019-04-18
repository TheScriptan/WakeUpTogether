package com.example.wakeuptogether.business.repository;

import com.example.wakeuptogether.business.firebase.FirestoreHelper;
import com.example.wakeuptogether.business.model.Alarm;
import com.example.wakeuptogether.business.model.Customer;
import com.example.wakeuptogether.business.model.Time;

import java.util.List;

import androidx.lifecycle.LiveData;

public class AlarmRepository {

    private static AlarmRepository sInstance;
    private static FirestoreHelper firestoreHelper;

    private AlarmRepository(){
        firestoreHelper = FirestoreHelper.getInstance();
    }

    public static AlarmRepository getInstance(){
        if(sInstance == null){
            synchronized (AlarmRepository.class){
                if(sInstance == null){
                    sInstance = new AlarmRepository();
                }
            }
        }
        return sInstance;
    }

    public void addAlarm(Time time){
        firestoreHelper.addAlarm(time);
    }

    public void updateAlarm(String alarmUid, Time time){
        firestoreHelper.updateAlarm(alarmUid, time);
    }

    public void inviteCustomerToAlarm(String alarmUid, String friendUid){
        firestoreHelper.inviteCustomerToAlarm(alarmUid, friendUid);
    }

    public void leaveAlarm(String alarmUid){
        firestoreHelper.leaveAlarm(alarmUid);
    }

    public void listenForAlarm(String alarmUid){
        firestoreHelper.listenForAlarm(alarmUid);
    }

    public LiveData<Alarm> getAlarm(){
        return firestoreHelper.getAlarm();
    }

    /*
     * Alarm Friend Functionality
     */

    public void refreshAlarmCustomerList(List<String> customers){
        firestoreHelper.refreshAlarmCustomerList(customers);
    }

    public LiveData<List<Customer>> getAlarmCustomerList(){
        return firestoreHelper.getAlarmCustomerList();
    }
}
