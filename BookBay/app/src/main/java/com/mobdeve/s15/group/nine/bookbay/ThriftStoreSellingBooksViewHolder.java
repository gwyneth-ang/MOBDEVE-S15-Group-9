package com.mobdeve.s15.group.nine.bookbay;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

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

    public void bindData(Books_sell book, int whichLayout) {
        // Create a reference with an initial file path and name
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl(book.getImage());

        gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                // Pass it to Picasso to download, show in ImageView and caching
                Picasso.get().load(uri.toString()).into(iv_book_image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                // Handle any errors
            }
        });

        this.tv_book_author.setText(book.getBookAuthor());
        this.tv_book_title.setText(book.getBookTitle());

        if (whichLayout == WhichLayout.SELLING_BOOKS.ordinal()) {
            this.tv_book_smallest_price.setVisibility(View.GONE);
        } else if (whichLayout == WhichLayout.THRIFT_STORE.ordinal()) {
            this.tv_book_smallest_price.setVisibility(View.VISIBLE);
            this.tv_book_smallest_price.setText(String.valueOf(book.getPrice()));
        }
    }
}
