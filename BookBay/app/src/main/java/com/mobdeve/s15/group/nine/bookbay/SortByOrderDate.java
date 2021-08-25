package com.mobdeve.s15.group.nine.bookbay;

import com.mobdeve.s15.group.nine.bookbay.model.Orders;

import java.util.Comparator;

public class SortByOrderDate implements Comparator<BooksOrders> {

    @Override
    public int compare(BooksOrders a, BooksOrders b) {
        if (a.getOrder().getOrderDate().compareTo(b.getOrder().getOrderDate()) == 0) {
            return 0;
        }
        else if (a.getOrder().getOrderDate().compareTo(b.getOrder().getOrderDate()) < 0) {
            return 1;
        }
        else {
            return -1;
        }
    }
}
