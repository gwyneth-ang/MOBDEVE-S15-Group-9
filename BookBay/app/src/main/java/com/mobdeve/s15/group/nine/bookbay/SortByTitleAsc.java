package com.mobdeve.s15.group.nine.bookbay;

import com.mobdeve.s15.group.nine.bookbay.model.Books_sell;

import java.util.Comparator;

/**
 * Custom Comparator Class for sorting book title in ascending order
 */
public class SortByTitleAsc implements Comparator<Books_sell> {

    @Override
    public int compare(Books_sell a, Books_sell b) {
        return a.getBookTitle().compareTo(b.getBookTitle());
    }
}
