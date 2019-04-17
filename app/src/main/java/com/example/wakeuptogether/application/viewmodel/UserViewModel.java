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

    public UserViewModel(){
        userRepository = UserRepository.getInstance();
        currentCustomerLiveData = userRepository.getCurrentCustomerLiveData();
        customerFindLiveData = userRepository.getCustomerFindList();
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
     * Add Pending Friend
     */

    public void addPendingFriend(String currentUser, String pendingFriend){
        userRepository.addPendingFriend(currentUser, pendingFriend);
    }

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

}
