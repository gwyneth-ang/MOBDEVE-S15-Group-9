package com.mobdeve.s15.group.nine.bookbay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SellingBooksActivity extends AppCompatActivity {
    private ArrayList<Book> books;

    private RecyclerView sellingBookRecyclerView;
    private ThriftStoreSellingBooksAdapter sellingBookAdapter;

    private TextView tv_my_books;
    private FloatingActionButton fab_add_book;
    private LinearLayout ll_thriftsellingbooks_search;
    private SearchView Sv_thriftsellingbooks_search_bar;
    private ImageButton Bt_thriftsellingbooks_filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_thrift_store);

        this.books = new DataHelper().populateData();

        this.tv_my_books = findViewById(R.id.tv_my_books);
        this.fab_add_book = findViewById(R.id.fab_add_book);
        this.ll_thriftsellingbooks_search = findViewById(R.id.ll_thriftsellingbooks_search);
        this.Sv_thriftsellingbooks_search_bar = findViewById(R.id.Sv_thriftsellingbooks_seach_bar);
        this.Bt_thriftsellingbooks_filter = findViewById(R.id.Bt_thriftsellingbooks_filter);

        setupUi();

        this.sellingBookRecyclerView = findViewById(R.id.rv_books);
        this.sellingBookAdapter = new ThriftStoreSellingBooksAdapter(books);
        this.sellingBookAdapter.setViewType(WhichLayout.SELLING_BOOKS.ordinal());

        readyRecyclerViewAndAdapter();

        fab_add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(this, AddBookActivity.class);
//                startActivity(intent);
            }
        });
    }

    private void readyRecyclerViewAndAdapter() {
        this.sellingBookRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        this.sellingBookRecyclerView.setAdapter(this.sellingBookAdapter);
    }

    public void setupUi(){
        this.tv_my_books.setVisibility(View.VISIBLE);
        this.fab_add_book.setVisibility(View.VISIBLE);
        this.ll_thriftsellingbooks_search.setBackgroundResource(R.color.pale);
        this.Bt_thriftsellingbooks_filter.setImageResource(R.drawable.filter_red);
    }
}
