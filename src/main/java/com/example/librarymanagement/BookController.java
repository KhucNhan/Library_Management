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
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.example.librarymanagement.UserController.currentUser;

public class BookController implements Initializable {
    private static ObservableList<Book> books;

    public Book getBook(String text) {
        for (Book book : books) {
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
//        table.refresh();
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1080, 720);
        stage.setScene(scene);
        stage.show();
    }

    public void goToLoanSlip(ActionEvent event) throws IOException {
//        table.refresh();
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

        // Kiểm tra các trường thông tin
        if (idAdd.getText().isEmpty() || titleAdd.getText().isEmpty() || authorAdd.getText().isEmpty() || releaseYearAdd.getText().isEmpty() || genreAdd.getText().isEmpty() || statusAdd.getText().isEmpty()) {
            alert.setContentText("No blank fields allowed!");
            alert.show();
            return false;
        }

        // Kiểm tra trạng thái
        if (!statusAdd.getText().equalsIgnoreCase("activated") && !statusAdd.getText().equalsIgnoreCase("unactivated")) {
            alert.setContentText("Status must be activated or unactivated.");
            alert.show();
            return false;
        }

        // Kiểm tra năm phát hành
        try {
            Integer.parseInt(releaseYearAdd.getText());
        } catch (NumberFormatException e) {
            alert.setContentText("Enter a valid number for Release Year.");
            alert.show();
            return false;
        }

        // Kiểm tra URL
        if (!isValidURL(imgAdd.getText())) {
            alert.setContentText("Invalid URL.");
            alert.show();
            return false;
        }

        // Kiểm tra ID đã tồn tại
        if (book == null) {
            books.add(new Book(
                    idAdd.getText(),
                    imgAdd.getText(),
                    titleAdd.getText(),
                    authorAdd.getText(),
                    releaseYearAdd.getText(),
                    genreAdd.getText(),
                    statusAdd.getText()
            ));
            saveBookToFile(); // Lưu vào file sau khi thêm thành công
            return true;
        } else {
            alert.setContentText("This ID already exists. Please try another.");
            alert.show();
            return false;
        }
    }

    private void loadBooksFromFile() {
        books.clear();
        File file = new File("books.txt");
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    Book book = new Book(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
                    books.add(book);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            showError("Invalid data format, " + "There was an error reading the product data.");
        }
    }


