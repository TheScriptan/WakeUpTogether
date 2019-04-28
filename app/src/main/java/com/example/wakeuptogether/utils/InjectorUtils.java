package com.example.wakeuptogether.utils;

import android.content.Context;

import com.example.wakeuptogether.application.viewmodel.ViewModelFactory;
import com.example.wakeuptogether.business.repository.AlarmRepository;
import com.example.wakeuptogether.business.repository.UserRepository;
import com.google.firebase.firestore.auth.User;

public class InjectorUtils {

    public static UserRepository provideUserRepository(Context context){
        AppExecutors executors = AppExecutors.getInstance();
        PreferenceUtils preferenceUtils = PreferenceUtils.getInstance(context);
        return UserRepository.getInstance(preferenceUtils);
    }

    public static AlarmRepository provideAlarmRepository(){
        return AlarmRepository.getInstance();
    }

    public static ViewModelFactory provideViewModelFactory(Context context){
        UserRepository userRepository = provideUserRepository(context);
        AlarmRepository alarmRepository = provideAlarmRepository();
        return new ViewModelFactory(userRepository, alarmRepository);
    }
}
