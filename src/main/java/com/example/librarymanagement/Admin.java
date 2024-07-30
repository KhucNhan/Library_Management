package com.example.librarymanagement;

public class Admin extends User{
    private String role = "Admin";

    public Admin(String userId, String username, String password, String role) {
        super(userId, username, password, role);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}