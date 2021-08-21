package com.mobdeve.s15.group.nine.bookbay;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

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
    private SwipeRefreshLayout sfl_store_selling_books;

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

        this.sfl_store_selling_books.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sfl_store_selling_books.setRefreshing(true);
                updateDataAndAdapter();
                sfl_store_selling_books.setRefreshing(false);
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
        Log.d("TEST", "Check");
        dbRef.collectionGroup(BookbayFirestoreReferences.ORDERS_COLLECTION)
                .whereNotEqualTo(BookbayFirestoreReferences.STATUS_FIELD, BookStatus.CONFIRMED.name())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            Log.d("TEST", "Hi");
                            ArrayList<Books_sell> books = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TEST", "In the loop" + document.getReference().getId());
                                document.getReference().getParent().getParent().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(Task<DocumentSnapshot> task3) {
                                        if(task.isSuccessful()) {
                                            Books_sell temp = task3.getResult().toObject(Books_sell.class);

                                            Boolean same = false;
                                            
                                            for (int i = 0; i < books.size();i++) {
                                                if (books.get(i).getBooks_sellID().getId().equals(temp.getBooks_sellID().getId()))
                                                    same = true;
                                            }
                                            if (!same)
                                                books.add(temp);

                                        } else {
                                            Log.d("TEST", "Error getting documents: ", task.getException());
                                        }

                                        thriftAdapter.setData(books);
                                        thriftAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        } else {
                            Log.d("TEST", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        updateDataAndAdapter();
    }
}