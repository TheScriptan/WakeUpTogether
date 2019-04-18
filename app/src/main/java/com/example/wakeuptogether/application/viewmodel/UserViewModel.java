package com.example.wakeuptogether.application.viewmodel;

import com.example.wakeuptogether.business.model.Customer;
import com.example.wakeuptogether.business.repository.UserRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {

    private UserRepository userRepository;
    private LiveData<Customer> currentCustomerLiveData;
    private LiveData<List<Customer>> customerFindLiveData;
    private LiveData<List<Customer>> pendingCustomerListLiveData;

    public UserViewModel(){
        userRepository = UserRepository.getInstance();
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

}
