package com.example.expensestracker.models;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class User {

    private long userId;
    private String userName;
    private String userEmail;
    private String userPassword;
    public static ArrayList<User> usersDB = new ArrayList<>();

    public User(long userId, String userName, String userEmail, String userPassword) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        usersDB.add(this);
    }

    public User(String userEmail, String userPassword) {
        this.userId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE; //Generate a Unique User ID
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        usersDB.add(this);
    }


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId && Objects.equals(userName, user.userName) && Objects.equals(userEmail, user.userEmail) && Objects.equals(userPassword, user.userPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, userEmail, userPassword);
    }
}
