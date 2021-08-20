package com.mobdeve.s15.group.nine.bookbay;

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
import com.squareup.picasso.Picasso;

public class BookbayFirestoreReferences {
    public final static String
            BOOKS_SELL_COLLECTION = "Books_sell",

            ADD_BOOK_DATE_FIELD = "addBookDate",
            BOOK_AUTHOR_FIELD = "bookAuthor",
            BOOK_TITLE_FIELD = "bookTitle",
            CONDITION_FIELD = "condition",
            IMAGE_FIELD = "image",
            OWNER_ID_UID_FIELD = "ownerID",
            PRICE_FIELD = "price",
            NOTIFICATION_DATE_TIME_FIELD = "notificationDateTime",
            BUYER_ID_UID_FIELD = "buyerID",
            ORDER_DATE_FIELD = "orderDate",
            STATUS_FIELD = "status";

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
}
