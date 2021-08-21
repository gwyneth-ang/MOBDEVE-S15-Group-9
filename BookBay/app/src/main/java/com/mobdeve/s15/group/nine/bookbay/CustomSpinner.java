package com.mobdeve.s15.group.nine.bookbay;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.content.Context;

import androidx.core.content.res.ResourcesCompat;

import java.util.List;

public class CustomSpinner<String> extends ArrayAdapter<String> {
    // Initialise custom font, for example:
    Typeface font;

    public CustomSpinner (Context context, int resource, List<String> items, Boolean bold) {
        super(context, resource, items);
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
        view.setTextSize(13);
        return view;
    }

    // Affects opened state of the spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTypeface(font);
        view.setTextSize(13);
        return view;
    }
}
