package com.example.wakeuptogether.application.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.wakeuptogether.business.repository.AlarmRepository;
import com.example.wakeuptogether.business.repository.UserRepository;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    public ViewModelFactory(UserRepository userRepository, AlarmRepository alarmRepository){
        this.userRepository = userRepository;
        this.alarmRepository = alarmRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(UserViewModel.class)){
            //noinspection unchecked
            return (T) new UserViewModel(userRepository);
        } else if (modelClass.isAssignableFrom(AlarmViewModel.class)){
            //noinspection unchecked
            return (T) new AlarmViewModel(alarmRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
