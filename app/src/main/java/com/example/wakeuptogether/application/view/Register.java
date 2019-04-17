package com.example.wakeuptogether.application.view;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.text.TextUtils;
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

import java.util.ArrayList;


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
            //Todo: Create static variables for minimum text length
            String username = "";
            String email = "";
            String password = "";
            String country = "";

            //No content provider so I will manually catch errors in the text fields
            if(TextUtils.isEmpty(editUsername.getText()) && editUsername.getText().length() > 6){
                username = editUsername.getText().toString();
            } else {
                Toast.makeText(getContext(), "Invalid username", Toast.LENGTH_SHORT).show();
            }

            if(TextUtils.isEmpty(editEmail.getText())){
                email = editEmail.getText().toString();
            } else {
                Toast.makeText(getContext(), "Invalid email", Toast.LENGTH_SHORT).show();
            }

            if(TextUtils.isEmpty(editPassword.getText()) && editPassword.length() > 5){
                password = editPassword.getText().toString();
            } else {
                Toast.makeText(getContext(), "Invalid email", Toast.LENGTH_SHORT).show();
            }

            //Todo: add countrypickdialog
            country = editCountry.getText().toString();
            Customer customer = new Customer(username,
                    country, "AWAKE", "Describe yourself",
                    -1, 0, new ArrayList<>(), new ArrayList<>());

            if(username.length() > 6 && password.length() > 5){
                userViewModel.register(email, password, customer);
            } else {
                Toast.makeText(getContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
            }

        });
        return view;
    }

}
