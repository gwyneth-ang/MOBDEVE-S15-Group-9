package com.mobdeve.s15.group.nine.bookbay;

import java.util.ArrayList;
import java.util.Calendar;

public class Notification {
    private String bookTitle;
    private String sellerName;
    private String status;
    private int imageId;
    private Calendar timeMade;
    private long toMilli;

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public Notification(String bookTitle, String sellerName, String status, int imageId, Calendar timeMade) {
        this.bookTitle = bookTitle;
        this.sellerName = sellerName;
        this.status = status;
        this.imageId = imageId;
        this.timeMade = timeMade;
    }

    /*public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }


        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }*/

    public String getBookTitle() { return bookTitle; }

    public String getSellerName() { return sellerName; }

    public String getStatus() { return status; }

    public int getImageId() { return imageId; }

    public String getTime() { return timeMade.getTime().toString(); }
}
