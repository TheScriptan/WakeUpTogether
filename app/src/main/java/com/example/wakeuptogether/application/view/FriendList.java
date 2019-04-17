package com.example.wakeuptogether.application.view;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wakeuptogether.R;
import com.example.wakeuptogether.application.adapter.PendingFriendListAdapter;
import com.example.wakeuptogether.application.viewmodel.UserViewModel;
import com.example.wakeuptogether.business.model.Customer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendList extends Fragment {

    private UserViewModel userViewModel;
    private PendingFriendListAdapter pendingFriendListAdapter;
    private Customer customer;

    @BindView(R.id.fab_add) FloatingActionButton fabAdd;
    @BindView(R.id.rv_pending_friends) RecyclerView rvPendingFriends;

    public FriendList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        customer = userViewModel.getCurrentCustomer().getValue();
        //Check if customer has pending friends
        if(customer.getPendingFriends() != null){
            userViewModel.refreshPendingCustomerList(customer.getPendingFriends());
        }
        ArrayList<Customer> pendingFriendList = new ArrayList<>();
        pendingFriendListAdapter = new PendingFriendListAdapter(pendingFriendList, userViewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        ButterKnife.bind(this, view);
        rvPendingFriends.setHasFixedSize(true);
        rvPendingFriends.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPendingFriends.setAdapter(pendingFriendListAdapter);

        userViewModel.getPendingCustomerList().observe(this, new Observer<List<Customer>>() {
            @Override
            public void onChanged(List<Customer> customers) {
                if(customers != null){
                    pendingFriendListAdapter.setPendingFriendList(customers);
                }
            }
        });
        return view;
    }

}
