package com.example.wakeuptogether.application.view;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wakeuptogether.R;
import com.example.wakeuptogether.application.viewmodel.UserViewModel;
import com.example.wakeuptogether.business.model.Customer;


/**
 * A simple {@link Fragment} subclass.
 */
public class Main extends Fragment {

    private UserViewModel userViewModel;

    public Main() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Customer customer = userViewModel.getCurrentCustomer().getValue();
        Toast.makeText(getContext(), customer.getUsername(), Toast.LENGTH_SHORT).show();
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

}
