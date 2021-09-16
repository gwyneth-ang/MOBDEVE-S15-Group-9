package com.mobdeve.s15.group.nine.bookbay;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreHelper;
import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreReferences;

public class view_selling_books extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

    private String mParam1;
    private String mParam2;

    public view_selling_books() {
        // Required empty public constructor
    }

    public static view_selling_books newInstance(String param1, String param2) {
        view_selling_books fragment = new view_selling_books();
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
        return inflater.inflate(R.layout.fragment_view_thrift_store, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // View initialization
        this.tv_my_books = view.findViewById(R.id.tv_my_books);
        this.fab_add_book = view.findViewById(R.id.fab_add_book);
        this.ll_thriftsellingbooks_search = view.findViewById(R.id.ll_thriftsellingbooks_search);
        this.Sv_thriftsellingbooks_search_bar = view.findViewById(R.id.Sv_thriftsellingbooks_seach_bar);
        this.Bt_thriftsellingbooks_filter = view.findViewById(R.id.Bt_thriftsellingbooks_filter);
        this.sfl_store_selling_books = view.findViewById(R.id.sfl_store_selling_books);

        //POPUP MENU
        PopupMenu popup = new PopupMenu(view.getContext(), Bt_thriftsellingbooks_filter);
        popup.inflate(R.menu.sort_menu);

        //user
        user = FirebaseAuth.getInstance().getCurrentUser();

        // change font for search view
        int id = this.Sv_thriftsellingbooks_search_bar.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        Typeface tf = ResourcesCompat.getFont(view.getContext(), R.font.cormorant_garamond);
        TextView searchText = (TextView) this.Sv_thriftsellingbooks_search_bar.findViewById(id);
        searchText.setTypeface(tf);
        searchText.setTextColor(Color.BLACK);
        searchText.setHintTextColor(Color.parseColor("#999999"));

        // change UI based on which activity
        setupUi();

        // for recycler view
        this.sellingBookRecyclerView = view.findViewById(R.id.rv_books);
        this.sellingBookRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));

        // Recycler and adapter
        sellingBookAdapter = new ThriftStoreSellingBooksAdapter();
        sellingBookAdapter.setViewType(WhichLayout.SELLING_BOOKS.ordinal());
        sellingBookRecyclerView.setAdapter(sellingBookAdapter);
        updateDataAndAdapter();

        // On refresh
        this.sfl_store_selling_books.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sfl_store_selling_books.setRefreshing(true);
                popup.getMenu().getItem(4).setChecked(true);
                Sv_thriftsellingbooks_search_bar.setQuery("", false);
                updateDataAndAdapter();
                sfl_store_selling_books.setRefreshing(false);
            }
        });

        // For search bar
        this.Sv_thriftsellingbooks_search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override // when the query is submitted
            public boolean onQueryTextSubmit(String query) {
                Log.d("TEST", String.valueOf(sortType));
                callSearch(query);
                return true;
            }

            @Override // when the query is changed and the text is 0
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    Log.d("SORT", String.valueOf(sortType));
                    BookbayFirestoreHelper.searchFilterBooksSeller("", sortType, sellingBookAdapter, user.getUid(), view.getContext());
                }
                return true;
            }

            // search query
            public void callSearch(String query) {
                if (!query.equals(null)) {
                    // search the query with the filter (sort) type
                    BookbayFirestoreHelper.searchFilterBooksSeller(
                            query,
                            sortType,
                            sellingBookAdapter,
                            user.getUid(),
                            view.getContext()
                    );
                }
            }
        });

        // pop up menu for which filter to use
        this.Bt_thriftsellingbooks_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // checked the item
                        item.setChecked(!item.isChecked());

                        // get which sort or filtering to use
                        sortType = item.getItemId();

                        // DO NOT CLOSE POP UP MENU WHEN CLICKING
                        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                        item.setActionView(new View(getContext()));
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

                // when the pop up is dismissed, that's when the search is processed
                popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu menu) {
                        // search books in the firestore
                        BookbayFirestoreHelper.searchFilterBooksSeller(Sv_thriftsellingbooks_search_bar.getQuery().toString(),
                                sortType,
                                sellingBookAdapter,
                                user.getUid(),
                                view.getContext());
                    }
                });

                popup.show(); //showing popup menu
            }
        });

        // if add book button is clicked
        this.fab_add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), AddBookActivity.class);
                ((HomePageActivity)view.getContext()).getUpdateAddResultLauncher().launch(intent);
            }
        });

    }

    // Set up the UI for this view
    private void setupUi(){
        this.tv_my_books.setVisibility(View.VISIBLE);
        this.fab_add_book.setVisibility(View.VISIBLE);
        this.ll_thriftsellingbooks_search.setBackgroundResource(R.color.pale);
        this.Bt_thriftsellingbooks_filter.setImageResource(R.drawable.filter_red);
    }

    // Get the user logged in
    public FirebaseUser getFirebaseUser () {
        return user;
    }

    // Get the adapter
    public ThriftStoreSellingBooksAdapter getSellingBookAdapter() {
        return sellingBookAdapter;
    }

    // Set the text in the search bar
    public void setSearchBar(String query){
        Sv_thriftsellingbooks_search_bar.setQuery(query, false);
    }

    // Update data and adapter
    public void updateDataAndAdapter() {
        // find all the books the owner sells
        BookbayFirestoreHelper.findAllBooksAvailableSeller(sellingBookAdapter, user.getUid());
    }
}