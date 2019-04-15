package com.example.wakeuptogether.business.firebase;

import android.util.Log;

import com.example.wakeuptogether.business.model.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class FirestoreHelper {

    private static final String TAG = "FirebaseTag";

    private static FirestoreHelper sInstance;
    private FirebaseFirestore db;
    private CollectionReference userRef;

    private MutableLiveData<Customer> customerMutableLiveData;


    private FirestoreHelper(){
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users");
        customerMutableLiveData = new MutableLiveData<>();
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

    public void addCustomer(Customer customer, String uid){
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

    public LiveData<Customer> getCustomer(){
        return customerMutableLiveData;
    }

    public void getCustomerById(String uid){
        DocumentReference ref = userRef.document(uid);
        ref
                .get()
                .addOnSuccessListener(documentSnapshot -> {
            Customer customer = documentSnapshot.toObject(Customer.class);
            customerMutableLiveData.postValue(customer);
        });
    }
}
