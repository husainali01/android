package com.dotcom.jamaat.model;import java.util.ArrayList;/** * Created by anjanik on 14/4/16. */public class Group {    private String Name;    private int image;    private ArrayList<Child> items;    public String getName() {        return Name;    }    public void setName(String name) {        this.Name = name;    }    public int getImage() {        return image;    }    public void setImage(int image) {        this.image = image;    }    public ArrayList<Child> getItems() {        return items;    }    public void setItems(ArrayList<Child> items) {        this.items = items;    }}