package com.mobdeve.s15.group.nine.bookbay;

import java.util.Comparator;

/**
 * Custom Comparator Class for sorting order date in descending order based on the orders
 */
public class SortByOrderDate implements Comparator<BooksOrders> {

    @Override
    public int compare(BooksOrders a, BooksOrders b) {
        // if a and b are equal
        if (a.getOrder().getOrderDate().compareTo(b.getOrder().getOrderDate()) == 0) {
            return 0;
        }
        else if (a.getOrder().getOrderDate().compareTo(b.getOrder().getOrderDate()) < 0) { // if a less than b, make it return 1
            return 1;
        }
        else { // if b less than a, make it return -1
            return -1;
        }
    }
}
