package com.example.wakeuptogether.application.view;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wakeuptogether.R;
import com.example.wakeuptogether.application.NotificationHelper;
import com.example.wakeuptogether.application.adapter.AlarmFriendListAdapter;
import com.example.wakeuptogether.application.viewmodel.AlarmViewModel;
import com.example.wakeuptogether.application.viewmodel.StateViewModel;
import com.example.wakeuptogether.application.viewmodel.UserViewModel;
import com.example.wakeuptogether.business.model.Alarm;
import com.example.wakeuptogether.business.model.Customer;
import com.example.wakeuptogether.business.model.Time;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.wakeuptogether.R.drawable.sleep;
import static com.example.wakeuptogether.R.drawable.wake;


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
    private StateViewModel stateViewModel;
    private AlarmFriendListAdapter alarmFriendListAdapter;
    private Customer currentCustomer;
    private Alarm currentAlarm;

    public Main() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ViewModel
        userViewModel = ViewModelProviders.of(getActivity(), MainActivity.viewModelFactory).get(UserViewModel.class);
        alarmViewModel = ViewModelProviders.of(getActivity(), MainActivity.viewModelFactory).get(AlarmViewModel.class);
        stateViewModel = ViewModelProviders.of(getActivity()).get(StateViewModel.class);

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
        stateViewModel.setHasAlarm(!currentCustomer.getAlarmUid().equals("-1"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        //initialize floating action button text
        if(currentCustomer.getStatus().equals("AWAKE")){
            fabAdd.setImageResource(sleep);
        } else if(currentCustomer.getStatus().equals("SLEEPING")){
            fabAdd.setImageResource(wake);
        }


        //Setup recyclerview
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
            stateViewModel.setHasAlarm(true);
        });

        //Create FAB listener to notify when user went to sleep and when he woke up
        fabAdd.setOnClickListener((View v) -> {
            //Spam protection against the button for 10 seconds
            Toast.makeText(getContext(), "Long: " + stateViewModel.getLastTimeClicked(), Toast.LENGTH_SHORT).show();
            if(System.currentTimeMillis() >= stateViewModel.getLastTimeClicked() + 5000){
                //Update currentCustomer values
                currentCustomer = userViewModel.getCurrentCustomer().getValue();

                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                if(currentCustomer.getStatus().equals("AWAKE")){
                    //Set to sleep
                    userViewModel.setCustomerSleep(hour, minute);
                    Customer customer = userViewModel.getCurrentCustomer().getValue();
                    alarmFriendListAdapter.updateCustomer(customer);
                    fabAdd.setImageResource(wake);
                } else if(currentCustomer.getStatus().equals("SLEEPING")){
                    //Set to wake up
                    userViewModel.setCustomerWakeUp(hour, minute);
                    Customer customer = userViewModel.getCurrentCustomer().getValue();
                    alarmFriendListAdapter.updateCustomer(customer);
                    fabAdd.setImageResource(sleep);
                }
                stateViewModel.setLastTimeClicked(System.currentTimeMillis());
            }
        });

        //Disable all visibilities
        labelAlarmTime.setVisibility(View.GONE);
        buttonCreateAlarm.setVisibility(View.GONE);
        fabAdd.hide();
        rvAlarmFriendList.setVisibility(View.GONE);

        //Enable visibilities for state when currentAlarm has already been created
        stateViewModel.getHasAlarm().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean hasAlarm) {
                if(hasAlarm){
                    buttonCreateAlarm.setVisibility(View.GONE);
                    labelAlarmTime.setVisibility(View.VISIBLE);
                    fabAdd.show();
                    rvAlarmFriendList.setVisibility(View.VISIBLE);
                } else {
                    buttonCreateAlarm.setVisibility(View.VISIBLE);
                }
            }
        });

        //Alarm Listeners to track current time of currentAlarm
        alarmViewModel.listenForAlarm(currentCustomer.getAlarmUid());
        alarmViewModel.getAlarm().observe(this, new Observer<Alarm>() {
            @Override
            public void onChanged(Alarm alarm) {
                if(alarm != null){
                    //Check if currentAlarm was not removed
                    if(!alarm.getAlarmUid().equals("-1")){
                        labelAlarmTime.setText(alarm.getTime().toString());
                        stateViewModel.setHasAlarm(true);
                        //Todo: Extract AlarmManager from here to a better place
                        Calendar calendar = Calendar.getInstance();
                        int minute = alarm.getTime().getMinute();
                        int hour = alarm.getTime().getHour();
                        //Todo: Add +1 here to the day to create alarm for tomorrow
                        int day = calendar.get(Calendar.DATE);
                        int month = calendar.get(Calendar.MONTH);
                        int year = calendar.get(Calendar.YEAR);
                        Calendar time = new GregorianCalendar(year, month, day, hour, minute);
                        //Check if alarm time is in future compared to current time so notifications don't spam
                        if(time.getTimeInMillis() > calendar.getTimeInMillis()){

                            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                            long triggerTime = time.getTimeInMillis();
                            PendingIntent pd = NotificationHelper.getInstance(getContext()).getDeliverWakeUpPendingIntent();
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pd);
                        }

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
