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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
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
        this.Bt_addBook = findViewById(R.id.Bt_addBook);

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
                            null,
                            author,
                            title,
                            selectorChoice,
                            user.getUid(),
                            price,
                            imageUri.toString(),
                            null,
                            null,
                            null,
                            null
                    );

                    StorageReference imageRef = BookbayFirestoreReferences.getStorageReferenceInstance()
                            .child(BookbayFirestoreReferences.generateNewImagePath(book.getBooks_sellID(), imageUri));

                    CollectionReference bookRef = BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION);

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
                    Task t2 = bookRef.add(book);
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
