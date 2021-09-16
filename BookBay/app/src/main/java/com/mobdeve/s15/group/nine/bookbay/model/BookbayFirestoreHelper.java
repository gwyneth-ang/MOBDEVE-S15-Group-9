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
import androidx.annotation.Nullable;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
import com.mobdeve.s15.group.nine.bookbay.SortByPriceAsc;
import com.mobdeve.s15.group.nine.bookbay.SortByPriceDes;
import com.mobdeve.s15.group.nine.bookbay.SortByTitleAsc;
import com.mobdeve.s15.group.nine.bookbay.SortByTitleDes;
import com.mobdeve.s15.group.nine.bookbay.ThriftStoreSellingBooksAdapter;
import com.mobdeve.s15.group.nine.bookbay.callback.CanEditCallback;
import com.mobdeve.s15.group.nine.bookbay.callback.NumBookCallBack;

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

    /* For Reference:
     *      Firebase Firestore
     *          Books_sell
     *              -> id
     *                  -> addBookDate (Timestamp)
     *                  -> available (Boolean)
     *                  -> bookAuthor (String)
     *                  -> bookTitle (String)
     *                  -> condition (String)
     *                  -> imageUri (String)
     *                  -> ownerID (String - Uid)
     *                  -> price (Number)
     *                  -> profileImage (String - url of Google Profile Image)
     *                  -> profileName (String)
     *                  -> review (String)
     *                  -> Orders (Collection)
     *                      -> id
     *                          -> buyerEmail (String)
     *                          -> buyerID (String)
     *                          -> buyerImage (String - url of Google Profile Image)
     *                          -> buyerName (String)
     *                          -> notificationDateTime (Timestamp)
     *                          -> orderDate (Timestamp)
     *                          -> status (String)
     *          Notifications
     *              -> id
     *                  -> bookID (String)
     *                  -> bookTitle (String)
     *                  -> buyerID (String)
     *                  -> image (String)
     *                  -> notificationDateTime (Timestamp)
     *                  -> profileName (String)
     *                  -> status (String)
     *
     *      Firebase Storage
     *          -> images/
     *              -> book_id + <name of image>
     * */

    /**
     * Function to find all the books available in the firestore
     * @param thriftStoreAdapter Adapter for thrift store
     */
    public static void findAllBooksAvailable(ThriftStoreSellingBooksAdapter thriftStoreAdapter) {
        BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
            .whereEqualTo(BookbayFirestoreReferences.AVAILABLE_FIELD, true)
            .orderBy(BookbayFirestoreReferences.ADD_BOOK_DATE_FIELD, Direction.DESCENDING)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        ArrayList<Books_sell> books = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult())
                            books.add(document.toObject(Books_sell.class));

                        // Set Data
                        thriftStoreAdapter.setData(books);
                        // Notify data changed
                        thriftStoreAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("TEST", "Error getting documents: ", task.getException());
                    }
                }
            });
    }

    /**
     * Function to find all the books available that is owned by the seller in the firestore
     * @param sellingBookAdapter
     * @param sellerUID
     */
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
                        ArrayList<Books_sell> books = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult())
                            books.add(document.toObject(Books_sell.class));

                        // Set Data
                        sellingBookAdapter.setData(books);
                        // Notify data changed
                        sellingBookAdapter.notifyDataSetChanged();
                    }
                }
            });
    }

    /**
     * Search all the books owned by the seller with title or author based on the searchText and the sorting/filtering based on the sort type
     * @param searchText
     * @param sortType
     * @param sellingBooksAdapter
     * @param sellerUID
     * @param context
     */
    public static void searchFilterBooksSeller(String searchText, int sortType, ThriftStoreSellingBooksAdapter sellingBooksAdapter, String sellerUID, Context context) {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Searching books for you");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Query t1 = BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                .whereEqualTo(BookbayFirestoreReferences.AVAILABLE_FIELD, true)
                .whereGreaterThanOrEqualTo(BookbayFirestoreReferences.BOOK_TITLE_FIELD, searchText)
                .whereLessThanOrEqualTo(BookbayFirestoreReferences.BOOK_TITLE_FIELD, searchText + "\uF7FF")
                .whereEqualTo(BookbayFirestoreReferences.OWNER_ID_UID_FIELD, sellerUID);

        Query t2 = BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                .whereEqualTo(BookbayFirestoreReferences.AVAILABLE_FIELD, true)
                .whereGreaterThanOrEqualTo(BookbayFirestoreReferences.BOOK_AUTHOR_FIELD, searchText)
                .whereLessThanOrEqualTo(BookbayFirestoreReferences.BOOK_AUTHOR_FIELD, searchText + "\uF7FF")
                .whereEqualTo(BookbayFirestoreReferences.OWNER_ID_UID_FIELD, sellerUID);

        Tasks.whenAllSuccess(t1.get(), t2.get())
                .addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> list) {
                        ArrayList<Books_sell> temp = new ArrayList<>();
                        ArrayList<Books_sell> books = new ArrayList<>();

                        QuerySnapshot a = ((QuerySnapshot) list.get(0));
                        QuerySnapshot b = ((QuerySnapshot) list.get(1));

                        temp.addAll(a.toObjects(Books_sell.class));
                        temp.addAll(b.toObjects(Books_sell.class));

                        //Removing Duplicates
                        for (Books_sell book : temp) {
                            if (!books.contains(book)) {
                                books.add(book);
                            }
                        }

                        // perform sorting when necessary
                        if (sortType == R.id.menu_sort_a_to_z) {
                            Collections.sort(books, new SortByTitleAsc());
                        } else if (sortType == R.id.menu_sort_z_to_a) {
                            Collections.sort(books, new SortByTitleDes());
                        } else if (sortType == R.id.menu_sort_low_to_high) {
                            Collections.sort(books, new SortByPriceAsc());
                        } else if (sortType == R.id.menu_sort_high_to_low) {
                            Collections.sort(books, new SortByPriceDes());
                        }

                        // Set Data
                        sellingBooksAdapter.setData(books);
                        // Notify data changed
                        sellingBooksAdapter.notifyDataSetChanged();

                        progressDialog.dismiss();
                    }
                });
    }

    /**
     * Search all the books with title or author based on the searchText and the sorting/filtering based on the sort type
     * @param searchText
     * @param sortType
     * @param thriftAdapter
     * @param context
     */
    public static void searchFilterBooks(String searchText, int sortType, ThriftStoreSellingBooksAdapter thriftAdapter, Context context) {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Searching books for you");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

            Query t1 = BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                    .whereEqualTo(BookbayFirestoreReferences.AVAILABLE_FIELD, true)
                    .whereGreaterThanOrEqualTo(BookbayFirestoreReferences.BOOK_TITLE_FIELD, searchText)
                    .whereLessThanOrEqualTo(BookbayFirestoreReferences.BOOK_TITLE_FIELD, searchText + "\uF7FF");

            Query t2 = BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                    .whereEqualTo(BookbayFirestoreReferences.AVAILABLE_FIELD, true)
                    .whereGreaterThanOrEqualTo(BookbayFirestoreReferences.BOOK_AUTHOR_FIELD, searchText)
                    .whereLessThanOrEqualTo(BookbayFirestoreReferences.BOOK_AUTHOR_FIELD, searchText + "\uF7FF");

            Tasks.whenAllSuccess(t1.get(), t2.get())
                    .addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                        @Override
                        public void onSuccess(List<Object> list) {
                            ArrayList<Books_sell> temp = new ArrayList<>();
                            ArrayList<Books_sell> books = new ArrayList<>();

                            QuerySnapshot a = ((QuerySnapshot) list.get(0));
                            QuerySnapshot b = ((QuerySnapshot) list.get(1));

                            temp.addAll(a.toObjects(Books_sell.class));
                            temp.addAll(b.toObjects(Books_sell.class));

                            //Removing Duplicates from title and author
                            for (Books_sell book : temp) {
                                if (!books.contains(book)) {
                                    books.add(book);
                                }
                            }

                            // perform sorting when necessary
                            if (sortType == R.id.menu_sort_a_to_z) {
                                Collections.sort(books, new SortByTitleAsc());
                            } else if (sortType == R.id.menu_sort_z_to_a) {
                                Collections.sort(books, new SortByTitleDes());
                            } else if (sortType == R.id.menu_sort_low_to_high) {
                                Collections.sort(books, new SortByPriceAsc());
                            } else if (sortType == R.id.menu_sort_high_to_low) {
                                Collections.sort(books, new SortByPriceDes());
                            }

                            // Set Data
                            thriftAdapter.setData(books);
                            // Notify data changed
                            thriftAdapter.notifyDataSetChanged();

                            progressDialog.dismiss();
                        }
                    });
    }

    /**
     * Find all the orders of the buyer
     * @param myOrdersAdapter
     * @param buyerUID
     * @param progressDialog
     */
    public static void findBuyerOrders(OrdersAdapter myOrdersAdapter, String buyerUID, ProgressDialog progressDialog) {
        BookbayFirestoreReferences.getFirestoreInstance().collectionGroup(BookbayFirestoreReferences.ORDERS_COLLECTION)
                .whereEqualTo(BookbayFirestoreReferences.BUYER_ID_UID_FIELD, buyerUID)
                .orderBy(BookbayFirestoreReferences.ORDER_DATE_FIELD, Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<BooksOrders> booksOrders = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
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

                                        // sort the orders
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

    /**
     * Find all the orders of the seller
     * @param sellerOrdersAdapter
     * @param sellerUID
     * @param progressDialog
     */
    public static void findSellerOrders(OrdersAdapter sellerOrdersAdapter, String sellerUID, ProgressDialog progressDialog) {
        BookbayFirestoreReferences.getFirestoreInstance().collectionGroup(BookbayFirestoreReferences.ORDERS_COLLECTION)
                .orderBy(BookbayFirestoreReferences.ORDER_DATE_FIELD, Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<BooksOrders> booksOrders = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
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

                                        // sort the orders
                                        Collections.sort(booksOrders, new SortByOrderDate());

                                        sellerOrdersAdapter.setData(booksOrders);
                                        sellerOrdersAdapter.notifyDataSetChanged();
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

    /**
     * Update the status to either confirmed or declined and add it to the notifications
     * @param booksOrders
     * @param position
     * @param bookStatus
     * @param context
     * @param ordersAdapter
     */
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

        // If confirmed
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

                                                // Decline all other orders when the status is confirmed
                                                for (QueryDocumentSnapshot document : task.getResult()) {
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

        } else if (bookStatus.equals(BookStatus.DECLINED.name())) { // when the status is changed to declined

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

    // Decline others and made a notification for those declined orders
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

    /**
     * Find all the notifications of the buyer
     * @param buyerID
     * @return FirestoreRecyclerOptions<Notifcations>
     */
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

    /**
     * Place order
     * @param bookID
     * @param progress
     * @param activityContext
     */
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
                            } else if(!task.getResult().getBoolean(BookbayFirestoreReferences.AVAILABLE_FIELD)){
                                Context context = progress.getContext();
                                progress.dismiss();
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                                        .setTitle("Order Unsuccessful")
                                        .setMessage("Book is no longer available")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                ((Activity) activityContext).finish();
                                            }
                                        });
                                Dialog dialog = dialogBuilder.create();
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();
                            }else {
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

    /**
     * Add a book to the firestore
     * @param progressDialog
     * @param imageUri
     * @param book
     * @param context
     */
    public static void AddBook(ProgressDialog progressDialog, Uri imageUri, Books_sell book, Context context) {
        CollectionReference bookRef = BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION);
        String ID = UUID.randomUUID().toString();

        StorageReference imageRef = BookbayFirestoreReferences.getStorageReferenceInstance()
                .child(BookbayFirestoreReferences.generateNewImagePath(ID, imageUri));
        imageRef.putFile(imageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage("Uploaded  " + (int) progress + "%");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                bookRef.document(ID).set(book).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.setCanceledOnTouchOutside(true);
                        progressDialog.setMessage("Success!");

                        Intent return_intent = new Intent();
                        ((AddBookActivity) context).setResult(Activity.RESULT_OK, return_intent);
                        ((AddBookActivity) context).finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        deleteBookImageFromStorage(ID, imageUri);
                        progressDialog.setCanceledOnTouchOutside(true);
                        progressDialog.setMessage("Error occurred. Please try again.");
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.setMessage("Error occurred. Please try again.");
            }
        });
