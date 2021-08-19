package com.mobdeve.s15.group.nine.bookbay;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

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

        // change font for search view
        int id = this.Sv_thriftsellingbooks_search_bar.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        Typeface tf = ResourcesCompat.getFont(view.getContext(),R.font.cormorant_garamond);
        TextView searchText = (TextView) this.Sv_thriftsellingbooks_search_bar.findViewById(id);
        searchText.setTypeface(tf);
        searchText.setTextColor(Color.BLACK);

        // change UI based on which activity
        setupUi();

        // Get the book from the Books_sell Collection
        this.dbRef = FirebaseFirestore.getInstance();
        Query query = dbRef
                .collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION)
                .orderBy(BookbayFirestoreReferences.BOOK_TITLE_FIELD);

        FirestoreRecyclerOptions<Books_sell> options = new FirestoreRecyclerOptions.Builder<Books_sell>()
                .setQuery(query, Books_sell.class)
                .build();

        // for recycler view
        this.thriftRecyclerView = view.findViewById(R.id.rv_books);
        this.thriftAdapter = new ThriftStoreSellingBooksAdapter(options);
        this.thriftAdapter.setViewType(WhichLayout.THRIFT_STORE.ordinal());

        readyRecyclerViewAndAdapter(view.getContext());

    }

    private void readyRecyclerViewAndAdapter(Context context) {
        this.thriftRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        this.thriftRecyclerView.setAdapter(this.thriftAdapter);
    }

    public void setupUi(){
        this.tv_my_books.setVisibility(View.GONE);
        this.fab_add_book.setVisibility(View.GONE);
        this.ll_thriftsellingbooks_search.setBackgroundResource(R.color.red);
        this.Bt_thriftsellingbooks_filter.setImageResource(R.drawable.filter);
    }

    @Override
    public void onStart() {
        super.onStart();
        // When our app is open, we need to have the adapter listening for any changes in the data.
        // To do so, we'd want to turn on the listening using the appropriate method in the onStart
        // or onResume (basically before the start but within the loop)
        this.thriftAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        // We want to eventually stop the listening when we're about to exit an app as we don't need
        // something listening all the time in the background.
        this.thriftAdapter.stopListening();
    }
}