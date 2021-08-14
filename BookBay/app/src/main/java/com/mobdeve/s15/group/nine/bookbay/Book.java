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

    private String getTitle(){
        return bookTitle;
    }

    private String getAuthor(){
        return bookAuthor;
    }

    private ArrayList<String> getBookCondition(){
        return bookCondition;
    }

    private ArrayList<Float> getBookPrice(){
        return bookPrice;
    }

    private String getStatus() {
        return status;
    }

    private int getImageId() {
        return imageId;
    }
}
