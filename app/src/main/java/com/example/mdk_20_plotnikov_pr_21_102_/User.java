package com.example.mdk_20_plotnikov_pr_21_102_;

public class User {
    public String username, email, phone, name, favourite_porridge;
    public int age;

    public User() {
        // Пустой конструктор требуется для использования с Firebase
    }

    public User(String username, String email, String phone, String name, String favourite_porridge, int age) {
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.name = name;
        this.favourite_porridge = favourite_porridge;
        this.age = age;
    }
}