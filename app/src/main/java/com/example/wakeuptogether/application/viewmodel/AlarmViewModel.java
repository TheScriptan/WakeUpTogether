package com.example.wakeuptogether.application.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wakeuptogether.business.model.Alarm;
import com.example.wakeuptogether.business.model.Customer;
import com.example.wakeuptogether.business.model.Time;
import com.example.wakeuptogether.business.repository.AlarmRepository;

import java.util.List;

public class AlarmViewModel extends ViewModel {

    private final LiveData<Alarm> alarmLiveData;
    private final AlarmRepository alarmRepository;

    public AlarmViewModel(AlarmRepository alarmRepository){
        this.alarmRepository = alarmRepository;
        alarmLiveData = alarmRepository.getAlarm();
    }

    public void addAlarm(Time time){
        alarmRepository.addAlarm(time);
    }

    public void updateAlarm(String alarmUid, Time time){
        alarmRepository.updateAlarm(alarmUid, time);
    }

    public void inviteCustomerToAlarm(String alarmUid, String friendUid){
        alarmRepository.inviteCustomerToAlarm(alarmUid, friendUid);
    }

    public void leaveAlarm(String alarmUid){
        alarmRepository.leaveAlarm(alarmUid);
    }

    public void listenForAlarm(String alarmUid){
        alarmRepository.listenForAlarm(alarmUid);
    }

    public LiveData<Alarm> getAlarm(){
        return alarmLiveData;
    }

    public void refreshAlarmCustomerList(List<String> customers){
        alarmRepository.refreshAlarmCustomerList(customers);
    }

    public LiveData<List<Customer>> getAlarmCustomerList(){
        return alarmRepository.getAlarmCustomerList();
    }
}
