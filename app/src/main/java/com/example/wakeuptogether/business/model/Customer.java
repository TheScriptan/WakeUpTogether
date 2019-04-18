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
}
