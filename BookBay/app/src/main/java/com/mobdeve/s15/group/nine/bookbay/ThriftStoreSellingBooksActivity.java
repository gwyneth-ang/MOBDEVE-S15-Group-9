package com.mobdeve.s15.group.nine.bookbay;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
//TODO: TRANSFER TO FRAGMENTS
public class ThriftStoreSellingBooksActivity extends AppCompatActivity {

    private ArrayList<Book> books;

    private RecyclerView thriftRecyclerView;
    private ThriftStoreSellingBooksAdapter thriftAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thrift_and_selling_recylcer);

        this.books = new DataHelper().populateData();

        this.thriftRecyclerView = findViewById(R.id.rv_books);
        this.thriftAdapter = new ThriftStoreSellingBooksAdapter(books);

        readyRecyclerViewAndAdapter();
    }

    private void readyRecyclerViewAndAdapter() {
        this.thriftRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        this.thriftRecyclerView.setAdapter(this.thriftAdapter);
    }

}
