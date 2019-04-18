package com.example.wakeuptogether.business.firebase;

import android.util.Log;

import com.example.wakeuptogether.business.model.Customer;
import com.example.wakeuptogether.utils.AppExecutors;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class FirestoreHelper {

    private static final String TAG = "FirebaseTag";

    private ListenerRegistration currentCustomerListener;
    private static FirestoreHelper sInstance;
    private FirebaseFirestore db;
    private CollectionReference userRef;

    private MutableLiveData<Customer> customerMutableLiveData;
    private MutableLiveData<List<Customer>> customerFindMutableLiveData;
    private MutableLiveData<List<Customer>> pendingCustomerListMutableLiveData;
    private MutableLiveData<List<Customer>> friendCustomerListMutableLiveData;


    private FirestoreHelper(){
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users");
        customerMutableLiveData = new MutableLiveData<>();
        customerFindMutableLiveData = new MutableLiveData<>();
        pendingCustomerListMutableLiveData = new MutableLiveData<>();
        friendCustomerListMutableLiveData = new MutableLiveData<>();
    }

    public static FirestoreHelper getInstance(){
        if(sInstance == null){
            synchronized (FirestoreHelper.class){
                if(sInstance == null){
                    sInstance = new FirestoreHelper();
                }
            }
        }
        return sInstance;
    }

    /*
     * Authentication
     */

    public void signOut(){
        customerMutableLiveData.setValue(null);
        customerFindMutableLiveData.setValue(null);
        pendingCustomerListMutableLiveData.setValue(null);
        friendCustomerListMutableLiveData.setValue(null);
        removeCurrentCustomerListener();
    }

    /*
     * Current User Management in FirebaseAuthHelper and MainActivity
     */

    public void addCurrentCustomer(Customer customer, String uid){
        DocumentReference ref = userRef.document(uid);
        customer.setUid(uid);
        ref
                .set(customer)
                .addOnSuccessListener(aVoid -> {
                    Log.v(TAG, "Customer added successfully!");
                    customerMutableLiveData.postValue(customer);
                })
                .addOnFailureListener(e -> Log.v(TAG, "Customer failed to be added"));
    }

    public void updateCurrentCustomerLiveData(String uid){
        DocumentReference ref = userRef.document(uid);
        ref
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Customer customer = documentSnapshot.toObject(Customer.class);
                    customerMutableLiveData.postValue(customer);
                });

    }

    public void listenForCurrentCustomer(String uid){
        DocumentReference ref = userRef.document(uid);

        currentCustomerListener = ref.addSnapshotListener(AppExecutors.getInstance().networkIO(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    return;
                }

                Customer customer = documentSnapshot.toObject(Customer.class);
                customerMutableLiveData.postValue(customer);
            }
        });
    }

    public void removeCurrentCustomerListener(){
        currentCustomerListener.remove();
    }

    public LiveData<Customer> getCurrentCustomerLiveData(){
        return customerMutableLiveData;
    }

    /*
     * Find Customer List functionality to display friend list according by given username
     */

    public void refreshCustomerFindList(String username){
        userRef.whereEqualTo("username", username).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Customer> customerList = new ArrayList<>();
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Customer customer = documentSnapshot.toObject(Customer.class);
                    customerList.add(customer);
                }
                customerFindMutableLiveData.postValue(customerList);
            }
        });
    }

    public LiveData<List<Customer>> getCustomerFindList(){
        return customerFindMutableLiveData;
    }

    /*
     * Pending Friend Functionality
     */

    public void addPendingCustomer(String pendingFriend){
        //Add currentUser UID to new friend's pendingFriend list
        String currentUser = FirebaseAuthHelper.getInstance().getCurrentUser().getUid();
        userRef.document(pendingFriend).update("pendingFriends", FieldValue.arrayUnion(currentUser));
    }

    public void acceptPendingCustomer(String pendingFriend){
        String currentUser = FirebaseAuthHelper.getInstance().getCurrentUser().getUid();
        userRef.document(currentUser).update("pendingFriends", FieldValue.arrayRemove(pendingFriend));
        userRef.document(currentUser).update("friends", FieldValue.arrayUnion(pendingFriend));
        userRef.document(pendingFriend).update("friends", FieldValue.arrayUnion(currentUser));
        //Todo: Temporary solution to update livedata. Should create CustomerList class and update everything
        //Refreshes when user accepts a friend. Refreshes pendingFriend list and friendList instantly
        List<Customer> tempPending = pendingCustomerListMutableLiveData.getValue();
        List<String> tempFriend = new ArrayList<>();
        tempFriend.add(pendingFriend);
        for(int i = 0; i < tempPending.size(); i++){
            if(tempPending.get(i).getUid().equals(pendingFriend)){
                Customer customer = tempPending.get(i);
                tempPending.remove(i);
            }
        }
        refreshFriendCustomerList(tempFriend);
        pendingCustomerListMutableLiveData.postValue(tempPending);
    }

    public void refusePendingCustomer(String pendingFriend){
        String currentUser = FirebaseAuthHelper.getInstance().getCurrentUser().getUid();
        userRef.document(currentUser).update("pendingFriends", FieldValue.arrayRemove(pendingFriend));
    }

    public void refreshPendingCustomerList(List<String> customers){
        List<Customer> pendingCustomers = new ArrayList<>();
        for(int i = 0; i < customers.size(); i++){
            userRef.document(customers.get(i)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Customer customer = documentSnapshot.toObject(Customer.class);
                    pendingCustomers.add(customer);
                    pendingCustomerListMutableLiveData.setValue(pendingCustomers);
                }
            }); //Success Listener
        } //For loop
    }

    public LiveData<List<Customer>> getPendingCustomerList(){
        return pendingCustomerListMutableLiveData;
    }

    /*
     * Friend Functionality
     */

    public void refreshFriendCustomerList(List<String> customers){
        List<Customer> friendList = new ArrayList<>();
        for(int i = 0; i < customers.size(); i++){
            userRef.document(customers.get(i)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Customer customer = documentSnapshot.toObject(Customer.class);
                    friendList.add(customer);
                    friendCustomerListMutableLiveData.postValue(friendList);
                }
            });
        }
    }

    public void removeFriend(String friend){
        String currentUser = FirebaseAuthHelper.getInstance().getCurrentUser().getUid();
        userRef.document(currentUser).update("friends", FieldValue.arrayRemove(friend));
        userRef.document(friend).update("friends", FieldValue.arrayRemove(currentUser));
        List<Customer> tempFriend = friendCustomerListMutableLiveData.getValue();
        for(int i = 0; i < tempFriend.size(); i++){
            if(tempFriend.get(i).getUid().equals(friend)){
                tempFriend.remove(i);
            }
        }
        friendCustomerListMutableLiveData.postValue(tempFriend);
    }

    public LiveData<List<Customer>> getFriendCustomerList(){
        return friendCustomerListMutableLiveData;
    }
}
