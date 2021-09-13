package com.mobdeve.s15.group.nine.bookbay;

import android.app.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

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
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.mobdeve.s15.group.nine.bookbay.callback.CanEditCallback;
import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreHelper;
import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreReferences;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SellingBooksDetails extends AppCompatActivity {
    private SearchView searchbar;
    private ImageView bookImage;
    private TextView bookTitle, authorName, price, condition;
    private Button editBook, deleteBook;
    private String bookID, title, author, conditionStr, imageUri, review;
    private Float priceFlt;
    private Boolean canEdit = true;

    private ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        bookID = result.getData().getStringExtra(AddBookActivity.BOOKID_KEY);
                        title = result.getData().getStringExtra(AddBookActivity.TITLE_KEY);
                        bookTitle.setText(title);
                        author = result.getData().getStringExtra(AddBookActivity.AUTHOR_KEY);
                        authorName.setText(author);
                        conditionStr = result.getData().getStringExtra(AddBookActivity.CONDITION_KEY);
                        condition.setText(conditionStr);
                        priceFlt = result.getData().getFloatExtra(AddBookActivity.PRICE_KEY, 0);
                        price.setText(String.valueOf(priceFlt));
                        review = result.getData().getStringExtra(AddBookActivity.REVIEW_KEY);
                        imageUri = result.getData().getStringExtra(AddBookActivity.IMAGE_KEY);
                        BookbayFirestoreReferences.downloadImageIntoImageViewUsingId(bookID, imageUri, bookImage);

                        Intent return_intent = new Intent();
                        SellingBooksDetails.this.setResult(10, return_intent);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selling_books_details);
        this.searchbar = findViewById(R.id.Sv_sellingbooksdetails_seach_bar);
        this.bookImage = findViewById(R.id.Iv_sellingbooksdetails_book_image);
        this.bookTitle = findViewById(R.id.Tv_sellingbooksdetails_title);
        this.authorName = findViewById(R.id.Tv_sellingbooksdetails_author);
        this.price = findViewById(R.id.Tv_sellingbooksdetails_price);
        this.condition = findViewById(R.id.Tv_selling_books_details_condition);
        this.editBook = findViewById(R.id.Bt_sellingbooksdetails_edit);
        this.deleteBook = findViewById(R.id.Bt_sellingbooksdetails_delete);

        Intent i = getIntent();
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        this.bookID = i.getStringExtra(IntentKeys.BOOK_ID_KEY.name());
        this.title = i.getStringExtra(IntentKeys.TITLE_KEY.name());
        this.bookTitle.setText(title);
        this.author = i.getStringExtra(IntentKeys.AUTHOR_KEY.name());
        this.authorName.setText(author);
        this.conditionStr = i.getStringExtra(IntentKeys.CONDITION_KEY.name());
        this.condition.setText(conditionStr);
        this.priceFlt = i.getFloatExtra(IntentKeys.PRICE_KEY.name(),((float)0));
        this.price.setText("₱" + decimalFormat.format(priceFlt));
        this.review = i.getStringExtra(IntentKeys.REVIEW_KEY.name());
        this.imageUri = i.getStringExtra(IntentKeys.BOOK_IMAGE_KEY.name());

        // load image from firebase
        BookbayFirestoreReferences.downloadImageIntoImageViewUsingId(bookID, imageUri, bookImage);

        //check if in cart
        BookbayFirestoreHelper.canEdit(bookID, new CanEditCallback() {
            @Override
            public void canEdit(boolean edit) {
                canEdit = edit;
            }
        });

        // change font for search view
        int id = this.searchbar.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        Typeface tf = ResourcesCompat.getFont(this,R.font.cormorant_garamond);
        TextView searchText = (TextView) this.searchbar.findViewById(id);
        searchText.setTypeface(tf);
        searchText.setTextColor(Color.BLACK);
        searchText.setHintTextColor(Color.parseColor("#999999"));

        this.searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent i = new Intent();
                i.putExtra(IntentKeys.FILTER_KEY.name(), query);
                setResult(Activity.RESULT_OK, i);
                finish();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        this.editBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canEdit) {
                    Intent intent = new Intent(SellingBooksDetails.this.getBaseContext(), AddBookActivity.class);
                    intent.putExtra(IntentKeys.BOOK_ID_KEY.name(), bookID);
                    intent.putExtra(IntentKeys.BOOK_IMAGE_KEY.name(), imageUri);
                    intent.putExtra(IntentKeys.TITLE_KEY.name(), title);
                    intent.putExtra(IntentKeys.AUTHOR_KEY.name(), author);
                    intent.putExtra(IntentKeys.PRICE_KEY.name(), priceFlt);
                    intent.putExtra(IntentKeys.CONDITION_KEY.name(), conditionStr);
                    intent.putExtra(IntentKeys.REVIEW_KEY.name(), review);
                    myActivityResultLauncher.launch(intent);
                } else {
                    Toast toast = Toast.makeText(
                            SellingBooksDetails.this.getBaseContext(),
                            "The book has already been placed as an Order.",
                            Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        this.deleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = deleteBook.getContext();
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                        .setTitle("Delete Book")
                        .setMessage("Are you sure you want to delete this book?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ProgressDialog progress = new ProgressDialog(context);
                                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progress.setTitle("Loading");
                                progress.setMessage("Your book is being deleted. Please wait...");
                                progress.setIndeterminate(true);
                                progress.setCanceledOnTouchOutside(false);
                                progress.show();
                                BookbayFirestoreHelper.deleteBook(i.getStringExtra(IntentKeys.BOOK_ID_KEY.name()), progress,context);
                            }
                        }).setNegativeButton("Cancel", null);
                Dialog dialog = dialogBuilder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });
    }
}
