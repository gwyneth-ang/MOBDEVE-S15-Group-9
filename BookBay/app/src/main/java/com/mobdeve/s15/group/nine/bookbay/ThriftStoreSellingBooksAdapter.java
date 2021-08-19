package com.mobdeve.s15.group.nine.bookbay;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ThriftStoreSellingBooksAdapter extends RecyclerView.Adapter<ThriftStoreSellingBooksViewHolder> {
    // FIXME: to be change to database
    private ArrayList<Book> book;

    private int whichView;

    // FIXME: to be change to database
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

        ThriftStoreSellingBooksViewHolder holder = new ThriftStoreSellingBooksViewHolder(itemView);

        // Determine which subclass of CustomViewHolder to generate based on the viewType
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), ViewBookingDetails.class);
                intent.putExtra(IntentKeys.AUTHOR_KEY.name(), book.get(holder.getAdapterPosition()).getAuthor());
                intent.putExtra(IntentKeys.CONDITION_KEY.name(), book.get(holder.getAdapterPosition()).getBookCondition().get(0));
                intent.putExtra(IntentKeys.TITLE_KEY.name(), book.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra(IntentKeys.PRICE_KEY.name(), book.get(holder.getAdapterPosition()).getBookPrice().get(0));
                intent.putExtra(IntentKeys.BOOK_IMAGE_KEY.name(), book.get(holder.getAdapterPosition()).getImageId());
                //TODO: Get book owner and owner image
                parent.getContext().startActivity(intent);

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ThriftStoreSellingBooksViewHolder holder, int position) {
        holder.bindData(book.get(position), whichView);
    }

    // FIXME: to be change to database
    @Override
    public int getItemCount() {
        return book.size();
    }
}
