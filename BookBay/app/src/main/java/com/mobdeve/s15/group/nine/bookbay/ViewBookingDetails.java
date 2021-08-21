package com.mobdeve.s15.group.nine.bookbay;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ViewBookingDetails extends AppCompatActivity {
    private SearchView searchbar;
    private ImageButton filter;
    private ImageView bookImage, ownerImage;
    private TextView bookTitle, authorName, ownerName, price, condition;
    private Button placeOrder;
    private FirebaseFirestore dbRef;

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

        // change font for search view
        int id = this.searchbar.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        Typeface tf = ResourcesCompat.getFont(this,R.font.cormorant_garamond);
        TextView searchText = (TextView) this.searchbar.findViewById(id);
        searchText.setTypeface(tf);
        searchText.setTextColor(Color.BLACK);

        String path = "images/" + i.getStringExtra(IntentKeys.BOOK_ID_KEY.name()) + "-" + Uri.parse(i.getStringExtra(IntentKeys.BOOK_IMAGE_KEY.name())).getLastPathSegment();

        FirebaseStorage.getInstance().getReference().child(path).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(Task<Uri> task) {
                        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(bookImage.getContext());
                        circularProgressDrawable.setCenterRadius(30);
                        Picasso.get()
                                .load(task.getResult())
                                .error(R.drawable.ic_error)
                                .placeholder(circularProgressDrawable)
                                .into(bookImage);
                    }
                });
//        TODO: owner image and name
//        this.ownerName.setText(i.getStringExtra(IntentKeys.OWNER_NAME_KEY.name()));
//        this.ownerImage.setImageResource(i.getIntExtra(IntentKeys.OWNER_IMAGE_KEY.name()));

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: check if already ordered
                // TODO:if yes, notify user and destroy activity
                // TODO: load order confirmation (dialog)

                Intent i = getIntent();
                if(checkBookSoldDB(i.getStringExtra(IntentKeys.BOOK_ID_KEY.name()))){
                    ConfirmOrderDialog dialog = new ConfirmOrderDialog(i.getStringExtra(IntentKeys.TITLE_KEY.name()), "₱" + decimalFormat.format(i.getFloatExtra(IntentKeys.PRICE_KEY.name(),((float)0))), i.getStringExtra(IntentKeys.CONDITION_KEY.name()));
                    dialog.show(getSupportFragmentManager(), "dialog");
                }
                else{
//                    TODO: Book is already sold, alert and return to homepage
                }
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

//  check db
    private boolean checkBookSoldDB(String bookID){
        this.dbRef = BookbayFirestoreReferences.getFirestoreInstance();
        this.dbRef.collection("Books_sell").whereEqualTo("orderDate", null).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //successful

                        } else {
                            //error
                        }
                    }
                });
        return false;
    }
}
