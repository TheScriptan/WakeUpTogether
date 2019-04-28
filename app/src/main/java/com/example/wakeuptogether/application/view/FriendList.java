package com.example.wakeuptogether.application.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wakeuptogether.R;
import com.example.wakeuptogether.application.adapter.FriendListAdapter;
import com.example.wakeuptogether.application.adapter.PendingFriendListAdapter;
import com.example.wakeuptogether.application.viewmodel.AlarmViewModel;
import com.example.wakeuptogether.application.viewmodel.UserViewModel;
import com.example.wakeuptogether.business.model.Customer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendList extends Fragment {

    private UserViewModel userViewModel;
    private AlarmViewModel alarmViewModel;
    private PendingFriendListAdapter pendingFriendListAdapter;
    private FriendListAdapter friendListAdapter;

    @BindView(R.id.rv_pending_friends) RecyclerView rvPendingFriends;
    @BindView(R.id.rv_friend_list) RecyclerView rvFriendList;
    @BindView(R.id.label_no_friends) TextView labelNoFriends;
    @BindView(R.id.label_no_pending_friends) TextView labelNoPendingFriends;

    public FriendList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = ViewModelProviders.of(getActivity(), MainActivity.viewModelFactory).get(UserViewModel.class);
        alarmViewModel = ViewModelProviders.of(getActivity(), MainActivity.viewModelFactory).get(AlarmViewModel.class);


        ArrayList<Customer> pendingFriendList = new ArrayList<>();
        ArrayList<Customer> friendList = new ArrayList<>();
        pendingFriendListAdapter = new PendingFriendListAdapter(pendingFriendList, userViewModel);
        friendListAdapter = new FriendListAdapter(friendList, userViewModel, alarmViewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        ButterKnife.bind(this, view);
        userViewModel.getCurrentCustomer().observe(this, new Observer<Customer>() {
            @Override
            public void onChanged(Customer customer) {
                if(customer != null){
                    //Check if customer has pending friends
                    if(customer.getPendingFriends().size() > 0){
                        userViewModel.refreshPendingCustomerList(customer.getPendingFriends());
                        labelNoPendingFriends.setVisibility(View.GONE);
                    } else {
                        labelNoPendingFriends.setVisibility(View.VISIBLE);
                    }
                    //Check if customer has friends
                    if(customer.getFriends().size() > 0){
                        userViewModel.refreshFriendCustomerList(customer.getFriends());
                        labelNoFriends.setVisibility(View.GONE);
                    } else {
                        labelNoFriends.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        //Pending Friend List recyclerview
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

        //FriendList recyclerView
        rvFriendList.setHasFixedSize(true);
        rvFriendList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFriendList.setAdapter(friendListAdapter);

        userViewModel.getFriendCustomerList().observe(this, new Observer<List<Customer>>() {
            @Override
            public void onChanged(List<Customer> customerList) {
                if(customerList != null){
                    friendListAdapter.setCustomerList(customerList);
                }
            }
        });
        return view;
    }

}
