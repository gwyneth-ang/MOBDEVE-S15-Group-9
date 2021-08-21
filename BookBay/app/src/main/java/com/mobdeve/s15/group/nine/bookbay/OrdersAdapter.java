package com.mobdeve.s15.group.nine.bookbay;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class OrdersAdapter extends FirestoreRecyclerAdapter<Books_sell, OrdersViewHolder> {

    private int whichView;

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

                String bookStatus = parent.getItemAtPosition(position).toString();
                Log.d("TEST", bookStatus + " " + position + " " + book.getBooks_sellID().getId());

                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));

                Map<String, Object> data = new HashMap<>();
                data.put(BookbayFirestoreReferences.STATUS_FIELD, bookStatus);
                data.put(BookbayFirestoreReferences.NOTIFICATION_DATE_TIME_FIELD, cal.getTime());

                dbRef.collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                        .document(book.getBooks_sellID().getId())
                        .update(data)
                        .addOnSuccessListener(new OnSuccessListener< Void >() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TEST2", bookStatus + " " + position + " " + book.getBooks_sellID().getId());
                            }
                        });

//                holder.bindData(book, whichView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
