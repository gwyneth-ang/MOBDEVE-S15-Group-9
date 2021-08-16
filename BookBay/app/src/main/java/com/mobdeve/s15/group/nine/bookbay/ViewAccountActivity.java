package com.mobdeve.s15.group.nine.bookbay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ViewAccountActivity extends AppCompatActivity {
    Button my_books, selling_orders, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_account);

        this.my_books = findViewById(R.id.Bt_myaccount_my_books);
        this.selling_orders = findViewById(R.id.Bt_myaccount_selling_orders);
        this.logout = findViewById(R.id.Bt_myaccount_logout);

//        TODO: intent to view my books
        this.my_books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ViewAccountActivity.this, ViewMyBooks.class);
//                startActivity(intent);
//                finish();
            }
        });

//        TODO: intent to view selling orders
        this.selling_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ViewAccountActivity.this, ViewMySellingOrders.class);
//                startActivity(intent);
//                finish();
            }
        });

//        TODO: logout of account
        this.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
            }
        });

    }
}
