package com.mobdeve.s15.group.nine.bookbay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersViewHolder>{
    // FIXME: to be change to database
    private ArrayList<Books_sell> book;

    private int whichView;

    // FIXME: to be change to database
    public OrdersAdapter(ArrayList<Books_sell> data) {
        this.book = data;
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
    public void onBindViewHolder(OrdersViewHolder holder, int position) {
        holder.bindData(book.get(position));
    }

    // FIXME: to be change to database
    @Override
    public int getItemCount() {
        return book.size();
    }
}
