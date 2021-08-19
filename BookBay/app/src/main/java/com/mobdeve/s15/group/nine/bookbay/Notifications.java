package com.mobdeve.s15.group.nine.bookbay;

import com.google.firebase.firestore.ServerTimestamp;
import com.google.type.Date;

public class Notifications {
    private @ServerTimestamp Date NotificationDateTime;
    private String OrderID;

    // Do not remove this. This is needed by Firebase when it creates instances of our model class.
    public Notifications() {

    }

    public Notifications(Date NotificationDateTime, String OrderID){
        this.NotificationDateTime = NotificationDateTime;
        this.OrderID = OrderID;
    }

    public Date getNotificationDateTime() {
        return NotificationDateTime;
    }

    public void setNotificationDateTime(Date notificationDateTime) {
        this.NotificationDateTime = notificationDateTime;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        this.OrderID = orderID;
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
}