    private void saveBookToFile() {
        // Xác định đường dẫn file trong thư mục dự án hoặc một thư mục cụ thể
        File file = new File("books.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Book book : books) {
                // Tạo chuỗi dữ liệu cho từng sách
                String line = String.join(",",
                        book.getId(),
                        book.getImg(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getReleaseYear(),
                        book.getGenre(),
                        book.getStatus()
                );
                writer.write(line);
                writer.newLine(); // Thêm dòng mới sau mỗi sản phẩm
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error saving data: Could not save product data to file.");
        }
    }

    private void clearInFile() {
        File file = new File("books.txt");
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
        books = FXCollections.observableArrayList();
        loadBooksFromFile();
        if (currentUser != null) {
            if (currentUser.getRole().equalsIgnoreCase("admin")) {
                table.setItems(books);
                initializeAdminTableView();
            } else {
                tableUser.setItems(books);
                initializeUserTableView();
            }
        } else {
            System.out.println(currentUser.getRole());
        }
    }

    @FXML
    private TableView<Book> tableUser;
    @FXML
    private TableColumn<Book, String> userImgCol;
    @FXML
    private TableColumn<Book, String> userTitleCol;
    @FXML
    private TableColumn<Book, String> userAuthorCol;
    @FXML
    private TableColumn<Book, String> userReleaseYearCol;
    @FXML
    private TableColumn<Book, String> userGenreCol;
    @FXML
    private TableColumn<Book, String> userStatusCol;
    @FXML
    private TableColumn<Book, Void> userActionCol;

    private void initializeAdminTableView() {
        books = FXCollections.observableArrayList();
        loadBooksFromFile();
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
        Callback<TableColumn<Book, String>, TableCell<Book, String>> statusCellFactory = new Callback<>() {
            @Override
            public TableCell<Book, String> call(TableColumn<Book, String> bookStatusTableColumn) {
                final TableCell<Book, String> statusCellFactory = new TableCell<>() {

                    private final Button statusButton = new Button();

                    {
                        statusButton.setOnAction((ActionEvent event) -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            Book book = getTableView().getItems().get(getIndex());
                            if (book.getStatus().equalsIgnoreCase("Activated")) {
                                book.setStatus("Unactivated");
                                statusButton.setText("Activated");
                            } else {
                                if (!isBookOnLoan(book.getId())) {
                                    book.setStatus("Activated");
                                    statusButton.setText("Unactivated");
                                } else {
                                    alert.setContentText("This book is on loan, can't change status.");
                                    alert.show();
                                }
                            }
                            save();
                            table.refresh();
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
                            save();
                        });
                        editButton.setPrefWidth(75);

                        removeButton.setOnAction((ActionEvent event) -> {
                            if (showConfirmation()) {
                                Book book = getTableView().getItems().get(getIndex());
                                getTableView().getItems().remove(book);
                                save();
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
        table.setItems(books);
    }

    private void initializeUserTableView() {
        books = FXCollections.observableArrayList();
        loadBooksFromFile();
        userImgCol.setCellValueFactory(new PropertyValueFactory<Book, String>("Img"));
        userImgCol.setCellFactory(column -> new TableCell<Book, String>() {
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
        userTitleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("Title"));
        userAuthorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("Author"));
        userReleaseYearCol.setCellValueFactory(new PropertyValueFactory<Book, String>("ReleaseYear"));
        userGenreCol.setCellValueFactory(new PropertyValueFactory<Book, String>("Genre"));
        userStatusCol.setCellValueFactory(new PropertyValueFactory<Book, String>("Status"));
        userActionCol.setCellFactory(column -> new TableCell<Book, Void>() {
            private final Button borrowButton = new Button("Borrow");

            {
                borrowButton.setOnAction((ActionEvent event) -> {
                    Book book = getTableView().getItems().get(getIndex());

                    // Kiểm tra điều kiện cho phép mượn sách
                    if ("Unactivated".equalsIgnoreCase(book.getStatus())) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("This book is not available for borrowing.");
                        alert.show();
                    } else {
                        showBorrowDialog(book);
                    }
                });
                borrowButton.setPrefWidth(75);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    Book book = getTableRow().getItem();
                    if ("Unactivated".equalsIgnoreCase(book.getStatus())) {
                        borrowButton.setDisable(true);
                        setGraphic(null);
                    } else {
                        borrowButton.setDisable(false);
                        HBox hBox = new HBox(borrowButton);
                        hBox.setSpacing(10);
                        HBox.setMargin(borrowButton, new Insets(0, 5, 0, 5)); // Thiết lập margin cho nút Borrow
                        setGraphic(hBox);
                    }
                }
            }
        });
        tableUser.setItems(books);
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


        VBox vbox = new VBox(title, img, author, releaseYear, genre, status, saveButton);
        vbox.setSpacing(10);
        Scene scene = new Scene(vbox, 240, 480);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Edit");
        stage.show();

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
            book.setTitle(title.getText());
            book.setImg(img.getText());
            book.setAuthor(author.getText());
            book.setReleaseYear(releaseYear.getText());
            book.setGenre(genre.getText());
            book.setStatus(status.getText());
            table.refresh(); // Cập nhật lại TableView
            stage.close();
        });
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

    public static LoanSlip newLoanSlip;
    private void showBorrowDialog(Book book) {
        // Tạo Stage cho dialog
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Borrow Book");

        // Tạo các thành phần giao diện
        TextField paidDate = new TextField();
        Button saveButton = new Button("Save");
        Label paidDateLabel = new Label("Return Date (yyyy/MM/dd):");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Tạo bố cục cho dialog
        VBox vbox = new VBox(10); // khoảng cách giữa các thành phần là 10
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(paidDateLabel, paidDate, saveButton);

        Scene scene = new Scene(vbox, 480, 360);
        dialogStage.setScene(scene);

        LoanSlipController loanSlipController = new LoanSlipController();

        // Lấy ngày hiện tại
        LocalDate currentDate = LocalDate.now();

        // Định dạng ngày
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String formattedDate = currentDate.format(formatter);

        saveButton.setOnAction(e -> {
            newLoanSlip = new LoanSlip(currentUser.getUserId(), book.getId(), formattedDate, paidDate.getText(), "on loan");
            if (!isValidDate(paidDate.getText(), "yyyy/MM/dd")) {
                alert.setContentText("Date format: yyyy/MM/dd");
                alert.show();
            } else {
                newLoanSlip.setReturnDate(paidDate.getText());
                book.setStatus("Unactivated");
                loanSlipController.addNewLoanSlip();
                save();
                tableUser.refresh();
                dialogStage.close(); // Đóng dialog sau khi lưu
            }
        });

        // Hiển thị dialog và chờ người dùng thao tác
        dialogStage.showAndWait();
    }

    public static boolean isValidDate(String dateStr, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        try {
            LocalDate date = LocalDate.parse(dateStr, formatter);

            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void save() {
        clearInFile();
        saveBookToFile();
    }

    public void updateBookStatus(String bookId, String status) {
        for (Book book : books) {  // books là danh sách chứa tất cả các đối tượng Book
            if (book.getId().equals(bookId)) {
                book.setStatus(status);
                saveBookToFile(); // Lưu thay đổi vào tệp
                break;
            }
        }
    }

    public boolean isBookOnLoan(String bookId) {
        // Tải danh sách phiếu mượn từ file (hoặc database)
        LoanSlipController loanSlipController = new LoanSlipController();
        loanSlipController.loadLoanSlipFromFile();

        // Kiểm tra xem sách có đang ở trạng thái "On loan" trong bất kỳ phiếu mượn nào không
        for (LoanSlip loanSlip : loanSlipController.getLoanSlips()) {
            if (loanSlip.getIdBook().equals(bookId) && loanSlip.getStatus().equalsIgnoreCase("On loan")) {
                return true; // Sách đang được mượn
            }
        }

        return false; // Sách không được mượn
    }
}
