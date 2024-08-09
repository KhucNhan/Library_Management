package com.example.librarymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class BookController implements Initializable {
    private ObservableList<Book> Books;

    public Book getBook(String text) {
        for (Book book : Books) {
            if (((book.getId()).equals((text)) || (book.getTitle().equals(text)))) {
                return book;
            }
        }
        return null;
    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void goToLoginScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1080, 720);
        stage.setScene(scene);
        stage.show();
    }

    public void goToLoanSlip(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LoanSlipView.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1080, 720);
        stage.setTitle("Loan Slip");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    TextField idAdd;
    @FXML
    TextField titleAdd;
    @FXML
    TextField authorAdd;
    @FXML
    TextField releaseYearAdd;
    @FXML
    TextField genreAdd;
    @FXML
    TextField statusAdd;

    public boolean add() {
        Book book = getBook(idAdd.getText());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (idAdd.getText().isEmpty() || titleAdd.getText().isEmpty() || authorAdd.getText().isEmpty() || releaseYearAdd.getText().isEmpty() || genreAdd.getText().isEmpty() || statusAdd.getText().isEmpty()) {
            alert.setContentText("Không được để trống");
            alert.show();
            return false;
        }

        if (!statusAdd.getText().equalsIgnoreCase("true") && !statusAdd.getText().equalsIgnoreCase("false")) {
            alert.setContentText("Status phải là true hoặc false");
            alert.show();
            return false;
        }

        try {
            Double num = Double.parseDouble(releaseYearAdd.getText());
        } catch (NumberFormatException e) {
            alert.setContentText("Hãy nhập đúng giá trị năm xuất bản");
            alert.show();
            return false;
        }

        if (book == null) {
            Books.add(new Book(idAdd.getText(), titleAdd.getText(), authorAdd.getText(), releaseYearAdd.getText(), genreAdd.getText(), statusAdd.getText()));
            return true;
        } else {
            alert.setContentText("This id is exist. Try again please.");
            alert.show();
        }
        return false;
    }

    @FXML
    private TableView<Book> table;
    @FXML
    private TableColumn<Book, String> idCol;
    @FXML
    private TableColumn<Book, String> titleCol;
    @FXML
    private TableColumn<Book, String> authorCol;
    @FXML
    private TableColumn<Book, String> releaseYearCol;
    @FXML
    private TableColumn<Book, String> genreCol;
    @FXML
    private TableColumn<Book, String> statusCol;
    @FXML
    private TableColumn<Book, Void> actionCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Books = FXCollections.observableArrayList(
                new Book("b1", "toan", "nhan", "2018", "Subject", "true"),
                new Book("b2", "tieng anh", "khanh", "2018", "Subject", "true"),
                new Book("b3", "one piece", "oda", "1999", "Animation", "true"),
                new Book("b4", "doraemon", "fuji", "2000", "Animation", "true")
        );
        idCol.setCellValueFactory(new PropertyValueFactory<Book, String>("Id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("Title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("Author"));
        releaseYearCol.setCellValueFactory(new PropertyValueFactory<Book, String>("ReleaseYear"));
        genreCol.setCellValueFactory(new PropertyValueFactory<Book, String>("Genre"));
        statusCol.setCellValueFactory(new PropertyValueFactory<Book, String>("Status"));
        actionCol.setCellValueFactory(new PropertyValueFactory<Book, Void>(""));
        Callback<TableColumn<Book, Void>, TableCell<Book, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Book, Void> call(TableColumn<Book, Void> bookVoidTableColumn) {
                final TableCell<Book, Void> cell = new TableCell<>() {

                    private final Button editButton = new Button("Edit");
                    private final Button removeButton = new Button("Remove");

                    {
                        editButton.setOnAction((ActionEvent event) -> {
                            Book book = getTableView().getItems().get(getIndex());
                            showEditDialog(book);
                        });
                        editButton.setPrefWidth(75);

                        removeButton.setOnAction((ActionEvent event) -> {
                            Book book = getTableView().getItems().get(getIndex());
                            getTableView().getItems().remove(book);
                        });
                        removeButton.setPrefWidth(75);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox hBox = new HBox(editButton, removeButton);
                            hBox.setSpacing(10);
                            HBox.setMargin(editButton, new Insets(0, 5, 0, 5)); // Thiết lập margin cho nút Edit
                            HBox.setMargin(removeButton, new Insets(0, 5, 0, 5)); // Thiết lập margin cho nút Remove
                            setGraphic(hBox);
                        }
                    }
                };
                return cell;
            }
        };
        actionCol.setCellFactory(cellFactory);
        table.setItems(Books);
    }

    private void showEditDialog(Book book) {
        // Tạo một dialog để chỉnh sửa sản phẩm
        TextField id = new TextField(book.getId());
        TextField title = new TextField(book.getTitle());
        TextField author = new TextField(book.getAuthor());
        TextField releaseYear = new TextField(book.getReleaseYear());
        TextField genre = new TextField(book.getGenre());
        TextField status = new TextField(book.getStatus());

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            if (id.getText().isEmpty() || title.getText().isEmpty() || author.getText().isEmpty() || releaseYear.getText().isEmpty() || genre.getText().isEmpty() || status.getText().isEmpty()) {
                alert.setContentText("Không được để trống");
                alert.show();
                return;
            }

            if (!status.getText().equalsIgnoreCase("true") && !status.getText().equalsIgnoreCase("false")) {
                alert.setContentText("Status phải là true hoặc false");
                alert.show();
                return;
            }

            try {
                Double num = Double.parseDouble(releaseYear.getText());
            } catch (NumberFormatException exception) {
                alert.setContentText("Hãy nhập đúng giá trị năm xuất bản");
                alert.show();
                return;
            }

            for(Book b : Books) {
                if (b.getId().equals(id)) {
                    alert.setContentText("This id is exist. Try again please.");
                    alert.show();
                    return;
                }
            }

            book.setId(id.getText());
            book.setTitle(title.getText());
            book.setAuthor(author.getText());
            book.setReleaseYear(releaseYear.getText());
            book.setGenre(genre.getText());
            book.setStatus(status.getText());

            table.refresh(); // Cập nhật lại TableView
        });

        VBox vbox = new VBox(id, title, author, releaseYear, genre, status, saveButton);
        vbox.setSpacing(10);
        Scene scene = new Scene(vbox, 240,480);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Edit");
        stage.show();
    }
}