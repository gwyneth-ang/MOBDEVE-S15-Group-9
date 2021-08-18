package com.mobdeve.s15.group.nine.bookbay;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private ArrayList<Book> books;

    private RecyclerView thriftRecyclerView;
    private ThriftStoreSellingBooksAdapter thriftAdapter;

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
        this.books = new DataHelper().populateData();
        this.thriftRecyclerView = view.findViewById(R.id.rv_books);
        this.thriftAdapter = new ThriftStoreSellingBooksAdapter(books);

        readyRecyclerViewAndAdapter(view.getContext());
    }

    private void readyRecyclerViewAndAdapter(Context view) {
        this.thriftRecyclerView.setLayoutManager(new GridLayoutManager(view, 2));

        this.thriftRecyclerView.setAdapter(this.thriftAdapter);
    }
}