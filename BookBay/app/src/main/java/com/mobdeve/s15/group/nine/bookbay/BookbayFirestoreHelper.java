package com.mobdeve.s15.group.nine.bookbay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.content.Context;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

public class BookbayFirestoreHelper {

    public static void findAllBooksAvailable(ThriftStoreSellingBooksAdapter thriftStoreAdapter) {
        BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                .whereEqualTo(BookbayFirestoreReferences.AVAILABLE_FIELD, true)
                .orderBy(BookbayFirestoreReferences.ADD_BOOK_DATE_FIELD, Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            Log.d("TEST", "Hi");

                            ArrayList<Books_sell> books = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult())
                                books.add(document.toObject(Books_sell.class));

                            thriftStoreAdapter.setData(books);
                            thriftStoreAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("TEST", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public static void findAllBooksAvailableSeller(ThriftStoreSellingBooksAdapter sellingBookAdapter, String sellerUID) {
        BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                .whereEqualTo(BookbayFirestoreReferences.AVAILABLE_FIELD, true)
                .whereEqualTo(BookbayFirestoreReferences.OWNER_ID_UID_FIELD, sellerUID)
                .orderBy(BookbayFirestoreReferences.ADD_BOOK_DATE_FIELD, Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("TEST", "Hi");

                            ArrayList<Books_sell> books = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult())
                                books.add(document.toObject(Books_sell.class));

                            sellingBookAdapter.setData(books);
                            sellingBookAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public static void searchFilterBooks (String searchText, String strDirection, String whichFilter, ThriftStoreSellingBooksAdapter adapter) {

        Direction direction = Direction.ASCENDING;
        if (strDirection.equals("DES")) {
            direction = Direction.DESCENDING;
        }

        String filter = BookbayFirestoreReferences.BOOK_TITLE_FIELD;

        if (whichFilter.equals(BookbayFirestoreReferences.PRICE_FIELD)) {
            filter = BookbayFirestoreReferences.PRICE_FIELD;
        }

        BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                .whereEqualTo(BookbayFirestoreReferences.AVAILABLE_FIELD, true)
                .whereEqualTo(BookbayFirestoreReferences.BOOK_TITLE_FIELD, searchText)
                .whereEqualTo(BookbayFirestoreReferences.BOOK_AUTHOR_FIELD, searchText)
                .orderBy(filter, direction)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            Log.d("TEST", "Hi");

                            ArrayList<Books_sell> books = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult())
                                books.add(document.toObject(Books_sell.class));

                            adapter.setData(books);
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("TEST", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public static void searchFilterBooksSeller () {

    }

    public static void findBuyerOrders (OrdersAdapter myOrdersAdapter, String buyerUID) {
        BookbayFirestoreReferences.getFirestoreInstance().collectionGroup(BookbayFirestoreReferences.ORDERS_COLLECTION)
                .whereEqualTo(BookbayFirestoreReferences.BUYER_ID_UID_FIELD, buyerUID)
                .orderBy(BookbayFirestoreReferences.ORDER_DATE_FIELD, Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("TEST", "Hi");

                            ArrayList<BooksOrders> booksOrders = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TEST", "In the loop" + document.getReference().getId());
                                Orders orderTemp = document.toObject(Orders.class);

                                document.getReference().getParent().getParent().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(Task<DocumentSnapshot> task3) {
                                        Books_sell bookTemp = null;

                                        if (task.isSuccessful()) {
                                            bookTemp = task3.getResult().toObject(Books_sell.class);
                                        } else {
                                            Log.d("TEST", "Error getting documents: ", task.getException());
                                        }

                                        booksOrders.add(new BooksOrders(orderTemp, bookTemp));

                                        myOrdersAdapter.setData(booksOrders);
                                        myOrdersAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                    }
                });
    }

    public static void findSellerOrders (OrdersAdapter sellerOrdersAdapter, String sellerUID) {
        BookbayFirestoreReferences.getFirestoreInstance().collectionGroup(BookbayFirestoreReferences.ORDERS_COLLECTION)
                .orderBy(BookbayFirestoreReferences.ORDER_DATE_FIELD, Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<BooksOrders> booksOrders = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TEST", "In the loop" + document.getReference().getId());
                                Orders orderTemp = document.toObject(Orders.class);

                                document.getReference().getParent().getParent().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(Task<DocumentSnapshot> task3) {
                                        Books_sell bookTemp = null;

                                        if (task.isSuccessful()) {
                                            bookTemp = task3.getResult().toObject(Books_sell.class);

                                            Boolean same = false;

                                            for (int i = 0; i < booksOrders.size(); i++) {
                                                if (booksOrders.get(i).getBook().getBooks_sellID().equals(bookTemp.getBooks_sellID().getId()))
                                                    same = true;
                                            }

                                            if (!same && bookTemp.getOwnerID().equals(sellerUID))
                                                booksOrders.add(new BooksOrders(orderTemp, bookTemp));
                                        } else {
                                            Log.d("TEST", "Error getting documents: ", task.getException());
                                        }

                                        sellerOrdersAdapter.setData(booksOrders);
                                        sellerOrdersAdapter.notifyDataSetChanged();

                                        Log.d("SELLER ORDERS", String.valueOf(booksOrders.size()));
                                    }
                                });
                            }
                        }
                    }
                });
    }

    public static void updateStatusAndNotifications (BooksOrders booksOrders, String bookStatus, Context context) {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("The order is being " + bookStatus);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        Map<String, Object> data = new HashMap<>();

        data.put(BookbayFirestoreReferences.STATUS_FIELD, bookStatus);
        data.put(BookbayFirestoreReferences.NOTIFICATION_DATE_TIME_FIELD, cal.getTime());

        Map<String, Object> notification = new HashMap<>();

        notification.put(BookbayFirestoreReferences.BOOK_REF_FIELD, booksOrders.getBook().getBooks_sellID());
        notification.put(BookbayFirestoreReferences.BOOK_TITLE_FIELD, booksOrders.getBook().getBookTitle());
        notification.put(BookbayFirestoreReferences.IMAGE_FIELD, booksOrders.getBook().getImage());
        notification.put(BookbayFirestoreReferences.PROFILE_NAME_FIELD, booksOrders.getBook().getProfileName());
        notification.put(BookbayFirestoreReferences.STATUS_FIELD, bookStatus);
        notification.put(BookbayFirestoreReferences.NOTIFICATION_DATE_TIME_FIELD, cal.getTime());
        notification.put(BookbayFirestoreReferences.BUYER_ID_UID_FIELD, booksOrders.getOrder().getBuyerID());

        //update the status of the order
        Task t1 = BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                .document(booksOrders.getBook().getBooks_sellID().getId())
                .collection(BookbayFirestoreReferences.ORDERS_COLLECTION)
                .document(booksOrders.getOrder().getOrderID().getId())
                .update(data);

        // Notify the user that the seller declined or confirmed
        Task t2 = BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.NOTIFICATIONS_COLLECTION)
                .add(notification);

        if (bookStatus.equals(BookStatus.CONFIRMED.name())) {

            Map<String, Object> available = new HashMap<>();

            available.put(BookbayFirestoreReferences.AVAILABLE_FIELD, false);

            Task t3 = BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION).document(booksOrders.getBook().getBooks_sellID().getId())
                    .update(available);

            Tasks.whenAllSuccess(t1, t2, t3)
                    .addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                        @Override
                        public void onSuccess(List<Object> objects) {
                            BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                                    .document(booksOrders.getBook().getBooks_sellID().getId())
                                    .collection(BookbayFirestoreReferences.ORDERS_COLLECTION)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {

                                                Map<String, Object> declineAll = new HashMap<>();

                                                declineAll.put(BookbayFirestoreReferences.STATUS_FIELD, BookStatus.DECLINED.name());
                                                declineAll.put(BookbayFirestoreReferences.NOTIFICATION_DATE_TIME_FIELD, cal.getTime());

                                                Log.d("ORDER TEST", booksOrders.getOrder().getOrderID().getId());

                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d("ORDER TEST", document.getId());

                                                    // do not update the one that was just confirmed
                                                    if (!document.getId().equals(booksOrders.getOrder().getOrderID().getId())) {
                                                        BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                                                                .document(booksOrders.getBook().getBooks_sellID().getId())
                                                                .collection(BookbayFirestoreReferences.ORDERS_COLLECTION)
                                                                .document(document.getId())
                                                                .update(declineAll);
                                                    }
                                                }
                                            }

                                            ((SellingOrdersActivity) context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ((SellingOrdersActivity) context).updateDataAndAdapter();

                                                    progressDialog.setCanceledOnTouchOutside(true);
                                                    progressDialog.setMessage("Success!");
                                                    progressDialog.dismiss();
                                                }
                                            });
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception updateStatus) {
                            Log.d("TEST 2", updateStatus.getMessage());

                            progressDialog.setCanceledOnTouchOutside(true);
                            progressDialog.setMessage("Error occurred. Please try again.");
                            progressDialog.dismiss();
                        }
                    });

        } else if (bookStatus.equals(BookStatus.DECLINED.name())) {

            Tasks.whenAllSuccess(t1, t2)
                    .addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                        @Override
                        public void onSuccess(List<Object> objects) {
                            ((SellingOrdersActivity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((SellingOrdersActivity) context).updateDataAndAdapter();

                                    progressDialog.setCanceledOnTouchOutside(true);
                                    progressDialog.setMessage("Success!");
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception updateStatus) {
                            Log.d("TEST 2", updateStatus.getMessage());

                            progressDialog.setCanceledOnTouchOutside(true);
                            progressDialog.setMessage("Error occurred. Please try again.");
                            progressDialog.dismiss();
                        }
                    });

        }
    }

    public static FirestoreRecyclerOptions<Notifications> findNotificationOptions (String buyerID) {
        Query myNotificationsQuery = BookbayFirestoreReferences.getFirestoreInstance()
                .collection(BookbayFirestoreReferences.NOTIFICATIONS_COLLECTION)
                .whereEqualTo(BookbayFirestoreReferences.BUYER_ID_UID_FIELD, buyerID)
                .orderBy(BookbayFirestoreReferences.NOTIFICATION_DATE_TIME_FIELD, Direction.DESCENDING);

        FirestoreRecyclerOptions<Notifications> notifOptions = new FirestoreRecyclerOptions.Builder<Notifications>()
                .setQuery(myNotificationsQuery, Notifications.class)
                .build();

        return notifOptions;
    }

    public static void placeOrder(String  bookID, ProgressDialog progress, Context activityContext){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                .document(bookID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().getString(BookbayFirestoreReferences.OWNER_ID_UID_FIELD).equals(user.getUid())){
                                Context context = progress.getContext();
                                progress.dismiss();
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                                        .setTitle("Order Unsuccessful")
                                        .setMessage("You cannot order your own book! Please try purchasing a book from other sellers.")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                ((Activity) activityContext).finish();
                                            }
                                        });
                                Dialog dialog = dialogBuilder.create();
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();
                            }
                            else{
                                BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                                        .document(bookID)
                                        .collection(BookbayFirestoreReferences.ORDERS_COLLECTION)
                                        .whereEqualTo(BookbayFirestoreReferences.BUYER_ID_UID_FIELD, user.getUid())
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult().size() == 0) {
                                                BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                                                        .document(bookID)
                                                        .collection(BookbayFirestoreReferences.ORDERS_COLLECTION)
                                                        .add(new Orders(user.getUid(), cal.getTime(), BookStatus.PENDING.name(), null, user.getDisplayName(), user.getPhotoUrl().toString()))
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                Context context = progress.getContext();
                                                                progress.dismiss();
                                                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                                                                        .setTitle("Order Successful")
                                                                        .setMessage("Your order has successfully been placed!")
                                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                dialog.dismiss();
                                                                                ((Activity) activityContext).finish();
                                                                            }
                                                                        });
                                                                Dialog dialog = dialogBuilder.create();
                                                                dialog.setCanceledOnTouchOutside(false);
                                                                dialog.show();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull @NotNull Exception e) {
                                                                Context context = progress.getContext();
                                                                progress.dismiss();
                                                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                                                                        .setTitle("Order Unsuccessful")
                                                                        .setMessage("An error is encountered while processing your order, please try again later")
                                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                dialog.dismiss();
                                                                                ((Activity) activityContext).finish();
                                                                            }
                                                                        });
                                                                Dialog dialog = dialogBuilder.create();
                                                                dialog.setCanceledOnTouchOutside(false);
                                                                dialog.show();
                                                            }
                                                        });
                                            } else {
                                                Context context = progress.getContext();
                                                progress.dismiss();
                                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                                                        .setTitle("Order Unsuccessful")
                                                        .setMessage("You have already ordered this book!")
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                                ((Activity) activityContext).finish();
                                                            }
                                                        });
                                                Dialog dialog = dialogBuilder.create();
                                                dialog.setCanceledOnTouchOutside(false);
                                                dialog.show();
                                            }
                                        } else {
                                            //unsuccessful
                                            Context context = progress.getContext();
                                            progress.dismiss();
                                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                                                    .setTitle("Order Unsuccessful")
                                                    .setMessage("An error is encountered while processing your order, please try again later")
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                            ((Activity) activityContext).finish();
                                                        }
                                                    });
                                            Dialog dialog = dialogBuilder.create();
                                            dialog.setCanceledOnTouchOutside(false);
                                            dialog.show();
                                        }
                                    }
                                });
                            }
                        }
                        else{
                            Context context = progress.getContext();
                            progress.dismiss();
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                                    .setTitle("Order Unsuccessful")
                                    .setMessage("An error is encountered while processing your order, please try again later")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            ((Activity) activityContext).finish();
                                        }
                                    });
                            Dialog dialog = dialogBuilder.create();
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();
                        }
                    }
                });


    }

    public static void AddBook(ProgressDialog progressDialog, Uri imageUri, Books_sell book, Context context) {
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
                ((AddBookActivity)context).finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.setMessage("Error occurred. Please try again.");
            }
        });
    }

    public static void editBookWithImage(ProgressDialog progressDialog, String bookID, Uri imageUri, Map<String, Object> updateBook, Context context) {
        CollectionReference bookRef = BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION);

        StorageReference photoRefAdd = BookbayFirestoreReferences.getStorageReferenceInstance()
                .child(BookbayFirestoreReferences.generateNewImagePath(bookID, imageUri));

        //upload the image to the Firebase
        Task uploadFirebase = photoRefAdd.putFile(imageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage("Uploaded  " + (int) progress + "%");
            }
        });

        Task update = bookRef.document(bookID).update(updateBook);

        Tasks.whenAllSuccess(uploadFirebase, update).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> objects) {
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.setMessage("Success!");
                ((AddBookActivity)context).finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.setMessage("Error occurred. Please try again.");
            }
        });
    }

    public static void editBookNoImage(String bookID, Map<String, Object> updateBook) {
        CollectionReference bookRef = BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION);
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
