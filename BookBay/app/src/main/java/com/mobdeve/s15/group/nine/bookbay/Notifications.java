package com.mobdeve.s15.group.nine.bookbay;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.type.Date;

public class Notifications {
    @DocumentId
    private DocumentReference notificationsID;

    private @ServerTimestamp Date NotificationDateTime;
    private DocumentReference OrderID;

    // Do not remove this. This is needed by Firebase when it creates instances of our model class.
    public Notifications() {

    }

    public Notifications(Date NotificationDateTime, DocumentReference OrderID){
        this.NotificationDateTime = NotificationDateTime;
        this.OrderID = OrderID;
    }

    public DocumentReference getNotificationsID() {
        return notificationsID;
    }

    public void setNotificationsID(DocumentReference notificationsID) {
        this.notificationsID = notificationsID;
    }

    public Date getNotificationDateTime() {
        return NotificationDateTime;
    }

    public void setNotificationDateTime(Date notificationDateTime) {
        this.NotificationDateTime = notificationDateTime;
    }

    public DocumentReference getOrderID() {
        return OrderID;
    }

    public void setOrderID(DocumentReference orderID) {
        this.OrderID = orderID;
    }
}
