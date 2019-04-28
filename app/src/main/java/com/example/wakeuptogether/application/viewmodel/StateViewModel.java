package com.example.wakeuptogether.application.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StateViewModel extends ViewModel {

    //Main.java states
    private MutableLiveData<Boolean> hasAlarm;

    //MainActivity.java states
    private boolean isAuth;
    private boolean isFindFriend;
    private boolean canLeaveAlarm;
    private long lastTimeClicked;

    public StateViewModel(){
        hasAlarm = new MutableLiveData<>();

        hasAlarm.setValue(false);
        isAuth = false;
        isFindFriend = false;
        canLeaveAlarm = false;
        lastTimeClicked = 0;
    }

    public LiveData<Boolean> getHasAlarm() {
        return hasAlarm;
    }

    public void setHasAlarm(boolean hasAlarm) {
        this.hasAlarm.postValue(hasAlarm);
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public boolean isFindFriend() {
        return isFindFriend;
    }

    public void setFindFriend(boolean findFriend) {
        isFindFriend = findFriend;
    }

    public boolean isCanLeaveAlarm() {
        return canLeaveAlarm;
    }

    public void setCanLeaveAlarm(boolean canLeaveAlarm) {
        this.canLeaveAlarm = canLeaveAlarm;
    }

    public long getLastTimeClicked() {
        return lastTimeClicked;
    }

    public void setLastTimeClicked(long lastTimeClicked) {
        this.lastTimeClicked = lastTimeClicked;
    }
}
