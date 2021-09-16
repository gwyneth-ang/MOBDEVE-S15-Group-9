package com.mobdeve.s15.group.nine.bookbay;

import android.app.ProgressDialog;
import android.graphics.Color;;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreHelper;

public class SellingOrdersActivity extends AppCompatActivity{

    private RecyclerView sellerOrdersRecyclerView;
    private OrdersAdapter sellerOrdersAdapter;

    // Other views
    private TextView tv_orders;
    private SwipeRefreshLayout sfl_orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_my_orders);

        // View Initialization
        this.sellerOrdersRecyclerView = findViewById(R.id.rv_orders);
        this.tv_orders = findViewById(R.id.tv_orders);
        this.sfl_orders = findViewById(R.id.sfl_orders);

        // Set layout manager for the recylerview
        this.sellerOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up the UI for selling orders
        setUpUI();

        // For Adapter
        sellerOrdersAdapter = new OrdersAdapter(this);
        sellerOrdersAdapter.setViewType(WhichLayout.SELLING_ORDERS.ordinal());

        sellerOrdersRecyclerView.setAdapter(sellerOrdersAdapter);

        // On refresh
        this.sfl_orders.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sfl_orders.setRefreshing(true);
                // Update the data and adapter
                updateDataAndAdapter(null);
                sfl_orders.setRefreshing(false);
            }
        });
    }

    // Set up the UI for this view
    private void setUpUI(){
        this.tv_orders.setBackgroundColor(Color.parseColor("#FF000000"));
        this.tv_orders.setText("Selling Orders");
        this.tv_orders.setTextColor(Color.parseColor("#FFFFFFFF"));
    }

    // Get the data and update it
    public void updateDataAndAdapter(ProgressDialog progressDialog) {
        // Get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Find selling orders in the Firestore
        BookbayFirestoreHelper.findSellerOrders(sellerOrdersAdapter, user.getUid(), progressDialog);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateDataAndAdapter(null);
    }
}
