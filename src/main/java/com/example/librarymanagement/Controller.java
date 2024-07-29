package com.example.librarymanagement;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    TextField username;
    @FXML
    PasswordField password;

    @FXML
    protected void login() {
        String username = this.username.getText();
        String password = this.password.getText();

//        if (username.equals("admin") && password.equals("admin")) {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setContentText("Đăng nhập thành công");
//            alert.show();
//        } else {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setContentText("Đăng nhập thất bại");
//            alert.show();
//        }

        switch (username) {
            case "khucnhan", "baokhanh", "vandam", "nguyenphuong" -> {
                if (password.equals("123456")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Đăng nhập thành công");
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Đăng nhập thất bại");
                    alert.show();
                }
            }
        }
    }
}