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
}
