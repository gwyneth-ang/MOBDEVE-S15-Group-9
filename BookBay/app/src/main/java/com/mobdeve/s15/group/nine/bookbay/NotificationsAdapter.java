package com.mobdeve.s15.group.nine.bookbay;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.mobdeve.s15.group.nine.bookbay.model.Notifications;

import org.jetbrains.annotations.NotNull;

public class NotificationsAdapter extends FirestoreRecyclerAdapter<Notifications, NotificationsViewHolder> {


    public NotificationsAdapter(FirestoreRecyclerOptions<Notifications> notifOptions) { super(notifOptions); }

    @Override
    public NotificationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.notifications_layout, parent, false);
        NotificationsViewHolder holder = new NotificationsViewHolder(itemView);

        return holder;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull NotificationsViewHolder holder, int position, @NonNull @NotNull Notifications model) {
        holder.bindData(model);
    }
}