package com.example.librarymanagement;

public class User {
    private String userId;
    private String username;
    private String password;
    private String role;

    public User(String userId,String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = "user";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User {" +
                "userId = '" + userId + '\'' +
                ", username = '" + username + '\'' +
                '}';
    }
}