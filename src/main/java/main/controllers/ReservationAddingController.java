package main.controllers;

import entity.Comic;
import entity.Customer;
import entity.Publishers;
import entity.Reservation;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import main.HelloApplication;

import java.util.*;

public class ReservationAddingController {
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

        pickedCustomer.setItems(FXCollections.observableList(allCustomers));
        comicsList.setItems(FXCollections.observableList(allComics));

        customerNameColumn.setCellValueFactory(data -> new SimpleStringProperty((data.getValue().getCustomer().firstName() + " " + data.getValue().getCustomer().lastName())));
        comicNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getComic().getBookName()));

        reservationTableView.setItems(FXCollections.observableList(allReservations));
    }

    public void add() {
        /*
        ArrayList<String> messages = new ArrayList<>();

        if (firstName.getText().isBlank()) {
            messages.add("First name is empty!");
        }

        if (lastName.getText().isBlank()) {
            messages.add("Last name is empty!");
        }

        if (messages.size() == 0) {
            HelloApplication.getDataSource().createCustomerInDatabase(new Customer(
                    -1,
                    firstName.getText(),
                    lastName.getText())
            );
        } else {
            String m = String.join("\n", messages);

            var alert = new Alert(Alert.AlertType.ERROR, m);
            alert.setTitle("Error while adding a new customer!");
            alert.show();
        }

        initialize(); */
    }
}
