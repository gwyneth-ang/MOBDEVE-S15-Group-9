package com.mobdeve.s15.group.nine.bookbay;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePageActivity extends AppCompatActivity {
    private BottomNavigationView navView;
    private Fragment selector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        navView = (BottomNavigationView)findViewById(R.id.bottom_navigation_view);
        Bundle bundle = getIntent().getExtras();
        navView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.view_thrift_store:
                    selector = new view_thrift_store();
                    selector.setArguments(bundle);
                    break;
                case R.id.view_notifications:
                    selector = new view_notifications();
                    selector.setArguments(bundle);
                    break;
                case R.id.view_my_orders:
                    selector = new view_my_orders();
                    selector.setArguments(bundle);
                    break;
                case R.id.view_account:
                    selector = new view_account();
                    selector.setArguments(bundle);
                    break;
            }
            if (selector != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment , selector).commit();
            }
            return  true;
        });
    }
}
