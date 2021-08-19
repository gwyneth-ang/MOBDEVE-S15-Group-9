package com.mobdeve.s15.group.nine.bookbay;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ConfirmOrderDialog extends AppCompatDialogFragment {
    private TextView title, price, condition;
    private String title_holder, condition_holder;
    private String price_holder;

    public ConfirmOrderDialog(String title, String price, String condition){
            this.title_holder = title;
            this.price_holder = price;
            this.condition_holder = condition;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.confirm_order_dialog, null);

        this.title = view.findViewById(R.id.Tv_dialog_title);
        this.price = view.findViewById(R.id.Tv_dialog_price);
        this.condition = view.findViewById(R.id.Tv_dialog_condition);

        this.title.setText(title_holder);
        this.price.setText(price_holder);
        this.condition.setText(condition_holder);

//        TODO: Set dialog text here
        builder.setView(view)
                .setTitle("Confirm Order")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which){
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        TODO: Send order data here, redirect to thrift store
                    }
                });
        return builder.create();
    }

}
