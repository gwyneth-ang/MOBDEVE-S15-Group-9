package com.mobdeve.s15.group.nine.bookbay;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class OrdersAdapter extends RecyclerView.Adapter <OrdersViewHolder> {

    private int whichView;
    private String prevBookStatus = BookStatus.PENDING.name();
    private ArrayList<BooksOrders> booksOrders;
    private Context context;

    public OrdersAdapter(Context context) {
        this.booksOrders = new ArrayList<>();
        this.context = context;
    }

    public void setData (ArrayList<BooksOrders> booksOrders) {
        this.booksOrders = booksOrders;
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
    public void onBindViewHolder(OrdersViewHolder holder, int position) {
        holder.bindData(booksOrders.get(position).getOrder(), booksOrders.get(position).getBook(), whichView);
        holder.setItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int choice, long id) {
                String bookStatus = parent.getItemAtPosition(choice).toString();
                Log.d("TEST", prevBookStatus + " " + bookStatus + " " + position + " " + booksOrders.get(position).getBook().getBooks_sellID().getId());

                if (!prevBookStatus.equals(bookStatus)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage(Html.fromHtml("Pressing Continue will notify the buyer that you " + "<b>" + bookStatus + "</b>" + " their order. You may not change it back to pending."));
                    builder.setCancelable(true);

                    builder.setPositiveButton(
                            "Continue",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    BookbayFirestoreHelper.updateStatusAndNotifications(booksOrders.get(position), bookStatus, context);

                                    Log.d("TEST", prevBookStatus + " " + bookStatus + " " + position + " " + booksOrders.get(position).getBook().getBooks_sellID().getId());
                                }
                            });

                    builder.setNegativeButton(
                            "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();

                    // set fonts for alert
                    TextView textView = (TextView) alert.findViewById(android.R.id.message);
                    Typeface font = ResourcesCompat.getFont(view.getContext(),R.font.cormorant_garamond);
                    textView.setTypeface(font);
                    textView.setTextSize(15);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return booksOrders.size();
    }
}
