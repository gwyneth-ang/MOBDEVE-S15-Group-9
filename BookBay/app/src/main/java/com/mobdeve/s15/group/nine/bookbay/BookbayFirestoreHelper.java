package com.mobdeve.s15.group.nine.bookbay;

import android.util.Log;
import android.content.Context;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookbayFirestoreHelper {
    public static void findAllBooksAvailable(ThriftStoreSellingBooksAdapter thriftStoreSellingBooksAdapter) {
        BookbayFirestoreReferences.getFirestoreInstance().collectionGroup(BookbayFirestoreReferences.ORDERS_COLLECTION)
                .whereNotEqualTo(BookbayFirestoreReferences.STATUS_FIELD, BookStatus.CONFIRMED.name())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            Log.d("TEST", "Hi");

                            ArrayList<Books_sell> books = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TEST", "In the loop" + document.getReference().getId());
                                document.getReference().getParent().getParent().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(Task<DocumentSnapshot> task3) {
                                        if(task.isSuccessful()) {
                                            com.mobdeve.s15.group.nine.bookbay.Books_sell temp = task3.getResult().toObject(com.mobdeve.s15.group.nine.bookbay.Books_sell.class);

                                            Boolean same = false;

                                            for (int i = 0; i < books.size();i++) {
                                                if (books.get(i).getBooks_sellID().getId().equals(temp.getBooks_sellID().getId()))
                                                    same = true;
                                            }
                                            if (!same)
                                                books.add(temp);

                                        } else {
                                            Log.d("TEST", "Error getting documents: ", task.getException());
                                        }

                                        thriftStoreSellingBooksAdapter.setData(books);
                                        thriftStoreSellingBooksAdapter.notifyDataSetChanged();

                                    }
                                });
                            }
                        } else {
                            Log.d("TEST", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public static void findAllBooksAvailableSeller(ThriftStoreSellingBooksAdapter sellingBookAdapter, String sellerUID) {
        BookbayFirestoreReferences.getFirestoreInstance().collectionGroup(BookbayFirestoreReferences.ORDERS_COLLECTION)
                .whereNotEqualTo(BookbayFirestoreReferences.STATUS_FIELD, BookStatus.CONFIRMED.name())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("TEST", "Hi");

                            ArrayList<Books_sell> books = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TEST", "In the loop" + document.getReference().getId());
                                document.getReference().getParent().getParent().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(Task<DocumentSnapshot> task3) {
                                        if (task.isSuccessful()) {
                                            Books_sell temp = task3.getResult().toObject(Books_sell.class);

                                            Boolean same = false;

                                            for (int i = 0; i < books.size(); i++) {
                                                if (books.get(i).getBooks_sellID().getId().equals(temp.getBooks_sellID().getId()))
                                                    same = true;
                                            }

                                            if (!same && temp.getOwnerID().equals(sellerUID))
                                                books.add(temp);

                                        } else {
                                            Log.d("TEST", "Error getting documents: ", task.getException());
                                        }

                                        sellingBookAdapter.setData(books);
                                        sellingBookAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                    }
                });
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

    public static void updateStatusAndNotifications (BooksOrders booksOrders, Map<String, Object> data, Context context, Map<String, Object> notification) {

        Task t1 = BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                .document(booksOrders.getBook().getBooks_sellID().getId())
                .collection(BookbayFirestoreReferences.ORDERS_COLLECTION)
                .document(booksOrders.getOrder().getOrderID().getId())
                .update(data);

        Task t2 = BookbayFirestoreReferences.getFirestoreInstance().collection(BookbayFirestoreReferences.NOTIFICATIONS_COLLECTION)
                .add(notification);

        Tasks.whenAllSuccess(t1, t2)
                .addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> objects) {

                        //TODO: change all orders of this book to decline when a seller approved
                        ((SellingOrdersActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((SellingOrdersActivity) context).updateDataAndAdapter();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception updateStatus) {
                        Log.d("TEST 2", updateStatus.getMessage());
                    }
                });
    }

    public static FirestoreRecyclerOptions<Notifications> findNotificationOptions (String buyerID) {
        Query myNotificationsQuery = BookbayFirestoreReferences.getFirestoreInstance()
                .collection(BookbayFirestoreReferences.NOTIFICATIONS_COLLECTION)
                .whereEqualTo(BookbayFirestoreReferences.BUYER_ID_UID_FIELD, buyerID)
                .orderBy(BookbayFirestoreReferences.NOTIFICATION_DATE_TIME_FIELD, Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Notifications> notifOptions = new FirestoreRecyclerOptions.Builder<Notifications>()
                .setQuery(myNotificationsQuery, Notifications.class)
                .build();

        return notifOptions;
    }
}
