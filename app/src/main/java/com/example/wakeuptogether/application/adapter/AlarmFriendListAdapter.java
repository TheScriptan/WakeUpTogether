package com.example.wakeuptogether.application.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wakeuptogether.R;
import com.example.wakeuptogether.application.viewmodel.AlarmViewModel;
import com.example.wakeuptogether.application.viewmodel.UserViewModel;
import com.example.wakeuptogether.business.model.Customer;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmFriendListAdapter extends RecyclerView.Adapter<AlarmFriendListAdapter.ViewHolder> {

    //Viewholder
    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.userProfile)
        ImageView imageUserProfile;
        @BindView(R.id.label_username)
        TextView labelUsername;
        @BindView(R.id.sleep_start_answer)
        TextView sleepStartAnswer;
        @BindView(R.id.sleep_wake_answer)
        TextView sleepWakeAnswer;
        @BindView(R.id.status_answer)
        TextView statusAnswer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //AlarmFriendListAdapter

    private List<Customer> customerList;
    private UserViewModel userViewModel;
    private AlarmViewModel alarmViewModel;

    public AlarmFriendListAdapter(List<Customer> customerList, UserViewModel userViewModel, AlarmViewModel alarmViewModel){
        this.customerList = customerList;
        this.userViewModel = userViewModel;
        this.alarmViewModel = alarmViewModel;
    }

    public void setCustomerList(List<Customer> customerList){
        this.customerList.clear();
        this.customerList.addAll(customerList);
        notifyDataSetChanged();
    }


    //RecyclerView
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_alarm_friend_list, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        String username = customer.getUsername();
        String sleepAt = "21:21";
        String wakeAt = "06:05";
        String status = customer.getStatus();

        if(customer.getUsername().equals(userViewModel.getCurrentCustomer().getValue().getUsername()))
            holder.labelUsername.setText("You");
        else {
            holder.labelUsername.setText(username);
        }
        holder.sleepStartAnswer.setText(sleepAt);
        holder.sleepWakeAnswer.setText(wakeAt);
        holder.statusAnswer.setText(status);
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }
}
