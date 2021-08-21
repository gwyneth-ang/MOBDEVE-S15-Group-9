package com.mobdeve.s15.group.nine.bookbay;


import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ServerTimestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Books_sell {
    @DocumentId
    private DocumentReference books_sellID;

    // Attributes
    private Date addBookDate;
    private String bookAuthor;
    private String bookTitle;
    private String condition;
    private String ownerID;
    private Float price;
    private String image;
    private String buyerID;
    private Date orderDate;
    private String status;
    private Date notificationDateTime;
    private String profileName;
    private String profileImage;

    // Do not remove this. This is needed by Firebase when it creates instances of our model class.
    public Books_sell() {

    }

    public Books_sell(Date addBookDate, String bookAuthor, String bookTitle, String condition, String ownerID, Float price, String image, String buyerID, Date orderDate, String status, Date NotifcationDateTime, String profileName, String profileImage) {
        this.addBookDate = addBookDate;
        this.bookAuthor = bookAuthor;
        this.bookTitle = bookTitle;
        this.condition = condition;
        this.ownerID = ownerID;
        this.price = price;
        this.image = image;
        this.buyerID = buyerID;
        this.orderDate = orderDate;
        this.status = status;
        this.notificationDateTime = NotifcationDateTime;
        this.profileName = profileName;
        this.profileImage = profileImage;
    }

    public DocumentReference getBooks_sellID() {
        return books_sellID;
    }

    public void setBooks_sellID(DocumentReference books_sellID) {
        this.books_sellID = books_sellID;
    }

    public Date getAddBookDate() {
        return addBookDate;
    }

    public void setAddBookDate(Date addBookDate) {
        this.addBookDate = addBookDate;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(String buyerID) {
        this.buyerID = buyerID;
    }

    public Date getOrderDate() {
        return orderDate;
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

    public String getNotificationDateTime() {
        SimpleDateFormat DateForm = new SimpleDateFormat("MMM dd | hh:mm");
        return DateForm.format(this.notificationDateTime);
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
