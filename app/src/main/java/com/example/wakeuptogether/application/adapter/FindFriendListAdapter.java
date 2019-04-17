package com.example.wakeuptogether.application.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wakeuptogether.R;
import com.example.wakeuptogether.application.viewmodel.UserViewModel;
import com.example.wakeuptogether.business.model.Customer;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FindFriendListAdapter extends RecyclerView.Adapter<FindFriendListAdapter.ViewHolder> {

    //ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.text_username) TextView textUsername;
        @BindView(R.id.image_user) ImageView imageProfile;
        @BindView(R.id.button_friend_add) Button buttonFriendAdd;
        @BindView(R.id.text_country) TextView textCountry;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //FindFriendListAdapter
    private List<Customer> friendList;
    private UserViewModel userViewModel;

    public FindFriendListAdapter(List<Customer> friendList, UserViewModel userViewModel){
        this.friendList = friendList;
        this.userViewModel = userViewModel;
    }

    //Notifies recyclerview for data changes
    public void setFriendList(List<Customer> newFriendList){
        friendList.clear();
        friendList.addAll(newFriendList);
        notifyDataSetChanged();
    }

    //RecyclerView Stuff

    private boolean isPending;
    private boolean isAdded;
    private boolean shouldAdd;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_view_find_friend_item, parent, false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Todo: Optimize state changing because now it sucks

        Customer currentCustomer = friendList.get(position);
        String username = currentCustomer.getUsername();
        String country = currentCustomer.getCountry();
        holder.textUsername.setText(username);
        holder.textCountry.setText(country);

        //Checks if user exists in pendingFriendList and if so, change the name of the button and accept friend request
        if(userViewModel.getCurrentCustomer().getValue().getPendingFriends().contains(currentCustomer.getUid())){
            holder.buttonFriendAdd.setText("Accept");
            isPending = true;
        }
        //Check if user is looking for himself
        else if(userViewModel.getCurrentCustomer().getValue().getUid().equals(currentCustomer.getUid())){
            holder.buttonFriendAdd.setActivated(false);
            holder.buttonFriendAdd.setText("Love yourself");
        }
        //Check if user is in your friend list
        else if(userViewModel.getCurrentCustomer().getValue().getFriends().contains(currentCustomer.getUid())){
            isAdded = true;
            holder.buttonFriendAdd.setActivated(false);
            holder.buttonFriendAdd.setText("Added");
        } else {
            shouldAdd = true;
            holder.buttonFriendAdd.setText("Add");
        }

        //When Add button is pressed, button is disabled
        final boolean finalIsPending = isPending;
        final boolean finalIsAdded = isAdded;
        final boolean finalShouldAdd = shouldAdd;
        holder.buttonFriendAdd.setOnClickListener((View v) -> {
            if(finalShouldAdd){
                userViewModel.addPendingCustomer(currentCustomer.getUid());
                holder.buttonFriendAdd.setActivated(false);
                holder.buttonFriendAdd.setText("Added");
            }
            else if(finalIsPending){
                userViewModel.acceptPendingCustomer(currentCustomer.getUid());
                holder.buttonFriendAdd.setActivated(false);
                holder.buttonFriendAdd.setText("Added");
            } else if(finalIsAdded){
                holder.buttonFriendAdd.setActivated(false);
                holder.buttonFriendAdd.setText("Added");
            }

        } );
    }


    @Override
    public int getItemCount() {
        return friendList.size();
    }
}
