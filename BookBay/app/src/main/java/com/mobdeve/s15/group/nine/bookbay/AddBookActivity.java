package com.mobdeve.s15.group.nine.bookbay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.content.Context;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreHelper;
import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreReferences;
import com.mobdeve.s15.group.nine.bookbay.model.Books_sell;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;
import java.util.TimeZone;


public class AddBookActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static String
            BOOKID_KEY = "BOOKID_KEY",
            TITLE_KEY = "TITLE_KEY",
            AUTHOR_KEY = "AUTHOR_KEY",
            CONDITION_KEY = "CONDITION_KEY",
            PRICE_KEY = "PRICE_KEY",
            REVIEW_KEY = "REVIEW_KEY",
            IMAGE_KEY = "IMAGE_KEY";

    private Button Bt_browse_addBook, Bt_addBook;
    private ImageView Iv_bookImage;
    private TextView TvAddOrEditTitle;
    private EditText Et_bookTitle_addBook, Et_author_addBook, Et_price_addBook, Et_review_addBook;
    private Uri imageUri, tempUri;
    private String selectorChoice = "New", oldTitle, oldAuthor, oldCondition, oldReview, bookID, TAG="inside";
    private float oldPrice;
    private int ViewKey = 0, spinnerIndex;
    private boolean imageChanged = false;

    private String currentPhotoPath;
    private File photoFile;

    private ActivityResultLauncher<Intent> choosePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        try {
                            if(result.getData() != null) {
                                // Get the path of the image
                                imageUri = result.getData().getData();
                                // Load the image into the tempImageIv using the path
                                Picasso.get().load(imageUri).into(Iv_bookImage);
                            }
                        } catch(Exception exception){
                            Log.d("TAG",""+exception.getLocalizedMessage());
                        }
                    }
                }
            });

    private ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        try {
                            if(result.getData() != null) {
                                Bundle extras = result.getData().getExtras();
                                Bitmap photo = (Bitmap) extras.get("data");

                                imageUri = getImageUri(getApplicationContext(), photo);
                                Picasso.get().load(imageUri).into(Iv_bookImage);
                            }
                        } catch(Exception exception){
                            Log.d("TAG",""+exception.getLocalizedMessage());
                        }
                    }
                }
            });

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_and_edit_book);

        Spinner spinner_addBook = findViewById(R.id.Spinner_addBook);

        this.Bt_browse_addBook = findViewById(R.id.Bt_browse_addBook);
        this.Iv_bookImage = findViewById(R.id.Iv_bookImage);
        this.Et_author_addBook = findViewById(R.id.Et_author_addBook);
        this.Et_bookTitle_addBook = findViewById(R.id.Et_bookTitle_addBook);
        this.Et_price_addBook = findViewById(R.id.Et_price_addBook);
        this.Bt_addBook = findViewById(R.id.Bt_addBook);
        this.Et_review_addBook = findViewById(R.id.Et_review_addBook);
        this.TvAddOrEditTitle = findViewById(R.id.TvAddOrEditTitle);


        //for checking edit or add, check intent
        Intent i = getIntent();

        CustomSpinner<String> book_conditions_adapter = new CustomSpinner(this, android.R.layout.simple_spinner_item, Arrays.asList(getResources().getStringArray(R.array.conditions)), true);
        book_conditions_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_addBook.setAdapter(book_conditions_adapter);
        spinner_addBook.setOnItemSelectedListener(this);

        this.TvAddOrEditTitle.setText("Add a Book");
        this.Bt_addBook.setText("Add Book");

        //if add book
        if (i.getStringExtra(IntentKeys.BOOK_ID_KEY.name()) == null) {
            this.TvAddOrEditTitle.setText("Add a Book");
            this.Bt_addBook.setText("Add Book");
            this.ViewKey = 0;
        } else {
            //if edit book
            this.TvAddOrEditTitle.setText("Edit Book");
            this.Bt_addBook.setText("Update");
            this.ViewKey = 1;

            bookID = i.getStringExtra(IntentKeys.BOOK_ID_KEY.name());
            oldTitle = i.getStringExtra(IntentKeys.TITLE_KEY.name());
            Log.d("OLD TITLE EDITING", oldTitle);
            this.Et_bookTitle_addBook.setText(oldTitle.trim());
            oldAuthor = i.getStringExtra(IntentKeys.AUTHOR_KEY.name());
            this.Et_author_addBook.setText(oldAuthor.trim());
            oldPrice = i.getFloatExtra(IntentKeys.PRICE_KEY.name(), 0);
            this.Et_price_addBook.setText(String.valueOf(oldPrice));
            oldReview = i.getStringExtra(IntentKeys.REVIEW_KEY.name());
            this.Et_review_addBook.setText(oldReview);
            oldCondition = IntentKeys.CONDITION_KEY.name();
            if (oldCondition == "New") {
                spinnerIndex = 0;
            } else if (oldCondition == "Good") {
                spinnerIndex = 1;
            } else {
                spinnerIndex = 2;
            }
            spinner_addBook.setSelection(spinnerIndex);

            this.tempUri = Uri.parse(i.getStringExtra(IntentKeys.BOOK_IMAGE_KEY.name()));
            imageUri = tempUri;
            BookbayFirestoreReferences.downloadImageIntoImageViewUsingId(bookID, imageUri.toString(), Iv_bookImage);
        }

        this.Bt_browse_addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] choice = {"Take Photo", "Choose From Gallery"};
                imageChanged = true;

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Add Photo");
                builder.setItems(choice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (choice[which].equals("Take Photo")) {
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            takePictureLauncher.launch(takePicture);

                        } else if (choice[which].equals("Choose From Gallery")) {

                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            pickPhoto.setType("image/*");

                            choosePictureLauncher.launch(pickPhoto);
                        }
                    }
                });
                builder.show();
            }
        });

        this.Bt_addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get the input
                String title = Et_bookTitle_addBook.getText().toString();
                String author = Et_author_addBook.getText().toString();
                Float price = Float.valueOf(Et_price_addBook.getText().toString());
                String review = Et_review_addBook.getText().toString();

                //if the view is for add book
                if (ViewKey == 0) {

                    //get current user
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    //check if inputs are null
                    if (imageUri != null && title != null && author != null && price != null && review != null) {
                        // This is a prompt for the user to know the status of the image upload
                        final ProgressDialog progressDialog = new ProgressDialog(AddBookActivity.this);
                        progressDialog.setTitle("Uploading");
                        progressDialog.show();

                        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));

                        Log.d("TEST", user.getDisplayName());

                        Books_sell book = new Books_sell(
                                cal.getTime(),
                                author,
                                title,
                                selectorChoice,
                                review,
                                user.getUid(),
                                price,
                                imageUri.toString(),
                                user.getDisplayName(),
                                user.getPhotoUrl().toString(),
                                true
                        );

                        BookbayFirestoreHelper.AddBook(progressDialog, imageUri, book, AddBookActivity.this);

                    } else {
                        Toast.makeText(
                                AddBookActivity.this,
                                "Please fill up all of the inputs.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                } else {
                    //if view is for editing book
                    Boolean changed = false;

                    //create a hashmap for all the changed values
                    if (imageUri != null && title != null && author != null && price != null && review != null) {
                        Map<String, Object> updateBook = new HashMap<>();
                        //if title is changed
                        if (!title.equals(oldTitle)) {
                            changed = true;
                            updateBook.put(BookbayFirestoreReferences.BOOK_TITLE_FIELD, title);
                        }
                        //if author is changed
                        if (!author.equals(oldAuthor)) {
                            changed = true;
                            updateBook.put(BookbayFirestoreReferences.BOOK_AUTHOR_FIELD, author);
                        }
                        //if price is changed
                        if (price != oldPrice) {
                            changed = true;
                            updateBook.put(BookbayFirestoreReferences.PRICE_FIELD, price);
                        }
                        //if condition is changed
                        if (!selectorChoice.equals(oldCondition)) {
                            changed = true;
                            updateBook.put(BookbayFirestoreReferences.CONDITION_FIELD, selectorChoice);
                        }
                        if (!review.equals(oldReview)){
                            changed = true;
                            updateBook.put(BookbayFirestoreReferences.REVIEW_FIELD, review);
                        }
                        //if the image has been changed:
                        if (imageChanged){
                            //used for deleting
                            BookbayFirestoreHelper.deleteBookImageFromStorage(bookID, tempUri);

                            updateBook.put(BookbayFirestoreReferences.IMAGE_FIELD, imageUri.toString());

                            final ProgressDialog progressDialog = new ProgressDialog(AddBookActivity.this);
                            progressDialog.setTitle("Uploading");
                            progressDialog.show();

                            BookbayFirestoreHelper.editBookWithImage(progressDialog, bookID, imageUri, updateBook, AddBookActivity.this, title, author, selectorChoice, price, review);
                        }

                        //check
                        if (changed && !imageChanged) {
                            Log.d("TEST EDIT", bookID);
                            BookbayFirestoreHelper.editBookNoImage(bookID, updateBook);
                            Intent return_intent = new Intent();
                            return_intent.putExtra(BOOKID_KEY, bookID);
                            return_intent.putExtra(TITLE_KEY, title);
                            return_intent.putExtra(AUTHOR_KEY, author);
                            return_intent.putExtra(CONDITION_KEY, selectorChoice);
                            return_intent.putExtra(REVIEW_KEY, review);
                            return_intent.putExtra(PRICE_KEY, price);
                            return_intent.putExtra(IMAGE_KEY, imageUri.toString());
                            setResult(Activity.RESULT_OK, return_intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(
                                AddBookActivity.this,
                                "Please fill up all of the inputs.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.selectorChoice = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
