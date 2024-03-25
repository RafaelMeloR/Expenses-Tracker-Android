package com.example.expensestracker;

import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Utilities {

    public static String hashPassword(String pass)
    {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte [] a = md.digest(pass.getBytes(StandardCharsets.UTF_8));
        String hash = bytesToHex(a);
        return hash;
    }
    private static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }

    public static boolean login(String userName, String password) {

        ArrayList<User> userDB = User.usersDB;
        for(User user: userDB)
        {
            if(user.getUserEmail().equals(userName) && user.getUserPassword().equals(password))
            {
                return true;
            }
        }

        return false;
    }

    public static User getUser(String userName, String password) {

        ArrayList<User> userDB = User.usersDB;
        for(User user: userDB)
        {
            if(user.getUserEmail().equals(userName) && user.getUserPassword().equals(password))
            {
                return user;
            }
        }

        return null;
    }
}
