package main.controllers;

import entity.Comic;
import entity.Customer;
import entity.Publishers;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.HelloApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ComicDeletionController {

    @FXML
    private TableView<Comic> comicTableView;
    @FXML
    private TableColumn<Comic, String> comicNameColumn;
    @FXML
    private TableColumn<Comic, String> publisherColumn;
    @FXML
    private TableColumn<Comic, String> ISBNColumn;

    private List<Comic> allComics = new ArrayList<>();

    public void initialize() {
        comicNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookName()));
        publisherColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getPublisher())));
        ISBNColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getIsbn().getISBNNumber())));

        allComics = HelloApplication.getDataSource().readAllComicsFromDatabase();

        comicTableView.setItems(FXCollections.observableList(allComics));
    }

    public void delete() {
        Comic selectedComic = comicTableView.getSelectionModel().getSelectedItem();

        ArrayList<String> messages = new ArrayList<>();

        if (selectedComic == null) {
            messages.add("You didn't select a comic!");
        }

        if (messages.size() == 0) {
            var confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Would you like to delete selected comic?");

            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.get() == ButtonType.OK) {
                HelloApplication.getDataSource().deleteComicInDatabase(selectedComic);
                initialize();
            } else {
                String allMessages = String.join("\n", messages);
                var alert = new Alert(Alert.AlertType.ERROR, allMessages);
                alert.setTitle("Error while deleting a comic");
                alert.show();
            }
        }
    }
}
