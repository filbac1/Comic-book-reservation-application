package main.controllers;

import entity.Comic;
import entity.Customer;
import entity.ISBN;
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

public class ComicUpdateController {

    @FXML
    private TextField comicName;
    @FXML
    private TextField ISBN;
    @FXML
    private ChoiceBox<Publishers> pickedPublisher;

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
        List<Publishers> publishersList = Arrays.asList(Publishers.values());

        pickedPublisher.setItems(FXCollections.observableList(publishersList));

        comicTableView.setItems(FXCollections.observableList(allComics));
    }

    public void update() {
        Comic selectedComic = comicTableView.getSelectionModel().getSelectedItem();

        ArrayList<String> messages = new ArrayList<>();

        if (comicName.getText().isBlank() && ISBN.getText().isBlank() && pickedPublisher.getSelectionModel().isEmpty()) {
            messages.add("All fields are empty! Please select at least one to update!");
        }

        if (selectedComic == null) {
            messages.add("You didn't select a comic which you would like to update!");
        }

        if (messages.size() == 0) {
                var confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Would you like to update selected comic?");

                Optional<ButtonType> result = confirmationAlert.showAndWait();

                String comicNameDB;
                Publishers publisherDB;
                ISBN<String> ISBNDB = new ISBN<>(ISBN.getText());

                if (comicName.getText().isBlank()) {
                    comicNameDB = selectedComic.getBookName();
                } else {
                    comicNameDB = comicName.getText();
                }

                if (pickedPublisher.getSelectionModel().isEmpty()) {
                    publisherDB = selectedComic.getPublisher();
                } else {
                    publisherDB = pickedPublisher.getValue();
                }

                if (ISBN.getText().isBlank()) {
                    ISBNDB = selectedComic.getIsbn();
                }

                Comic comicForDatabase = new Comic(comicNameDB, selectedComic.getComicID(), publisherDB, ISBNDB);

                if (result.get() == ButtonType.OK) {
                    HelloApplication.getDataSource().updateComicInDatabase(comicForDatabase);
                    initialize();
                }
        } else {
                String allMessages = String.join("\n", messages);
                var alert = new Alert(Alert.AlertType.ERROR, allMessages);
                alert.setTitle("Error while updating a comic!");
                alert.show();
        }
    }
}
