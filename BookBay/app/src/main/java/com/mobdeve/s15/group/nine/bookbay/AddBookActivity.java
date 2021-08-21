package com.mobdeve.s15.group.nine.bookbay;

import android.app.Activity;
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
    private EditText Et_bookTitle_addBook, Et_author_addBook, Et_price_addBook;
    private Uri imageUri;
    private String selectorChoice = "New";
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
        this.TvAddOrEditTitle = findViewById(R.id.TvAddOrEditTitle);


        //for checking edit or add, check intent
        Intent i = getIntent();

        CustomSpinner<String> book_conditions_adapter = new CustomSpinner(this, android.R.layout.simple_spinner_item, Arrays.asList(getResources().getStringArray(R.array.conditions)), true);
        book_conditions_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_addBook.setAdapter(book_conditions_adapter);
        spinner_addBook.setOnItemSelectedListener(this);

        this.TvAddOrEditTitle.setText("Add a Book");
        this.Bt_addBook.setText("Add Book");

        if (i.getLongExtra(IntentKeys.BOOK_ID_KEY.name(), -1) == -1) {
            this.TvAddOrEditTitle.setText("Add a Book");
            this.Bt_addBook.setText("Add Book");
            this.ViewKey = 0;
        } else {
            this.TvAddOrEditTitle.setText("Edit Book");
            this.Bt_addBook.setText("Update");
            this.ViewKey = 1;

            this.Et_price_addBook.setText(i.getStringExtra(IntentKeys.TITLE_KEY.name()));
            this.Et_author_addBook.setText(i.getStringExtra(IntentKeys.AUTHOR_KEY.name()));
            this.Et_price_addBook.setText(i.getStringExtra(IntentKeys.PRICE_KEY.name()));
            //TODO: Check this part
            Uri tempUri = Uri.parse(i.getStringExtra(IntentKeys.BOOK_IMAGE_KEY.name()));
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

        this.Bt_addBook.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get the input
                String title = Et_bookTitle_addBook.getText().toString();
                String author = Et_author_addBook.getText().toString();
                Float price = Float.valueOf(Et_price_addBook.getText().toString());

                //if the view is for add book
                if (ViewKey == 0) {

                    //get current user
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    //check if inputs are null
                    if (imageUri != null && title != null && author != null && price != null) {
                        // This is a prompt for the user to know the status of the image upload
                        final ProgressDialog progressDialog = new ProgressDialog(AddBookActivity.this);
                        progressDialog.setTitle("Uploading");
                        progressDialog.show();

                        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));

                        Log.d("TEST", user.getDisplayName());

                        Orders order = new Orders(
                                null,
                                null,
                                null,
                                null,
                                user.getDisplayName(),
                                user.getPhotoUrl().toString()
                        );

                        List<Orders> orders = new ArrayList<Orders>();
                        orders.add(order);

                        Log.d("TEST", orders.get(0).getProfileName());

                        //TODO: adjust to the db later
                        Books_sell book = new Books_sell(
                                cal.getTime(),
                                author,
                                title,
                                selectorChoice,
                                user.getUid(),
                                price,
                                imageUri.toString(),
                                orders
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

                        Tasks.whenAllSuccess(t1, t2).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
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
                    BookbayFirestoreReferences.getDocumentReference(i.getStringExtra(IntentKeys.BOOK_ID_KEY.name()));

                    //if the image has been changed:
                    if (imageChanged){

                    }
                }
            }
        }));

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
