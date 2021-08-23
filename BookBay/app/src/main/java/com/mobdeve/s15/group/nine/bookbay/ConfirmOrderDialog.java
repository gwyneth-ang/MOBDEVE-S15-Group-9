package com.mobdeve.s15.group.nine.bookbay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class ConfirmOrderDialog extends AppCompatDialogFragment {
    private TextView title, price, condition;
    private String title_holder, condition_holder;
    private String price_holder;
    private FirebaseFirestore dbRef;
    private Context viewBookingDetailsContext;

    public ConfirmOrderDialog(String title, String price, String condition) {
        this.title_holder = title;
        this.price_holder = price;
        this.condition_holder = condition;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.viewBookingDetailsContext = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.confirm_order_dialog, null);
        this.dbRef = BookbayFirestoreReferences.getFirestoreInstance();


        this.title = view.findViewById(R.id.Tv_dialog_title);
        this.price = view.findViewById(R.id.Tv_dialog_price);
        this.condition = view.findViewById(R.id.Tv_dialog_condition);

        this.title.setText(title_holder);
        this.price.setText(price_holder);
        this.condition.setText(condition_holder);

        builder.setView(view)
                .setTitle("Confirm Order")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle = getArguments();
                        String bookID = bundle.getString(IntentKeys.BOOK_ID_KEY.name(), "");
                        dialog.cancel();
                        ProgressDialog progress = new ProgressDialog(getActivity()); // this = YourActivity
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setTitle("Loading");
                        progress.setMessage("Your order is being processed. Please wait...");
                        progress.setIndeterminate(true);
                        progress.setCanceledOnTouchOutside(false);
                        progress.show();
                        BookbayFirestoreHelper.placeOrder(bookID, progress, viewBookingDetailsContext);
                    }
                });
        return builder.create();
    }
}
