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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wakeuptogether.R;
import com.example.wakeuptogether.application.viewmodel.UserViewModel;
import com.example.wakeuptogether.business.firebase.FirebaseAuthHelper;
import com.example.wakeuptogether.business.model.Customer;


/**
 * A simple {@link Fragment} subclass.
 */
public class Register extends Fragment {

    private UserViewModel userViewModel;

    @BindView(R.id.button_register)
    Button button_register;
    @BindView(R.id.edit_username)
    EditText editUsername;
    @BindView(R.id.edit_email)
    EditText editEmail;
    @BindView(R.id.edit_password)
    EditText editPassword;
    @BindView(R.id.edit_country)
    EditText editCountry;

    public Register() {
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);

        button_register.setOnClickListener((View v) -> {
            String username = editUsername.getText().toString();
            String email = editEmail.getText().toString();
            String password = editPassword.getText().toString();
            String country = editCountry.getText().toString();
            Customer customer = new Customer(username, country, "AWAKE", "Describe yourself", -1, 0, null, null);

            userViewModel.register(email, password, customer);
        });
        return view;
    }

}
