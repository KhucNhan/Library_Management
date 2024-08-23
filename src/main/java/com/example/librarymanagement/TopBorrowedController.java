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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static com.example.librarymanagement.UserController.currentUser;

public class TopBorrowedController implements Initializable {
    @FXML
    private TableView<Book> tableTop;
    @FXML
    private TableColumn<Book, String> idTop;
    @FXML
    private TableColumn<Book, String> imgTop;
    @FXML
    private TableColumn<Book, String> titleTop;
    @FXML
    private TableColumn<Book, String> authorTop;
    @FXML
    private TableColumn<Book, String> releaseYearTop;
    @FXML
    private TableColumn<Book, String> genreTop;
    @FXML
    private TableColumn<Book, String> statusTop;
    @FXML
    private TableColumn<Book, String> quantityTop;
    @FXML
    private TableColumn<Book, Integer> countTop;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(currentUser.getRole().equalsIgnoreCase("admin")) {
            initializeAdmin();
        } else {
            initializeUser();
        }
    }

    public void filteredBook(String startDate, String endDate) {
        BookController bookController = new BookController();
        LoanSlipController loanSlipController = new LoanSlipController();
        ObservableList<Book> filteredBook = FXCollections.observableArrayList();

        for (LoanSlip loanSlip : loanSlipController.getLoanSlips()) {
            if (loanSlip.getDate().compareTo(startDate) >= 0 && loanSlip.getReturnDate().compareTo(endDate) <= 0) {
                filteredBook.add(bookController.getBook(loanSlip.getIdBook()));
                if (filteredBook.size() == 5) {
                    tableTop.setItems(filteredBook);
                    tableTop.refresh();
                    return;
                }
            }
        }
    }

    public void search() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        filteredBook(start.getValue().format(format), end.getValue().format(format));
    }

    @FXML
    private DatePicker start;
    @FXML
    private DatePicker end;

    private void initializeAdmin() {
        LoanSlipController loanSlipController = new LoanSlipController();
        ObservableList<Book> top5 = loanSlipController.getTop5Books();
        idTop.setCellValueFactory(new PropertyValueFactory<Book, String>("Id"));
        imgTop.setCellValueFactory(new PropertyValueFactory<Book, String>("Img"));
        imgTop.setCellFactory(column -> new TableCell<Book, String>() {

            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                if (empty || imagePath == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(new Image(imagePath));
                    imageView.setFitHeight(50); // Chiều cao của ảnh
                    imageView.setFitWidth(50);  // Chiều rộng của ảnh
                    setGraphic(imageView);
                }
            }
        });
        titleTop.setCellValueFactory(new PropertyValueFactory<Book, String>("Title"));
        authorTop.setCellValueFactory(new PropertyValueFactory<Book, String>("Author"));
        releaseYearTop.setCellValueFactory(new PropertyValueFactory<Book, String>("ReleaseYear"));
        genreTop.setCellValueFactory(new PropertyValueFactory<Book, String>("Genre"));
        statusTop.setCellValueFactory(new PropertyValueFactory<Book, String>("Status"));
        quantityTop.setCellValueFactory(new PropertyValueFactory<Book, String>("Quantity"));
        countTop.setCellValueFactory(new PropertyValueFactory<Book, Integer>("Count"));
        tableTop.setItems(top5);
    }

    private void initializeUser() {
        LoanSlipController loanSlipController = new LoanSlipController();
        ObservableList<Book> top5 = loanSlipController.getTop5Books();
        idTop.setVisible(false);
        imgTop.setCellValueFactory(new PropertyValueFactory<Book, String>("Img"));
        imgTop.setCellFactory(column -> new TableCell<Book, String>() {

            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                if (empty || imagePath == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(new Image(imagePath));
                    imageView.setFitHeight(50); // Chiều cao của ảnh
                    imageView.setFitWidth(50);  // Chiều rộng của ảnh
                    setGraphic(imageView);
                }
            }
        });
        titleTop.setCellValueFactory(new PropertyValueFactory<Book, String>("Title"));
        authorTop.setCellValueFactory(new PropertyValueFactory<Book, String>("Author"));
        releaseYearTop.setCellValueFactory(new PropertyValueFactory<Book, String>("ReleaseYear"));
        genreTop.setCellValueFactory(new PropertyValueFactory<Book, String>("Genre"));
        statusTop.setCellValueFactory(new PropertyValueFactory<Book, String>("Status"));
        quantityTop.setVisible(false);
        countTop.setCellValueFactory(new PropertyValueFactory<Book, Integer>("Count"));
        tableTop.setItems(top5);
    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void goToLoginScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.show();
    }

    public void goToBookScene(ActionEvent event) throws IOException {
        if (currentUser.getRole().equalsIgnoreCase("admin")) {
            Parent root = FXMLLoader.load(getClass().getResource("AdminView.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1200, 800);
            stage.setTitle("Home");
            stage.setScene(scene);
            stage.show();
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("UserView.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1200, 800);
            stage.setTitle("Home");
            stage.setScene(scene);
            stage.show();
        }
    }

    public void goToLoanSlip(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LoanSlipView.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1200, 800);
        stage.setTitle("Loan SLip");
        stage.setScene(scene);
        stage.show();
    }
}
