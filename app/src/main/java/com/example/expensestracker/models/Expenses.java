package com.example.expensestracker.models;

import java.util.UUID;

public class Expenses {
    private String id;
    private String dateTime;
    private  String location;
    private Double expense;
    private String category;
    private String userID;

    // Default constructor
    public Expenses() {
        this.id =UUID.randomUUID().toString();
        this.dateTime="";
        this.location="";
        this.expense=0.0;
        this.category="";
        this.userID="";
    }

    public Expenses(String dateTime, String location, Double expense, String category, String userID) {
        this.id = UUID.randomUUID().toString();
        this.dateTime = dateTime; //Calendar.getInstance().getTime();
        this.location = location;
        this.expense = expense;
        this.category = category;
        this.userID =  userID;
    }

    public String  getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getExpense() {
        return expense;
    }

    public void setExpense(Double expense) {
        this.expense = expense;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Expenses{" +
                "id='" + id + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", location='" + location + '\'' +
                ", expense=" + expense +
                ", category='" + category + '\'' +
                '}';
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
