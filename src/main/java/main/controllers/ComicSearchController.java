package main.controllers;

import entity.Comic;
import entity.Customer;
import entity.Publishers;
import exception.DataSourceException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import main.HelloApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComicSearchController {

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
        comicTableView.getSortOrder().addAll(comicNameColumn, publisherColumn, ISBNColumn);
    }

    public void search() {
        List<Comic> filtered;

        if (pickedPublisher.getValue() != null) {
            filtered = allComics.stream()
                    .filter(c -> c.getBookName().contains(comicName.getText()))
                    .filter(c -> c.getPublisher().name().contains(pickedPublisher.getValue().toString()))
                    .filter(c -> c.getIsbn().getISBNNumber().contains(ISBN.getText()))
                    .toList();
        } else {
            filtered = allComics.stream()
                    .filter(c -> c.getBookName().contains(comicName.getText()))
                    .filter(c -> c.getIsbn().getISBNNumber().contains(ISBN.getText()))
                    .toList();
        }

        comicTableView.setItems(FXCollections.observableList(filtered));
        comicName.clear();
        ISBN.clear();
        pickedPublisher.getSelectionModel().clearSelection();
    }
}
