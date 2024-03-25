package com.example.expensestracker.models;

public class Categories {
    @Override
    public String toString() {
        return "Categories{" +
                "Category='" + Category + '\'' +
                '}';
    }

    private String Category;

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }
}
