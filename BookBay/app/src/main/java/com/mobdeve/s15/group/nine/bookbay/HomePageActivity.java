package com.mobdeve.s15.group.nine.bookbay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreHelper;


public class HomePageActivity extends AppCompatActivity {
    private BottomNavigationView navView;
    private Fragment selector;

    private ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        BookbayFirestoreHelper.searchFilterBooks(result.getData().getStringExtra(IntentKeys.FILTER_KEY.name()), -1,  ((view_thrift_store)(selector)).getAdapter(), HomePageActivity.this);
                        ((view_thrift_store)(selector)).setSearchBar(result.getData().getStringExtra(IntentKeys.FILTER_KEY.name()));
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        navView = (BottomNavigationView)findViewById(R.id.bottom_navigation_view);
        Bundle bundle = getIntent().getExtras();
        navView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.view_thrift_store:
                    this.selector = new view_thrift_store();
                    this.selector.setArguments(bundle);
                    break;
                case R.id.view_notifications:
                    this.selector = new view_notifications();
                    this.selector.setArguments(bundle);
                    break;
                case R.id.view_my_orders:
                    this.selector = new view_my_orders();
                    this.selector.setArguments(bundle);
                    break;
                case R.id.view_account:
                    this.selector = new view_account();
                    this.selector.setArguments(bundle);
                    break;
            }
            if (this.selector != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment , this.selector).commit();
            }
            return  true;
        });

        this.selector = new view_thrift_store();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_fragment, this.selector)
                .commit();
    }

    public ActivityResultLauncher<Intent> getMyActivityResultLauncher(){
        return this.myActivityResultLauncher;
    }
}
