package com.example.wakeuptogether.application.view;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wakeuptogether.R;
import com.example.wakeuptogether.application.viewmodel.UserViewModel;
import com.example.wakeuptogether.business.model.Customer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendList extends Fragment {

    private UserViewModel userViewModel;
    private Customer customer;

    @BindView(R.id.label_friend_list)
    TextView labelFriendList;
    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;

    public FriendList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        customer = userViewModel.getCurrentCustomer().getValue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        ButterKnife.bind(this, view);
//        String friends = "";
//        if(customer.getPendingFriends() != null){
//            for(String friend : customer.getPendingFriends()){
//                friends += friend;
//            }
//        } else {
//            friends = "No friends :^(";
//        }
//        labelFriendList.setText(friends);

        return view;
    }

}
