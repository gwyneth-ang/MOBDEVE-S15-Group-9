package com.mobdeve.s15.group.nine.bookbay;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class OrdersViewHolder extends RecyclerView.ViewHolder {
    private TextView tv_book_author, tv_book_title, tv_book_smallest_price, tv_book_status;
    private ImageView iv_book_image;

    public OrdersViewHolder(View view) {
        super(view);

        this.tv_book_author = view.findViewById(R.id.tv_book_author);
        this.tv_book_title = view.findViewById(R.id.tv_book_title);
        this.tv_book_smallest_price = view.findViewById(R.id.tv_book_smallest_price);
        this.tv_book_status = view.findViewById(R.id.tv_book_status);

        this.iv_book_image = view.findViewById(R.id.iv_book_image);
    }

    public void bindData(Books_sell book) {
        BookbayFirestoreReferences.downloadImageIntoImageView(book, this.iv_book_image);

        this.tv_book_author.setText(book.getBookAuthor());
        this.tv_book_title.setText(book.getBookTitle());

        if (book.getStatus().equals(BookStatus.DECLINED.name())) {

            this.tv_book_status.setText(BookStatus.DECLINED.name());
            this.tv_book_status.setBackgroundResource(R.drawable.declined_roundable_book_status);
            this.tv_book_status.setTextColor(Color.parseColor("#FFFFFFFF"));

        } else if (book.getStatus().equals(BookStatus.CONFIRMED.name())) {

            this.tv_book_status.setText(BookStatus.CONFIRMED.name());
            this.tv_book_status.setBackgroundResource(R.drawable.confirmed_roundable_book_status);
            this.tv_book_status.setTextColor(Color.parseColor("#FFFFFFFF"));

        } else if (book.getStatus().equals(BookStatus.PENDING.name())) {

            this.tv_book_status.setText(BookStatus.PENDING.name());
            this.tv_book_status.setBackgroundResource(R.drawable.pending_roundable_book_status);
            this.tv_book_status.setTextColor(Color.parseColor("#FF000000"));

        }
    }
}
