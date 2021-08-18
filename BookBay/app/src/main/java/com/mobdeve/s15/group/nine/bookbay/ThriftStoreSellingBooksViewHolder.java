package com.mobdeve.s15.group.nine.bookbay;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ThriftStoreSellingBooksViewHolder extends RecyclerView.ViewHolder {

    private TextView tv_book_author, tv_book_title, tv_book_smallest_price;
    private ImageView iv_book_image;

    public ThriftStoreSellingBooksViewHolder(View view) {
        super(view);

        this.tv_book_author = view.findViewById(R.id.tv_book_author);
        this.tv_book_title = view.findViewById(R.id.tv_book_title);
        this.tv_book_smallest_price = view.findViewById(R.id.tv_book_smallest_price);

        this.iv_book_image = view.findViewById(R.id.iv_book_image);
    }

    public void bindData(Book book, int whichLayout) {
        this.iv_book_image.setImageResource(book.getImageId());

        this.tv_book_author.setText(book.getAuthor());
        this.tv_book_title.setText(book.getTitle());

        if (whichLayout == WhichLayout.THRIFT_STORE.ordinal()) {
            this.tv_book_smallest_price.setVisibility(View.GONE);
        } else if (whichLayout == WhichLayout.SELLING_BOOKS.ordinal()) {
            this.tv_book_smallest_price.setVisibility(View.VISIBLE);
            this.tv_book_smallest_price.setText(String.valueOf(book.getBookPrice().get(0)));
        }
    }
}
