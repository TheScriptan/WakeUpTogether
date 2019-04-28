package com.example.wakeuptogether.application.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.wakeuptogether.R;
import com.example.wakeuptogether.application.viewmodel.UserViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfile extends Fragment {


    private UserViewModel userViewModel;

    @BindView(R.id.profile_username) TextView profileUsername;
    @BindView(R.id.profile_country) TextView profileCountry;
    @BindView(R.id.profile_country_image) ImageView profileImage;
    @BindView(R.id.profile_status) TextView profileStatus;
    @BindView(R.id.profile_streaks) TextView profileStreaks;

    public UserProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = ViewModelProviders.of(getActivity(), MainActivity.viewModelFactory).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ButterKnife.bind(this, view);

        userViewModel.getCurrentCustomer().observe(this, customer -> {
            if(customer != null){
                String username = customer.getUsername();
                String country = customer.getCountry();
                String bio = customer.getBio();
                String status = customer.getStatus();
                String streaks = "" + customer.getStreaks();
                profileUsername.setText(username);
                profileCountry.setText(country);
                profileStatus.setText(status);
                profileStreaks.setText(streaks);
                int countryImageId = getContext().getResources().getIdentifier("flag_" + country.toLowerCase(), "drawable", getContext().getPackageName());
                profileImage.setImageDrawable(getContext().getDrawable(countryImageId));
            }
        });


        return view;
    }

}
