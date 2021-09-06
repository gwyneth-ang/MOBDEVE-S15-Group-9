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
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreHelper;
import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreReferences;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link view_thrift_store#newInstance} factory method to
 * create an instance of this fragment.
 */
public class view_thrift_store extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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

    // DB reference
    private FirebaseFirestore dbRef;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public view_thrift_store() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment view_thrift_store.
     */
    // TODO: Rename and change types and number of parameters
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

        //POPUP MENU
        PopupMenu popup = new PopupMenu(view.getContext(), Bt_thriftsellingbooks_filter);
        popup.inflate(R.menu.sort_menu);

        // change UI based on which activity
        setupUi();

        // for recycler view
        this.thriftRecyclerView = view.findViewById(R.id.rv_books);
        thriftRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));

        // Get the book from the Books_sell Collection
        this.dbRef = BookbayFirestoreReferences.getFirestoreInstance();

        thriftAdapter = new ThriftStoreSellingBooksAdapter();
        thriftAdapter.setViewType(WhichLayout.THRIFT_STORE.ordinal());

        thriftRecyclerView.setAdapter(thriftAdapter);

        // For swipe down refresh layout
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
                callSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                callSearch(newText);
                return true;
            }

            public void callSearch(String query) {
                if (!query.equals(null) && sortType != -1) {
                    BookbayFirestoreHelper.searchWithSortAllBooks(thriftAdapter, query, sortType);
                }
                //Do searching

            }
        });

        this.Bt_thriftsellingbooks_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.isChecked()){
                            //TODO: clear radio selection...

                        }
                        item.setChecked(!item.isChecked());
                        sortType = item.getItemId();

                        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                        item.setActionView(new View(getContext()));
                        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
                            @Override
                            public boolean onMenuItemActionExpand(MenuItem item) {
                                return false;
                            }

                            @Override
                            public boolean onMenuItemActionCollapse(MenuItem item) {
                                return false;
                            }
                        });

                        //TODO: add on dismiss listener

                        return false;
                    }
                });

                popup.show(); //showing popup menu
            }
        });
    }

    private void setupUi(){
        this.tv_my_books.setVisibility(View.GONE);
        this.fab_add_book.setVisibility(View.GONE);
        this.ll_thriftsellingbooks_search.setBackgroundResource(R.color.red);
        this.Bt_thriftsellingbooks_filter.setImageResource(R.drawable.filter);
    }

    private void updateDataAndAdapter() {
        BookbayFirestoreHelper.findAllBooksAvailable(this.thriftAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        updateDataAndAdapter();
    }

    public void setSearchBar(String query){
         Sv_thriftsellingbooks_search_bar.setQuery(query, false);

    }

    public ThriftStoreSellingBooksAdapter getAdapter(){
        return this.thriftAdapter;
    }
}