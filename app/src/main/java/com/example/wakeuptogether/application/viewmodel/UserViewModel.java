package com.example.wakeuptogether.application.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wakeuptogether.business.model.Customer;
import com.example.wakeuptogether.business.repository.UserRepository;

import java.util.List;

public class UserViewModel extends ViewModel {

    private final UserRepository userRepository;
    private final LiveData<Customer> currentCustomerLiveData;
    private final LiveData<List<Customer>> customerFindLiveData;
    private final LiveData<List<Customer>> pendingCustomerListLiveData;

    public UserViewModel(UserRepository userRepository){
        this.userRepository = userRepository;
        currentCustomerLiveData = userRepository.getCurrentCustomerLiveData();
        customerFindLiveData = userRepository.getCustomerFindList();
        pendingCustomerListLiveData = userRepository.getPendingCustomerList();
    }

    /*
     * AUTHENTICATION
     */

    public void signIn(String email, String password){
        userRepository.signIn(email, password);
    }

    public void register(String email, String password, Customer customer){
        userRepository.register(email, password, customer);
    }

    public void signOut(){
        userRepository.signOut();
    }

    /*
     * CUSTOMER MANAGEMENT
     */

    /*
     * Get Current Customer
     */

    public LiveData<Customer> getCurrentCustomer(){
        return currentCustomerLiveData;
    }

    /*
     * Find Friend Functionality
     */
    public LiveData<List<Customer>> getCurrentCustomerList() {return customerFindLiveData;}

    public void refreshCustomerFindList(String username) {
        userRepository.refreshCustomerFindList(username);
    }

    /*
     * Add Pending Friend
     */

    public void addPendingCustomer(String pendingFriend){
        userRepository.addPendingCustomer(pendingFriend);
    }

    public void acceptPendingCustomer(String pendingFriend){
        userRepository.acceptPendingCustomer(pendingFriend);
    }

    public void refusePendingCustomer(String pendingFriend){
        userRepository.refusePendingCustomer(pendingFriend);
    }

    public void refreshPendingCustomerList(List<String> customers){
        userRepository.refreshPendingCustomerList(customers);
    }

    public LiveData<List<Customer>> getPendingCustomerList(){
        return pendingCustomerListLiveData;
    }

    /*
     * Friend functionality
     */

    public void refreshFriendCustomerList(List<String> customers){
        userRepository.refreshFriendCustomerList(customers);
    }

    public void removeFriend(String friend){
        userRepository.removeFriend(friend);
    }

    public LiveData<List<Customer>> getFriendCustomerList(){
        return userRepository.getFriendCustomerList();
    }

    /*
     * Sleep
     */

    public void setCustomerWakeUp(int hour, int minute){
        userRepository.setCustomerWakeUp(hour, minute);
    }

    public void setCustomerSleep(int hour, int minute){
        userRepository.setCustomerSleep(hour, minute);
    }

}
