package com.mobdeve.s15.group.nine.bookbay;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class OrdersAdapter extends FirestoreRecyclerAdapter<Books_sell, OrdersViewHolder> {

    private int whichView;
    private String prevBookStatus = BookStatus.PENDING.name();

    // DB reference
    private FirebaseFirestore dbRef;

    public OrdersAdapter(FirestoreRecyclerOptions<Books_sell> options) {
        super(options);

        this.dbRef = BookbayFirestoreReferences.getFirestoreInstance();
    }

    public void setViewType(int whichView) {
        this.whichView = whichView;
    }

    @Override
    public OrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the appropriate layout based on the viewType returned and generate the itemView.
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.view_my_selling_orders_layout, parent, false);

        OrdersViewHolder holder = new OrdersViewHolder(itemView);

        return holder;

    }



    @Override
    public void onBindViewHolder(OrdersViewHolder holder, int position, Books_sell book) {
        holder.bindData(book, whichView);
        holder.setItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO: DIALOGUE
//                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//                builder.setMessage("CHANGE BLAH BLAH BLAH");
//                builder.setCancelable(true);
//
//                builder.setPositiveButton(
//                        "Continue",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//
//                            }
//                        });
//
//                builder.setNegativeButton(
//                        "Cancel",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//
//                AlertDialog alert = builder.create();
//                alert.show();

                String bookStatus = parent.getItemAtPosition(position).toString();
                Log.d("TEST", prevBookStatus + " " + bookStatus + " " + position + " " + book.getBooks_sellID().getId());

                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));

                Map<String, Object> data = new HashMap<>();
                data.put(BookbayFirestoreReferences.STATUS_FIELD, bookStatus);
                data.put(BookbayFirestoreReferences.NOTIFICATION_DATE_TIME_FIELD, cal.getTime());

                if (!prevBookStatus.equals(bookStatus)) {
                    dbRef.collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                            .document(book.getBooks_sellID().getId())
                            .update(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TEST2", bookStatus + " " + position + " " + book.getBooks_sellID().getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Log.d("TEST 2", e.getMessage());
                                }
                            });
//                    prevBookStatus = bookStatus;
                    Log.d("TEST", prevBookStatus + " " + bookStatus + " " + position + " " + book.getBooks_sellID().getId());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
