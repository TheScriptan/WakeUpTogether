package com.example.wakeuptogether.application.adapter;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wakeuptogether.R;
import com.example.wakeuptogether.application.view.FriendList;
import com.example.wakeuptogether.application.viewmodel.AlarmViewModel;
import com.example.wakeuptogether.application.viewmodel.UserViewModel;
import com.example.wakeuptogether.business.model.Customer;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {

    //ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.label_username) TextView textUsername;
        @BindView(R.id.userProfile) ImageView userProfile;
        @BindView(R.id.label_country_answer) TextView textCountryAnswer;
        @BindView(R.id.label_status_answer) TextView textStatusAnswer;
        @BindView(R.id.label_streaks_answer) TextView textStreakAnswer;
        @BindView(R.id.button_invite) Button buttonInvite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //FriendListAdapter
    private List<Customer> customerList;
    private UserViewModel userViewModel;
    private AlarmViewModel alarmViewModel;

    public FriendListAdapter(List<Customer> customerList, UserViewModel userViewModel, AlarmViewModel alarmViewModel){
        this.customerList = customerList;
        this.userViewModel = userViewModel;
        this.alarmViewModel = alarmViewModel;
    }

    public void setCustomerList(List<Customer> newCustomerList){
        customerList.clear();
        customerList.addAll(newCustomerList);
        notifyDataSetChanged();
    }

    //RecyclerView
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_friend_list, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Customer currentCustomer = customerList.get(position);
        String username = currentCustomer.getUsername();
        String country = currentCustomer.getCountry();
        String status = currentCustomer.getStatus();
        int streaks = currentCustomer.getStreaks();

        holder.textUsername.setText(username);
        holder.textCountryAnswer.setText(country);
        holder.textStatusAnswer.setText(status);
        holder.textStreakAnswer.setText("" + streaks);

        holder.itemView.setOnLongClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), holder.itemView);
            popup.inflate(R.menu.menu_friend_popup);

            popup.setOnMenuItemClickListener(item -> {
                switch(item.getItemId()){
                    case R.id.action_user_remove:
                        userViewModel.removeFriend(currentCustomer.getUid());
                        customerList.remove(position);
                        notifyItemRemoved(position);
                    return true;
                    default:
                        return false;
                }
            });
            popup.show();
            return true;
        });
        Customer authCustomer = userViewModel.getCurrentCustomer().getValue();

        //Check if alarm was created
        if(authCustomer.getAlarmUid().equals("-1")){
            holder.buttonInvite.setVisibility(View.GONE);
        } else {
            holder.buttonInvite.setVisibility(View.VISIBLE);
        }

        //Check if alarm was not created yet
        if(alarmViewModel.getAlarm().getValue() != null){
            //Check if user does not exist in alarm already
            List<String> alarmCustomers = alarmViewModel.getAlarm().getValue().getCustomers();
            if(alarmCustomers.contains(currentCustomer.getAlarmUid())){
                holder.buttonInvite.setText("Invited");
                holder.buttonInvite.setActivated(false);
            } else {
                holder.buttonInvite.setText("Invite");
                holder.buttonInvite.setActivated(true);
            }
        }

        holder.buttonInvite.setOnClickListener((View v) -> {
            Toast.makeText(v.getContext(), "Invited", Toast.LENGTH_SHORT).show();
            alarmViewModel.inviteCustomerToAlarm(alarmViewModel.getAlarm().getValue().getAlarmUid(), currentCustomer.getUid());
        });

    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }
}
