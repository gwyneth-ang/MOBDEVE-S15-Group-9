package com.mobdeve.s15.group.nine.bookbay;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

public class Notifications {
    @DocumentId
    private DocumentReference notificationID;

    // Attributes
    private DocumentReference bookRef;
    private String bookTitle;
    private String image;
    private String profileName;
    private Date notificationDateTime;
    private String status;
    private String buyerID;

    // Do not remove this. This is needed by Firebase when it creates instances of our model class.
    public Notifications() {

    }

    public Notifications (DocumentReference bookRef, String bookTitle, String image, String profileName, Date notificationDateTime, String status, String buyerID) {
        this.bookRef = bookRef;
        this.bookTitle = bookTitle;
        this.image = image;
        this.profileName = profileName;
        this.notificationDateTime = notificationDateTime;
        this.status = status;
        this.buyerID = buyerID;
    }

    public DocumentReference getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(DocumentReference notificationID) {
        this.notificationID = notificationID;
    }

    public DocumentReference getBookRef() {
        return bookRef;
    }

    public void setBookRef(DocumentReference bookRef) {
        this.bookRef = bookRef;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public Date getNotificationDateTime() {
        return notificationDateTime;
    }

    public void setNotificationDateTime(Date notificationDateTime) {
        this.notificationDateTime = notificationDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(String buyerID) {
        this.buyerID = buyerID;
    }
}
