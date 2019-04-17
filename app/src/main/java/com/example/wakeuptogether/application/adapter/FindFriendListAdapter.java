package com.example.wakeuptogether.application.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wakeuptogether.R;
import com.example.wakeuptogether.application.viewmodel.UserViewModel;
import com.example.wakeuptogether.business.model.Customer;

import java.util.ArrayList;
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

    public FindFriendListAdapter(UserViewModel userViewModel){
        friendList = new ArrayList<>();
        this.userViewModel = userViewModel;
    }

    //Notifies recyclerview for data changes
    public void setFriendList(List<Customer> newFriendList){
        friendList.clear();
        friendList = newFriendList;
        notifyDataSetChanged();
    }

    //RecyclerView Stuff
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_view_find_friend_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Customer currentCustomer = friendList.get(position);
        String username = currentCustomer.getUsername();
        String country = currentCustomer.getCountry();

        holder.textUsername.setText(username);
        holder.textCountry.setText(country);

        //Checks if user was already added and if so, disables Add button
        if(userViewModel.getCurrentCustomer().getValue().getPendingFriends().contains(currentCustomer.getUid())){
            holder.buttonFriendAdd.setActivated(false);
            holder.buttonFriendAdd.setText("Added");
        }

        //When Add button is pressed, button is disabled
        holder.buttonFriendAdd.setOnClickListener((View v) -> {
            userViewModel.addPendingFriend(userViewModel.getCurrentCustomer().getValue().getUid(), currentCustomer.getUid());
            holder.buttonFriendAdd.setActivated(false);
            holder.buttonFriendAdd.setText("Added");
        } );
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }
}
