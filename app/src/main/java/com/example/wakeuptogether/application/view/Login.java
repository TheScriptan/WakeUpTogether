package com.example.wakeuptogether.application.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
public class Login extends Fragment {

    private UserViewModel userViewModel;

    @BindView(R.id.button_login)
    Button button_login;
    @BindView(R.id.button_register)
    Button button_register;
    @BindView(R.id.edit_email)
    EditText editEmail;
    @BindView(R.id.edit_password)
    EditText editPassword;

    public Login() {
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        button_login.setOnClickListener((View v) -> {
            //MainActivity.navController.navigate(R.id.action_login_to_main);
            String email = editEmail.getText().toString();
            String password = editPassword.getText().toString();
            userViewModel.signIn(email, password);
        });

        button_register.setOnClickListener((View v) -> {
            MainActivity.navController.navigate(R.id.action_login_to_register);
        });

        return view;
    }
}
