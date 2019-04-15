package com.example.wakeuptogether;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment {

    @BindView(R.id.button_login)
    Button button_login;
    @BindView(R.id.button_register)
    Button button_register;

    public Login() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        button_login.setOnClickListener((View v) -> {
            MainActivity.navController.navigate(R.id.action_login_to_main);
        });

        button_register.setOnClickListener((View v) -> {
            MainActivity.navController.navigate(R.id.action_login_to_register);
        });

        return view;
    }
}
