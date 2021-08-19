package com.mobdeve.s15.group.nine.bookbay;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class ViewBookingDetails extends AppCompatActivity {
    private SearchView searchbar;
    private ImageButton filter;
    private ImageView bookImage, ownerImage;
    private TextView bookTitle, authorName, ownerName, price, condition;
    private Button placeOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_booking_details);
        this.searchbar = findViewById(R.id.Sv_viewbookingdetails_seach_bar);
        this.filter = findViewById(R.id.Bt_viewbookingdetails_filter);
        this.bookImage = findViewById(R.id.Iv_viewbookingdetails_book_image);
        this.ownerImage = findViewById(R.id.Iv_viewbookingdetails_owner_image);
        this.bookTitle = findViewById(R.id.Tv_viewbookingdetails_title);
        this.authorName = findViewById(R.id.Tv_viewbookingdetails_author);
        this.ownerName = findViewById(R.id.Tv_viewbookingdetails_owner_name);
        this.placeOrder = findViewById(R.id.Bt_viewbookingdetails_place);
        this.price = findViewById(R.id.Tv_viewbookingdetails_price);
        this.condition = findViewById(R.id.Tv_viewbookingdetails_condition);

        Intent i = getIntent();
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        this.bookTitle.setText(i.getStringExtra(IntentKeys.TITLE_KEY.name()));
        this.authorName.setText(i.getStringExtra(IntentKeys.AUTHOR_KEY.name()));
        this.condition.setText(i.getStringExtra(IntentKeys.CONDITION_KEY.name()));
        this.price.setText("₱" + decimalFormat.format(i.getFloatExtra(IntentKeys.PRICE_KEY.name(),((float)0))));
        this.bookImage.setImageResource(i.getIntExtra(IntentKeys.BOOK_IMAGE_KEY.name(), 0));
//        this.ownerName.setText(i.getStringExtra(IntentKeys.OWNER_NAME_KEY.name()));
//        this.ownerImage.setImageResource(i.getIntExtra(IntentKeys.OWNER_IMAGE_KEY.name()));

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: check if already ordered
                // TODO:if yes, notify user and destroy activity
                // TODO: load order confirmation (dialog)
                Intent i = getIntent();
                ConfirmOrderDialog dialog = new ConfirmOrderDialog(i.getStringExtra(IntentKeys.TITLE_KEY.name()), "₱" + decimalFormat.format(i.getFloatExtra(IntentKeys.PRICE_KEY.name(),((float)0))), i.getStringExtra(IntentKeys.CONDITION_KEY.name()));
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });

        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                callSearch(newText);
                return true;
            }

            public void callSearch(String query) {
                //Do searching
            }
        });

//        disable filter for this view
        filter.setEnabled(false);


    }
}
