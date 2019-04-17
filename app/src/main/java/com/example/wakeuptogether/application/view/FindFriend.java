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
import android.widget.Button;
import android.widget.EditText;

import com.example.wakeuptogether.R;
import com.example.wakeuptogether.application.adapter.FindFriendListAdapter;
import com.example.wakeuptogether.application.viewmodel.UserViewModel;
import com.example.wakeuptogether.business.model.Customer;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindFriend extends Fragment {

    private UserViewModel userViewModel;
    private FindFriendListAdapter findFriendListAdapter;

    @BindView(R.id.edit_find_friend) EditText editFindFriend;
    @BindView(R.id.button_find_friend) Button buttonFindFriend;
    @BindView(R.id.rv_friend_list) RecyclerView rvFriendList;

    public FindFriend() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ViewModel
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        findFriendListAdapter = new FindFriendListAdapter(userViewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View binding
        View view = inflater.inflate(R.layout.fragment_find_friend, container, false);
        ButterKnife.bind(this, view);

        //RecyclerView
        rvFriendList.setHasFixedSize(true);
        rvFriendList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFriendList.setAdapter(findFriendListAdapter);

        userViewModel.getCurrentCustomerList().observe(this, new Observer<List<Customer>>() {
            @Override
            public void onChanged(List<Customer> customers) {
                if(customers != null){
                    Log.v("TEST", "YOU SHOULD NOT WORK");
                    findFriendListAdapter.setFriendList(customers);
                }
            }
        });

        //Button click listener
        buttonFindFriend.setOnClickListener((View v) -> {
            userViewModel.refreshCustomerFindList(editFindFriend.getText().toString());
        });

        return view;
    }

}
