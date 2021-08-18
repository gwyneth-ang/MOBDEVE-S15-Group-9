package com.mobdeve.s15.group.nine.bookbay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsViewHolder> {

    private ArrayList<Notification> notifications;

    public NotificationsAdapter(ArrayList<Notification> data) {this.notifications = data;}

    @Override
    public NotificationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.notifications_layout, parent, false);
        NotificationsViewHolder holder = new NotificationsViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(NotificationsViewHolder holder, int position) {
        holder.bindData(notifications.get(position));
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }
}