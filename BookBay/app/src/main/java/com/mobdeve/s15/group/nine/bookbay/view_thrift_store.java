package com.mobdeve.s15.group.nine.bookbay;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.PopupMenu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreHelper;

public class view_thrift_store extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // for recylerview
    private RecyclerView thriftRecyclerView;
    private ThriftStoreSellingBooksAdapter thriftAdapter;

    // for other views in the layout
    private TextView tv_my_books;
    private FloatingActionButton fab_add_book;
    private LinearLayout ll_thriftsellingbooks_search;
    private SearchView Sv_thriftsellingbooks_search_bar;
    private ImageButton Bt_thriftsellingbooks_filter;
    private SwipeRefreshLayout sfl_store_selling_books;

    // variables
    private int sortType = -1;

    private String mParam1;
    private String mParam2;

    public view_thrift_store() {
        // Required empty public constructor
    }

    public static view_thrift_store newInstance(String param1, String param2) {
        view_thrift_store fragment = new view_thrift_store();
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

        // change font for search view
        int id = this.Sv_thriftsellingbooks_search_bar.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        Typeface tf = ResourcesCompat.getFont(view.getContext(),R.font.cormorant_garamond);
        TextView searchText = (TextView) this.Sv_thriftsellingbooks_search_bar.findViewById(id);
        searchText.setTypeface(tf);
        searchText.setTextColor(Color.BLACK);
        searchText.setHintTextColor(Color.parseColor("#999999"));

        //POPUP MENU
        PopupMenu popup = new PopupMenu(view.getContext(), Bt_thriftsellingbooks_filter);
        popup.inflate(R.menu.sort_menu);

        // change UI based on which activity
        setupUi();

        // for recycler view
        this.thriftRecyclerView = view.findViewById(R.id.rv_books);
        thriftRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));

        thriftAdapter = new ThriftStoreSellingBooksAdapter();
        thriftAdapter.setViewType(WhichLayout.THRIFT_STORE.ordinal());

        thriftRecyclerView.setAdapter(thriftAdapter);
        updateDataAndAdapter();

        // For swipe down refresh layout
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
                callSearch(query);
                return true;
            }

            @Override // when the query is changed and the text is 0
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    Log.d("SORT", String.valueOf(sortType));
                    BookbayFirestoreHelper.searchFilterBooks("", sortType, thriftAdapter, view.getContext());
                }
                return false;
            }

            // search query
            public void callSearch(String query) {
                if (!query.equals(null)) {
                    // search the query with the filter (sort) type
                    BookbayFirestoreHelper.searchFilterBooks(query, sortType, thriftAdapter, view.getContext());
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
                        BookbayFirestoreHelper.searchFilterBooks(Sv_thriftsellingbooks_search_bar.getQuery().toString(),
                                sortType,
                                thriftAdapter,
                                view.getContext());
                    }
                });

                popup.show(); //showing popup menu
            }
        });
    }

    // Set up the UI for this view
    private void setupUi(){
        this.tv_my_books.setVisibility(View.GONE);
        this.fab_add_book.setVisibility(View.GONE);
        this.ll_thriftsellingbooks_search.setBackgroundResource(R.color.red);
        this.Bt_thriftsellingbooks_filter.setImageResource(R.drawable.filter);
    }

    // Update data and adapter
    public void updateDataAndAdapter() {
        BookbayFirestoreHelper.findAllBooksAvailable(this.thriftAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    // Set the text in the search bar
    public void setSearchBar(String query){
         Sv_thriftsellingbooks_search_bar.setQuery(query, false);
    }

    // Get the adapter
    public ThriftStoreSellingBooksAdapter getAdapter(){
        return this.thriftAdapter;
    }
}