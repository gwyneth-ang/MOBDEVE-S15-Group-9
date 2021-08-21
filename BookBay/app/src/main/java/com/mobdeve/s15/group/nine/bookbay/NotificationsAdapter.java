package com.mobdeve.s15.group.nine.bookbay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NotificationsAdapter extends FirestoreRecyclerAdapter<Books_sell, NotificationsViewHolder> {


    public NotificationsAdapter(FirestoreRecyclerOptions<Books_sell> notifOptions) { super(notifOptions); }

    @Override
    public NotificationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.notifications_layout, parent, false);
        NotificationsViewHolder holder = new NotificationsViewHolder(itemView);

        return holder;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull NotificationsViewHolder holder, int position, @NonNull @NotNull Books_sell model) {
        holder.bindData(model);
    }
}