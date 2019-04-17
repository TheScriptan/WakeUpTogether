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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class FirestoreHelper {

    private static final String TAG = "FirebaseTag";

    private static FirestoreHelper sInstance;
    private FirebaseFirestore db;
    private CollectionReference userRef;

    private MutableLiveData<Customer> customerMutableLiveData;
    private MutableLiveData<List<Customer>> customerFindMutableLiveData;


    private FirestoreHelper(){
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users");
        customerMutableLiveData = new MutableLiveData<>();
        customerFindMutableLiveData = new MutableLiveData<>();
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

        ref.addSnapshotListener(AppExecutors.getInstance().networkIO(), new EventListener<DocumentSnapshot>() {
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
     * Add Pending Friend
     */

    public void addPendingFriend(String currentUser, String pendingFriend){
        //Add currentUser UID to new friend's pendingFriend list
        userRef.document(pendingFriend).update("pendingFriends", FieldValue.arrayUnion(currentUser));
        //Add pendingFriend's UID to currentUser pendingFriend list I THINK I DONT NEED THIS
        //userRef.document(currentUser).update("pendingFriends", FieldValue.arrayUnion(pendingFriend));
    }
}
