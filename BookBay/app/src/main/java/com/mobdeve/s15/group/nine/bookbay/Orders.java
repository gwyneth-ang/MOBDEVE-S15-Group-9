package com.mobdeve.s15.group.nine.bookbay;

public class Orders {
    private String BookID;
    private String BuyerID;
    private String OrderDate;
    private String Status;

    // Do not remove this. This is needed by Firebase when it creates instances of our model class.
    public Orders() {

    }

    public Orders(String BookID, String BuyerID, String OrderDate, String Status) {
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

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        this.OrderDate = orderDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }
}
