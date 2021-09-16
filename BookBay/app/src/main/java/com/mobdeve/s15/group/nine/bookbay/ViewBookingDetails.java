package com.mobdeve.s15.group.nine.bookbay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreHelper;
import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreReferences;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ViewBookingDetails extends AppCompatActivity {
    private SearchView searchbar;
    private ImageView bookImage, ownerImage;
    private TextView bookTitle, authorName, ownerName, price, condition, review;
    private Button placeOrder;
    private String userID, ownerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_booking_details);

        // View Initialization
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

        // Get intent from the Thrift Store
        Intent i = getIntent();
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        this.bookTitle.setText(i.getStringExtra(IntentKeys.TITLE_KEY.name()));
        this.authorName.setText(i.getStringExtra(IntentKeys.AUTHOR_KEY.name()));
        this.condition.setText(i.getStringExtra(IntentKeys.CONDITION_KEY.name()));
        this.price.setText("₱" + decimalFormat.format(i.getFloatExtra(IntentKeys.PRICE_KEY.name(),((float)0))));
        this.review.setText(i.getStringExtra(IntentKeys.REVIEW_KEY.name()));

        //check if button is displayed for placing order
        // if the owner is the one logged in, place order would be gone
        this.ownerID = i.getStringExtra(IntentKeys.OWNER_ID_KEY.name());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        this.userID = user.getUid();
        if (ownerID.equals(userID)) {
            this.placeOrder.setVisibility(View.GONE);
        } else {
            this.placeOrder.setVisibility(View.VISIBLE);
        }

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

        // when the place order button is clicked
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

        // Set return intent to Thrift Store when the user search in the View Books Details View
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
