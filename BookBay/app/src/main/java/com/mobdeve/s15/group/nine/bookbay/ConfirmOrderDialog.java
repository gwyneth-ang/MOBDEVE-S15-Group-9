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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

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
    public ConfirmOrderDialog(String title, String price, String condition){
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
    public Dialog onCreateDialog(Bundle savedInstanceState){
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
//                        TODO: Send order data here, redirect to thrift store
                        Bundle bundle = getArguments();
                        String bookID = bundle.getString(IntentKeys.BOOK_ID_KEY.name(),"");
                        dialog.cancel();
                        ProgressDialog progress = new ProgressDialog(getActivity()); // this = YourActivity
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setTitle("Loading");
                        progress.setMessage("Your order is being processed. Please wait...");
                        progress.setIndeterminate(true);
                        progress.setCanceledOnTouchOutside(false);
                        progress.show();
                        checkOrderQuery(bookID, progress);
                    }
                });
        return builder.create();
    }

    private void checkOrderQuery(String bookID, ProgressDialog progress){
        dbRef.collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION).document(bookID);
        this.dbRef.collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION).document(bookID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.get(BookbayFirestoreReferences.STATUS_FIELD) == null){
                            //call function to add in order
                            placeOrderQuery(bookID, progress);
                        }
                        else{
                            //error - order cannot be fulfilled
                            Context context = progress.getContext();
                            progress.dismiss();
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                                    .setTitle("Order Unsuccessful")
                                    .setMessage("Sorry, your order cannot be fulfilled")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            closeActivity();
                                        }
                                    });
                            Dialog dialog = dialogBuilder.create();
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();
                        }
                    } else {
                        //error - item does not exist
                        Context context = progress.getContext();
                        progress.dismiss();
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                                .setTitle("Order Unsuccessful")
                                .setMessage("Item does not exist")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        closeActivity();
                                    }
                                });
                        Dialog dialog = dialogBuilder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                } else {
                    // error - please try again
                    Context context = progress.getContext();
                    progress.dismiss();
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                            .setTitle("Order Unsuccessful")
                            .setMessage("An error is encountered while processing your order, please try again later")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    closeActivity();
                                }
                            });
                    Dialog dialog = dialogBuilder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
            }
        });
    }

    private void placeOrderQuery(String bookID, ProgressDialog progress){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        Map<String, Object> data = new HashMap<>();
        data.put(BookbayFirestoreReferences.BUYER_ID_UID_FIELD, user.getUid());
        data.put(BookbayFirestoreReferences.ORDER_DATE_FIELD, cal.getTime());
        data.put(BookbayFirestoreReferences.STATUS_FIELD, BookStatus.PENDING.name());

        this.dbRef.collection(BookbayFirestoreReferences.BOOKS_SELL_COLLECTION).document(bookID).set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Context context = progress.getContext();
                progress.dismiss();
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                        .setTitle("Order Unsuccessful")
                        .setMessage("An error is encountered while processing your order, please try again later")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                closeActivity();
                            }
                        });
                Dialog dialog = dialogBuilder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // error - please try again
                Context context = progress.getContext();
                progress.dismiss();
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                        .setTitle("Order Unsuccessful")
                        .setMessage("An error is encountered while processing your order, please try again later")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                closeActivity();
                            }
                        });
                Dialog dialog = dialogBuilder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });
    }
    private void closeActivity(){
        ((Activity)this.viewBookingDetailsContext).finish();
    }
}
