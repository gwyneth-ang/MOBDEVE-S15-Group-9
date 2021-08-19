package com.mobdeve.s15.group.nine.bookbay;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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
        this.dbRef = FirebaseFirestore.getInstance();

        updateDataAndAdapter();

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
        dbRef.collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                .orderBy(BookbayFirestoreReferences.BOOK_TITLE_FIELD).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                                books.add(document.toObject(Books_sell.class));

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sellingBookAdapter = new ThriftStoreSellingBooksAdapter(books);
                                    sellingBookAdapter.setViewType(WhichLayout.SELLING_BOOKS.ordinal());

                                    sellingBookRecyclerView.setAdapter(sellingBookAdapter);
                                }
                            });
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateDataAndAdapter();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        // When our app is open, we need to have the adapter listening for any changes in the data.
//        // To do so, we'd want to turn on the listening using the appropriate method in the onStart
//        // or onResume (basically before the start but within the loop)
//        this.sellingBookAdapter.startListening();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        // We want to eventually stop the listening when we're about to exit an app as we don't need
//        // something listening all the time in the background.
//        this.sellingBookAdapter.stopListening();
//    }
}
