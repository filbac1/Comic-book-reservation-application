package main.controllers;

import entity.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.HelloApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ComicDeletionController {
    User currentUser = User.getUserInstance();
    User helperUser = new User(currentUser.getId(), currentUser.getUsername(), currentUser.getPassword(), currentUser.getRole());

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

                if (!HelloApplication.getDataSource().comicConnectedToReservation(selectedComic)) {
                    Change changeOne = new Change("comicName", selectedComic.getBookName(), null, helperUser, LocalDateTime.now());
                    Change changeTwo = new Change("pickedPublisher", selectedComic.getPublisher(), null, helperUser, LocalDateTime.now());
                    Change changeThree = new Change("ISBN", selectedComic.getIsbn().getISBNNumber(), null, helperUser, LocalDateTime.now());

                    List<Change> changeList = HelloApplication.getDataSource().loadAllChanges();
                    changeList.add(changeOne);
                    changeList.add(changeTwo);
                    changeList.add(changeThree);
                    HelloApplication.getDataSource().writeChanges(changeList);
                }

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
