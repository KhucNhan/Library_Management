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
import java.text.Normalizer;
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

    public ObservableList<Book> getBooks() {
        loadBooksFromFile();
        return books;
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

    public void goToLoanSlip(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LoanSlipView.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1200, 800);
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
    @FXML
    TextField quantityAdd;

    public boolean add() {
        Book book = getBook(idAdd.getText());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Kiểm tra các trường thông tin
        if (idAdd.getText().isEmpty() || titleAdd.getText().isEmpty() || authorAdd.getText().isEmpty() || releaseYearAdd.getText().isEmpty() || genreAdd.getText().isEmpty() || quantityAdd.getText().isEmpty()) {
            alert(alert, "No blank fields allowed!");
            return false;
        }

        // Kiểm tra năm phát hành
        try {
            Integer.parseInt(releaseYearAdd.getText());
        } catch (NumberFormatException e) {
            alert(alert, "Enter a valid number for Release Year.");
            return false;
        }

        try {
            Integer.parseInt(quantityAdd.getText());
        } catch (NumberFormatException e) {
            alert(alert, "Enter a valid number for Quantity.");
            return false;
        }

        // Kiểm tra URL
        if (!isValidURL(imgAdd.getText())) {
            alert(alert, "Invalid URL.");
            return false;
        }

        // Kiểm tra ID đã tồn tại
        if (book == null) {
            if (statusAdd.getText().isEmpty()) {
                alert(alert, "No blank fields allowed!");
                return false;
            }
            if (!statusAdd.getText().equalsIgnoreCase("activated") && !statusAdd.getText().equalsIgnoreCase("unactivated")) {
                alert(alert, "Status must be activated or unactivated.");
                return false;
            } else {
                books.add(new Book(
                        idAdd.getText(),
                        imgAdd.getText(),
                        titleAdd.getText(),
                        authorAdd.getText(),
                        releaseYearAdd.getText(),
                        genreAdd.getText(),
                        statusAdd.getText(),
                        quantityAdd.getText()
                ));
                saveBookToFile(); // Lưu vào file sau khi thêm thành công
                return true;
            }
        } else if (idAdd.getText().equals(getBook(idAdd.getText()).getId()) || titleAdd.getText().equals(getBook(idAdd.getText()).getTitle()) || authorAdd.getText().equals(getBook(idAdd.getText()).getAuthor()) || releaseYearAdd.getText().equals(getBook(idAdd.getText()).getReleaseYear()) || genreAdd.getText().equals(getBook(idAdd.getText()).getGenre())) {
            int newQuantity = Integer.parseInt(getBook(idAdd.getText()).getQuantity()) + Integer.parseInt(quantityAdd.getText());
            getBook(idAdd.getText()).setQuantity("" + newQuantity);
            save();
            table.refresh();
            return true;
        } else {
            alert(alert, "This id is existed!");
            return false;
        }
    }

    void loadBooksFromFile() {
        books.clear();
        File file = new File("books.txt");
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 9) {
                    Book book = new Book(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7]);
                    book.setCount(parts[8]);
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
                        book.getStatus(),
                        book.getQuantity(),
                        book.getCount()
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
    private TableColumn<Book, String> quantityCol;
    @FXML
    private TableColumn<Book, String> countCol;
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

    private void showBookDetailsDialog(Book book) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Book Details");
        alert.setHeaderText(null);

        // Tạo một ImageView với hình ảnh từ URL
        ImageView imageView = new ImageView();
        Image image = new Image(book.getImg());
        imageView.setImage(image);
        imageView.setFitHeight(100); // Thiết lập chiều cao cho hình ảnh
        imageView.setFitWidth(100);  // Thiết lập chiều rộng cho hình ảnh

        // Tạo nội dung chính bao gồm cả hình ảnh và văn bản
        VBox content = new VBox();
        content.setSpacing(10); // Khoảng cách giữa các phần tử trong VBox

        Label bookDetails = new Label(
                "ID: " + book.getId() + "\n" +
                        "Title: " + book.getTitle() + "\n" +
                        "Author: " + book.getAuthor() + "\n" +
                        "Release Year: " + book.getReleaseYear() + "\n" +
                        "Genre: " + book.getGenre() + "\n" +
                        "Status: " + book.getStatus()
        );

        content.getChildren().addAll(imageView, bookDetails); // Thêm hình ảnh và văn bản vào VBox

        alert.getDialogPane().setContent(content); // Đặt VBox làm nội dung chính của Alert

        alert.getDialogPane().setPrefSize(250, 300);

        alert.showAndWait(); // Hiển thị hộp thoại và chờ người dùng đóng nó
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
        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Kiểm tra xem người dùng có nhấp đúp chuột hay không
                Book selectedBook = table.getSelectionModel().getSelectedItem(); // Lấy đối tượng được chọn
                if (selectedBook != null) {
                    showBookDetailsDialog(selectedBook); // Hiển thị hộp thoại với thông tin chi tiết
                }
            }
        });
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
                            if (book.getStatus().equalsIgnoreCase("Activated") && !isBookOnLoan(book.getId())) {
                                book.setStatus("Unactivated");
                                statusButton.setText("Activated");
                            } else if (book.getStatus().equalsIgnoreCase("Unactivated")) {
                                book.setStatus("Activated");
                                statusButton.setText("Unactivated");
                            } else {
                                alert(alert, "This book is on loan, can not change status!");
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
        quantityCol.setCellValueFactory(new PropertyValueFactory<Book, String>("Quantity"));
        countCol.setVisible(false);
        actionCol.setCellValueFactory(new PropertyValueFactory<Book, Void>(""));
        Callback<TableColumn<Book, Void>, TableCell<Book, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Book, Void> call(TableColumn<Book, Void> bookVoidTableColumn) {
                final TableCell<Book, Void> cell = new TableCell<>() {

                    private final Button editButton = new Button("Edit");
                    private final Button removeButton = new Button("Remove");

                    {
                        editButton.setOnAction((ActionEvent event) -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            Book book = getTableView().getItems().get(getIndex());
                            if (!isBookOnLoan(book.getId())) {
                                showEditDialog(book);
                                save();
                            } else {
                                alert(alert, "This book is on loan, can't remove.");
                            }
                        });
                        editButton.setPrefWidth(75);

                        removeButton.setOnAction((ActionEvent event) -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            Book book = getTableView().getItems().get(getIndex());
                            if (!isBookOnLoan(book.getId())) {
                                if (showConfirmation()) {
                                    getTableView().getItems().remove(book);
                                    save();
                                }
                            } else {
                                alert(alert, "This book is on loan, can't remove.");
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

    private static void alert(Alert alert, String s) {
        alert.setContentText(s);
        alert.show();
    }

    private void initializeUserTableView() {
        books = FXCollections.observableArrayList();
        loadBooksFromFile();
        tableUser.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Kiểm tra xem người dùng có nhấp đúp chuột hay không
                Book selectedBook = tableUser.getSelectionModel().getSelectedItem(); // Lấy đối tượng được chọn
                if (selectedBook != null) {
                    showBookDetailsDialog(selectedBook); // Hiển thị hộp thoại với thông tin chi tiết
                }
            }
        });
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
                        alert(alert, "This book is not available for borrowing.");
                    } else {
                        showBorrowDialog(book);
                        decreaseQuantity(book.getId());
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
        TextField quantity = new TextField(book.getQuantity());

        Button saveButton = new Button("Save");


        VBox vbox = new VBox(title, img, author, releaseYear, genre, status, quantity, saveButton);
        vbox.setSpacing(10);
        Scene scene = new Scene(vbox, 240, 480);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Edit");
        stage.show();

        saveButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            if (title.getText().isEmpty() || author.getText().isEmpty() || releaseYear.getText().isEmpty() || genre.getText().isEmpty() || status.getText().isEmpty()) {
                alert(alert, "No blank!");
                return;
            }

            if (!status.getText().equalsIgnoreCase("Activated") && !status.getText().equalsIgnoreCase("Unactivated")) {
                alert(alert, "Status must be activated or unactivated.");
                return;
            }

            try {
                Double num = Double.parseDouble(releaseYear.getText());
            } catch (NumberFormatException exception) {
                alert(alert, "Enter a numbers in Release year.");
                return;
            }

            try {
                Double num = Double.parseDouble(quantity.getText());
            } catch (NumberFormatException exception) {
                alert(alert, "Enter a numbers in Quantity.");
                return;
            }

            if (!isValidURL(img.getText())) {
                alert(alert, "Can't use this url.");
                return;
            }
            book.setTitle(title.getText());
            book.setImg(img.getText());
            book.setAuthor(author.getText());
            book.setReleaseYear(releaseYear.getText());
            book.setGenre(genre.getText());
            book.setStatus(status.getText());
            book.setQuantity(quantity.getText());
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

    public void increaseQuantity(String bookId) {
        Book book = getBook(bookId);
        int newQuantity = Integer.parseInt(book.getQuantity()) + 1;
        book.setQuantity("" + newQuantity);
        save();
    }

    public void decreaseQuantity(String bookId) {
        Book book = getBook(bookId);
        int newQuantity = Integer.parseInt(book.getQuantity()) - 1;
        book.setQuantity("" + newQuantity);
        save();
    }

    public static LoanSlip newLoanSlip;

    private void showBorrowDialog(Book book) {
        // Tạo Stage cho dialog
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Borrow Book");

        // Tạo các thành phần giao diện
        DatePicker datePicker = new DatePicker();
        Button saveButton = new Button("Save");
        Label paidDateLabel = new Label("Return Date (dd/MM/yyyy):");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // Tạo bố cục cho dialog
        VBox vbox = new VBox(10); // khoảng cách giữa các thành phần là 10
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(paidDateLabel, datePicker, saveButton);

        Scene scene = new Scene(vbox, 480, 360);
        dialogStage.setScene(scene);

        LoanSlipController loanSlipController = new LoanSlipController();

        // Lấy ngày hiện tại
        LocalDate currentDate = LocalDate.now();

        // Định dạng ngày
        String formattedDate = currentDate.format(format);

        saveButton.setOnAction(e -> {
            if (!isValidDate(datePicker.getValue().format(format), "dd/MM/yyyy")) {
                alert(alert, "Date format: dd/MM/yyyy");
            } else {
                if (Integer.parseInt(getBook(book.getId()).getQuantity()) == 0) {
                    book.setStatus("Unactivated");
                    alert(alert, "Tất cả cuốn sách này đã được mượn hết");
                    return;
                }
                newLoanSlip = new LoanSlip(currentUser.getUserId(), book.getImg(), book.getId(), formattedDate, datePicker.getValue().format(format), "Waiting");
//                newLoanSlip.setReturnDate(datePicker.getValue().format(format));

                loanSlipController.addNewLoanSlip();
                save();
                tableUser.refresh();
                dialogStage.close(); // Đóng dialog sau khi lưu
            }
        });

        // Hiển thị dialog và chờ người dùng thao tác
        dialogStage.showAndWait();
    }

    public void increaseCount(Book book) {
        int newCount = Integer.parseInt(book.getCount()) + 1;
        book.setCount("" + newCount);
    }

    public void decreaseCount(Book book) {
        if (book.getCount().equals("0")) {
            return;
        }
        int newCount = Integer.parseInt(book.getCount()) - 1;
        book.setCount("" + newCount);
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

    void save() {
        clearInFile();
        saveBookToFile();
    }

    public boolean isBookOnLoan(String bookId) {
        // Tải danh sách phiếu mượn từ file (hoặc database)
        LoanSlipController loanSlipController = new LoanSlipController();
        loanSlipController.loadLoanSlipFromFile();

        // Kiểm tra xem phiếu mượn có đang ở trạng thái "On loan" trong bất kỳ phiếu mượn nào không
        for (LoanSlip loanSlip : loanSlipController.getLoanSlips()) {
            if (loanSlip.getIdBook().equals(bookId) && loanSlip.getStatus().equalsIgnoreCase("On loan")) {
                return true; // Sách đang được mượn
            }
        }

        return false; // Sách không được mượn
    }

    public void goToTopBorrowed(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("TopBorrowedBooks.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1200, 800);
        stage.setTitle("Top borrowed");
        stage.setScene(scene);
        stage.show();
    }

    public int getAmount() {
        return books.size();
    }

    @FXML
    private ChoiceBox<String> myChoiceBox;
    @FXML
    private TextField searchByInfor;

    public void search() {
        ObservableList<Book> filteredBooks = FXCollections.observableArrayList();
        loadBooksFromFile();

        // Lấy lựa chọn từ ChoiceBox và giá trị tìm kiếm từ TextField
        String selectedChoice = myChoiceBox.getValue();
        String searchText = Normalizer.normalize(searchByInfor.getText().toLowerCase(), Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        for (Book book : books) {
            boolean matches = false;

            // Kiểm tra lựa chọn và thực hiện tìm kiếm tương ứng
            if ("Search by Title".equals(selectedChoice)) {
                matches = Normalizer.normalize(book.getTitle().toLowerCase(), Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}]", "").contains(searchText);
            } else if ("Search by Author".equals(selectedChoice)) {
                matches = Normalizer.normalize(book.getAuthor().toLowerCase(), Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}]", "").contains(searchText);
            } else if ("Search by Release year".equals(selectedChoice)) {
                matches = book.getReleaseYear().equalsIgnoreCase(searchText);
            }

            // Nếu sách phù hợp với lựa chọn, thêm vào danh sách kết quả
            if (matches) {
                filteredBooks.add(book);
            }
        }

        if (currentUser.getRole().equalsIgnoreCase("admin")) {
            table.setItems(filteredBooks);
            table.refresh();
        } else {
            tableUser.setItems(filteredBooks);
            tableUser.refresh();
        }
    }

    public void resetCount() {
        for (Book book : books) {
            book.setCount("0");
        }
        save();
    }
}