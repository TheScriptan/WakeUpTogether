package com.example.wakeuptogether.business.repository;

import androidx.lifecycle.LiveData;

import com.example.wakeuptogether.business.firebase.FirebaseAuthHelper;
import com.example.wakeuptogether.business.firebase.FirestoreHelper;
import com.example.wakeuptogether.business.model.Customer;
import com.example.wakeuptogether.utils.PreferenceUtils;

import java.util.List;

public class UserRepository {

    private static UserRepository sInstance;
    private final FirestoreHelper firestoreHelper;
    private final FirebaseAuthHelper firebaseAuthHelper;
    private final PreferenceUtils preferenceUtils;

    private UserRepository(PreferenceUtils preferenceUtils){
        this.preferenceUtils = preferenceUtils;
        firestoreHelper = FirestoreHelper.getInstance();
        firebaseAuthHelper = FirebaseAuthHelper.getInstance(preferenceUtils);
    }

    public static UserRepository getInstance(PreferenceUtils preferenceUtils){
        if(sInstance == null){
            synchronized (UserRepository.class){
                if(sInstance == null){
                    sInstance = new UserRepository(preferenceUtils);
                }
            }
        }
        return sInstance;
    }

    /*
     * AUTHENTICATION
     */

    public void signIn(String email, String password){
        firebaseAuthHelper.signInWithEmailAndPassword(email, password);
    }

    public void register(String email, String password, Customer customer){
        firebaseAuthHelper.registerWithEmailAndPassword(email, password, customer);
    }

    public void signOut(){
        firebaseAuthHelper.signOut();
        firestoreHelper.signOut();
    }

    /*
     * Get Current Customer Data
     */

    public LiveData<Customer> getCurrentCustomerLiveData(){
        return firestoreHelper.getCurrentCustomerLiveData();
    }

    /*
     * Get Find Friend functionality data
     */

    public LiveData<List<Customer>> getCustomerFindList() {
        return firestoreHelper.getCustomerFindList();
    }

    public void refreshCustomerFindList(String username){
        firestoreHelper.refreshCustomerFindList(username);
    }

    /*
     * Add pending friend to current user
     */

    public void addPendingCustomer(String pendingFriend){
        firestoreHelper.addPendingCustomer(pendingFriend);
    }

    public void acceptPendingCustomer(String pendingFriend){
        firestoreHelper.acceptPendingCustomer(pendingFriend);
    }

    public void refusePendingCustomer(String pendingFriend){
        firestoreHelper.refusePendingCustomer(pendingFriend);
    }

    public void refreshPendingCustomerList(List<String> customers){
        firestoreHelper.refreshPendingCustomerList(customers);
    }

    public LiveData<List<Customer>> getPendingCustomerList(){
        return firestoreHelper.getPendingCustomerList();
    }

    /*
     * Friend Functionality
     */

    public void refreshFriendCustomerList(List<String> customers){
        firestoreHelper.refreshFriendCustomerList(customers);
    }

    public void removeFriend(String friend){
        firestoreHelper.removeFriend(friend);
    }

    public LiveData<List<Customer>> getFriendCustomerList(){
        return firestoreHelper.getFriendCustomerList();
    }

    /*
     * Sleep and waking up functionality
     */

    public void setCustomerWakeUp(int hour, int minute){
        firestoreHelper.setCustomerWakeUp(hour, minute);
    }

    public void setCustomerSleep(int hour, int minute){
        firestoreHelper.setCustomerSleep(hour, minute);
    }
}
