package com.mobdeve.s15.group.nine.bookbay;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreHelper;

import java.util.Set;

public class view_my_orders extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView myOrdersRecyclerView;
    private OrdersAdapter myOrdersAdapter;

    // Other views
    private TextView tv_orders;
    private SwipeRefreshLayout sfl_orders;

    private String mParam1;
    private String mParam2;

    public view_my_orders() {
        // Required empty public constructor
    }

    public static view_my_orders newInstance(String param1, String param2) {
        view_my_orders fragment = new view_my_orders();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_my_orders, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // View initialization
        this.myOrdersRecyclerView = view.findViewById(R.id.rv_orders);
        this.tv_orders = view.findViewById(R.id.tv_orders);
        this.sfl_orders = view.findViewById(R.id.sfl_orders);

        // Set layout manager for the recylerview
        this.myOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // Set up the UI for my orders
        setUpUI();

        // For Adapter
        myOrdersAdapter = new OrdersAdapter(view.getContext());
        myOrdersAdapter.setViewType(WhichLayout.MY_ORDERS.ordinal());

        // Set adapter
        this.myOrdersRecyclerView.setAdapter(this.myOrdersAdapter);

        // On refresh
        this.sfl_orders.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sfl_orders.setRefreshing(true);
                updateDataAndAdapter();
                sfl_orders.setRefreshing(false);
            }
        });
    }

    // Set up the UI for this view
    private void setUpUI(){
        this.tv_orders.setBackgroundColor(Color.parseColor("#FFEFE6D5"));
        this.tv_orders.setText("My Orders");
        this.tv_orders.setTextColor(Color.parseColor("#FF000000"));
    }

    // Get the data and update it
    private void updateDataAndAdapter() {
        //get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Find my orders in the Firestore
        BookbayFirestoreHelper.findBuyerOrders(myOrdersAdapter, user.getUid(), null);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateDataAndAdapter();
    }
}