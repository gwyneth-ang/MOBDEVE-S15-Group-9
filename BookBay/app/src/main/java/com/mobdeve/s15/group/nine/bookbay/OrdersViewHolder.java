package com.mobdeve.s15.group.nine.bookbay;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.Arrays;

public class OrdersViewHolder extends RecyclerView.ViewHolder {
    private TextView tv_book_author, tv_book_title, tv_book_smallest_price, tv_book_status, tv_buyer_name, tv_order_date;
    private ImageView iv_book_image, iv_buyer_image;
    private Spinner s_book_status;

    public OrdersViewHolder(View view) {
        super(view);

        this.tv_book_author = view.findViewById(R.id.tv_book_author);
        this.tv_book_title = view.findViewById(R.id.tv_book_title);
        this.tv_book_smallest_price = view.findViewById(R.id.tv_book_smallest_price);
        this.tv_book_status = view.findViewById(R.id.tv_book_status);
        this.s_book_status = view.findViewById(R.id.s_book_status);
        this.tv_buyer_name = view.findViewById(R.id.tv_buyer_name);
        this.tv_order_date = view.findViewById(R.id.tv_order_date);

        this.iv_book_image = view.findViewById(R.id.iv_book_image);
        this.iv_buyer_image = view.findViewById(R.id.iv_buyer_image);

    }

//    public void bindData(Books_sell book, int whichView) {
//        BookbayFirestoreReferences.downloadImageIntoImageView(book, this.iv_book_image);
//
//        this.tv_book_author.setText(book.getBookAuthor().toUpperCase());
//        this.tv_book_title.setText(book.getBookTitle());
//        this.tv_book_smallest_price.setText("₱" + String.valueOf(book.getPrice()) + " - " + book.getCondition());
//
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        CustomSpinner<String> adapter = new CustomSpinner(itemView.getContext(), android.R.layout.simple_spinner_item, Arrays.asList(itemView.getContext().getResources().getStringArray(R.array.book_status)), false);
//
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        this.s_book_status.setAdapter(adapter);
//        this.s_book_status.setSelection(0,false);
//        this.tv_order_date.setText(book.getOrderDate().toUpperCase());
//
//        // Profile
//        if (whichView == WhichLayout.MY_ORDERS.ordinal()) {
//
//            this.tv_buyer_name.setVisibility(View.GONE);
//            this.iv_buyer_image.setVisibility(View.GONE);
//
//            this.tv_order_date.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
//
//        } else if (whichView == WhichLayout.SELLING_ORDERS.ordinal()) {
//
//            this.tv_buyer_name.setVisibility(View.VISIBLE);
//            this.iv_buyer_image.setVisibility(View.VISIBLE);
//
//            this.tv_buyer_name.setText(book.getProfileName().toUpperCase());
//            Picasso.get().load(book.getProfileImage()).into(this.iv_book_image);
//
//            this.tv_order_date.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
//
//        }
//
//        // For Book Status
//        if (whichView == WhichLayout.MY_ORDERS.ordinal() && book.getStatus().equals(BookStatus.PENDING.name())) {
//
//            this.s_book_status.setVisibility(View.GONE);
//            this.tv_book_status.setVisibility(View.VISIBLE);
//            this.tv_book_status.setText(BookStatus.PENDING.name());
//            this.tv_book_status.setBackgroundResource(R.drawable.pending_roundable_book_status);
//            this.tv_book_status.setTextColor(Color.parseColor("#FF000000"));
//
//        } else if (whichView == WhichLayout.SELLING_ORDERS.ordinal() && book.getStatus().equals(BookStatus.PENDING.name())) {
//
//            this.s_book_status.setVisibility(View.VISIBLE);
//            this.tv_book_status.setVisibility(View.GONE);
//            this.s_book_status.setBackgroundResource(R.drawable.status_spinner_background);
//
//        }
//
//        if (book.getStatus().equals(BookStatus.DECLINED.name())) {
//
//            this.s_book_status.setVisibility(View.GONE);
//            this.tv_book_status.setVisibility(View.VISIBLE);
//            this.tv_book_status.setText(BookStatus.DECLINED.name());
//            this.tv_book_status.setBackgroundResource(R.drawable.declined_roundable_book_status);
//            this.tv_book_status.setTextColor(Color.parseColor("#FFFFFFFF"));
//
//        } else if (book.getStatus().equals(BookStatus.CONFIRMED.name())) {
//
//            this.s_book_status.setVisibility(View.GONE);
//            this.tv_book_status.setVisibility(View.VISIBLE);
//            this.tv_book_status.setText(BookStatus.CONFIRMED.name());
//            this.tv_book_status.setBackgroundResource(R.drawable.confirmed_roundable_book_status);
//            this.tv_book_status.setTextColor(Color.parseColor("#FFFFFFFF"));
//
//        }
//    }

    public void setItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        this.s_book_status.setOnItemSelectedListener(onItemSelectedListener);
    }


}
