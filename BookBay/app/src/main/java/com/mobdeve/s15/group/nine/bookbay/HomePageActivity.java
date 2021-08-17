package com.mobdeve.s15.group.nine.bookbay;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        BottomNavigationView navView = (BottomNavigationView)findViewById(R.id.bottom_navigation_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.view_thrift_store, R.id.view_notifications, R.id.view_my_orders, R.id.view_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }
}
