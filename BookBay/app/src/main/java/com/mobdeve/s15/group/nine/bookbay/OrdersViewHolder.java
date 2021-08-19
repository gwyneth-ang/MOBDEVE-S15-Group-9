package com.mobdeve.s15.group.nine.bookbay;

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

    public void bindData(Orders order) {
//        this.iv_book_image.setImageResource(book.getImageId());

        this.tv_book_author.setText(book.getBookAuthor());
        this.tv_book_title.setText(book.getBookTitle());

        //FIXME: fix based on database
//        if (book.getStatus().equals("DECLINED")) {
//
//        }
    }
}
