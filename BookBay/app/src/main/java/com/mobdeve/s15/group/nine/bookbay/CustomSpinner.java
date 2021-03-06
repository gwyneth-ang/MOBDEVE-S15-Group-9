package com.mobdeve.s15.group.nine.bookbay;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.content.Context;

import androidx.core.content.res.ResourcesCompat;

import java.util.List;

/**
 * Class for the Custom Spinner that is used to change order status and book condition
 * @param <String>
 */
public class CustomSpinner<String> extends ArrayAdapter<String> {
    // Initialise custom font, for example:
    Typeface font;
    Boolean bold;

    public CustomSpinner (Context context, int resource, List<String> items, Boolean bold) {
        super(context, resource, items);
        this.bold = bold;

        if (bold) {
            font = ResourcesCompat.getFont(getContext(),R.font.cormorant_garamond_semibold);
        } else {
            font = ResourcesCompat.getFont(getContext(),R.font.cormorant_garamond_medium);
        }
    }

    // Affects default (closed) state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTypeface(font);

        if (bold) {
            view.setTextSize(18);
        } else {
            view.setTextSize(13);
        }

        view.setTextColor(Color.parseColor("#555555"));

        return view;
    }

    // Affects opened state of the spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTypeface(font);

        if (bold) {
            view.setTextSize(18);
        } else {
            view.setTextSize(13);
        }

        view.setTextColor(Color.parseColor("#555555"));

        return view;
    }
}
