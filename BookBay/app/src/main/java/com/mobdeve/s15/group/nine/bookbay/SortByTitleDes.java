package com.mobdeve.s15.group.nine.bookbay;

import com.mobdeve.s15.group.nine.bookbay.model.Books_sell;
import com.mobdeve.s15.group.nine.bookbay.model.Orders;

import java.util.Comparator;

public class SortByTitleDes implements Comparator<Books_sell> {

    @Override
    public int compare(Books_sell a, Books_sell b) {
        if (a.getBookTitle().compareTo(b.getBookTitle()) == 0) {
            return 0;
        }
        else if (a.getBookTitle().compareTo(b.getBookTitle()) < 0) {
            return 1;
        }
        else {
            return -1;
        }
    }
}