package com.mobdeve.s15.group.nine.bookbay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ThriftStoreSellingBooksAdapter extends RecyclerView.Adapter<ThriftStoreViewHolder> {
    private ArrayList<Book> book;

    /* Variables to keep track of the current apps settings. Each variable has a setter and these
     * values must be set or else the Adapter will not be able to... adapt... appropriately.
     * */
    private Boolean hideLikeBtn;
    private int viewType;

    public ThriftStoreSellingBooksAdapter(ArrayList<Book> data) {
        this.book = data;
    }

    @Override
    public ThriftStoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the appropriate layout based on the viewType returned and generate the itemView.
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.thrift_and_selling_layout, parent, false);

        // Determine which subclass of CustomViewHolder to generate based on the viewType
        ThriftStoreViewHolder holder = new ThriftStoreViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(ThriftStoreViewHolder holder, int position) {
        holder.bindData(book.get(position));
    }

    @Override
    public int getItemCount() {
        return book.size();
    }
}
