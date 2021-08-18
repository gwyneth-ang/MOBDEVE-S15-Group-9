package com.mobdeve.s15.group.nine.bookbay;

import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import java.util.ArrayList;

public class NotificationsAdapter extends AppCompatActivity {

    private ArrayList<Notification> notifications;

    public NotificationsAdapter(ArrayList<Notification> data) {this.notifications = data;}

    public NotificationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.notifications_layout, parent, false);
        NotificationsViewHolder holder = new NotificationsViewHolder(itemView);

        return holder;
    }
}