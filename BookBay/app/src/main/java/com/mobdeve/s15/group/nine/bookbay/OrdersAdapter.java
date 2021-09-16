package com.mobdeve.s15.group.nine.bookbay;

import android.content.DialogInterface;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreHelper;

import java.util.ArrayList;

/**
 * Adapter for the My Orders and Selling Orders Recylerview
 */
public class OrdersAdapter extends RecyclerView.Adapter <OrdersViewHolder> {

    // Variables
    private int whichView;
    private String prevBookStatus = BookStatus.PENDING.name();

    // Data
    private ArrayList<BooksOrders> booksOrders;

    // Context
    private Context context;

    // Constructor
    public OrdersAdapter(Context context) {
        this.booksOrders = new ArrayList<>();
        this.context = context;
    }

    // Set data
    public void setData (ArrayList<BooksOrders> booksOrders) {
        this.booksOrders = booksOrders;
    }

    // Set view type (if selling orders or my orders)
    public void setViewType(int whichView) {
        this.whichView = whichView;
    }

    @Override
    public OrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.view_my_selling_orders_layout, parent, false);

        OrdersViewHolder holder = new OrdersViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(OrdersViewHolder holder, int position) {
        holder.bindData(booksOrders.get(position).getOrder(), booksOrders.get(position).getBook(), whichView);
        holder.setItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override // for the spinner
            public void onItemSelected(AdapterView<?> parent, View view, int choice, long id) {
                // get the selected item from the spinner
                String bookStatus = parent.getItemAtPosition(choice).toString();

                // if the current status is not the previous one
                if (!prevBookStatus.equals(bookStatus)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage(Html.fromHtml("Pressing Continue will notify the buyer that you " + "<b>" + bookStatus + "</b>" + " their order. You may not change it back to pending."));
                    builder.setCancelable(true);

                    // Confirming alert dialog
                    builder.setPositiveButton(
                            "Continue",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Update status and placed notification in the Firestore
                                    BookbayFirestoreHelper.updateStatusAndNotifications(booksOrders.get(position), position, bookStatus, context, OrdersAdapter.this);
                                }
                            });

                    // Canceling alert dialog
                    builder.setNegativeButton(
                            "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    holder.setSelectedToPending(); // change selected to pending
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
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
