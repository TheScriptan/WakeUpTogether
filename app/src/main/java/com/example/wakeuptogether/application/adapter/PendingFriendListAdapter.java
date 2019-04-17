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

public class PendingFriendListAdapter extends RecyclerView.Adapter<PendingFriendListAdapter.ViewHolder> {

    //ViewHolder

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.image_user) ImageView userImage;
        @BindView(R.id.text_username) TextView textUsername;
        @BindView(R.id.button_friend_accept) Button buttonFriendAccept;
        @BindView(R.id.button_refuse_friend) Button buttonFriendRefuse;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //PendingFriendListAdapter
    private UserViewModel userViewModel;
    private List<Customer> pendingFriendList;

    public PendingFriendListAdapter(List<Customer> pendingFriendList, UserViewModel userViewModel){
        this.userViewModel = userViewModel;
        this.pendingFriendList = pendingFriendList;
    }

    public void setPendingFriendList(List<Customer> pendingFriendList) {
        this.pendingFriendList.clear();
        this.pendingFriendList.addAll(pendingFriendList);
        notifyDataSetChanged();
    }

    //RecyclerView

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_view_pending_friend_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Customer currentCustomer = pendingFriendList.get(position);
        String username = currentCustomer.getUsername();

        holder.textUsername.setText(username);

        //When user accepts friend update database
        holder.buttonFriendAccept.setOnClickListener((View v) -> {
            userViewModel.acceptPendingCustomer(currentCustomer.getUid());
            pendingFriendList.remove(position);
            notifyItemRemoved(position);
        });

        //When user refuses friend request update database
        holder.buttonFriendRefuse.setOnClickListener((View v) -> {
            userViewModel.refusePendingCustomer(currentCustomer.getUid());
            pendingFriendList.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return pendingFriendList.size();
    }
}
