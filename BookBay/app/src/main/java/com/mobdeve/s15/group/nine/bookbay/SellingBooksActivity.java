package com.mobdeve.s15.group.nine.bookbay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreHelper;

public class  SellingBooksActivity extends AppCompatActivity {

    private RecyclerView sellingBookRecyclerView;
    private ThriftStoreSellingBooksAdapter sellingBookAdapter;

    private TextView tv_my_books;
    private FloatingActionButton fab_add_book;
    private LinearLayout ll_thriftsellingbooks_search;
    private SearchView Sv_thriftsellingbooks_search_bar;
    private ImageButton Bt_thriftsellingbooks_filter;
    private SwipeRefreshLayout sfl_store_selling_books;

    // variables
    private int sortType = -1;

    // user
    FirebaseUser user;

    private ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        BookbayFirestoreHelper.searchFilterBooksSeller(result.getData().getStringExtra(IntentKeys.FILTER_KEY.name()), -1,  sellingBookAdapter, user.getUid(), SellingBooksActivity.this);
                        Sv_thriftsellingbooks_search_bar.setQuery(result.getData().getStringExtra(IntentKeys.FILTER_KEY.name()), false);
                    } else if (result.getResultCode() == 10) {
                        updateDataAndAdapter();
                    }
                }
            });

    private ActivityResultLauncher<Intent> updateAddResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK) {

                        Log.d("HERE IN ADD LAUNCHER", "IN");
                        updateDataAndAdapter();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("selling books activity", "OnCreate");
        setContentView(R.layout.fragment_view_thrift_store);

        this.tv_my_books = findViewById(R.id.tv_my_books);
        this.fab_add_book = findViewById(R.id.fab_add_book);
        this.ll_thriftsellingbooks_search = findViewById(R.id.ll_thriftsellingbooks_search);
        this.Sv_thriftsellingbooks_search_bar = findViewById(R.id.Sv_thriftsellingbooks_seach_bar);
        this.Bt_thriftsellingbooks_filter = findViewById(R.id.Bt_thriftsellingbooks_filter);
        this.sfl_store_selling_books = findViewById(R.id.sfl_store_selling_books);

        //POPUP MENU
        PopupMenu popup = new PopupMenu(this, Bt_thriftsellingbooks_filter);
        popup.inflate(R.menu.sort_menu);

        //user
        user = FirebaseAuth.getInstance().getCurrentUser();

        // change font for search view
        int id = this.Sv_thriftsellingbooks_search_bar.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        Typeface tf = ResourcesCompat.getFont(this,R.font.cormorant_garamond);
        TextView searchText = (TextView) this.Sv_thriftsellingbooks_search_bar.findViewById(id);
        searchText.setTypeface(tf);
        searchText.setTextColor(Color.BLACK);
        searchText.setHintTextColor(Color.parseColor("#999999"));

        // change UI based on which activity
        setupUi();

        // for recycler view
        this.sellingBookRecyclerView = findViewById(R.id.rv_books);
        this.sellingBookRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Recycler and adapter
        sellingBookAdapter = new ThriftStoreSellingBooksAdapter();
        sellingBookAdapter.setViewType(WhichLayout.SELLING_BOOKS.ordinal());
        sellingBookRecyclerView.setAdapter(sellingBookAdapter);
        updateDataAndAdapter();

        this.sfl_store_selling_books.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sfl_store_selling_books.setRefreshing(true);
                updateDataAndAdapter();
                sfl_store_selling_books.setRefreshing(false);
            }
        });

        this.Sv_thriftsellingbooks_search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("TEST", String.valueOf(sortType));
                callSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    Log.d("SORT", String.valueOf(sortType));
                    BookbayFirestoreHelper.searchFilterBooksSeller("", sortType, sellingBookAdapter, user.getUid(), SellingBooksActivity.this);
                }
                return true;
            }

            public void callSearch(String query) {
                if (!query.equals(null)) {
                    Log.d("SORT", String.valueOf(sortType));
                    BookbayFirestoreHelper.searchFilterBooksSeller(query, sortType, sellingBookAdapter, user.getUid(), SellingBooksActivity.this);
                }
            }
        });

        this.Bt_thriftsellingbooks_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        item.setChecked(!item.isChecked());

                        sortType = item.getItemId();

                        // DO NOT CLOSE POP UP MENU WHEN CLICKING
                        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                        item.setActionView(new View(SellingBooksActivity.this));
                        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                            @Override
                            public boolean onMenuItemActionExpand(MenuItem item) {
                                return false;
                            }

                            @Override
                            public boolean onMenuItemActionCollapse(MenuItem item) {
                                return false;
                            }
                        });

                        return false;
                    }
                });

                popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu menu) {
                        BookbayFirestoreHelper.searchFilterBooksSeller(Sv_thriftsellingbooks_search_bar.getQuery().toString(),
                                sortType,
                                sellingBookAdapter,
                                user.getUid(),
                                SellingBooksActivity.this);
                    }
                });

                popup.show(); //showing popup menu
            }
        });

        this.fab_add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellingBooksActivity.this, AddBookActivity.class);
                updateAddResultLauncher.launch(intent);
            }
        });
    }

    private void setupUi(){
        this.tv_my_books.setVisibility(View.VISIBLE);
        this.fab_add_book.setVisibility(View.VISIBLE);
        this.ll_thriftsellingbooks_search.setBackgroundResource(R.color.pale);
        this.Bt_thriftsellingbooks_filter.setImageResource(R.drawable.filter_red);
    }

    public void updateDataAndAdapter() {
        BookbayFirestoreHelper.findAllBooksAvailableSeller(sellingBookAdapter, user.getUid());
    }

    public ActivityResultLauncher<Intent> getActivityResultLauncher(){
        return this.myActivityResultLauncher;
    }
}
