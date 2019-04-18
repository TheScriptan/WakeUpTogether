package com.example.wakeuptogether.business.model;

import java.util.Date;
import java.util.List;

public class Alarm {

    private String alarmUid;
    private List<String> customers;
    private Time time;

    public Alarm(){
        //No arg constructor for Firestore
    }

    public Alarm(String alarmUid, List<String> customers, Time time) {
        this.alarmUid = alarmUid;
        this.customers = customers;
        this.time = time;
    }

    public String getAlarmUid() {
        return alarmUid;
    }

    public List<String> getCustomers() {
        return customers;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
