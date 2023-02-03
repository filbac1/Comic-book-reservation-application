package main.controllers;

import entity.Comic;
import entity.Customer;
import entity.Reservation;
import entity.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.HelloApplication;
import sorter.ComicSorter;
import sorter.CustomerSorter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReservationSearchController {

    @FXML
    private Label tableInfo;
    @FXML
    private ChoiceBox<Customer> pickedCustomer;
    @FXML
    private ComboBox<Comic> comicsList;

    @FXML
    private TableView<Reservation> reservationTableView;
    @FXML
    private TableColumn<Reservation, String> customerNameColumn;
    @FXML
    private TableColumn<Reservation, String> comicNameColumn;

    private List<Customer> allCustomers = new ArrayList<>();
    private List<Comic> allComics = new ArrayList<>();
    private List<Reservation> allReservations = new ArrayList<>();

    public void initialize() {
        allCustomers = HelloApplication.getDataSource().readAllCustomersFromDatabase();
        allComics = HelloApplication.getDataSource().readAllComicsFromDatabase();
        allReservations = HelloApplication.getDataSource().readAllReservationsFromDatabase();

        Collections.sort(allCustomers, new CustomerSorter());
        pickedCustomer.setItems(FXCollections.observableList(allCustomers));

        Collections.sort(allComics, new ComicSorter());
        comicsList.setItems(FXCollections.observableList(allComics));

        customerNameColumn.setCellValueFactory(data -> new SimpleStringProperty((data.getValue().getCustomer().toString())));
        comicNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getComic().toString()));

        reservationTableView.setItems(FXCollections.observableList(allReservations));
        reservationTableView.getSortOrder().addAll(customerNameColumn, comicNameColumn);
    }

    public void search() {
        List<Reservation> filtered = new ArrayList<>();

        if (pickedCustomer.getValue() != null && comicsList.getValue() == null) {
            filtered = allReservations.stream()
                    .filter(c -> c.getCustomer().toString().contains(pickedCustomer.getValue().toString()))
                    .toList();
        }

        if (pickedCustomer.getValue() == null && comicsList.getValue() != null) {
            filtered = allReservations.stream()
                    .filter(c -> c.getComic().getBookName().contains(comicsList.getValue().getBookName()))
                    .toList();
        }

        if (pickedCustomer.getValue() != null && comicsList.getValue() != null) {
            filtered = allReservations.stream()
                    .filter(c -> c.getCustomer().toString().contains(pickedCustomer.getValue().toString()))
                    .filter(c -> c.getComic().getBookName().contains(comicsList.getValue().getBookName()))
                    .toList();
        }

        reservationTableView.setItems(FXCollections.observableList(filtered));
    }

}