//        //task 1 - upload the image to the Firebase
//        Task t1 = imageRef.putFile(imageUri)
//                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
//                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
//                        progressDialog.setCanceledOnTouchOutside(false);
//                        progressDialog.setMessage("Uploaded  " + (int) progress + "%");
//                    }
//                });
//
//        //adding the book to the book_sell collection
//        Task t2 = bookRef.document(ID).set(book);
//
//
//        Tasks.whenAllSuccess(t1, t2).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
//            @Override
//            public void onSuccess(List<Object> objects) {
//                progressDialog.setCanceledOnTouchOutside(true);
//                progressDialog.setMessage("Success!");
//
//                Intent return_intent = new Intent();
//                ((AddBookActivity) context).setResult(Activity.RESULT_OK, return_intent);
//                ((AddBookActivity) context).finish();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull @NotNull Exception e) {
//                progressDialog.setCanceledOnTouchOutside(true);
//                progressDialog.setMessage("Error occurred. Please try again.");
//            }
//        });
    }

    /**
     * Delete image from firebase storage
     * @param bookID
     * @param tempUri
     */
    public static void deleteBookImageFromStorage(String bookID, Uri tempUri) {
        StorageReference photoRef = BookbayFirestoreReferences.getStorageReferenceInstance()
                .child(BookbayFirestoreReferences.generateNewImagePath(bookID, tempUri));

        // Delete picture from firebase storage
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

    /**
     * Check if the book can be edited (if there are no orders, then the owner can edit)
     * @param bookID
     * @param canEditCallback
     */
    public static void canEdit(String bookID, CanEditCallback canEditCallback){
        BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                .document(bookID).collection(BookbayFirestoreReferences.ORDERS_COLLECTION)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.getResult().size() != 0) {
                    canEditCallback.canEdit(false);
                } else {
                    canEditCallback.canEdit(true);
                }
            }
        });
    }

    /**
     * Edit book with an image changed
     * @param progressDialog
     * @param bookID
     * @param imageUri
     * @param updateBook
     * @param context
     * @param title
     * @param author
     * @param selectorChoice
     * @param price
     * @param review
     */
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

    /**
     * Edit book with no image changed
     * @param bookID
     * @param updateBook
     */
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

    /**
     * Delete the book from the firestore
     * @param bookID
     * @param progress
     * @param context
     */
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

                                            Intent return_intent = new Intent();
                                            ((Activity) context).setResult(10, return_intent);
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

    /**
     * Find the total number of books that the seller is selling that are available
     * @param userUid
     * @param numBookCallBack
     */
    public static void findTotalSellingBooks (String userUid, NumBookCallBack numBookCallBack) {
        BookbayFirestoreReferences.getFirestoreInstance()
            .collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
            .whereEqualTo(BookbayFirestoreReferences.OWNER_ID_UID_FIELD, userUid)
            .whereEqualTo(BookbayFirestoreReferences.AVAILABLE_FIELD, true)
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e == null) {
                        numBookCallBack.totalBooks(value.size());
                    }
                    else{
                        numBookCallBack.totalBooks(-1);
                    }
                }
            });
    }
}

