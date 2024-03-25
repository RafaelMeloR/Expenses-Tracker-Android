package com.example.expensestracker.models;

import java.util.UUID;

public class Expenses {
    private UUID id;
    private String dateTime;
    private  String location;
    private Double expense;
    private String category;

    public Expenses(String dateTime, String location, Double expense, String category) {
        this.id = UUID.randomUUID();
        this.dateTime = dateTime; //Calendar.getInstance().getTime();
        this.location = location;
        this.expense = expense;
        this.category = category;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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
}
