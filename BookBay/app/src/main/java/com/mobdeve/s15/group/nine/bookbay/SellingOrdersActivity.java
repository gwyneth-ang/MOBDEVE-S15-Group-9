package com.mobdeve.s15.group.nine.bookbay;

import android.graphics.Color;;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

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

        this.sellerOrdersRecyclerView = findViewById(R.id.rv_orders);
        this.tv_orders = findViewById(R.id.tv_orders);
        this.sfl_orders = findViewById(R.id.sfl_orders);

        this.sellerOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setUpUI();

        sellerOrdersAdapter = new OrdersAdapter(this);
        sellerOrdersAdapter.setViewType(WhichLayout.SELLING_ORDERS.ordinal());

        sellerOrdersRecyclerView.setAdapter(sellerOrdersAdapter);

        this.sfl_orders.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sfl_orders.setRefreshing(true);
                updateDataAndAdapter();
                sfl_orders.setRefreshing(false);
            }
        });
    }

    private void setUpUI(){
        this.tv_orders.setBackgroundColor(Color.parseColor("#FF000000"));
        this.tv_orders.setText("Selling Orders");
        this.tv_orders.setTextColor(Color.parseColor("#FFFFFFFF"));
    }

    public void updateDataAndAdapter() {
        //get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        BookbayFirestoreHelper.findSellerOrders(sellerOrdersAdapter, user.getUid());
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateDataAndAdapter();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
