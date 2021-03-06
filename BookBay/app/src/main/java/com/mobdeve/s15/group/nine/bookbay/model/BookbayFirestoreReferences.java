package com.mobdeve.s15.group.nine.bookbay.model;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mobdeve.s15.group.nine.bookbay.R;
import com.squareup.picasso.Picasso;

public class BookbayFirestoreReferences {

    /* For Reference:
     *      Firebase Firestore
     *          Books_sell
     *              -> id
     *                  -> addBookDate (Timestamp)
     *                  -> available (Boolean)
     *                  -> bookAuthor (String)
     *                  -> bookTitle (String)
     *                  -> condition (String)
     *                  -> imageUri (String)
     *                  -> ownerID (String - Uid)
     *                  -> price (Number)
     *                  -> profileImage (String - url of Google Profile Image)
     *                  -> profileName (String)
     *                  -> review (String)
     *                  -> Orders (Collection)
     *                      -> id
     *                          -> buyerEmail (String)
     *                          -> buyerID (String)
     *                          -> buyerImage (String - url of Google Profile Image)
     *                          -> buyerName (String)
     *                          -> notificationDateTime (Timestamp)
     *                          -> orderDate (Timestamp)
     *                          -> status (String)
     *          Notifications
     *              -> id
     *                  -> bookID (String)
     *                  -> bookTitle (String)
     *                  -> buyerID (String)
     *                  -> image (String)
     *                  -> notificationDateTime (Timestamp)
     *                  -> profileName (String)
     *                  -> status (String)
     *
     *      Firebase Storage
     *          -> images/
     *              -> book_id + <name of image>
     * */

    public final static String
            BOOKS_SELL_COLLECTION = "Books_sell",
            ORDERS_COLLECTION = "Orders",
            NOTIFICATIONS_COLLECTION = "Notifications",

            ADD_BOOK_DATE_FIELD = "addBookDate",
            BOOK_AUTHOR_FIELD = "bookAuthor",
            BOOK_TITLE_FIELD = "bookTitle",
            CONDITION_FIELD = "condition",
            IMAGE_FIELD = "image",
            OWNER_ID_UID_FIELD = "ownerID",
            PRICE_FIELD = "price",
            PROFILE_NAME_FIELD = "profileName",
            PROFILE_IMAGE_FIELD = "profileImage",
            REVIEW_FIELD = "review",
            BOOK_ID_FIELD = "bookID",
            NOTIFICATION_DATE_TIME_FIELD = "notificationDateTime",
            BUYER_ID_UID_FIELD = "buyerID",
            BUYER_NAME_FIELD = "buyerName",
            BUYER_IMAGE_FIELD = "buyerImage",
            ORDER_DATE_FIELD = "orderDate",
            STATUS_FIELD = "status",
            AVAILABLE_FIELD = "available";

    // All our instances of Firestore and Storage
    private static FirebaseFirestore firebaseFirestoreInstance = null;
    private static StorageReference storageReferenceInstance = null;

    public static String generateNewImagePath(String books_sellID, Uri imageUri) {
        return "images/" + books_sellID + "-" + imageUri.getLastPathSegment();
    }

    public static FirebaseFirestore getFirestoreInstance() {
        if(firebaseFirestoreInstance == null) {
            firebaseFirestoreInstance = FirebaseFirestore.getInstance();
        }
        return firebaseFirestoreInstance;
    }

    public static StorageReference getStorageReferenceInstance() {
        if (storageReferenceInstance == null) {
            Log.d("TEST", "Hi");
            storageReferenceInstance = FirebaseStorage.getInstance().getReference();
        }
        return storageReferenceInstance;
    }

    public static DocumentReference getDocumentReference(String stringRef) {
        return getFirestoreInstance().document(stringRef);
    }

    public static void downloadImageIntoImageViewNotifcation(Notifications notifications, ImageView iv) {
        String path = "images/" + notifications.getBookID() + "-" + Uri.parse(notifications.getImage()).getLastPathSegment();

        Log.d("TEST", path);

        getStorageReferenceInstance().child(path).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(Task<Uri> task) {
                        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(iv.getContext());
                        circularProgressDrawable.setCenterRadius(30);
                        Picasso.get()
                                .load(task.getResult())
                                .error(R.drawable.ic_error)
                                .placeholder(circularProgressDrawable)
                                .into(iv);
                    }
                });
    }

    public static void downloadImageIntoImageView(Books_sell book, ImageView iv) {
        String path = "images/" + book.getBooks_sellID().getId() + "-" + Uri.parse(book.getImage()).getLastPathSegment();

        Log.d("TEST", path);

        getStorageReferenceInstance().child(path).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(Task<Uri> task) {
                        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(iv.getContext());
                        circularProgressDrawable.setCenterRadius(30);
                        Picasso.get()
                                .load(task.getResult())
                                .error(R.drawable.ic_error)
                                .placeholder(circularProgressDrawable)
                                .into(iv);
                    }
                });
    }

    public static void downloadImageIntoImageViewUsingId(String ID, String image, ImageView iv) {
        String path = "images/" + ID + "-" + Uri.parse(image).getLastPathSegment();

        Log.d("TEST", path);

        getStorageReferenceInstance().child(path).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(Task<Uri> task) {
                        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(iv.getContext());
                        circularProgressDrawable.setCenterRadius(30);
                        Picasso.get()
                                .load(task.getResult())
                                .error(R.drawable.ic_error)
                                .placeholder(circularProgressDrawable)
                                .into(iv);
                    }
                });
    }
}
