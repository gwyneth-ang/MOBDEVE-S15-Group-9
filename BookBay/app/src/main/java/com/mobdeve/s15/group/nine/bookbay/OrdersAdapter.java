package com.mobdeve.s15.group.nine.bookbay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class OrdersAdapter extends FirestoreRecyclerAdapter<Books_sell, OrdersViewHolder> {

    private int whichView;

    public OrdersAdapter(FirestoreRecyclerOptions<Books_sell> options) {
        super(options);
    }

    public void setViewType(int whichView) {
        this.whichView = whichView;
    }

    @Override
    public OrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the appropriate layout based on the viewType returned and generate the itemView.
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.view_my_selling_orders_layout, parent, false);

        OrdersViewHolder holder = new OrdersViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(OrdersViewHolder holder, int position, Books_sell book) {
        holder.bindData(book);
    }
}
