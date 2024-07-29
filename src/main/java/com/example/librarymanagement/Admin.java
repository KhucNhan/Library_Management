package com.example.librarymanagement;

public class Admin extends User{
    private String role = "Admin";

    public Admin(String userId, String username, String password) {
        super(userId, username, password);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
