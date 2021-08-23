package com.mobdeve.s15.group.nine.bookbay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

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
                            java.util.ArrayList<com.mobdeve.s15.group.nine.bookbay.Books_sell> books = new ArrayList<>();
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
}
