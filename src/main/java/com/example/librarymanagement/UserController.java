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
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML
    public TextField signUpUsername;
    @FXML
    public PasswordField signUpPassword;
    @FXML
    public PasswordField re_password;
    private static int count;

    // Chuyển từ mảng User[] sang ObservableList<User>
    static ObservableList<User> users = FXCollections.observableArrayList();

    @FXML
    TextField username;
    @FXML
    PasswordField password;

    // Implement phương thức setRole để thay đổi role của User
    public void setRole(String username, String role) {
        User user = findUser(username);
        if (user != null) {
            user.setRole(role);
        }
    }

//    private void addAdmin() {
//        users.add(new Admin("Nhan12345", "khucnhan", "nhan2005", "admin"));
//        users.add(new Admin("Khanh23456", "baokhanh", "khanh2005", "admin"));
//        save();
//    }

    public static User currentUser;

    @FXML
    protected boolean login(ActionEvent event) throws IOException {
        loadUsersFromFile();
        String username = this.username.getText();
        String password = this.password.getText();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        if (!findUserBoolean(username)) {
            alert.setContentText("Username không tồn tại.");
            alert.show();
            return false;
        } else {
            currentUser = findUser(username);
            if (currentUser.getUsername().equals(username) && currentUser.getPassword().equals(password)) {
                FXMLLoader loader;
                Parent root;
                if (currentUser.getRole().equalsIgnoreCase("admin")) {
                    alert.setContentText("Success! Xin chào admin " + currentUser.getUsername() + ".");
                    loader = new FXMLLoader(getClass().getResource("AdminView.fxml"));
                    root = loader.load();
                } else {
                    alert.setContentText("Success! Xin chào user " + currentUser.getUsername() + ".");
                    loader = new FXMLLoader(getClass().getResource("UserView.fxml"));
                    root = loader.load();
                }

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 1200, 800);
                stage.setTitle("Home");
                stage.setScene(scene);
                stage.show();
            } else {
                alert.setContentText("Incorrect username or password!");
                alert.show();
                return false;
            }
        }
        return true;
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

    private void switchScene(ActionEvent event, String fxmlFile, String title) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1200, 800);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public void goToLogin(ActionEvent event) throws IOException {
        switchScene(event, "Login.fxml", "Home");
    }

    public void goToSignup(ActionEvent event) throws IOException {
        switchScene(event, "SignUp.fxml", "Home");
    }

    @FXML
    public void signup(ActionEvent event) throws IOException {
        String username = this.signUpUsername.getText();
        String password = this.signUpPassword.getText();
        String repassword = this.re_password.getText();
        String id = String.valueOf(count);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (findUserBoolean(username)) {
            alert.setContentText("Username này đã tồn tại.");
            alert.show();
            return;
        }
        if (username.isEmpty()) {
            alert.setContentText("Không được để trống username.");
            alert.show();
            return;
        }

        if (username.length() < 8) {
            alert.setContentText("Username phải có số ký tự lớn hơn hoặc bằng 8.");
            alert.show();
            return;
        }

        if (password.isEmpty()) {
            alert.setContentText("Không được để trống password.");
            alert.show();
            return;
        }

        if (password.length() < 8) {
            alert.setContentText("Password phải có số ký tự lớn hơn hoặc bằng 8.");
            alert.show();
            return;
        }

        if (password.equals(repassword)) {
            alert.setContentText("Tạo tài khoản thành công! Bạn vui lòng đăng nhập lại.");
            alert.show();
            User newuser = new User(id, username, password);
            users.add(newuser);
            saveUserToFile();
            saveIdToFile(++count); // Tăng count và lưu lại vào file

            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1200, 800);
            stage.setTitle("Home");
            stage.setScene(scene);
            stage.show();
        } else {
            alert.setContentText("Mật khẩu không trùng khớp. Vui lòng nhập lại!");
            alert.show();
        }
    }

    private int loadIdFromFile() {
        File file = new File("user_id.txt");
        if (!file.exists()) {
            return 1; // Nếu file không tồn tại, bắt đầu từ 1
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null) {
                return Integer.parseInt(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1; // Nếu có lỗi, bắt đầu từ 1
    }

    private void saveIdToFile(int id) {
        File file = new File("user_id.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(String.valueOf(id));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Khởi tạo danh sách người dùng và load từ file
        users = FXCollections.observableArrayList(
                new Admin("Nhan12345", "khucnhan", "nhan2005", "admin"),
                new Admin("Khanh23456", "baokhanh", "khanh2005", "admin")
        );
        loadUsersFromFile();
        count = loadIdFromFile(); // Load id từ file
    }

    private void loadUsersFromFile() {
        File file = new File("users.txt");
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            users.clear(); // Xóa tất cả các người dùng hiện có trước khi tải lại
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) { // Kiểm tra số lượng phần tử
                    User user = new User(parts[0], parts[1], parts[2]);
                    user.setRole(parts[3]); // Cập nhật role
                    users.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading data: Could not load user data from file.");
        }
    }

    private void saveUserToFile() {
        File file = new File("users.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (User user : users) {
                String line = String.join(",",
                        user.getUserId(),
                        user.getUsername(),
                        user.getPassword(),
                        user.getRole()
                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error saving data: Could not save user data to file.");
        }
    }

    private void clearInFile() {
        File file = new File("users.txt");
        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void save() {
        clearInFile();
        loadUsersFromFile();
    }
}
