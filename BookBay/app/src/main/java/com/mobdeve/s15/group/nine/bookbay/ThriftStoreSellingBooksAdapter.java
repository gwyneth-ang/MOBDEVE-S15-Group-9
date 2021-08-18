package com.mobdeve.s15.group.nine.bookbay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ThriftStoreSellingBooksAdapter extends RecyclerView.Adapter<ThriftStoreSellingBooksViewHolder> {
    private ArrayList<Book> book;

    private int whichView;

    public ThriftStoreSellingBooksAdapter(ArrayList<Book> data) {
        this.book = data;
    }

    public void setViewType(int whichView) {
        this.whichView = whichView;
    }

    @Override
    public ThriftStoreSellingBooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the appropriate layout based on the viewType returned and generate the itemView.
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.thrift_and_selling_layout, parent, false);

        // Determine which subclass of CustomViewHolder to generate based on the viewType
        ThriftStoreSellingBooksViewHolder holder = new ThriftStoreSellingBooksViewHolder(itemView);

        return holder;
    }


    @Override
    public void onBindViewHolder(ThriftStoreSellingBooksViewHolder holder, int position) {
        holder.bindData(book.get(position), whichView);
    }

    @Override
    public int getItemCount() {
        return book.size();
    }
}
