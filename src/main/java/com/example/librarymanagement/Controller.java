package com.example.librarymanagement;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Controller {
    private User[] users = new User[1000];
    private int i = 0;

    @FXML
    TextField username;
    @FXML
    PasswordField password;

    public void add(User user) {
        users[i++] = user;
    }

    public void setRole(String username, String role) {
        findUser(username).setRole(role);
    }

    public User findUser(String username) {
        for (int a = 0; a < i; a++) {
            if (users[a].getUsername().equalsIgnoreCase(username)) {
                return users[a];
            }
        }
        return null;
    }


    @FXML
    protected void login() {
        String username = this.username.getText();
        String password = this.password.getText();

        if (findUser(username) == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Username không tồn tại.");
            alert.show();
        }

        for (int a = 0; a < i; a++) {
            if (users[a].getUsername().equals(username) && users[a].getPassword().equals(password)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                if (users[a].getRole().equalsIgnoreCase("admin")) {
                    alert.setContentText("Success! Xin chào admin" + users[a].getUsername() + ".");
                } else {
                    alert.setContentText("Success! Xin chào user " + users[a].getUsername() + ".");
                }
                alert.show();
            }
        }
    }

    public void display() {
        for (int a = 0; a < i; a++) {
            System.out.println(users[a].toString());
        }
    }
}