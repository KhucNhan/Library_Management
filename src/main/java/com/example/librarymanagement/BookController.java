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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class BookController implements Initializable {
    private static ObservableList<Book> Books;

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
        table.refresh();
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1080, 720);
        stage.setScene(scene);
        stage.show();
    }

    public void goToLoanSlip(ActionEvent event) throws IOException {
        table.refresh();
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
    TextField imgAdd;
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
            alert.setContentText("No blank!");
            alert.show();
            return false;
        }

        if (!statusAdd.getText().equalsIgnoreCase("activated") && !statusAdd.getText().equalsIgnoreCase("unactivated")) {
            alert.setContentText("Status must be activated or unactivated.");
            alert.show();
            return false;
        }

        try {
            Double num = Double.parseDouble(releaseYearAdd.getText());
        } catch (NumberFormatException e) {
            alert.setContentText("Enter a numbers in Release year.");
            alert.show();
            return false;
        }

        if (!isValidURL(imgAdd.getText())) {
            alert.setContentText("Can't use this url.");
            alert.show();
            return false;
        }

        if (book == null) {
            Books.add(new Book(idAdd.getText(), imgAdd.getText(), titleAdd.getText(), authorAdd.getText(), releaseYearAdd.getText(), genreAdd.getText(), statusAdd.getText()));
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
    private TableColumn<Book, String> imgCol;
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
                new Book("b1", "file:///C:/Users/ADMIN/AppData/Local/Messenger/TamStorage/media_bank/AdvancedCrypto/100055416699838/persistent/att.Vft2Qo0-MMjvfI4FZ8391CJmhPK9Pvc_vqrS9_7gwxg.jpg", "toan", "nhan", "2018", "Subject", "Activated"),
                new Book("b2", "file:///C:/Users/ADMIN/AppData/Local/Messenger/TamStorage/media_bank/AdvancedCrypto/100055416699838/persistent/att.VbhMsFVRRJ3A6vNB7I-_y4OO6EBXUATKAnhnvusjiuU.jpg", "tieng anh", "khanh", "2018", "Subject", "Activated"),
                new Book("b3", "file:///C:/Users/ADMIN/AppData/Local/Messenger/TamStorage/media_bank/AdvancedCrypto/100055416699838/persistent/att.3GlCsPxLsXIL-mLj_UvwGJTiGfFB49UYUhZWVQpBUEQ.jpg", "one piece", "oda", "1999", "Animation", "Activated"),
                new Book("b4", "file:///C:/Users/ADMIN/AppData/Local/Messenger/TamStorage/media_bank/AdvancedCrypto/100055416699838/persistent/att.cm6mbU9Q3v_YXeHPt4OInVIw2NgL0XNMKsZ_tfIcLCI.jpg", "doraemon", "fuji", "2000", "Animation", "Unactivated")
        );
        idCol.setCellValueFactory(new PropertyValueFactory<Book, String>("Id"));
        imgCol.setCellValueFactory(new PropertyValueFactory<Book, String>("Img"));
        imgCol.setCellFactory(column -> new TableCell<Book, String>() {
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
        titleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("Title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("Author"));
        releaseYearCol.setCellValueFactory(new PropertyValueFactory<Book, String>("ReleaseYear"));
        genreCol.setCellValueFactory(new PropertyValueFactory<Book, String>("Genre"));
        statusCol.setCellValueFactory(new PropertyValueFactory<Book, String>("Status"));
        loadBooksFromFile();
        Callback<TableColumn<Book, String>, TableCell<Book, String>> statusCellFactory = new Callback<>() {
            @Override
            public TableCell<Book, String> call(TableColumn<Book, String> bookStatusTableColumn) {
                final TableCell<Book, String> statusCellFactory = new TableCell<>() {

                    private final Button statusButton = new Button();

                    {
                        statusButton.setOnAction((ActionEvent event) -> {
                            Book book = getTableView().getItems().get(getIndex());
                            if (book.getStatus().equalsIgnoreCase("Activated")) {
                                book.setStatus("Unactivated");
                                statusButton.setText("Activated");
                                table.refresh();
                            } else {
                                book.setStatus("Activated");
                                statusButton.setText("Unactivated");
                                table.refresh();
                            }
                        });
                        statusButton.setPrefWidth(150);
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Book book = getTableView().getItems().get(getIndex());
                            statusButton.setText(book.getStatus());
                            HBox hBox = new HBox(statusButton);
                            hBox.setSpacing(10);
                            HBox.setMargin(statusButton, new Insets(0, 5, 0, 5)); // Thiết lập margin cho nút Edit
                            setGraphic(hBox);
                        }
                    }
                };
                return statusCellFactory;
            }
        };
        statusCol.setCellFactory(statusCellFactory);
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
                            if (showConfirmation()) {
                                Book book = getTableView().getItems().get(getIndex());
                                getTableView().getItems().remove(book);
                            }
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
        TextField title = new TextField(book.getTitle());
        TextField img = new TextField(book.getImg());
        TextField author = new TextField(book.getAuthor());
        TextField releaseYear = new TextField(book.getReleaseYear());
        TextField genre = new TextField(book.getGenre());
        TextField status = new TextField(book.getStatus());

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            if (title.getText().isEmpty() || author.getText().isEmpty() || releaseYear.getText().isEmpty() || genre.getText().isEmpty() || status.getText().isEmpty()) {
                alert.setContentText("No blank!");
                alert.show();
                return;
            }

            if (!status.getText().equalsIgnoreCase("Activated") && !status.getText().equalsIgnoreCase("Unactivated")) {
                alert.setContentText("Status must be activated or unactivated.");
                alert.show();
                return;
            }

            try {
                Double num = Double.parseDouble(releaseYear.getText());
            } catch (NumberFormatException exception) {
                alert.setContentText("Enter a numbers in Release year.");
                alert.show();
                return;
            }

            if (!isValidURL(img.getText())) {
                alert.setContentText("Can't use this url.");
                alert.show();
                return;
            }

//            for (Book value : Books) {
//                if (value.getId().equals(id.getText())) {
//                    alert.setContentText("This id is exist. Try again please.");
//                    alert.show();
//                    return;
//                }
//            }

//            book.setId(id.getText());
            book.setTitle(title.getText());
            book.setImg(img.getText());
            book.setAuthor(author.getText());
            book.setReleaseYear(releaseYear.getText());
            book.setGenre(genre.getText());
            book.setStatus(status.getText());

            table.refresh(); // Cập nhật lại TableView
        });

        VBox vbox = new VBox(title, img, author, releaseYear, genre, status, saveButton);
        vbox.setSpacing(10);
        Scene scene = new Scene(vbox, 240, 480);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Edit");
        stage.show();
    }

    public boolean showConfirmation() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete book");
        alert.setHeaderText("Are you sure want to remove this book ?");

        Optional<ButtonType> option = alert.showAndWait();

        return option.get() == ButtonType.OK;
    }

    public static boolean isValidURL(String urlString) {
        try {
            Image image = new Image(urlString, true);
            // Kiểm tra nếu ảnh không bị lỗi
            return !image.isError();
        } catch (Exception e) {
            return false;
        }
    }

    private void loadBooksFromFile() {
        File file = new File("C:\\Users\\theho\\Downloads\\Library_Management\\Project.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    String id = parts[0];
                    String title = parts[1];
                    String author = parts[2];
                    String releaseYear = parts[3];
                    String genre = parts[4];
                    String status = parts[5];
                    Books.add(new Book(id, title, author, releaseYear, genre, status));
                }
                table.setItems(Books);
            } catch (IOException e) {
                e.printStackTrace(); // Hoặc sử dụng phương thức showError() để thông báo lỗi
            }
        }
    }

    @FXML
    private void saveProductsToFile() {
        FileChooser fileChooser = new FileChooser();
        String file = "Project.txt";

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Book product : Books) {
                    writer.write(product.toString());
                    writer.newLine();
                }
            } catch (IOException e) {
                showError("Failed to save products: " + e.getMessage());
            }
        }
    }
    private void showError(String message) {
        // Show an error dialog or alert here
    }
}

