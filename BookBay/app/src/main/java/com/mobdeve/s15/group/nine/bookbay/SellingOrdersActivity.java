package com.mobdeve.s15.group.nine.bookbay;

import android.graphics.Color;;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.lang.reflect.Array;
import java.util.Arrays;

public class SellingOrdersActivity extends AppCompatActivity{

    private RecyclerView sellerOrdersRecyclerView;
    private OrdersAdapter sellerOrdersAdapter;

    // Other views
    private TextView tv_orders;

    // DB reference
    private FirebaseFirestore dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_my_orders);

        this.sellerOrdersRecyclerView = findViewById(R.id.rv_orders);
        this.tv_orders = findViewById(R.id.tv_orders);

        setUpUI();

        //get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        this.dbRef = BookbayFirestoreReferences.getFirestoreInstance();

        Query myOrdersQuery = dbRef
                .collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                .whereEqualTo(BookbayFirestoreReferences.OWNER_ID_UID_FIELD, user.getUid())
                .whereNotEqualTo(BookbayFirestoreReferences.ORDER_DATE_FIELD, null)
                .orderBy(BookbayFirestoreReferences.ORDER_DATE_FIELD, Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Books_sell> options = new FirestoreRecyclerOptions.Builder<Books_sell>()
                .setQuery(myOrdersQuery, Books_sell.class)
                .build();

        this.sellerOrdersAdapter = new OrdersAdapter(options);
        this.sellerOrdersAdapter.setViewType(WhichLayout.SELLING_ORDERS.ordinal());

        readyRecyclerViewAndAdapter();
    }

    private void readyRecyclerViewAndAdapter() {
        this.sellerOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.sellerOrdersRecyclerView.setAdapter(this.sellerOrdersAdapter);
    }

    private void setUpUI(){
        this.tv_orders.setBackgroundColor(Color.parseColor("#FF000000"));
        this.tv_orders.setText("Selling Orders");
        this.tv_orders.setTextColor(Color.parseColor("#FFFFFFFF"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        // When our app is open, we need to have the adapter listening for any changes in the data.
        // To do so, we'd want to turn on the listening using the appropriate method in the onStart
        // or onResume (basically before the start but within the loop)
        this.sellerOrdersAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // We want to eventually stop the listening when we're about to exit an app as we don't need
        // something listening all the time in the background.
        this.sellerOrdersAdapter.stopListening();
    }
}
