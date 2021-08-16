package com.mobdeve.s15.group.nine.bookbay;

import java.util.ArrayList;

public class Book {
    private String bookTitle;
    private String bookAuthor;
    private ArrayList<String> bookCondition = new ArrayList<>();
    private ArrayList<Float> bookPrice = new ArrayList<>();
    private String status;
    private int imageId;

    public Book(String bookTitle, String bookAuthor, int imageId) {
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.imageId = imageId;

        this.bookCondition.add("New");
        this.bookCondition.add("Good");
        this.bookCondition.add("Acceptable");

        this.bookPrice.add((float)1200.00);
        this.bookPrice.add((float)32000.00);
        this.bookPrice.add((float)40000.00);

        this.status = BookStatus.NOT_ORDERED.name();
    }

    public String getTitle(){
        return bookTitle;
    }

    public String getAuthor(){
        return bookAuthor;
    }

    public ArrayList<String> getBookCondition(){
        return bookCondition;
    }

    public ArrayList<Float> getBookPrice(){
        return bookPrice;
    }

    public String getStatus() {
        return status;
    }

    public int getImageId() {
        return imageId;
    }
}
