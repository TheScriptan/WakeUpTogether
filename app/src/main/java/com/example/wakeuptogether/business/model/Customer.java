package com.example.wakeuptogether.business.model;

import java.util.List;

public class Customer {

    private String uid;
    private String username;
    private String country;
    private String status;
    private String bio;
    private String alarmUid;
    private int streaks;
    private List<String> friends;
    private List<String> pendingFriends;
    private Time wakeUpTime;
    private Time sleepTime;

    public Customer() {}

    public Customer(String username, String country, String status, String bio, String alarmUid, int streaks, List<String> friends, List<String> pendingFriends) {
        this.username = username;
        this.country = country;
        this.status = status;
        this.bio = bio;
        this.alarmUid = alarmUid;
        this.streaks = streaks;
        this.friends = friends;
        this.pendingFriends = pendingFriends;
        wakeUpTime = new Time(0, 0);
        sleepTime = new Time(0, 0);
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getCountry() {
        return country;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {this.status = status;}

    public String getBio() {
        return bio;
    }

    public String getAlarmUid() {
        return alarmUid;
    }

    public int getStreaks() {
        return streaks;
    }

    public List<String> getFriends() {
        return friends;
    }

    public List<String> getPendingFriends() {
        return pendingFriends;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Time getWakeUpTime() {
        return wakeUpTime;
    }

    public void setWakeUpTime(Time wakeUpTime) {
        this.wakeUpTime = wakeUpTime;
    }

    public Time getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(Time sleepTime) {
        this.sleepTime = sleepTime;
    }
}
