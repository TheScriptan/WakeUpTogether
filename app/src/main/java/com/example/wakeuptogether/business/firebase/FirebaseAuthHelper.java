package com.example.wakeuptogether.business.firebase;

import android.util.Log;

import com.example.wakeuptogether.business.model.Customer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthHelper {

    private static FirebaseAuthHelper sInstance;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private static final String TAG = "FirebaseTag";

    private FirebaseAuthHelper(){
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
    }

    public static FirebaseAuthHelper getInstance(){
        if(sInstance == null){
            synchronized (FirebaseAuthHelper.class){
                if(sInstance == null){
                    sInstance = new FirebaseAuthHelper();
                }
            }
        }
        return sInstance;
    }

    public boolean isUserLoggedIn(){
        if(currentUser != null){
            return true;
        } else {
            return false;
        }
    }

    public void signOut(){
        auth.signOut();
        currentUser = null;
    }

    public FirebaseUser getCurrentUser(){
        return currentUser;
    }

    public void registerWithEmailAndPassword(String email, String password, Customer customer){
        auth
                .createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
            currentUser = authResult.getUser();
            FirestoreHelper.getInstance().addCustomer(customer, currentUser.getUid());
        })
                .addOnFailureListener(e -> Log.v(TAG, e.toString()));
    }

    public void signInWithEmailAndPassword(String email, String password){
        auth
                .signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
            currentUser = authResult.getUser();
            FirestoreHelper.getInstance().getCurrentCustomer(currentUser.getUid());
        })
                .addOnFailureListener(e -> Log.v(TAG, e.toString()));
    }



}
