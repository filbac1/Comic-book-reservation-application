package main.controllers;

import entity.Comic;
import entity.ISBN;
import entity.Publishers;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.HelloApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComicAddingController {
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
        pickedPublisher.getSelectionModel().selectFirst();

        comicTableView.setItems(FXCollections.observableList(allComics));
    }

    public void add() {
        ArrayList<String> messages = new ArrayList<>();

        if (comicName.getText().isBlank()) {
            messages.add("Comic name is empty!");
        }

        if (pickedPublisher.getSelectionModel().isEmpty()) {
            messages.add("You haven't selected a publisher!");
        }

        if (ISBN.getText().isBlank()) {
            messages.add("ISBN is empty!");
        }

        ISBN<String> ISBNString = new ISBN<>(ISBN.getText());

        if (messages.size() == 0) {
            HelloApplication.getDataSource().createComicInDatabase(new Comic(
                    comicName.getText(),
                    -1,
                    pickedPublisher.getValue(),
                    ISBNString)
            );
        } else {
            String m = String.join("\n", messages);

            var alert = new Alert(Alert.AlertType.ERROR, m);
            alert.setTitle("Error while adding a new customer!");
            alert.show();
        }

        initialize();
    }
}
