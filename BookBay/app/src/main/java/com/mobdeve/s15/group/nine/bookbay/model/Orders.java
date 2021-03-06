package com.mobdeve.s15.group.nine.bookbay.model;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

public class Orders {
    @DocumentId
    private DocumentReference orderID;

    private String buyerID;
    private Date orderDate;
    private String status;
    private Date notificationDateTime;
    private String buyerName;
    private String buyerImage;
    private String buyerEmail;

    public Orders() {

    }

    public Orders(String buyerID, Date orderDate, String status, Date notifcationDateTime, String buyerName, String buyerImage, String buyerEmail) {
        this.buyerID = buyerID;
        this.orderDate = orderDate;
        this.status = status;
        this.notificationDateTime = notifcationDateTime;
        this.buyerName = buyerName;
        this.buyerImage = buyerImage;
        this.buyerEmail = buyerEmail;
    }

    public DocumentReference getOrderID() {
        return orderID;
    }

    public void setOrderID(DocumentReference orderID) {
        this.orderID = orderID;
    }

    public String getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(String buyerID) {
        this.buyerID = buyerID;
    }

    public Date getOrderDate() {
        return this.orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getNotificationDateTime() {
        return this.notificationDateTime;
    }

    public void setNotificationDateTime(Date notificationDateTime) {
        this.notificationDateTime = notificationDateTime;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerImage() {
        return buyerImage;
    }

    public void setBuyerImage(String buyerImage) {
        this.buyerImage = buyerImage;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }
}
