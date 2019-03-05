package com.example.krishbhatia.instagramclone.models;

public class User {
    private String user_id;
    private long phone_number;
    private  String email;
    private String username;

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setPhone_number(long phone_number) {
        this.phone_number = phone_number;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_id() {

        return user_id;
    }

    public long getPhone_number() {
        return phone_number;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public User() {

    }

    public User(String email, String user_id , String username,long phone_number) {

        this.user_id = user_id;
        this.phone_number = phone_number;
        this.email = email;
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
