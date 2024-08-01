package com.example.librarymanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class UserController {
    @FXML
    public TextField signUpUsername;
    @FXML
    public PasswordField signUpPassword;
    @FXML
    public PasswordField re_password;
    private int count = 4;
    User user1 = new Admin("Nhan12345", "khucnhan", "nhan2005", "admin");
    User user2 = new Admin("Khanh23456", "baokhanh", "khanh2005", "admin");
    User user3 = new Admin("Dam34567", "vandam", "dam2005", "admin");
    User user4 = new User("Phuong45678", "nguyenphuong", "phuong2005");
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
    protected boolean login() {
        String username = this.username.getText();
        String password = this.password.getText();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        if (!findUserBoolean(username)) {
            alert.setContentText("Username không tồn tại.");
            alert.show();
            return false;
        } else {
            if (findUser(username).getUsername().equals(username) && findUser(username).getPassword().equals(password)) {
                if (findUser(username).getRole().equalsIgnoreCase("admin")) {
                    alert.setContentText("Success! Xin chào admin " + findUser(username).getUsername() + ".");
                } else {
                    alert.setContentText("Success! Xin chào user " + findUser(username).getUsername() + ".");
                }
            } else {
                alert.setContentText("Incorrect username or password!");
                alert.show();
                return false;
            }
        }
        alert.show();
        return true;
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

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void login(ActionEvent event) throws IOException {
        if (login()) {
            Parent root = FXMLLoader.load(getClass().getResource("Book.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 720,480);
            stage.setTitle("Home");
            stage.setScene(scene);
            stage.show();
        }
    }
    @FXML
    public void signup(ActionEvent event) throws IOException{
        String username = this.signUpUsername.getText();
        String password = this.signUpPassword.getText();
        String repassword = this.re_password.getText();
        String id = String.valueOf(count);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        if(password.equals(repassword)) {
            alert.setContentText("Tạo tài khoản thành công! Bạn vui lòng đăng nhập lại.");
            alert.show();
            User newuser = new User(id, username, password);
            users = Arrays.copyOf(users, users.length + 1);
            users[users.length - 1] = newuser;
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 720,480);
            stage.setTitle("Home");
            stage.setScene(scene);
            stage.show();
        } else {
            alert.setContentText("Mật khẩu khng trùng khớp. Vui lòng nhập lại!");
            alert.show();
        }
    }

    @FXML
    public void goToSignup(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 720,480);
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();
    }
}