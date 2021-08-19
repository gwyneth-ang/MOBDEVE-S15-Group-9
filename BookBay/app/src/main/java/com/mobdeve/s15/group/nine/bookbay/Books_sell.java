package com.mobdeve.s15.group.nine.bookbay;


public class Books_sell {
    private String BookAuthor;
    private String BookTitle;
    private String Condition;
    private String Image;
    private String OwnerID;
    private Float Price;

    // Do not remove this. This is needed by Firebase when it creates instances of our model class.
    public Books_sell() {

    }

    public Books_sell(String BookAuthor, String BookTitle, String Condition, String OwnerID, Float Price, String Image) {
        this.BookAuthor = BookAuthor;
        this.BookTitle = BookTitle;
        this.Condition = Condition;
        this.Image = Image;
        this.OwnerID = OwnerID;
        this.Price = Price;
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

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
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
}
