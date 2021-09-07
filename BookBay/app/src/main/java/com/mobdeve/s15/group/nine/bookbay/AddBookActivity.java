package com.mobdeve.s15.group.nine.bookbay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreHelper;
import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreReferences;
import com.mobdeve.s15.group.nine.bookbay.model.Books_sell;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Date;

import java.text.SimpleDateFormat;

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

//    private ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if (result.getResultCode() == Activity.RESULT_OK){
//                        try {
//                            if(result.getData() != null) {
//                                // Get the path of the image
//                                imageUri = result.getData().getData();
//                                // Load the image into the tempImageIv using the path
//                                Picasso.get().load(imageUri).into(Iv_bookImage);
//                            }
//                        } catch(Exception exception){
//                            Log.d("TAG",""+exception.getLocalizedMessage());
//                        }
//                    }
//                }
//            });

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    imageUri = imageReturnedIntent.getData();
                    Picasso.get().load(imageUri).into(Iv_bookImage);

                    Log.d("HERE IN CHOOSE", imageUri.toString());
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){

//                    imageUri = imageReturnedIntent.getData();
//                    imageUri = Uri.parse(imageReturnedIntent.getData().getStringExtra(MediaStore.EXTRA_OUTPUT));
//                    imageUri = imageReturnedIntent.getData();
//                    galleryAddPic();

//                    Bundle extras = imageReturnedIntent.getExtras();
//
//                    imageUri = (Uri) extras.get("data");
//
                    // ANOTHER VERSION
//                    Log.d("HERE TOO", imageUri.toString());
//                    // FIXME: does not work yung gallery
//                    galleryAddPic();
//
//                    // PISCASOO DOES NOT WORK
//                    Picasso.get().load(imageUri).into(Iv_bookImage, new Callback() {
//                        @Override
//                        public void onSuccess() {
//                        }
//
//                        @Override
//                        public void onError(Exception e) {
//                            e.printStackTrace();
//                        }
//                    });
//
//                    Bundle extras = imageReturnedIntent.getExtras();
//                    Bitmap bmp = (Bitmap) extras.get("data");
//                    Iv_bookImage.setImageBitmap(bmp);
                }
                break;
        }
    }

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


            //TODO: Check this part
            this.tempUri = Uri.parse(i.getStringExtra(IntentKeys.BOOK_IMAGE_KEY.name()));
            Picasso.get().load(tempUri).into(Iv_bookImage);
            imageUri = tempUri;
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

//                            File file=getOutputMediaFile(1);
//                            picUri = Uri.fromFile(file); // create
//                            i.putExtra(MediaStore.EXTRA_OUTPUT,picUri); // set the image file

//                            if (takePicture.resolveActivity(getPackageManager()) != null) {
//                                startActivityForResult(takePicture, 1);
//                            }

                            if (takePicture.resolveActivity(getPackageManager()) != null) {
                                // Create the File where the photo should go
                                File photoFile = null;
                                try {
                                    photoFile = createImageFile();
                                } catch (IOException ex) {
                                    // Error occurred while creating the File
                                    ex.printStackTrace();
                                }
                                // Continue only if the File was successfully created
                                if (photoFile != null) {
                                    Uri photoUri = FileProvider.getUriForFile(view.getContext(),
                                            "com.example.android.fileprovider",
                                            photoFile);

                                    Log.d("HERE", photoUri.toString());
                                    takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                    startActivityForResult(takePicture, 1);
                                }
                            }
                        } else if (choice[which].equals("Choose From Gallery")) {
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            pickPhoto.setType("image/*");

                            startActivityForResult(pickPhoto, 0);
                        }
                    }
                });
                builder.show();



//                imageChanged = true;
//
//                Intent i = new Intent();
//                i.setType("image/*");
//                i.setAction(Intent.ACTION_OPEN_DOCUMENT);
//                myActivityResultLauncher.launch(Intent.createChooser(i, "Select Picture"));
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

                        //TODO: start here
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
                            //TODO: Make intent
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
        //Toast.makeText(parent.getContext(), selectorChoice, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        Log.d("HERE GALLERY", contentUri.toString());
        sendBroadcast(mediaScanIntent);
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        outState.putParcelable("imageUri", imageUri);
//    }
//
//    // Recover the saved state when the activity is recreated.
//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        imageUri = savedInstanceState.getParcelable("imageUri");
//
//    }


}
