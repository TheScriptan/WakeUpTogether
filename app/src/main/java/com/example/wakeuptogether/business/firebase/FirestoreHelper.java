package com.example.wakeuptogether.business.firebase;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wakeuptogether.business.model.Alarm;
import com.example.wakeuptogether.business.model.Customer;
import com.example.wakeuptogether.business.model.Time;
import com.example.wakeuptogether.utils.AppExecutors;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class FirestoreHelper {

    private static final String TAG = "FirebaseTag";


    private ListenerRegistration currentCustomerListener;
    private ListenerRegistration alarmListener;

    private static FirestoreHelper sInstance;
    private FirebaseFirestore db;

    private CollectionReference userRef;
    private CollectionReference alarmRef;

    private MutableLiveData<Customer> customerMutableLiveData;
    private MutableLiveData<List<Customer>> customerFindMutableLiveData;
    private MutableLiveData<List<Customer>> pendingCustomerListMutableLiveData;
    private MutableLiveData<List<Customer>> friendCustomerListMutableLiveData;
    private MutableLiveData<List<Customer>> alarmCustomerListMutableLiveData;
    private MutableLiveData<Alarm> alarmMutableLiveData;


    private FirestoreHelper(){
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users");
        alarmRef = db.collection("alarms");
        customerMutableLiveData = new MutableLiveData<>();
        customerFindMutableLiveData = new MutableLiveData<>();
        pendingCustomerListMutableLiveData = new MutableLiveData<>();
        friendCustomerListMutableLiveData = new MutableLiveData<>();
        alarmCustomerListMutableLiveData = new MutableLiveData<>();
        alarmMutableLiveData = new MutableLiveData<>();
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
        alarmCustomerListMutableLiveData.setValue(null);
        alarmMutableLiveData.setValue(null);
        removeCurrentCustomerListener();
        removeAlarmListener();
    }

    /*
     * Current User Management in MainActivity
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
        if(currentCustomerListener != null)
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
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef.document(pendingFriend).update("pendingFriends", FieldValue.arrayUnion(currentUser));
    }

    public void acceptPendingCustomer(String pendingFriend){
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
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

    /*
     * Alarm Functionality
     */

    public void addAlarm(Time time){
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference newAlarmRef = alarmRef.document();
        List<String> customers = new ArrayList<>();
        customers.add(currentUser);
        Alarm alarm = new Alarm(newAlarmRef.getId(), customers, time);
        newAlarmRef.set(alarm).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.v(TAG, "Alarm created successfully!");
                alarmMutableLiveData.postValue(alarm);
            }
        });
        userRef.document(currentUser).update("alarmUid", newAlarmRef.getId());
    }

    public void updateAlarm(String alarmUid, Time time){
        alarmRef.document(alarmUid).update("time", time);
        Alarm alarm = alarmMutableLiveData.getValue();
        alarm.setTime(time);
        alarmMutableLiveData.postValue(alarm);
    }

    public void leaveAlarm(String alarmUid){
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Alarm alarm = alarmMutableLiveData.getValue();
        userRef.document(currentUser).update("alarmUid", "-1");
        if(alarm == null){
            return;
        }
        if(alarm.getCustomers().size() > 1){
            alarmRef.document(alarmUid).update("customers", FieldValue.arrayRemove(currentUser));
        } else {
            alarmRef.document(alarmUid).delete();
        }
    }

    public void inviteCustomerToAlarm(String alarmUid, String friendUid){
        userRef.whereEqualTo("uid", friendUid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Customer customer = documentSnapshot.toObject(Customer.class);
                    if(customer.getAlarmUid().equals("-1")){
                        userRef.document(friendUid).update("alarmUid", alarmUid);
                        alarmRef.document(alarmUid).update("customers", FieldValue.arrayUnion(friendUid));
                    }
                }
            }
        });
    }

    public void listenForAlarm(String alarmUid){
        DocumentReference alarmListenRef = alarmRef.document(alarmUid);
        alarmListener =  alarmListenRef.addSnapshotListener(AppExecutors.getInstance().networkIO(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    return;
                }
                Alarm alarm = documentSnapshot.toObject(Alarm.class);
                if(alarm != null){
                    if(alarm.getCustomers() != null){
                        refreshAlarmCustomerList(alarm.getCustomers());
                    }
                }
                alarmMutableLiveData.postValue(alarm);
            }
        });
    }

    public void removeAlarmListener(){
        if(alarmListener != null)
            alarmListener.remove();
    }

    public LiveData<Alarm> getAlarm() {
        return alarmMutableLiveData;
    }

    /*
     * Alarm Friend Functionality
     */

    public void refreshAlarmCustomerList(List<String> customers){
        List<Customer> alarmCustomers = new ArrayList<>();
        for(int i = 0; i < customers.size(); i++){
            userRef.document(customers.get(i)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Customer customer = documentSnapshot.toObject(Customer.class);
                    alarmCustomers.add(customer);
                    alarmCustomerListMutableLiveData.setValue(alarmCustomers);
                }
            }); //Success Listener
        } //For loop
    }

    public LiveData<List<Customer>> getAlarmCustomerList(){
        return alarmCustomerListMutableLiveData;
    }

    /*
     *  Sleeping and waking up functionality
     */

    public void setCustomerWakeUp(int hour, int minute){
        Customer customer = customerMutableLiveData.getValue();
        Time newTime = new Time(hour, minute);
        if(customer != null){
            customer.setStatus("AWAKE");
            customer.setWakeUpTime(newTime);
            customerMutableLiveData.setValue(customer);
            userRef.document(customer.getUid()).update("wakeUpTime", newTime);
            userRef.document(customer.getUid()).update("status", "AWAKE");
        } else {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            String uid = user.getUid();
            userRef.document(uid).update("wakeUpTime", newTime);
            userRef.document(uid).update("status", "AWAKE");
        }
    }

    public void setCustomerSleep(int hour, int minute){
        Customer customer = customerMutableLiveData.getValue();
        Time newTime = new Time(hour, minute);
        if(customer != null){
            customer.setStatus("SLEEPING");
            customer.setSleepTime(newTime);
            customerMutableLiveData.setValue(customer);
            userRef.document(customer.getUid()).update("sleepTime", newTime);
            userRef.document(customer.getUid()).update("status", "SLEEPING");
        }
    }
}
