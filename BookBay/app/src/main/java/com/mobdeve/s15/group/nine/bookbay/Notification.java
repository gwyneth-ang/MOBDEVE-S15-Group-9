package com.mobdeve.s15.group.nine.bookbay;

import java.util.ArrayList;

public class Notification {
    private String bookTitle;
    private String sellerName;
    private String status;
    private String time;
    private int imageId;

    public Notification(String bookTitle, String sellerName, String status, int imageId) {
        this.bookTitle = bookTitle;
        this.sellerName = sellerName;
        this.status = status;
        this.time = "5 MINS AGO";
        this.imageId = imageId;
    }

    public String getBookTitle() { return bookTitle; }

    public String getSellerName() { return sellerName; }

    public String getStatus() { return status; }

    public String getTimePosted() { return time; }

    public int getImageId() { return imageId; }

    public String getTime() { return time; }
}
