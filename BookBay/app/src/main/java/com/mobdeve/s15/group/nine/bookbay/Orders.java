package com.mobdeve.s15.group.nine.bookbay;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Orders {
    @DocumentId
    private DocumentReference orderID;

    private String buyerID;
    private Date orderDate;
    private String status;
    private Date notificationDateTime;
    private String profileName;
    private String profileImage;

    public Orders() {

    }

    public Orders(String buyerID, Date orderDate, String status, Date notifcationDateTime, String profileName, String profileImage) {
        this.buyerID = buyerID;
        this.orderDate = orderDate;
        this.status = status;
        this.notificationDateTime = notifcationDateTime;
        this.profileName = profileName;
        this.profileImage = profileImage;
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
//        SimpleDateFormat DateForm = new SimpleDateFormat("MMM dd, yyyy | hh:mm");
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
//        SimpleDateFormat DateForm = new SimpleDateFormat("MMM dd, yyyy | hh:mm");
        return this.notificationDateTime;
    }

    public void setNotificationDateTime(Date notificationDateTime) {
        this.notificationDateTime = notificationDateTime;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
