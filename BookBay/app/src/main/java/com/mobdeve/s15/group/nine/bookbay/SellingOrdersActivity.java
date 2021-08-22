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

    // DB reference
    private FirebaseFirestore dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_my_orders);

        this.sellerOrdersRecyclerView = findViewById(R.id.rv_orders);
        this.tv_orders = findViewById(R.id.tv_orders);
        this.sfl_orders = findViewById(R.id.sfl_orders);

        this.sellerOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setUpUI();

        this.dbRef = BookbayFirestoreReferences.getFirestoreInstance();

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

        dbRef.collectionGroup(BookbayFirestoreReferences.ORDERS_COLLECTION)
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

                                            if (!same && bookTemp.getOwnerID().equals(user.getUid()))
                                                booksOrders.add(new BooksOrders(orderTemp, bookTemp));
                                        } else {
                                            Log.d("TEST", "Error getting documents: ", task.getException());
                                        }

                                        sellerOrdersAdapter.setData(booksOrders);
                                        sellerOrdersAdapter.notifyDataSetChanged();

                                        Log.d("TEST", "Size: " + String.valueOf(booksOrders.size()));
                                    }
                                });
                            }
                        }
                    }
                });
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
