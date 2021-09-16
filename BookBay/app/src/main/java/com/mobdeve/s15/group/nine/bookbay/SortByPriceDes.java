package com.mobdeve.s15.group.nine.bookbay;

import com.mobdeve.s15.group.nine.bookbay.model.Books_sell;

import java.util.Comparator;

/**
 * Custom Comparator Class for sorting price of the book in ascending order
 */
public class SortByPriceDes implements Comparator<Books_sell> {

    @Override
    public int compare(Books_sell a, Books_sell b) {
        // if a and b are equal
        if (a.getPrice().compareTo(b.getPrice()) == 0) {
            return 0;
        }
        else if (a.getPrice().compareTo(b.getPrice()) < 0) { // if a less than b, make it return 1
            return 1;
        }
        else { // if b less than a, make it return -1
            return -1;
        }
    }
}
