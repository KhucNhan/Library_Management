package com.example.librarymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.example.librarymanagement.BookController.newLoanSlip;
import static com.example.librarymanagement.UserController.currentUser;
import static java.lang.CharSequence.compare;

public class LoanSlipController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private static ObservableList<LoanSlip> loanSlips = FXCollections.observableArrayList();
    private static ObservableList<LoanSlip> userLoanSlips = FXCollections.observableArrayList();
    BookController bookController = new BookController();

    public void removeLoanSlip(LoanSlip loanSlip) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (loanSlip.getStatus().equalsIgnoreCase("On loan") && currentUser.getRole().equalsIgnoreCase("admin")) {
            alert.setContentText("Cannot remove loan slip. The book is currently on loan.");
            alert.show();
            return;
        }
        loanSlips.remove(loanSlip);
        save(); // Gọi phương thức save() để lưu các thay đổi vào tệp tin
    }

    public ObservableList<LoanSlip> getLoanSlips() {
        return loanSlips;
    }

    void loadLoanSlipFromFile() {
        loanSlips.clear(); // Xóa tất cả các phiếu mượn hiện có trước khi tải lại
        File file = new File("loanSlip.txt");
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    LoanSlip loanSlip = new LoanSlip(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                    loanSlips.add(loanSlip);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            showError("Invalid data format, There was an error reading the loan slip data.");
        }
    }

    void loadLoanSlipFromFileForUser() {
        loanSlips.clear();
        File file = new File("loanSlip.txt");
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    LoanSlip loanSlip = new LoanSlip(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                    if (loanSlip.getIdUser().equals(currentUser.getUserId())) {
                        userLoanSlips.add(loanSlip);
                    }
                    loanSlips.add(loanSlip);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            showError("Invalid data format, There was an error reading the loan slip data.");
        }
    }

    public void addNewLoanSlip() {
        loanSlips.add(newLoanSlip);
        save();
        loadLoanSlipFromFile();
    }

    private void saveProductsToFile() {
        File file = new File("loanSlip.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            for (LoanSlip loanSlip : loanSlips) {
                String line = String.join(",",
                        loanSlip.getIdUser(),
                        loanSlip.getImgBook(),
                        loanSlip.getIdBook(),
                        loanSlip.getDate(),
                        loanSlip.getReturnDate(),
                        loanSlip.getStatus()
                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error saving data: Could not save LoanSlip data to file.");
        }
    }

    public void clearInFile() {
        File file = new File("loanSlip.txt");
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

    @FXML
    private TableView<LoanSlip> tableView;
    @FXML
    private TableColumn<LoanSlip, String> idUserCol;
    @FXML
    private TableColumn<LoanSlip, String> imgCol;
    @FXML
    private TableColumn<LoanSlip, String> idBookCol;
    @FXML
    private TableColumn<LoanSlip, String> dateCol;
    @FXML
    private TableColumn<LoanSlip, String> returnDateCol;
    @FXML
    private TableColumn<LoanSlip, String> statusCol;
    @FXML
    private TableColumn<LoanSlip, Void> actionCol;

    @FXML

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (currentUser.getRole().equalsIgnoreCase("admin")) {
            initializeAdmin();
        } else {
            initializeUser();
        }
        start.valueProperty().addListener((obs, oldDate, newDate) -> refreshTableView());
        end.valueProperty().addListener((obs, oldDate, newDate) -> refreshTableView());
    }

    private void initializeUser() {
        userLoanSlips = FXCollections.observableArrayList();
        loadLoanSlipFromFileForUser();
        tableView.setItems(userLoanSlips);
//        idUserCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("idUser"));
        idUserCol.setVisible(false);
        // Cột ảnh
        imgCol.setCellValueFactory(new PropertyValueFactory<>("imgBook"));
        imgCol.setCellFactory(column -> new TableCell<LoanSlip, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                if (empty || imagePath == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(new Image(imagePath));
                    imageView.setFitHeight(50);
                    imageView.setFitWidth(50);
                    setGraphic(imageView);
                }
            }
        });
        idBookCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("idBook"));
        dateCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("date"));
        returnDateCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("returnDate"));
        statusCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("status"));
        actionCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, Void>(""));
        Callback<TableColumn<LoanSlip, Void>, TableCell<LoanSlip, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<LoanSlip, Void> call(TableColumn<LoanSlip, Void> loanSlipTableColumn) {
                final TableCell<LoanSlip, Void> cell = new TableCell<>() {
                    private final Button changeStatusButton = new Button("Paid");

                    {
                        changeStatusButton.setOnAction((ActionEvent event) -> {
                            LoanSlip loanSlip = getTableView().getItems().get(getIndex());
                            if (loanSlip.getStatus().equalsIgnoreCase("On loan")) {
                                loanSlip.setStatus("Paid");
                                save();
                                bookController.increaseQuantity(loanSlip.getIdBook());
                                tableView.refresh();
                            }
                        });
                        changeStatusButton.setPrefWidth(150);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            LoanSlip loanSlip = getTableView().getItems().get(getIndex());
                            changeStatusButton.setDisable(loanSlip.getStatus().equalsIgnoreCase("Paid"));
                            HBox hBox = new HBox(changeStatusButton);
                            hBox.setSpacing(10);
                            HBox.setMargin(changeStatusButton, new Insets(0, 5, 0, 5)); // Thiết lập margin cho nút Edit
                            setGraphic(hBox);
                        }
                    }
                };
                return cell;
            }
        };
        actionCol.setCellFactory(cellFactory);
    }

    public boolean showConfirmation() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete loan slip");
        alert.setHeaderText("Are you sure want to remove this loan slip ?");

        Optional<ButtonType> option = alert.showAndWait();

        return option.get() == ButtonType.OK;
    }

    private void initializeAdmin() {
        loanSlips = FXCollections.observableArrayList();
        loadLoanSlipFromFile();
        tableView.setItems(loanSlips);
        idUserCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("idUser"));
        imgCol.setCellValueFactory(new PropertyValueFactory<>("imgBook"));
        imgCol.setCellFactory(column -> new TableCell<LoanSlip, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                if (empty || imagePath == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(new Image(imagePath));
                    imageView.setFitHeight(50);
                    imageView.setFitWidth(50);
                    setGraphic(imageView);
                }
            }
        });
        idBookCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("idBook"));
        dateCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("date"));
        returnDateCol.setCellValueFactory(new PropertyValueFactory<LoanSlip, String>("returnDate"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Tạo cellFactory cho cột hành động
        Callback<TableColumn<LoanSlip, Void>, TableCell<LoanSlip, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<LoanSlip, Void> call(final TableColumn<LoanSlip, Void> param) {
                return new TableCell<>() {
                    private final Button removeButton = new Button("Remove");
                    private final Button acceptedButton = new Button("Accepted");
                    private final Button cancelButton = new Button("Cancel");

                    {
                        removeButton.setOnAction((ActionEvent event) -> {
                            LoanSlip loanSlip = getTableView().getItems().get(getIndex());
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            if (bookController.isBookOnLoan(loanSlip.getIdBook())) {
                                alert.setContentText("Cannot remove loan slip. The book is currently on loan.");
                                alert.show();
                            } else if (showConfirmation()) {
                                removeLoanSlip(loanSlip);
                                tableView.refresh();
                            }
                        });
                        removeButton.setPrefWidth(80);

                        acceptedButton.setOnAction((ActionEvent event) -> {
                            LoanSlip loanSlip = getTableView().getItems().get(getIndex());
                            loanSlip.setStatus("On loan");
                            bookController.increaseCount(bookController.getBook(loanSlip.getIdBook()));
                            tableView.refresh();
                            save();
                        });
                        acceptedButton.setPrefWidth(80);

                        cancelButton.setOnAction((ActionEvent event) -> {
                            LoanSlip loanSlip = getTableView().getItems().get(getIndex());
                            loanSlip.setStatus("Canceled");
                            tableView.refresh();
                            save();
                        });
                        cancelButton.setPrefWidth(80);
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            LoanSlip loanSlip = getTableView().getItems().get(getIndex());
                            HBox hBox;
                            if (!loanSlip.getStatus().equalsIgnoreCase("waiting")) {
                                hBox = new HBox(removeButton);
                            } else {
                                hBox = new HBox(cancelButton, acceptedButton);
                            }
                            hBox.setSpacing(10);
                            setGraphic(hBox);
                        }
                    }
                };
            }
        };

        // Tạo và thêm cột hành động vào TableView
        TableColumn<LoanSlip, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(cellFactory);
        tableView.getColumns().add(actionCol);
    }

    public void save() {
        clearInFile();
        saveProductsToFile();
    }

    public ObservableList<LoanSlip> filterAdmin(LocalDate startDate, LocalDate endDate, DateTimeFormatter formatter) {
        ObservableList<LoanSlip> filteredLoanSlips = FXCollections.observableArrayList();
        loadLoanSlipFromFile();
        for (LoanSlip loanSlip : loanSlips) {
            LocalDate startLoanSlip = LocalDate.parse(loanSlip.getDate(), formatter);
            LocalDate endLoanSlip = LocalDate.parse(loanSlip.getReturnDate(), formatter);
            if ((startDate.isEqual(startLoanSlip) || startDate.isBefore(startLoanSlip)) && (endDate.isEqual(endLoanSlip) || endDate.isAfter(endLoanSlip))) {
                filteredLoanSlips.add(loanSlip);
            }
        }
        return filteredLoanSlips;
    }

    public ObservableList<LoanSlip> filterUser(LocalDate startDate, LocalDate endDate, DateTimeFormatter formatter) {
        ObservableList<LoanSlip> filteredLoanSlips = FXCollections.observableArrayList();
        loadLoanSlipFromFileForUser();
        for (LoanSlip loanSlip : userLoanSlips) {
            LocalDate startLoanSlip = LocalDate.parse(loanSlip.getDate(), formatter);
            LocalDate endLoanSlip = LocalDate.parse(loanSlip.getReturnDate(), formatter);
            if ((startDate.isEqual(startLoanSlip) || startDate.isBefore(startLoanSlip)) && (endDate.isEqual(endLoanSlip) || endDate.isAfter(endLoanSlip))) {
                filteredLoanSlips.add(loanSlip);
            }
        }
        return filteredLoanSlips;
    }

    @FXML
    private DatePicker start;
    @FXML
    private DatePicker end;

    private static ObservableList<LoanSlip> historicLoanSlip = FXCollections.observableArrayList();

    public void search() {
        filter();
    }

    private void refreshTableView() {
        if (start.getValue() != null && end.getValue() != null) {
            filter();
        }
    }

    private void filter() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate startDate = LocalDate.parse(end.getValue().format(format), format);
        LocalDate endDate = LocalDate.parse(start.getValue().format(format), format);
        if (currentUser.getRole().equalsIgnoreCase("admin")) {
            historicLoanSlip = filterAdmin(start.getValue(), end.getValue(), format);
        } else {
            historicLoanSlip = filterUser(start.getValue(), end.getValue(), format);
        }
        tableView.setItems(historicLoanSlip);
        tableView.refresh();
    }

    public ObservableList<Book> getTop5Books() {
        // Lấy danh sách tất cả sách
        ObservableList<Book> allBooks = bookController.getBooks(); // Phương thức này cần có trong BookController

        // Tạo một danh sách để lưu số lần mượn của mỗi sách
        List<BookBorrowCount> borrowCounts = new ArrayList<>();

        // Khởi tạo số lần mượn cho tất cả sách
        for (Book book : allBooks) {
            borrowCounts.add(new BookBorrowCount(book, 0));
        }

        // Cập nhật số lần mượn từ danh sách phiếu mượn
        for (LoanSlip loanSlip : loanSlips) {
            String bookId = loanSlip.getIdBook();
            for (BookBorrowCount borrowCount : borrowCounts) {
                if (borrowCount.getBook().getId().equals(bookId) && !loanSlip.getStatus().equalsIgnoreCase("Waiting")) {
                    borrowCount.setCount(borrowCount.getCount() + 1);
                    break;
                }
            }
        }

        // Sắp xếp danh sách theo số lần mượn giảm dần
        borrowCounts.sort((bc1, bc2) -> Integer.compare(bc2.getCount(), bc1.getCount()));

        // Lấy top 5 sách được mượn nhiều nhất
        ObservableList<Book> top5Books = FXCollections.observableArrayList();
        for (int i = 0; i < Math.min(5, borrowCounts.size()); i++) {
            top5Books.add(borrowCounts.get(i).getBook());
        }
        return top5Books;
    }

    public ObservableList<Book> getTop5BooksByTime(String time) {

        bookController.resetCount();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate today = LocalDate.now();
        LocalDate startDate = switch (time) {
            case "Today" -> today;
            case "Yesterday" -> today.minusDays(1);
            case "Last 7 days" -> today.minusDays(7);
            case "Last 30 days" -> today.minusDays(30);
            default -> null;
        };

        loadLoanSlipFromFile();
        for (LoanSlip loanSlip : loanSlips) {
            String bookId = loanSlip.getIdBook();
            LocalDate loanDate = LocalDate.parse(loanSlip.getDate(), formatter);

            if (!loanDate.isAfter(startDate)) { // Chọn tất cả các sách mượn sau ngày bắt đầu
                for (Book book : bookController.getBooks()) {
                    if (book.getId().equals(bookId) && !loanSlip.getStatus().equalsIgnoreCase("Waiting")) {
                        int newCount = Integer.parseInt(book.getCount()) + 1;
                        book.setCount("" + newCount);
                        bookController.save();
                        break;
                    }
                }
            }
        }

        bookController.getBooks().sort((bc1, bc2) -> compare(bc2.getCount(), bc1.getCount()));

        bookController.save();

        ObservableList<Book> top5Books = FXCollections.observableArrayList();
        for (int i = 0; i < Math.min(5, bookController.getBooks().size()); i++) {
            top5Books.add(bookController.getBooks().get(i));
        }

        return top5Books;
    }


    class BookBorrowCount {
        private Book book;
        private int count;

        public BookBorrowCount(Book book, int count) {
            this.book = book;
            this.count = count;
        }

        public Book getBook() {
            return book;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    public void goToTopBorrowed(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("TopBorrowedBooks.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1200, 800);
        stage.setTitle("Top borrowed");
        stage.setScene(scene);
        stage.show();
    }
}