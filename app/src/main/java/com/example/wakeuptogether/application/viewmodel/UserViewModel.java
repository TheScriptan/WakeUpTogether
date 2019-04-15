package com.example.wakeuptogether.application.viewmodel;

import com.example.wakeuptogether.business.model.Customer;
import com.example.wakeuptogether.business.repository.UserRepository;
import com.google.firebase.firestore.auth.User;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {

    private UserRepository userRepository;
    private LiveData<Customer> customerLiveData;

    public UserViewModel(){
        userRepository = UserRepository.getInstance();
        customerLiveData = userRepository.getCurrentCustomer();
    }

    public void signIn(String email, String password){
        userRepository.signIn(email, password);
    }

    public void register(String email, String password, Customer customer){
        userRepository.register(email, password, customer);
    }

    public void signOut(){
        userRepository.signOut();
    }

    public LiveData<Customer> getCurrentCustomer(){
        return customerLiveData;
    }
}
