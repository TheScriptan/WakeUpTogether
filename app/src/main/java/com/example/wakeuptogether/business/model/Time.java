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
        if(getMinute() < 10 && getHour() < 10){
            return "0" + getHour() + ":0" + getMinute();
        } else if(getHour() < 10){
            return "0" + getHour() + ":" + getMinute();
        } else if(getMinute() < 10){
            return "" + getHour() + ":0" + getMinute();
        } else {
           return "" + getHour() + ":" + getMinute();
        }
    }
}
