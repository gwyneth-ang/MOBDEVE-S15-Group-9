package com.mobdeve.s15.group.nine.bookbay;

import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreReferences;
import com.mobdeve.s15.group.nine.bookbay.model.Books_sell;
import com.mobdeve.s15.group.nine.bookbay.model.Orders;
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

    public void bindData(Orders order, Books_sell book, int whichView) {
        BookbayFirestoreReferences.downloadImageIntoImageView(book, this.iv_book_image);

        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        this.tv_book_author.setText(book.getBookAuthor().toUpperCase());
        this.tv_book_title.setText(book.getBookTitle());
        this.tv_book_smallest_price.setText("₱" + decimalFormat.format(book.getPrice()) + " - " + book.getCondition());

        // Create an ArrayAdapter using the string array and a default spinner layout
        CustomSpinner<String> adapter = new CustomSpinner(itemView.getContext(), android.R.layout.simple_spinner_item, Arrays.asList(itemView.getContext().getResources().getStringArray(R.array.book_status)), false);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.s_book_status.setAdapter(adapter);
        this.s_book_status.setSelection(0, false);

        SimpleDateFormat DateForm = new SimpleDateFormat("MMM dd, yyyy | hh:mm aa");


        this.tv_order_date.setText(DateForm.format(order.getOrderDate()).toUpperCase());

        // Profile
        if (whichView == WhichLayout.MY_ORDERS.ordinal()) {

            this.tv_buyer_name.setVisibility(View.GONE);
            this.iv_buyer_image.setVisibility(View.GONE);

            this.tv_order_date.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

        } else if (whichView == WhichLayout.SELLING_ORDERS.ordinal()) {

            this.tv_buyer_name.setVisibility(View.VISIBLE);
            this.iv_buyer_image.setVisibility(View.VISIBLE);

            this.tv_buyer_name.setText(order.getBuyerName().toUpperCase());
            Picasso.get().load(Uri.parse(order.getBuyerImage())).into(this.iv_buyer_image);

            this.tv_order_date.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

        }

        // For Book Status
        if (whichView == WhichLayout.MY_ORDERS.ordinal() && order.getStatus().equals(BookStatus.PENDING.name())) {

            this.s_book_status.setVisibility(View.GONE);
            this.tv_book_status.setVisibility(View.VISIBLE);
            this.tv_book_status.setText(BookStatus.PENDING.name());
            this.tv_book_status.setBackgroundResource(R.drawable.pending_roundable_book_status);
            this.tv_book_status.setTextColor(Color.parseColor("#FF000000"));

        } else if (whichView == WhichLayout.SELLING_ORDERS.ordinal() && order.getStatus().equals(BookStatus.PENDING.name())) {

            this.s_book_status.setVisibility(View.VISIBLE);
            this.tv_book_status.setVisibility(View.GONE);
            this.s_book_status.setBackgroundResource(R.drawable.status_spinner_background);

        }

        if (order.getStatus().equals(BookStatus.DECLINED.name())) {

            this.s_book_status.setVisibility(View.GONE);
            this.tv_book_status.setVisibility(View.VISIBLE);
            this.tv_book_status.setText(BookStatus.DECLINED.name());
            this.tv_book_status.setBackgroundResource(R.drawable.declined_roundable_book_status);
            this.tv_book_status.setTextColor(Color.parseColor("#FFFFFFFF"));

        } else if (order.getStatus().equals(BookStatus.CONFIRMED.name())) {

            this.s_book_status.setVisibility(View.GONE);
            this.tv_book_status.setVisibility(View.VISIBLE);
            this.tv_book_status.setText(BookStatus.CONFIRMED.name());
            this.tv_book_status.setBackgroundResource(R.drawable.confirmed_roundable_book_status);
            this.tv_book_status.setTextColor(Color.parseColor("#FFFFFFFF"));

        }
    }

    public void setItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        this.s_book_status.setOnItemSelectedListener(onItemSelectedListener);
    }

    public void setSelectedToPending(){
        this.s_book_status.setSelection(0);
    }


}
