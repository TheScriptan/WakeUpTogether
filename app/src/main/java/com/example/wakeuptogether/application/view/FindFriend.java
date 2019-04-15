package com.example.wakeuptogether.application.view;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wakeuptogether.R;
import com.example.wakeuptogether.application.viewmodel.UserViewModel;
import com.example.wakeuptogether.business.model.Customer;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindFriend extends Fragment {

    private UserViewModel userViewModel;

    @BindView(R.id.label_friend_list) TextView labelFriendList;
    @BindView(R.id.edit_find_friend) EditText editFindFriend;
    @BindView(R.id.button_find_friend) Button buttonFindFriend;

    public FindFriend() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        userViewModel.findCustomer("").observe(this, new Observer<List<Customer>>() {
            @Override
            public void onChanged(List<Customer> customers) {
                if(customers != null){
                    String data = "";
                    for(Customer customer : customers){
                        String username = customer.getUsername();
                        String country = customer.getCountry();
                        data += "\nUsername: " + username
                                + "\nCountry: " + country;
                    }
                    labelFriendList.setText(data);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_friend, container, false);
        ButterKnife.bind(this, view);

        buttonFindFriend.setOnClickListener((View v) -> {
            userViewModel.findCustomer(editFindFriend.getText().toString());
        });
        return view;
    }

}
