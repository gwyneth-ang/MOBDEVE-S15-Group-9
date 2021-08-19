package com.mobdeve.s15.group.nine.bookbay;

import com.google.firebase.firestore.ServerTimestamp;
import com.google.type.Date;

public class Orders {
    private String BookID;
    private String BuyerID;
    private @ServerTimestamp Date OrderDate;
    private String Status;

    // Do not remove this. This is needed by Firebase when it creates instances of our model class.
    public Orders() {

    }

    public Orders(String BookID, String BuyerID, Date OrderDate, String Status) {
        this.BookID = BookID;
        this.BuyerID = BuyerID;
        this.OrderDate = OrderDate;
        this.Status = Status;
    }

    public String getBookID() {
        return BookID;
    }

    public void setBookID(String bookID) {
        this.BookID = bookID;
    }

    public String getBuyerID() {
        return BuyerID;
    }

    public void setBuyerID(String buyerID) {
        this.BuyerID = buyerID;
    }

    public Date getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.OrderDate = orderDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }
}
