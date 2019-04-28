package com.example.wakeuptogether.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String USER_PASSWORD = "USER_PASSWORD";
    private static final String USER_UID = "USER_UID";

    private static PreferenceUtils sInstance;
    private Context context;

    public PreferenceUtils(Context context) {
        this.context = context;
        //When application is installed at the first time, set user data to "" "" so SharedPrefs don't have null
        if (getEmail() == null || getPassword() == null) {
            saveUserData("", "", "");
        }
    }

    public static PreferenceUtils getInstance(Context context) {
        if (sInstance == null) {
            synchronized (PreferenceUtils.class) {
                if (sInstance == null) {
                    sInstance = new PreferenceUtils(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    public void saveUserData(String email, String password, String uid) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(USER_EMAIL, email);
        prefsEditor.putString(USER_PASSWORD, password);
        prefsEditor.putString(USER_UID, uid);
        prefsEditor.apply();
    }

    public String getEmail() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(USER_EMAIL, null);
    }

    public String getPassword() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(USER_PASSWORD, null);
    }

    public String getUserUid(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(USER_UID, null);
    }

    public void removeData() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(USER_EMAIL, "");
        prefsEditor.putString(USER_PASSWORD, "");
        prefsEditor.putString(USER_UID, "");
        prefsEditor.apply();
    }
}
