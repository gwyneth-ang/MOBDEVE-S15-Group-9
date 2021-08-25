package com.mobdeve.s15.group.nine.bookbay;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreHelper;
import com.mobdeve.s15.group.nine.bookbay.model.BookbayFirestoreReferences;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link view_notifications#newInstance} factory method to
 * create an instance of this fragment.
 */
public class view_notifications extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private NotificationsAdapter myNotificationsAdapter;

    private FirebaseFirestore dbRef;
    //vars used
//    private ArrayList<Notification> notifications;

    private RecyclerView notificationsRecyclerView;
    private NotificationsAdapter notificationsAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public view_notifications() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment view_notifications.
     */
    // TODO: Rename and change types and number of parameters
    public static view_notifications newInstance(String param1, String param2) {
        view_notifications fragment = new view_notifications();
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
        return inflater.inflate(R.layout.fragment_view_notifications, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

//        this.tvBookTitle_notifLayout = view.findViewById(R.id.tvBookTitle_notifLayout);
//        this.tvSellerName_notifLayout = view.findViewById(R.id.tvSellerName_notifLayout);
//        this.tvStatusBook_notifLayout = view.findViewById(R.id.tvStatusBook_notifLayout);
//        this.tvTimePassed_notifLayout = view.findViewById(R.id.tvTimePassed_notifLayout);
//        this.notifImageView = view.findViewById(R.id.notifImageView);

        this.recyclerView = view.findViewById(R.id.rvNotifications);

        //get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        this.dbRef = BookbayFirestoreReferences.getFirestoreInstance();

        this.myNotificationsAdapter = new NotificationsAdapter(BookbayFirestoreHelper.findNotificationOptions(user.getUid()));
        readyRecyclerViewAndAdapter(view.getContext());
    }

    private void readyRecyclerViewAndAdapter(Context view) {
        this.recyclerView.setAdapter(this.myNotificationsAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(view));
    }

    @Override
    public void onStart() {
        super.onStart();
        this.myNotificationsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.myNotificationsAdapter.stopListening();
    }
}