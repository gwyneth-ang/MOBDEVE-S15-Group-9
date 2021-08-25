package com.mobdeve.s15.group.nine.bookbay;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s15.group.nine.bookbay.model.Books_sell;

import java.util.ArrayList;

public class ThriftStoreSellingBooksAdapter extends RecyclerView.Adapter<ThriftStoreSellingBooksViewHolder> {
    private ArrayList<Books_sell> book;

    private int whichView;
    private String TAG = "THRIFT_SELLING_ADAPTER";

    public ThriftStoreSellingBooksAdapter() {
        this.book = new ArrayList<>();
    }

    public void setViewType(int whichView) {
        this.whichView = whichView;
    }

    public void setData(ArrayList<Books_sell> data) {
        this.book = data;
    }

    @Override
    public ThriftStoreSellingBooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the appropriate layout based on the viewType returned and generate the itemView.
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.thrift_and_selling_layout, parent, false);

        ThriftStoreSellingBooksViewHolder holder = new ThriftStoreSellingBooksViewHolder(itemView);

        // Determine which subclass of CustomViewHolder to generate based on the viewType
        if(this.whichView == WhichLayout.THRIFT_STORE.ordinal()) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(parent.getContext(), ViewBookingDetails.class);
                    intent.putExtra(IntentKeys.AUTHOR_KEY.name(), book.get(holder.getAdapterPosition()).getBookAuthor());
                    intent.putExtra(IntentKeys.CONDITION_KEY.name(), book.get(holder.getAdapterPosition()).getCondition());
                    intent.putExtra(IntentKeys.TITLE_KEY.name(), book.get(holder.getAdapterPosition()).getBookTitle());
                    intent.putExtra(IntentKeys.PRICE_KEY.name(), book.get(holder.getAdapterPosition()).getPrice());
                    intent.putExtra(IntentKeys.BOOK_IMAGE_KEY.name(), book.get(holder.getAdapterPosition()).getImage());
                    intent.putExtra(IntentKeys.BOOK_ID_KEY.name(), book.get(holder.getAdapterPosition()).getBooks_sellID().getId());
                    intent.putExtra(IntentKeys.OWNER_ID_KEY.name(), book.get(holder.getAdapterPosition()).getOwnerID());
                    intent.putExtra(IntentKeys.OWNER_NAME_KEY.name(), book.get(holder.getAdapterPosition()).getProfileName());
                    intent.putExtra(IntentKeys.OWNER_IMAGE_KEY.name(), book.get(holder.getAdapterPosition()).getProfileImage());
                    intent.putExtra(IntentKeys.REVIEW_KEY.name(), book.get(holder.getAdapterPosition()).getReview());
                    parent.getContext().startActivity(intent);
                }
            });
        }
        else if(this.whichView == WhichLayout.SELLING_BOOKS.ordinal()) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(parent.getContext(), SellingBooksDetails.class);
                    intent.putExtra(IntentKeys.AUTHOR_KEY.name(), book.get(holder.getAdapterPosition()).getBookAuthor());
                    intent.putExtra(IntentKeys.CONDITION_KEY.name(), book.get(holder.getAdapterPosition()).getCondition());
                    intent.putExtra(IntentKeys.TITLE_KEY.name(), book.get(holder.getAdapterPosition()).getBookTitle());
                    intent.putExtra(IntentKeys.PRICE_KEY.name(), book.get(holder.getAdapterPosition()).getPrice());
                    intent.putExtra(IntentKeys.BOOK_IMAGE_KEY.name(), book.get(holder.getAdapterPosition()).getImage());
                    intent.putExtra(IntentKeys.BOOK_ID_KEY.name(), book.get(holder.getAdapterPosition()).getBooks_sellID().getId());
                    intent.putExtra(IntentKeys.REVIEW_KEY.name(), book.get(holder.getAbsoluteAdapterPosition()).getReview());
                    parent.getContext().startActivity(intent);
                }
            });
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(ThriftStoreSellingBooksViewHolder holder, int position) {
//        Log.d(TAG, book.get().getBookTitle());
        holder.bindData(book.get(position), this.whichView);

        if (!book.get(position).getAvailable())
            holder.itemView.setClickable(false);
    }

    @Override
    public int getItemCount() {
        return book.size();
    }
}
