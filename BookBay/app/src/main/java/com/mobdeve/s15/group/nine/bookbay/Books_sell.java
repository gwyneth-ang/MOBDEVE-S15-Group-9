package com.mobdeve.s15.group.nine.bookbay;


import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class Books_sell {
    @DocumentId
    private DocumentReference books_sellID;

    // Attributes
    private @ServerTimestamp Date AddBookDate;
    private String BookAuthor;
    private String BookTitle;
    private String Condition;
    private String OwnerID;
    private Float Price;
    private String Image;
    private String BuyerID;
    private @ServerTimestamp Date OrderDate;
    private String Status;
    private @ServerTimestamp Date NotificationDateTime;

    // Do not remove this. This is needed by Firebase when it creates instances of our model class.
    public Books_sell() {

    }

    public Books_sell(Date AddBookDate, String BookAuthor, String BookTitle, String Condition, String OwnerID, Float Price, String Image, String BuyerID, Date OrderDate, String Status, Date NotifcationDateTime) {
        this.AddBookDate = AddBookDate;
        this.BookAuthor = BookAuthor;
        this.BookTitle = BookTitle;
        this.Condition = Condition;
        this.OwnerID = OwnerID;
        this.Price = Price;
        this.Image = Image;
        this.BuyerID = BuyerID;
        this.OrderDate = OrderDate;
        this.Status = Status;
        this.NotificationDateTime = NotifcationDateTime;
    }

    public Date getAddBookDate() {
        return AddBookDate;
    }

    public void setAddBookDate(Date addBookDate) {
        this.AddBookDate = addBookDate;
    }

    public DocumentReference getBooks_sellID() {
        return books_sellID;
    }

    public void setBooks_sellID(DocumentReference books_sellID) {
        this.books_sellID = books_sellID;
    }

    public String getBookAuthor() {
        return BookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.BookAuthor = bookAuthor;
    }

    public String getBookTitle() {
        return BookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.BookTitle = bookTitle;
    }

    public String getCondition() {
        return Condition;
    }

    public void setCondition(String condition) {
        this.Condition = condition;
    }

    public String getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(String ownerID) {
        this.OwnerID = ownerID;
    }

    public Float getPrice() {
        return Price;
    }

    public void setPrice(Float price) {
        this.Price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
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

    public Date getNotificationDateTime() {
        return NotificationDateTime;
    }

    public void setNotificationDateTime(Date notificationDateTime) {
        this.NotificationDateTime = notificationDateTime;
    }
}
