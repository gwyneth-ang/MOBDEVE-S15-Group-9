package com.mobdeve.s15.group.nine.bookbay;

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
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class SellingBooksDetails extends AppCompatActivity {
    private SearchView searchbar;
    private ImageButton filter;
    private ImageView bookImage;
    private TextView bookTitle, authorName, price, condition;
    private Button editBook, deleteBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selling_books_details);
        this.searchbar = findViewById(R.id.Sv_sellingbooksdetails_seach_bar);
        this.filter = findViewById(R.id.Bt_sellingbooksdetails_filter);
        this.bookImage = findViewById(R.id.Iv_sellingbooksdetails_book_image);
        this.bookTitle = findViewById(R.id.Tv_sellingbooksdetails_title);
        this.authorName = findViewById(R.id.Tv_sellingbooksdetails_author);
        this.price = findViewById(R.id.Tv_sellingbooksdetails_price);
        this.condition = findViewById(R.id.Tv_selling_books_details_condition);
        this.editBook = findViewById(R.id.Bt_sellingbooksdetails_edit);
        this.deleteBook = findViewById(R.id.Bt_sellingbooksdetails_delete);

        Intent i = getIntent();
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        this.bookTitle.setText(i.getStringExtra(IntentKeys.TITLE_KEY.name()));
        this.authorName.setText(i.getStringExtra(IntentKeys.AUTHOR_KEY.name()));
        this.condition.setText(i.getStringExtra(IntentKeys.CONDITION_KEY.name()));
        this.price.setText("₱" + decimalFormat.format(i.getFloatExtra(IntentKeys.PRICE_KEY.name(),((float)0))));

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

        // change font for search view
        int id = this.searchbar.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        Typeface tf = ResourcesCompat.getFont(this,R.font.cormorant_garamond);
        TextView searchText = (TextView) this.searchbar.findViewById(id);
        searchText.setTypeface(tf);
        searchText.setTextColor(Color.BLACK);

        this.editBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellingBooksDetails.this.getBaseContext(), AddBookActivity.class);
                intent.putExtra(IntentKeys.BOOK_ID_KEY.name(),i.getStringExtra(IntentKeys.BOOK_ID_KEY.name()));
                intent.putExtra(IntentKeys.BOOK_IMAGE_KEY.name(),i.getStringExtra(IntentKeys.BOOK_IMAGE_KEY.name()));
                intent.putExtra(IntentKeys.TITLE_KEY.name(), i.getStringExtra(IntentKeys.TITLE_KEY.name()));
                intent.putExtra(IntentKeys.AUTHOR_KEY.name(), i.getStringExtra(IntentKeys.AUTHOR_KEY.name()));
                intent.putExtra(IntentKeys.PRICE_KEY.name(), i.getStringExtra(IntentKeys.PRICE_KEY.name()));
                intent.putExtra(IntentKeys.CONDITION_KEY.name(), i.getStringExtra(IntentKeys.CONDITION_KEY.name()));
                intent.putExtra(IntentKeys.REVIEW_KEY.name(), i.getStringExtra(IntentKeys.REVIEW_KEY.name()));
                startActivity(intent);
            }
        });
    }
}
