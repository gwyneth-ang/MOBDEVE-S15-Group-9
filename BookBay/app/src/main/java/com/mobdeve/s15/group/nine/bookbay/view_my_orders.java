package com.mobdeve.s15.group.nine.bookbay;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link view_my_orders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class view_my_orders extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // FIXME: to be change to database
    private ArrayList<Books_sell> books;

    private RecyclerView myOrdersRecyclerView;
    private OrdersAdapter myOrdersAdapter;

    // DB reference
    private FirebaseFirestore dbRef;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String TAG = "MY_ORDERS_ACTIVTIY"

    public view_my_orders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment view_my_orders.
     */
    // TODO: Rename and change types and number of parameters
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

        this.myOrdersRecyclerView = view.findViewById(R.id.rv_orders);

        //get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        this.dbRef = FirebaseFirestore.getInstance();

        Query myOrdersQuery = dbRef
                .collection(BookbayFirestoreReferences.ORDERS_COLLECTION)
                .whereEqualTo(BookbayFirestoreReferences.BUYER_ID_UID_FIELD, user.getUid())
                .orderBy(BookbayFirestoreReferences.ORDER_DATE_FIELD);

        myOrdersQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // If there are no results, then there is no sign of the username in
                    // the DB.
                    if(task.getResult().isEmpty()) {
                        showNewUserDialog(usersRef, username);
                    } else {
                        moveToChatRoomActivity(username);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        FirestoreRecyclerOptions<Books_sell> options = new FirestoreRecyclerOptions.Builder<Books_sell>()
                .setQuery(query, Books_sell.class)
                .build();

        this.myOrdersAdapter = new OrdersAdapter(books);
        this.myOrdersAdapter.setViewType(WhichLayout.MY_ORDERS.ordinal());

        readyRecyclerViewAndAdapter(view.getContext());
    }

    private void readyRecyclerViewAndAdapter(Context context) {
        this.myOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        this.myOrdersRecyclerView.setAdapter(this.myOrdersAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        // When our app is open, we need to have the adapter listening for any changes in the data.
        // To do so, we'd want to turn on the listening using the appropriate method in the onStart
        // or onResume (basically before the start but within the loop)
        this.myOrdersAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        // We want to eventually stop the listening when we're about to exit an app as we don't need
        // something listening all the time in the background.
        this.myOrdersAdapter.stopListening();
    }
}
}