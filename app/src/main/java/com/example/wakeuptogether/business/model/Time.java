package com.example.wakeuptogether.business.model;

public class Time {

    private int hour;
    private int minute;

    public Time() {
        //No constructor for firebase
    }

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    @Override
    public String toString() {
        return "" + getHour() + ":" + getMinute();
    }
}
