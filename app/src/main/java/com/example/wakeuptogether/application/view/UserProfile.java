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
import android.widget.Toast;

import com.example.wakeuptogether.R;
import com.example.wakeuptogether.application.viewmodel.UserViewModel;
import com.example.wakeuptogether.business.model.Customer;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfile extends Fragment {


    private UserViewModel userViewModel;

    @BindView(R.id.label_user_profile)
    TextView labelProfile;

    public UserProfile() {
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
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ButterKnife.bind(this, view);

        Customer customer = userViewModel.getCurrentCustomer().getValue();
        String profile = "Username: " + customer.getUsername()
                + "\nCountry: " + customer.getCountry()
                + "\nBio: " + customer.getBio()
                + "\nStatus: " + customer.getStatus()
                + "\nStreaks: " + customer.getStreaks();
        labelProfile.setText(profile);
        return view;
    }

}
