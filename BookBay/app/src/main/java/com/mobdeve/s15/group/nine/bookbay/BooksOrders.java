package com.mobdeve.s15.group.nine.bookbay;

import com.mobdeve.s15.group.nine.bookbay.model.Books_sell;
import com.mobdeve.s15.group.nine.bookbay.model.Orders;

public class BooksOrders {
    private Orders order;
    private Books_sell book;

    public BooksOrders(Orders order, Books_sell book) {
        this.order = order;
        this.book = book;
    }

    public BooksOrders() {

    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public Books_sell getBook() {
        return book;
    }

    public void setBook(Books_sell book) {
        this.book = book;
    }
}
