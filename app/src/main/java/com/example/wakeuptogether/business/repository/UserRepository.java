package com.example.wakeuptogether.business.repository;

import com.example.wakeuptogether.business.firebase.FirebaseAuthHelper;
import com.example.wakeuptogether.business.firebase.FirestoreHelper;
import com.example.wakeuptogether.business.model.Customer;
import com.google.firebase.firestore.FieldValue;

import java.util.List;

import androidx.lifecycle.LiveData;

public class UserRepository {

    private static UserRepository sInstance;
    private FirestoreHelper firestoreHelper;
    private FirebaseAuthHelper firebaseAuthHelper;

    private UserRepository(){
        firestoreHelper = FirestoreHelper.getInstance();
        firebaseAuthHelper = FirebaseAuthHelper.getInstance();
    }

    public static UserRepository getInstance(){
        if(sInstance == null){
            synchronized (UserRepository.class){
                if(sInstance == null){
                    sInstance = new UserRepository();
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
}
