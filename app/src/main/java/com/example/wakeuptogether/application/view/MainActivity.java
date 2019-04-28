package com.example.wakeuptogether.application.view;

import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.wakeuptogether.R;
import com.example.wakeuptogether.application.NotificationHelper;
import com.example.wakeuptogether.application.WakeUpReceiver;
import com.example.wakeuptogether.application.viewmodel.AlarmViewModel;
import com.example.wakeuptogether.application.viewmodel.StateViewModel;
import com.example.wakeuptogether.application.viewmodel.UserViewModel;
import com.example.wakeuptogether.application.viewmodel.ViewModelFactory;
import com.example.wakeuptogether.business.model.Customer;
import com.example.wakeuptogether.utils.InjectorUtils;
import com.example.wakeuptogether.utils.PreferenceUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private NotificationHelper notificationHelper;
    private PreferenceUtils preferenceUtils;

    public static NavController navController;
    public static ViewModelFactory viewModelFactory;
    private AppBarConfiguration appBarConfiguration;

    private UserViewModel userViewModel;
    private AlarmViewModel alarmViewModel;
    private StateViewModel stateViewModel;

    private MenuItem logoutItem;
    private MenuItem findFriendItem;
    private MenuItem leaveAlarmItem;

    private WakeUpReceiver wakeUpReceiver;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.bottomNavigationView) BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Setup NotificationHelper
        notificationHelper = NotificationHelper.getInstance(this);

        //Setup viewmodels
        viewModelFactory = InjectorUtils.provideViewModelFactory(getApplicationContext());
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        alarmViewModel = ViewModelProviders.of(this, viewModelFactory).get(AlarmViewModel.class);
        stateViewModel = ViewModelProviders.of(this).get(StateViewModel.class);

        //Setup receivers
        wakeUpReceiver = new WakeUpReceiver();
        registerReceiver(wakeUpReceiver, new IntentFilter(NotificationHelper.ACTION_WAKE_UP));

        //Setup PreferenceUtilsHelper
        preferenceUtils = PreferenceUtils.getInstance(this);


        //Setup toolbar
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        //Setup navController
        navController = Navigation.findNavController(this, R.id.nav_fragment);
        navController.addOnDestinationChangedListener(navDestinationListener());
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.main, R.id.login, R.id.friendList, R.id.userProfile).build();
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


        if(!preferenceUtils.getEmail().equals("") && !preferenceUtils.getPassword().equals("")){
            userViewModel.signIn(preferenceUtils.getEmail(), preferenceUtils.getPassword());
        }

        userViewModel.getCurrentCustomer().observe(this, new Observer<Customer>() {
            @Override
            public void onChanged(Customer customer) {
                if(customer != null){
                    if(navController.getCurrentDestination().getId() == R.id.login){
                        navController.navigate(R.id.action_login_to_main);
                    } else if(navController.getCurrentDestination().getId() == R.id.register){
                        navController.navigate(R.id.action_register_to_nav_bottom);
                    }
                }
            }
        });

    }

    private NavController.OnDestinationChangedListener navDestinationListener(){
        return new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() == R.id.main){
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    stateViewModel.setAuth(true);
                    stateViewModel.setFindFriend(false);
                    //Check if user has alarmUid, if so then enable Leave Alarm menu item
                    if(userViewModel.getCurrentCustomer().getValue() != null){
                        if(!userViewModel.getCurrentCustomer().getValue().getAlarmUid().equals("-1"))
                            stateViewModel.setCanLeaveAlarm(true);
                    }
                } else if(destination.getId() == R.id.login){
                    stateViewModel.setAuth(false);
                    stateViewModel.setCanLeaveAlarm(false);
                    bottomNavigationView.setVisibility(View.INVISIBLE);
                } else if(destination.getId() == R.id.register){
                    stateViewModel.setAuth(false);
                    stateViewModel.setCanLeaveAlarm(false);
                    bottomNavigationView.setVisibility(View.INVISIBLE);
                } else if(destination.getId() == R.id.findFriend){
                    stateViewModel.setFindFriend(true);
                    stateViewModel.setCanLeaveAlarm(false);
                } else {
                    stateViewModel.setCanLeaveAlarm(false);
                }


            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        logoutItem = menu.findItem(R.id.action_logout);
        findFriendItem = menu.findItem(R.id.action_find_friend);
        leaveAlarmItem = menu.findItem(R.id.action_leave_alarm);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        invalidateOptionsMenu();
        if(stateViewModel.isAuth()){
            logoutItem.setVisible(true);
            findFriendItem.setVisible(true);
        } else {
            logoutItem.setVisible(false);
            findFriendItem.setVisible(false);
        }

        if(stateViewModel.isFindFriend()){
            findFriendItem.setVisible(false);
        } else if(stateViewModel.isAuth()) {
            findFriendItem.setVisible(true);
        }

        if(stateViewModel.isCanLeaveAlarm()){
            leaveAlarmItem.setVisible(true);
        } else {
            leaveAlarmItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_logout:
                navController.navigate(R.id.action_logout);
                stateViewModel.setAuth(false);
                userViewModel.signOut();
                return true;
            case R.id.action_find_friend:
                    navController.navigate(R.id.action_global_findFriend);
                    stateViewModel.setFindFriend(true);
                    return true;
            case R.id.action_leave_alarm:
                alarmViewModel.leaveAlarm(userViewModel.getCurrentCustomer().getValue().getAlarmUid());
                stateViewModel.setCanLeaveAlarm(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //userViewModel.signOut();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userViewModel.signOut();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp()|| super.onSupportNavigateUp();
    }
}
