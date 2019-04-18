package com.example.wakeuptogether.application.view;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wakeuptogether.R;
import com.example.wakeuptogether.application.adapter.AlarmFriendListAdapter;
import com.example.wakeuptogether.application.viewmodel.AlarmViewModel;
import com.example.wakeuptogether.application.viewmodel.UserViewModel;
import com.example.wakeuptogether.business.model.Alarm;
import com.example.wakeuptogether.business.model.Customer;
import com.example.wakeuptogether.business.model.Time;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Main extends Fragment {

    @BindView(R.id.label_alarm_time) TextView labelAlarmTime;
    @BindView(R.id.button_create_alarm) Button buttonCreateAlarm;
    @BindView(R.id.fab_add) FloatingActionButton fabAdd;
    @BindView(R.id.rv_alarm_friend_list) RecyclerView rvAlarmFriendList;

    private UserViewModel userViewModel;
    private AlarmViewModel alarmViewModel;
    private AlarmFriendListAdapter alarmFriendListAdapter;
    private Customer currentCustomer;
    private Alarm currentAlarm;

    private boolean hasAlarm;

    public Main() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ViewModel
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        alarmViewModel = ViewModelProviders.of(this).get(AlarmViewModel.class);

        //Recyclerview
        ArrayList<Customer> customerList = new ArrayList<>();
        alarmFriendListAdapter = new AlarmFriendListAdapter(customerList, userViewModel, alarmViewModel);

        //Current values of customer and alarm
        currentCustomer = userViewModel.getCurrentCustomer().getValue();
        currentAlarm = alarmViewModel.getAlarm().getValue();

        //Check if alarm has a list of customers
        if(currentAlarm != null){
            if(currentAlarm.getCustomers() != null) {
                alarmViewModel.refreshAlarmCustomerList(currentAlarm.getCustomers());
            }
        }

        //Check if there is an alarm
        hasAlarm = !currentCustomer.getAlarmUid().equals("-1");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        rvAlarmFriendList.setHasFixedSize(true);
        rvAlarmFriendList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAlarmFriendList.setAdapter(alarmFriendListAdapter);

        //Label Alarm Time listener to change time
        labelAlarmTime.setOnClickListener((View v) -> {
            DialogFragment timePicker = new DialogTimePicker();
            timePicker.show(getFragmentManager(), "Time Picker");
        });

        //Create Alarm Listener to create currentAlarm
        buttonCreateAlarm.setOnClickListener((View v) -> {
            int hour = Calendar.HOUR_OF_DAY;
            int minute = Calendar.MINUTE;
            alarmViewModel.addAlarm(new Time(hour, minute));
            buttonCreateAlarm.setVisibility(View.GONE);
            labelAlarmTime.setText(new Time(hour, minute).toString());
            labelAlarmTime.setVisibility(View.VISIBLE);
            fabAdd.show();
            hasAlarm = true;
        });

        //Disable all visibilities
        labelAlarmTime.setVisibility(View.GONE);
        buttonCreateAlarm.setVisibility(View.GONE);
        fabAdd.hide();
        rvAlarmFriendList.setVisibility(View.GONE);

        //Enable visibilities for state when currentAlarm has already been created
        if(hasAlarm){
            buttonCreateAlarm.setVisibility(View.GONE);
            labelAlarmTime.setVisibility(View.VISIBLE);
            fabAdd.show();
            rvAlarmFriendList.setVisibility(View.VISIBLE);
        }
        //Enable visibilities when currentAlarm has not been created
        else {
            buttonCreateAlarm.setVisibility(View.VISIBLE);
        }

        //Alarm Listeners to track current time of currentAlarm
        alarmViewModel.listenForAlarm(currentCustomer.getAlarmUid());
        alarmViewModel.getAlarm().observe(this, new Observer<Alarm>() {
            @Override
            public void onChanged(Alarm alarm) {
                if(alarm != null){
                    //Check if currentAlarm was not removed
                    if(!alarm.getAlarmUid().equals("-1")){
                        labelAlarmTime.setText(alarm.getTime().toString());
                        hasAlarm = true;
                    }
                } else {
                    labelAlarmTime.setVisibility(View.GONE);
                    rvAlarmFriendList.setVisibility(View.GONE);
                    fabAdd.hide();
                    buttonCreateAlarm.setVisibility(View.VISIBLE);

                }
            }
        });

        //Invited friends to currentAlarm listener
        alarmViewModel.getAlarmCustomerList().observe(this, new Observer<List<Customer>>() {
            @Override
            public void onChanged(List<Customer> customerList) {
                if(customerList != null){
                    alarmFriendListAdapter.setCustomerList(customerList);
                }
            }
        });

        return view;
    }

}
