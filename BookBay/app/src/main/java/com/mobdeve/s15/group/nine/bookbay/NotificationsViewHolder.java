package com.mobdeve.s15.group.nine.bookbay;

import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationsViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private TextView tvSellerName_notifLayout, tvBookTitle_notifLayout, tvStatusBook_notifLayout, tvTimePassed_notifLayout;
    private ImageButton ibButton_notifLayout;

    public NotificationsViewHolder(View itemView) {
        super(itemView);
        this.tvSellerName_notifLayout = itemView.findViewById(R.id.tvSellerName_notifLayout);
        this.tvBookTitle_notifLayout = itemView.findViewById(R.id.tvBookTitle_notifLayout);
        this.tvStatusBook_notifLayout = itemView.findViewById(R.id.tvStatusBook_notifLayout);
        this.tvTimePassed_notifLayout = itemView.findViewById(R.id.tvTimePassed_notifLayout);

        this.imageView = itemView.findViewById(R.id.notifImageView);

        this.ibButton_notifLayout = itemView.findViewById(R.id.ibButton_notifLayout);
    }

//    public void bindData(Books_sell book) {
//        BookbayFirestoreReferences.downloadImageIntoImageView(book, this.imageView);
//
//        this.tvSellerName_notifLayout.setText(book.getProfileName());
//        this.tvBookTitle_notifLayout.setText(book.getBookTitle());
//        this.tvTimePassed_notifLayout.setText(book.getNotificationDateTime().toUpperCase());
//
//        if (book.getStatus().equals("CONFIRMED")) {
//            this.tvStatusBook_notifLayout.setText(Html.fromHtml("Woohoo! Your purchase of <b>" + book.getBookTitle() + "</b> has been approved by the seller! You will be receiving this book by your doorstep soon"));
//            this.ibButton_notifLayout.setImageResource(R.drawable.check_mark);
//        } else {
//            this.tvStatusBook_notifLayout.setText(Html.fromHtml("I’m sorry, your purchase of <b>" + book.getBookTitle() + "</b> has been denied by the seller. Check our Thrift Store for more book options."));
//            this.ibButton_notifLayout.setImageResource(R.drawable.cancel);
//        }
//
//    }
}