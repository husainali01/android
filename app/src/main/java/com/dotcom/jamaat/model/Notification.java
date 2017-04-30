package com.dotcom.jamaat.model;

/**
 * Created by hali on 11/8/16.
 */
public class Notification {

    String title;
    String date;
    Boolean isNew;

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    String bgColor;
    String fontColor;


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
