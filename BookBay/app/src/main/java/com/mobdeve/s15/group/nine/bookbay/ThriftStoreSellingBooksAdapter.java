package com.mobdeve.s15.group.nine.bookbay;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s15.group.nine.bookbay.model.Books_sell;

import java.util.ArrayList;

/**
 * Adapter for the Thrift Store and My Books (Selling Books) Recylerview
 */
public class ThriftStoreSellingBooksAdapter extends RecyclerView.Adapter<ThriftStoreSellingBooksViewHolder> {

    // Data
    private ArrayList<Books_sell> book;

    // Variables
    private int whichView;
    private String TAG = "THRIFT_SELLING_ADAPTER";

    // Constructor
    public ThriftStoreSellingBooksAdapter() {
        this.book = new ArrayList<>();
    }

    // Set view type (if thrift store or if my books
    public void setViewType(int whichView) {
        this.whichView = whichView;
    }

    // Set data/books
    public void setData(ArrayList<Books_sell> data) {
        this.book = data;
    }

    @Override
    public ThriftStoreSellingBooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.thrift_and_selling_layout, parent, false);

        ThriftStoreSellingBooksViewHolder holder = new ThriftStoreSellingBooksViewHolder(itemView);

        // Pass the intents to the view book details activity for viewing the specific book details
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
        } // Pass the intents to the selling book details activity for viewing the specific book details
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
                    ((HomePageActivity)parent.getContext()).getSetSellingSearchLauncher().launch(intent);
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
