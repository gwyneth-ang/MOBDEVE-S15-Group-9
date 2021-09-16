package com.mobdeve.s15.group.nine.bookbay;

import com.mobdeve.s15.group.nine.bookbay.model.Books_sell;

import java.util.Comparator;

/**
 * Custom Comparator Class for sorting price of the book in ascending order
 */
public class SortByPriceAsc implements Comparator<Books_sell> {

    @Override
    public int compare(Books_sell a, Books_sell b) {
        return a.getPrice().compareTo(b.getPrice());
    }
}
