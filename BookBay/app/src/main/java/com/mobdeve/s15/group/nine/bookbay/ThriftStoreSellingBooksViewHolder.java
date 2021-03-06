package com.mobdeve.s15.group.nine.bookbay;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreReferences;
import com.mobdeve.s15.group.nine.bookbay.model.Books_sell;

import java.text.DecimalFormat;

/**
 * ViewHolder for the Thrift Store and Selling Books (My Books) Recylerview
 */
public class ThriftStoreSellingBooksViewHolder extends RecyclerView.ViewHolder {

    private TextView tv_book_author, tv_book_title, tv_book_smallest_price;
    private ImageView iv_book_image;

    public ThriftStoreSellingBooksViewHolder(View view) {
        super(view);

        // View initialization
        this.tv_book_author = view.findViewById(R.id.tv_book_author);
        this.tv_book_title = view.findViewById(R.id.tv_book_title);
        this.tv_book_smallest_price = view.findViewById(R.id.tv_book_smallest_price);

        this.iv_book_image = view.findViewById(R.id.iv_book_image);
    }

    public void bindData(Books_sell book, int whichLayout) {
        BookbayFirestoreReferences.downloadImageIntoImageView(book, this.iv_book_image);


        this.tv_book_author.setText(book.getBookAuthor().toUpperCase());
        this.tv_book_title.setText(book.getBookTitle());

        // If the view is My Books (Selling Books)
        if (whichLayout == WhichLayout.SELLING_BOOKS.ordinal()) {
            this.tv_book_smallest_price.setVisibility(View.GONE);
        // If the view is the thrift store
        } else if (whichLayout == WhichLayout.THRIFT_STORE.ordinal()) {
            DecimalFormat decimalFormat = new DecimalFormat("#.00");

            this.tv_book_smallest_price.setVisibility(View.VISIBLE);
            this.tv_book_smallest_price.setText("₱" + decimalFormat.format(book.getPrice()) + " - " + book.getCondition());
        }
    }
}
