package com.mobdeve.s15.group.nine.bookbay;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Date;

import java.text.SimpleDateFormat;

public class AddBookActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button Bt_browse_addBook, Bt_addBook;
    private ImageView Iv_bookImage;
    private TextView TvAddOrEditTitle;
    private EditText Et_bookTitle_addBook, Et_author_addBook, Et_price_addBook, Et_review_addBook;
    private Uri imageUri, tempUri, updateUri;
    private String selectorChoice = "New", oldTitle, oldAuthor, oldCondition, oldReview, bookID, TAG="inside";
    private float oldPrice;
    private int ViewKey = 0;
    private boolean imageChanged = false;

    private ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
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
            this.Et_bookTitle_addBook.setText(oldTitle.trim());
            oldAuthor = i.getStringExtra(IntentKeys.AUTHOR_KEY.name());
            this.Et_author_addBook.setText(oldAuthor.trim());
            oldPrice = i.getFloatExtra(IntentKeys.PRICE_KEY.name(), 0);
            this.Et_price_addBook.setText(String.valueOf(oldPrice));
            //oldReview = i.getStringExtra(IntentKeys.REVIEW_KEY.name());
            //this.Et_review_addBook.setText(oldReview);
            oldCondition = IntentKeys.CONDITION_KEY.name();

            //TODO: Check this part
            this.tempUri = Uri.parse(i.getStringExtra(IntentKeys.BOOK_IMAGE_KEY.name()));
            Picasso.get().load(tempUri).into(Iv_bookImage);
        }

        this.Bt_browse_addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChanged = true;

                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_OPEN_DOCUMENT);
                myActivityResultLauncher.launch(Intent.createChooser(i, "Select Picture"));
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
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));

                        Log.d("TEST", user.getDisplayName());


                        //TODO: to be deleted for testing if adding sub collection order is working
                        Orders order = new Orders(
                                null,
                                null,
                                BookStatus.PENDING.name(),
                                null,
                                null,
                                null
                        );

//                        Log.d("TEST", orders.get(0).getProfileName());

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
                                user.getPhotoUrl().toString()
                        );

                        CollectionReference bookRef = BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION);
                        String ID = UUID.randomUUID().toString();

                        StorageReference imageRef = BookbayFirestoreReferences.getStorageReferenceInstance()
                                .child(BookbayFirestoreReferences.generateNewImagePath(ID, imageUri));

                        //task 1 - upload the image to the Firebase
                        Task t1 = imageRef.putFile(imageUri)
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                                        progressDialog.setCanceledOnTouchOutside(false);
                                        progressDialog.setMessage("Uploaded  " + (int) progress + "%");
                                    }
                                });

                        //adding the book to the book_sell collection
                        Task t2 = bookRef.document(ID).set(book);

                        //TODO: to be deleted for testing if adding sub collection order is working
                        Task t3 = bookRef.document(ID).collection(BookbayFirestoreReferences.ORDERS_COLLECTION).document(UUID.randomUUID().toString()).set(order);

                        Tasks.whenAllSuccess(t1, t2, t3).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                            @Override
                            public void onSuccess(List<Object> objects) {
                                progressDialog.setCanceledOnTouchOutside(true);
                                progressDialog.setMessage("Success!");
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                progressDialog.setCanceledOnTouchOutside(true);
                                progressDialog.setMessage("Error occurred. Please try again.");
                            }
                        });
                    } else {
                        Toast.makeText(
                                AddBookActivity.this,
                                "Please fill up all of the inputs.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                } else {
                    //if view is for editing book
                    CollectionReference bookRef = BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION);
                    Boolean changed = false;

                    //TODO: add this later on
                    //Task t2 = bookRef.document(bookID).set(book);

                    //create a hashmap for all the changed values
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
                    //if the image has been changed:
                    if (imageChanged){
                        StorageReference photoRef = BookbayFirestoreReferences.getStorageReferenceInstance()
                                .child(BookbayFirestoreReferences.generateNewImagePath(bookID, tempUri));

                        // Delete picture from firestore
                        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // File deleted successfully
                                Log.d(TAG, "onSuccess: deleted file");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Uh-oh, an error occurred!
                                Log.d(TAG, "onFailure: did not delete file");
                            }
                        });

                        updateBook.put(BookbayFirestoreReferences.IMAGE_FIELD, updateUri);

                        CollectionReference photoRefAdd = BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION);
                        String ID = UUID.randomUUID().toString();

                        StorageReference imageRef = BookbayFirestoreReferences.getStorageReferenceInstance()
                                .child(BookbayFirestoreReferences.generateNewImagePath(bookID, updateUri));
                    }

                    final ProgressDialog progressDialog = new ProgressDialog(AddBookActivity.this);
                    progressDialog.setTitle("Uploading");
                    progressDialog.show();

                    if (ViewKey == 0) {
                        //upload the image to the Firebase
                        imageRef.putFile(updateUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.setMessage("Uploaded  " + (int) progress + "%");
                            }
                        });
                    }

                    //check
                    if (changed) {
                        Log.d("TEST EDIT", bookID);
                        bookRef.document(bookID)
                                .update(updateBook)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Edit Book", "Edit successful");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Log.d("Edit Book", e.getMessage());
                                    }
                                });
                    }
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.selectorChoice = parent.getItemAtPosition(position).toString();
        //Toast.makeText(parent.getContext(), selectorChoice, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
