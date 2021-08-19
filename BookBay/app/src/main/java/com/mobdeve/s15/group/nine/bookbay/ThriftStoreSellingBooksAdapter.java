package com.mobdeve.s15.group.nine.bookbay;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class ThriftStoreSellingBooksAdapter extends FirestoreRecyclerAdapter<Books_sell, ThriftStoreSellingBooksViewHolder> {

    private int whichView;
    private String TAG = "THRIFT_SELLING_ADAPTER";

    public ThriftStoreSellingBooksAdapter(FirestoreRecyclerOptions<Books_sell> options) {
        super(options);
    }

    public void setViewType(int whichView) {
        this.whichView = whichView;
    }

    @Override
    public ThriftStoreSellingBooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the appropriate layout based on the viewType returned and generate the itemView.
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.thrift_and_selling_layout, parent, false);

        ThriftStoreSellingBooksViewHolder holder = new ThriftStoreSellingBooksViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(ThriftStoreSellingBooksViewHolder holder, int position, Books_sell books_sell) {
        Log.d(TAG, books_sell.getBookTitle());
        holder.bindData(books_sell, this.whichView);
    }
}
