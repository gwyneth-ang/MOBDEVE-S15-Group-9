package com.mobdeve.s15.group.nine.bookbay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreReferences;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ViewBookingDetails extends AppCompatActivity {
    private SearchView searchbar;
    private ImageView bookImage, ownerImage;
    private TextView bookTitle, authorName, ownerName, price, condition, review;
    private Button placeOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_booking_details);
        this.searchbar = findViewById(R.id.Sv_viewbookingdetails_seach_bar);
        this.bookImage = findViewById(R.id.Iv_viewbookingdetails_book_image);
        this.ownerImage = findViewById(R.id.Iv_viewbookingdetails_owner_image);
        this.bookTitle = findViewById(R.id.Tv_viewbookingdetails_title);
        this.authorName = findViewById(R.id.Tv_viewbookingdetails_author);
        this.ownerName = findViewById(R.id.Tv_viewbookingdetails_owner_name);
        this.placeOrder = findViewById(R.id.Bt_viewbookingdetails_place);
        this.price = findViewById(R.id.Tv_viewbookingdetails_price);
        this.condition = findViewById(R.id.Tv_viewbookingdetails_condition);
        this.review = findViewById(R.id.Tv_viewbookingdetails_review);

        Intent i = getIntent();
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        this.bookTitle.setText(i.getStringExtra(IntentKeys.TITLE_KEY.name()));
        this.authorName.setText(i.getStringExtra(IntentKeys.AUTHOR_KEY.name()));
        this.condition.setText(i.getStringExtra(IntentKeys.CONDITION_KEY.name()));
        this.price.setText("₱" + decimalFormat.format(i.getFloatExtra(IntentKeys.PRICE_KEY.name(),((float)0))));
        this.review.setText(i.getStringExtra(IntentKeys.REVIEW_KEY.name()));

        // change font for search view
        int id = this.searchbar.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        Typeface tf = ResourcesCompat.getFont(this,R.font.cormorant_garamond);
        TextView searchText = (TextView) this.searchbar.findViewById(id);
        searchText.setTypeface(tf);
        searchText.setTextColor(Color.BLACK);
        searchText.setHintTextColor(Color.parseColor("#999999"));


        // load image from firebase
        BookbayFirestoreReferences.downloadImageIntoImageViewUsingId(
                i.getStringExtra(IntentKeys.BOOK_ID_KEY.name()),
                i.getStringExtra(IntentKeys.BOOK_IMAGE_KEY.name()),
                bookImage
        );

        this.ownerName.setText(i.getStringExtra(IntentKeys.OWNER_NAME_KEY.name()));
        Picasso.get().load(i.getStringExtra(IntentKeys.OWNER_IMAGE_KEY.name())).into(ownerImage);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                ConfirmOrderDialog dialog = new ConfirmOrderDialog(i.getStringExtra(IntentKeys.TITLE_KEY.name()), "₱" + decimalFormat.format(i.getFloatExtra(IntentKeys.PRICE_KEY.name(),((float)0))), i.getStringExtra(IntentKeys.CONDITION_KEY.name()));
                Bundle bundle = new Bundle();
                bundle.putString(IntentKeys.BOOK_ID_KEY.name(), i.getStringExtra(IntentKeys.BOOK_ID_KEY.name()));
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });

        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent i = new Intent();
                i.putExtra(IntentKeys.FILTER_KEY.name(), query);
                setResult(Activity.RESULT_OK, i);
                finish();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

    }

}
