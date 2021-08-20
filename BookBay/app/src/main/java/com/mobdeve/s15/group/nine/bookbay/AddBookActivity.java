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
import android.widget.Toast;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class AddBookActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button Bt_browse_addBook, Bt_addBook;
    private ImageView Iv_bookImage;
    private EditText Et_bookTitle_addBook, Et_author_addBook, Et_price_addBook;
    private Uri imageUri;
    private String selectorChoice = "New";

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
        ArrayAdapter<CharSequence> book_conditions_adapter = ArrayAdapter.createFromResource(this, R.array.conditions, android.R.layout.simple_spinner_item);
        book_conditions_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_addBook.setAdapter(book_conditions_adapter);
        spinner_addBook.setOnItemSelectedListener(this);

        this.Bt_browse_addBook = findViewById(R.id.Bt_browse_addBook);
        this.Iv_bookImage = findViewById(R.id.Iv_bookImage);
        this.Et_author_addBook = findViewById(R.id.Et_author_addBook);
        this.Et_bookTitle_addBook = findViewById(R.id.Et_bookTitle_addBook);
        this.Et_price_addBook = findViewById(R.id.Et_price_addBook);

        this.Bt_browse_addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_OPEN_DOCUMENT);
                myActivityResultLauncher.launch(Intent.createChooser(i, "Select Picture"));
            }
        });

        this.Bt_addBook.setOnClickListener((new View.OnClickListener() {
            private FirebaseFirestore dbRef;

            @Override
            public void onClick(View v) {
                //get current user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                //get the input
                String title = Et_bookTitle_addBook.getText().toString();
                String author = Et_author_addBook.getText().toString();
                Float price = Float.valueOf(Et_price_addBook.getText().toString());

                //check if inputs are null
                if (imageUri != null && title != null && author != null && price != null) {
                    // This is a prompt for the user to know the status of the image upload
                    final ProgressDialog progressDialog = new ProgressDialog(AddBookActivity.this);
                    progressDialog.setTitle("Uploading");
                    progressDialog.show();

                    //TODO: adjust to the db later
                    Books_sell book = new Books_sell(
                            author,
                            title,
                            selectorChoice,
                            user,
                            price,
                            imageUri.toString()
                    );
                    // Reference of the image in Firebase Storage
                    StorageReference imageRef = MyFirestoreReferences.getStorageReferenceInstance()
                            .child(MyFirestoreReferences.generateNewImagePath(book.getBooks_sellID().getId(), imageUri));
                    // Post collection reference
                    CollectionReference postsRef = MyFirestoreReferences.getPostCollectionReference();


                }

                //TODO: DELETE
                //ready the values
                Map<String, Object> data = new HashMap<>();
                data.put(BookbayFirestoreReferences.OWNER_ID_UID_FIELD, user.getUid());
                data.put(BookbayFirestoreReferences.BOOK_TITLE_FIELD, title);
                data.put(BookbayFirestoreReferences.BOOK_AUTHOR_FIELD, author);
                data.put(BookbayFirestoreReferences.PRICE_FIELD, price);
                data.put(BookbayFirestoreReferences.CONDITION_FIELD, selectorChoice);

                // Get the DB from Firebase
                this.dbRef = FirebaseFirestore.getInstance();
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
