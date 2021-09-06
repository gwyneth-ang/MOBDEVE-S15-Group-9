package com.mobdeve.s15.group.nine.bookbay.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.mobdeve.s15.group.nine.bookbay.AddBookActivity;
import com.mobdeve.s15.group.nine.bookbay.BookStatus;
import com.mobdeve.s15.group.nine.bookbay.BooksOrders;
import com.mobdeve.s15.group.nine.bookbay.OrdersAdapter;
import com.mobdeve.s15.group.nine.bookbay.R;
import com.mobdeve.s15.group.nine.bookbay.SellingOrdersActivity;
import com.mobdeve.s15.group.nine.bookbay.SortByOrderDate;
import com.mobdeve.s15.group.nine.bookbay.ThriftStoreSellingBooksAdapter;
import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreReferences;
import com.mobdeve.s15.group.nine.bookbay.model.Books_sell;
import com.mobdeve.s15.group.nine.bookbay.model.Notifications;
import com.mobdeve.s15.group.nine.bookbay.model.Orders;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                    if (task.isSuccessful()) {
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

    public static void searchWithSortAllBooks(ThriftStoreSellingBooksAdapter thriftStoreAdapter, String searchText, int sortType) {

        Direction direction = null;
        String filter = null;

        if (sortType == R.id.menu_sort_a_to_z) {
            direction = Direction.ASCENDING;
            filter = BookbayFirestoreReferences.BOOK_TITLE_FIELD;
        } else if (sortType == R.id.menu_sort_z_to_a) {
            direction = Direction.DESCENDING;
            filter = BookbayFirestoreReferences.BOOK_TITLE_FIELD;
        } else if (sortType == R.id.menu_sort_low_to_high) {
            direction = Direction.ASCENDING;
            filter = BookbayFirestoreReferences.PRICE_FIELD;
        } else if (sortType == R.id.menu_sort_high_to_low) {
            direction = Direction.DESCENDING;
            filter = BookbayFirestoreReferences.PRICE_FIELD;
        }

        //FIXME: substrings
        Task task1 = BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
            .whereEqualTo(BookbayFirestoreReferences.AVAILABLE_FIELD, true)
            .whereEqualTo(BookbayFirestoreReferences.BOOK_TITLE_FIELD, searchText)
            .orderBy(filter, direction)
            .get();

        Task task2 = BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
            .whereEqualTo(BookbayFirestoreReferences.AVAILABLE_FIELD, true)
            .whereEqualTo(BookbayFirestoreReferences.BOOK_AUTHOR_FIELD, searchText)
            .orderBy(filter, direction)
            .get();

        Tasks.whenAllSuccess(task1, task2)
            .addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                @Override
                public void onSuccess(List<Object> list) {
                    ArrayList<Books_sell> books = new ArrayList<>();

                    List<Books_sell> titles = ((QuerySnapshot) list.get(0)).toObjects(Books_sell.class);
                    List<Books_sell> authors  = ((QuerySnapshot) list.get(1)).toObjects(Books_sell.class);

                    int title_ctr = 0, author_ctr = 0;

                    // DESCENDING
//                    if (sortType == R.id.menu_sort_high_to_low || sortType == R.id.menu_sort_z_to_a) {
//                        if (titles.get(title_ctr) )
//                            Books_sell temp_author = ((DocumentSnapshot) list.get(0)).toObject(Books_sell.class);
//
//                        //                        ((DocumentSnapshot) list.get(1)).toObject(User.class)
//                    }


                    Log.d("TEST", "Hi");

//                    for (QueryDocumentSnapshot document : task.getResult())
//                        books.add(document.toObject(Books_sell.class));

                    thriftStoreAdapter.setData(books);
                    thriftStoreAdapter.notifyDataSetChanged();
                }
            });


    }

    public static void searchFilterBooks(String searchText, String strDirection, String whichFilter, ThriftStoreSellingBooksAdapter adapter) {

        Direction direction = Direction.ASCENDING;
        if (strDirection.equals("DES")) {
            direction = Direction.DESCENDING;
        }

        String filter = BookbayFirestoreReferences.BOOK_TITLE_FIELD;

        if (whichFilter.equals(BookbayFirestoreReferences.PRICE_FIELD)) {
            filter = BookbayFirestoreReferences.PRICE_FIELD;
        }

        // FIXME: wrong because where euqal to title and author should be 'OR'
        BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                .whereEqualTo(BookbayFirestoreReferences.AVAILABLE_FIELD, true)
                .whereEqualTo(BookbayFirestoreReferences.BOOK_TITLE_FIELD, searchText)
                .whereEqualTo(BookbayFirestoreReferences.BOOK_AUTHOR_FIELD, searchText)
                .orderBy(filter, direction)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
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

    public static void searchFilterBooksSeller(String searchText, String strDirection, String whichFilter, ThriftStoreSellingBooksAdapter adapter, String sellerUID) {

        Direction direction = Direction.ASCENDING;
        if (strDirection.equals("DES")) {
            direction = Direction.DESCENDING;
        }

        String filter = BookbayFirestoreReferences.BOOK_TITLE_FIELD;

        if (whichFilter.equals(BookbayFirestoreReferences.PRICE_FIELD)) {
            filter = BookbayFirestoreReferences.PRICE_FIELD;
        }

        // FIXME: wrong because where euqal to title and author should be 'OR'
        BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                .whereEqualTo(BookbayFirestoreReferences.AVAILABLE_FIELD, true)
                .whereEqualTo(BookbayFirestoreReferences.BOOK_TITLE_FIELD, searchText)
                .whereEqualTo(BookbayFirestoreReferences.BOOK_AUTHOR_FIELD, searchText)
                .whereEqualTo(BookbayFirestoreReferences.OWNER_ID_UID_FIELD, sellerUID)
                .orderBy(filter, direction)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
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

    public static void findBuyerOrders(OrdersAdapter myOrdersAdapter, String buyerUID, ProgressDialog progressDialog) {
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

                                        Collections.sort(booksOrders, new SortByOrderDate());

                                        myOrdersAdapter.setData(booksOrders);
                                        myOrdersAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }

                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    public static void findSellerOrders(OrdersAdapter sellerOrdersAdapter, String sellerUID, ProgressDialog progressDialog) {
        BookbayFirestoreReferences.getFirestoreInstance().collectionGroup(BookbayFirestoreReferences.ORDERS_COLLECTION)
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

                                        Collections.sort(booksOrders, new SortByOrderDate());

                                        sellerOrdersAdapter.setData(booksOrders);
                                        sellerOrdersAdapter.notifyDataSetChanged();

                                        Log.d("SELLER ORDERS", String.valueOf(booksOrders.size()));
                                    }
                                });
                            }
                        }

                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    public static void updateStatusAndNotifications(BooksOrders booksOrders, int position, String bookStatus, Context context, OrdersAdapter ordersAdapter) {

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

        notification.put(BookbayFirestoreReferences.BOOK_ID_FIELD, booksOrders.getBook().getBooks_sellID().getId());
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

                                                    // if statement - do not update the one that was just confirmed
                                                    if (!document.getId().equals(booksOrders.getOrder().getOrderID().getId())) {
                                                        String buyerID = document.getString(BookbayFirestoreReferences.BUYER_ID_UID_FIELD);
                                                        declineOthersAndNotify(declineAll, booksOrders, document.getId(), cal, buyerID, progressDialog, context, ordersAdapter);

                                                    }
                                                }
                                            }
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

                            booksOrders.getOrder().setStatus(BookStatus.DECLINED.name());
                            ordersAdapter.notifyItemChanged(position);

                            progressDialog.setCanceledOnTouchOutside(true);
                            progressDialog.setMessage("Success!");
                            progressDialog.dismiss();
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


    public static void declineOthersAndNotify (Map<String, Object> declineAll, BooksOrders booksOrders, String orderID, Calendar cal, String buyerID, ProgressDialog progressDialog, Context context, OrdersAdapter ordersAdapter) {

        Map<String, Object> notificationDecline = new HashMap<>();

        notificationDecline.put(BookbayFirestoreReferences.BOOK_ID_FIELD, booksOrders.getBook().getBooks_sellID().getId());
        notificationDecline.put(BookbayFirestoreReferences.BOOK_TITLE_FIELD, booksOrders.getBook().getBookTitle());
        notificationDecline.put(BookbayFirestoreReferences.IMAGE_FIELD, booksOrders.getBook().getImage());
        notificationDecline.put(BookbayFirestoreReferences.PROFILE_NAME_FIELD, booksOrders.getBook().getProfileName());
        notificationDecline.put(BookbayFirestoreReferences.STATUS_FIELD, BookStatus.DECLINED.name());
        notificationDecline.put(BookbayFirestoreReferences.NOTIFICATION_DATE_TIME_FIELD, cal.getTime());
        notificationDecline.put(BookbayFirestoreReferences.BUYER_ID_UID_FIELD, buyerID);

        Task task1 = BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                .document(booksOrders.getBook().getBooks_sellID().getId())
                .collection(BookbayFirestoreReferences.ORDERS_COLLECTION)
                .document(orderID)
                .update(declineAll);

        Task task2 = BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.NOTIFICATIONS_COLLECTION)
                .add(notificationDecline);

        Tasks.whenAllSuccess(task1, task2)
                .addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> objects) {
                        Log.d("TEST 2", "Decline all orders and notify");

                        ((SellingOrdersActivity) context).updateDataAndAdapter(progressDialog);

                        progressDialog.setCanceledOnTouchOutside(true);
                        progressDialog.setMessage("Success!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.d("TEST 2", e.getMessage());

                        progressDialog.setCanceledOnTouchOutside(true);
                        progressDialog.setMessage("Error occurred. Please try again.");
                        progressDialog.dismiss();
                    }
                });

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

    public static void placeOrder(String bookID, ProgressDialog progress, Context activityContext) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                .document(bookID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().getString(BookbayFirestoreReferences.OWNER_ID_UID_FIELD).equals(user.getUid())) {
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
                            } else {
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
                                                        .add(new Orders(user.getUid(), cal.getTime(), BookStatus.PENDING.name(), null, user.getDisplayName(), user.getPhotoUrl().toString(), user.getEmail()))
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
                        } else {
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
                ((AddBookActivity) context).finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.setMessage("Error occurred. Please try again.");
            }
        });
    }

    public static void deleteBookImageFromStorage(String bookID, Uri tempUri) {
        StorageReference photoRef = BookbayFirestoreReferences.getStorageReferenceInstance()
                .child(BookbayFirestoreReferences.generateNewImagePath(bookID, tempUri));

        // Delete picture from firestore
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Log.d("Storage Result", "onSuccess: deleted file");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.d("Storage Result", "onFailure: did not delete file");
            }
        });
    }

    public static void editBookWithImage(ProgressDialog progressDialog, String bookID, Uri imageUri, Map<String, Object> updateBook, Context context, String title, String author, String selectorChoice, Float price, String review) {
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

                Intent return_intent = new Intent();
                return_intent.putExtra(AddBookActivity.BOOKID_KEY, bookID);
                return_intent.putExtra(AddBookActivity.TITLE_KEY, title);
                return_intent.putExtra(AddBookActivity.AUTHOR_KEY, author);
                return_intent.putExtra(AddBookActivity.CONDITION_KEY, selectorChoice);
                return_intent.putExtra(AddBookActivity.PRICE_KEY, price);
                return_intent.putExtra(AddBookActivity.IMAGE_KEY, imageUri.toString());
                return_intent.putExtra(AddBookActivity.REVIEW_KEY, review);
                ((AddBookActivity) context).setResult(Activity.RESULT_OK, return_intent);
                ((AddBookActivity) context).finish();
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

    public static void deleteBook (String bookID, AlertDialog progress, Context context){
        BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                .document(bookID).collection(BookbayFirestoreReferences.ORDERS_COLLECTION)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                boolean notDeclined = false;
                ArrayList<Boolean> haveOrders = new ArrayList<Boolean>();
                haveOrders.add(false);
                if (task.getResult().size() > 0) {
                    haveOrders.set(0, true);
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (!(document.getData().get(BookbayFirestoreReferences.STATUS_FIELD).toString().equals(BookStatus.DECLINED.name()))) {
                            notDeclined = true;
                        }
                    }
                }
                if(task.getResult().size() > 0 && notDeclined) {
                    progress.dismiss();
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                            .setTitle("Delete Unsuccessful")
                            .setMessage("Book cannot be deleted!")
                            .setPositiveButton("OK", null);
                    Dialog dialog = dialogBuilder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                } else {
                    BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                            .document(bookID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String image = documentSnapshot.getString(BookbayFirestoreReferences.IMAGE_FIELD);
                            BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                                    .document(bookID).collection(BookbayFirestoreReferences.ORDERS_COLLECTION).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        document.getReference().delete();
                                    }
                                    BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                                            .document(bookID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            if(!(haveOrders.get(0))){
                                                deleteBookImageFromStorage(bookID, Uri.parse(image));
                                            }
                                            progress.dismiss();
                                            ((Activity) context).finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {
                                            progress.dismiss();
                                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                                                    .setTitle("Delete Unsuccessful")
                                                    .setMessage("An error has occured please try again")
                                                    .setPositiveButton("OK", null);
                                            Dialog dialog = dialogBuilder.create();
                                            dialog.setCanceledOnTouchOutside(false);
                                            dialog.show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    progress.dismiss();
                                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                                            .setTitle("Delete Unsuccessful")
                                            .setMessage("An error has occured please try again")
                                            .setPositiveButton("OK", null);
                                    Dialog dialog = dialogBuilder.create();
                                    dialog.setCanceledOnTouchOutside(false);
                                    dialog.show();
                                }
                            });
                                }
                            });
                }
            }
        });
    }
}

