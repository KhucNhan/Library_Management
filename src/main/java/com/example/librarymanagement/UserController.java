package com.example.librarymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UserController {
    @FXML
    public TextField signUpUsername;
    @FXML
    public PasswordField signUpPassword;
    @FXML
    public PasswordField re_password;
//    User user1 = new Admin("1", "khucnhan", "nhan2005", "admin");
//    User user2 = new Admin("2", "baokhanh", "khanh2005", "admin");
//    User user3 = new Admin("3","vandam","dam2005","admin");
//    User user4 = new User("4","nguyenphuong","phuong2005");
    private ArrayList<User> users;
    @FXML
    TextField username;
    @FXML
    PasswordField password;
    public void setRole(String username, String role) {
        findUser(username).setRole(role);
    }

    public void add(User user) {
        if(findUser(user.getUsername()) == null) {
            users.add(user);
        }
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
            Parent root = FXMLLoader.load(getClass().getResource("ListView.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1080,720);
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
        String id = String.valueOf(users.size());
        if (users.isEmpty()) {
            id = "0";
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if(username.isEmpty()) {
            alert.setContentText("Không được để trống username.");
            alert.show();
            return;
        }

        if(password.isEmpty()) {
            alert.setContentText("Không được để trống password.");
            alert.show();
            return;
        }

        if(password.equals(repassword)) {
            alert.setContentText("Tạo tài khoản thành công! Bạn vui lòng đăng nhập lại.");
            alert.show();
            User newuser = new User(id, username, password);
            users.add(newuser);
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1080,720);
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
        scene = new Scene(root, 1080,720);
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void goToLogin(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1080,720);
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();
    }
//    @FXML
//    private TableView<User> tableUser;
//    @FXML
//    private TableColumn<User, String> userIdCol;
//    @FXML
//    private TableColumn<User, String> usernameCol;
//    @FXML
//    private TableColumn<User, String> passwordCol;
//    @FXML
//    private TableColumn<User, String> roleCol;
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        users = FXCollections.observableArrayList(
//                new Admin("1", "khucnhan", "nhan2005", "admin"),
//                new Admin("2", "baokhanh", "khanh2005", "admin"),
//                new Admin("3", "vandam", "dam2005", "admin"),
//                new User("4", "nguyenphuong", "phuong2005")
//        );
//        userIdCol.setCellValueFactory(new PropertyValueFactory<User, String>("userId"));
//        usernameCol.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
//        passwordCol.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
//        roleCol.setCellValueFactory(new PropertyValueFactory<User, String>("role"));
//        tableUser.setItems(users);
//    }
}