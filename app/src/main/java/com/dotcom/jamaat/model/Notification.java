package com.dotcom.jamaat.model;

/**
 * Created by hali on 11/8/16.
 */
public class Notification {
//    {
//        "notificationId": 3129,
//            "Id": 22,
//            "message": "Meeting cancelled: Discussion on new technology",
//            "messageType": "meetingCancelled",
//            "isRead": false,
//            "isSubscribed": false
//    },

    String title;
    String date;
    Boolean isNew;

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public Notification(String title,String date,boolean isReadable){
//        super();
//        this.title = title;
//        this.date = date;
//        this.isReadable();
//
//    }

}
