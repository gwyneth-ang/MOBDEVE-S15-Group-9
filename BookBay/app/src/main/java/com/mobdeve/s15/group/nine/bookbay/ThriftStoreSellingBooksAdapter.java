package com.mobdeve.s15.group.nine.bookbay;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s15.group.nine.bookbay.model.Books_sell;

import java.util.ArrayList;
import java.util.List;

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
                    intent.putExtra(IntentKeys.AUTHOR_KEY.name(), book.get(holder.getBindingAdapterPosition()).getBookAuthor());
                    intent.putExtra(IntentKeys.CONDITION_KEY.name(), book.get(holder.getBindingAdapterPosition()).getCondition());
                    intent.putExtra(IntentKeys.TITLE_KEY.name(), book.get(holder.getBindingAdapterPosition()).getBookTitle());
                    intent.putExtra(IntentKeys.PRICE_KEY.name(), book.get(holder.getBindingAdapterPosition()).getPrice());
                    intent.putExtra(IntentKeys.BOOK_IMAGE_KEY.name(), book.get(holder.getBindingAdapterPosition()).getImage());
                    intent.putExtra(IntentKeys.BOOK_ID_KEY.name(), book.get(holder.getBindingAdapterPosition()).getBooks_sellID().getId());
                    intent.putExtra(IntentKeys.OWNER_ID_KEY.name(), book.get(holder.getBindingAdapterPosition()).getOwnerID());
                    intent.putExtra(IntentKeys.OWNER_NAME_KEY.name(), book.get(holder.getBindingAdapterPosition()).getProfileName());
                    intent.putExtra(IntentKeys.OWNER_IMAGE_KEY.name(), book.get(holder.getBindingAdapterPosition()).getProfileImage());
                    intent.putExtra(IntentKeys.REVIEW_KEY.name(), book.get(holder.getBindingAdapterPosition()).getReview());
                    ((HomePageActivity)parent.getContext()).getMyActivityResultLauncher().launch(intent);
                }
            });
        }
        else if(this.whichView == WhichLayout.SELLING_BOOKS.ordinal()) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(parent.getContext(), SellingBooksDetails.class);
                    intent.putExtra(IntentKeys.AUTHOR_KEY.name(), book.get(holder.getBindingAdapterPosition()).getBookAuthor());
                    intent.putExtra(IntentKeys.CONDITION_KEY.name(), book.get(holder.getBindingAdapterPosition()).getCondition());
                    intent.putExtra(IntentKeys.TITLE_KEY.name(), book.get(holder.getBindingAdapterPosition()).getBookTitle());
                    intent.putExtra(IntentKeys.PRICE_KEY.name(), book.get(holder.getBindingAdapterPosition()).getPrice());
                    intent.putExtra(IntentKeys.BOOK_IMAGE_KEY.name(), book.get(holder.getBindingAdapterPosition()).getImage());
                    intent.putExtra(IntentKeys.BOOK_ID_KEY.name(), book.get(holder.getBindingAdapterPosition()).getBooks_sellID().getId());
                    intent.putExtra(IntentKeys.REVIEW_KEY.name(), book.get(holder.getBindingAdapterPosition()).getReview());
                    ((SellingBooksActivity)parent.getContext()).getActivityResultLauncher().launch(intent);
                }
            });
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(ThriftStoreSellingBooksViewHolder holder, int position) {
        holder.bindData(book.get(position), this.whichView);

        if (!book.get(position).getAvailable())
            holder.itemView.setClickable(false);
    }

    @Override
    public int getItemCount() {
        return book.size();
    }
}
