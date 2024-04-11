package com.example.expensestracker.models;

public class Notifications {
    private String Title;
    private String Body;
    private String Date;
    private Boolean Status;
    private String userId;

    public Notifications(String title, String body, String date, Boolean status, String UserId) {
        Title = title;
        Body = body;
        Date = date;
        Status = status;
        userId = UserId;
    }

    public Notifications() {
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }
}

