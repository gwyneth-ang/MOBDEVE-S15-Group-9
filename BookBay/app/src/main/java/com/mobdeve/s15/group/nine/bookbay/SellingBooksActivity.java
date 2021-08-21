package com.mobdeve.s15.group.nine.bookbay;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;

public class SellingBooksActivity extends AppCompatActivity {

    private RecyclerView sellingBookRecyclerView;
    private ThriftStoreSellingBooksAdapter sellingBookAdapter;

    private TextView tv_my_books;
    private FloatingActionButton fab_add_book;
    private LinearLayout ll_thriftsellingbooks_search;
    private SearchView Sv_thriftsellingbooks_search_bar;
    private ImageButton Bt_thriftsellingbooks_filter;
    private SwipeRefreshLayout sfl_store_selling_books;

    // DB reference
    private FirebaseFirestore dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_thrift_store);

        this.tv_my_books = findViewById(R.id.tv_my_books);
        this.fab_add_book = findViewById(R.id.fab_add_book);
        this.ll_thriftsellingbooks_search = findViewById(R.id.ll_thriftsellingbooks_search);
        this.Sv_thriftsellingbooks_search_bar = findViewById(R.id.Sv_thriftsellingbooks_seach_bar);
        this.Bt_thriftsellingbooks_filter = findViewById(R.id.Bt_thriftsellingbooks_filter);
        this.sfl_store_selling_books = findViewById(R.id.sfl_store_selling_books);

        // change font for search view
        int id = this.Sv_thriftsellingbooks_search_bar.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        Typeface tf = ResourcesCompat.getFont(this,R.font.cormorant_garamond);
        TextView searchText = (TextView) this.Sv_thriftsellingbooks_search_bar.findViewById(id);
        searchText.setTypeface(tf);
        searchText.setTextColor(Color.BLACK);

        // change UI based on which activity
        setupUi();

        // for recycler view
        this.sellingBookRecyclerView = findViewById(R.id.rv_books);
        this.sellingBookRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Get the book from the Books_sell Collection
        this.dbRef = BookbayFirestoreReferences.getFirestoreInstance();

        // Recycler and adapter
        sellingBookAdapter = new ThriftStoreSellingBooksAdapter();
        sellingBookAdapter.setViewType(WhichLayout.SELLING_BOOKS.ordinal());
        sellingBookRecyclerView.setAdapter(sellingBookAdapter);

        this.sfl_store_selling_books.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sfl_store_selling_books.setRefreshing(true);
                updateDataAndAdapter();
                sfl_store_selling_books.setRefreshing(false);
            }
        });

        fab_add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellingBooksActivity.this, AddBookActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupUi(){
        this.tv_my_books.setVisibility(View.VISIBLE);
        this.fab_add_book.setVisibility(View.VISIBLE);
        this.ll_thriftsellingbooks_search.setBackgroundResource(R.color.pale);
        this.Bt_thriftsellingbooks_filter.setImageResource(R.drawable.filter_red);
    }

    private void updateDataAndAdapter() {
        ArrayList<Books_sell> books = new ArrayList<>();

        //get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        dbRef.collectionGroup(BookbayFirestoreReferences.ORDERS_COLLECTION)
                .whereEqualTo(BookbayFirestoreReferences.OWNER_ID_UID_FIELD, user.getUid())
                .whereEqualTo(BookbayFirestoreReferences.STATUS_FIELD, null)
                .orderBy(BookbayFirestoreReferences.ADD_BOOK_DATE_FIELD, Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            Log.d("TEST", "Hi");
                            ArrayList<Books_sell> books = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TEST", "In the loop" + document.getReference().getId());
                                document.getReference().getParent().getParent().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(Task<DocumentSnapshot> task3) {
                                        if(task.isSuccessful()) {
                                            Books_sell temp = task3.getResult().toObject(Books_sell.class);

                                            Boolean same = false;

                                            for (int i = 0; i < books.size();i++) {
                                                if (books.get(i).getBooks_sellID().getId().equals(temp.getBooks_sellID().getId()))
                                                    same = true;
                                            }
                                            if (!same)
                                                books.add(temp);
                                            Log.d("TEST", String.valueOf(books.size()) + books.get(0).getBookTitle());
                                        } else {
                                            Log.d("TEST", "Error getting documents: ", task.getException());
                                        }

                                        thriftAdapter.setData(books);
                                        thriftAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateDataAndAdapter();
    }
}
