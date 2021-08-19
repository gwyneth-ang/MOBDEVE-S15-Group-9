package com.mobdeve.s15.group.nine.bookbay;

import android.net.Uri;
import android.widget.ImageView;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

//FIXME: feel ko may kulang
public class BookbayFirestoreReferences {
    public final static String
            BOOKS_SELL_COLLECTION = "Books_sell",
            NOTIFICATIONS_COLLECTION = "Notifications",
            ORDERS_COLLECTION = "Orders",

            BOOK_AUTHOR_FIELD = "BookAuthor",
            BOOK_TITLE_FIELD = "BookTitle",
            CONDITION_FIELD = "Condition",
            IMAGE_FIELD = "Image",
            OWNER_ID_UID_FIELD = "OwnerID",
            PRICE_FIELD = "Price",
            MESSAGE_FIELD = "Message",
            NOTIFICATION_DATE_TIME_FIELD = "NotificationDateTime",
            ORDER_ID_FK_FIELD = "OrderID",
            TIMESTAMP_FIELD = "timestamp",
            BOOK_ID_FK_FIELD = "BookID",
            BUYER_ID_UID_FIELD = "BuyerID",
            ORDER_DATE_FIELD = "OrderDate",
            STATUS_FIELD = "Status";

//    public static void downloadImageIntoImageView(Books_sell book, ImageView iv) {
//        String path = "images/" + book.getUserRef().getId() + "-" + Uri.parse(book.getImage()).getLastPathSegment();
//
//        getStorageReferenceInstance().child(path).getDownloadUrl()
//                .addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(Task<Uri> task) {
//                        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(iv.getContext());
//                        circularProgressDrawable.setCenterRadius(30);
//                        Picasso.get()
//                                .load(task.getResult())
//                                .error(R.drawable.ic_error_foreground)
//                                .placeholder(circularProgressDrawable)
//                                .into(iv);
//                    }
//                });
}
