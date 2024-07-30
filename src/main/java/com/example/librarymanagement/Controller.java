package com.example.librarymanagement;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {
    User user1 = new Admin("Nhan12345","khucnhan","nhan2005", "admin");
    User user2 = new Admin("Khanh23456","baokhanh","khanh2005", "admin");
    User user3 = new Admin("Dam34567","vandam","dam2005", "admin");
    User user4 = new User("Phuong45678","nguyenphuong","phuong2005", "user");
    public User[] users = {user1, user2, user3, user4};
    @FXML
    TextField username;
    @FXML
    PasswordField password;

    public void add(User user) {
        users = Arrays.copyOf(users, users.length + 1);
        users[users.length - 1] = user;
    }

    public void setRole(String username, String role) {
        findUser(username).setRole(role);
    }
    @FXML
    protected void login() {
        String username = this.username.getText();
        String password = this.password.getText();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        if (!findUserBoolean(username)) {
            alert.setContentText("Username không tồn tại.");
        } else {
            if(findUser(username).getUsername().equals(username) && findUser(username).getPassword().equals(password)) {
                if (findUser(username).getRole().equalsIgnoreCase("admin")) {
                    alert.setContentText("Success! Xin chào admin " + findUser(username).getUsername() + ".");
                } else {
                    alert.setContentText("Success! Xin chào user " + findUser(username).getUsername() + ".");
                }
            } else {
                alert.setContentText("Incorrect username or password!");
            }
        }
        alert.show();
    }

    public void display() {
        for (User user : users) {
            System.out.println(user.toString());
        }
    }
    public User findUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    public boolean findUserBoolean(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}