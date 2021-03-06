package com.mobdeve.s15.group.nine.bookbay;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobdeve.s15.group.nine.bookbay.callback.NumBookCallBack;
import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreHelper;
import com.squareup.picasso.Picasso;

public class view_account extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ImageView profilePicture;
    private TextView userName, numBooks;
    private Button sellingOrder, logout;

    public view_account() {
        // Required empty public constructor
    }

    public static view_account newInstance(String param1, String param2) {
        view_account fragment = new view_account();
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
        View view = inflater.inflate(R.layout.fragment_view_account, container, false);

        // View initialization
        this.profilePicture = view.findViewById(R.id.Iv_myaccount_profile_picture);
        this.userName = view.findViewById(R.id.Tv_myaccount_user_name);
        this.numBooks = view.findViewById(R.id.Tv_myaccount_num_books);
        this.sellingOrder = view.findViewById(R.id.Bt_myaccount_selling_orders);
        this.logout = view.findViewById(R.id.Bt_myaccount_logout);

        // Set profile information
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userName.setText(user.getDisplayName());
        Picasso.get().load(user.getPhotoUrl()).into(profilePicture);

        // Set total number of books
        BookbayFirestoreHelper.findTotalSellingBooks(user.getUid(), new NumBookCallBack() {
            @Override
            public void totalBooks(int book_num) {
                setNumBooksText(book_num);
            }
        });

        // If view selling orders is clicked
        sellingOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SellingOrdersActivity.class);
                startActivity(intent);
            }
        });

        // If logout is clicked
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                GoogleSignIn.getClient(
                        getContext(),
                        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                ).signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

//   Setter for TextView numBooks
    private void setNumBooksText(int num){
        if(num != -1){
            this.numBooks.setText("SELLING " + Integer.toString(num)+ " BOOKS");
        }
        else{
            this.numBooks.setText("SELLING ?? BOOKS");
        }
    }
}