package main.controllers;

import entity.Comic;
import entity.Customer;
import entity.Reservation;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.HelloApplication;

import java.util.ArrayList;
import java.util.List;

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

        customerNameColumn.setCellValueFactory(data -> new SimpleStringProperty((data.getValue().getCustomer().toString())));
        comicNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getComic().toString()));

        reservationTableView.setItems(FXCollections.observableList(allReservations));
    }

    public void add() {

        ArrayList<String> messages = new ArrayList<>();

        if (pickedCustomer.getSelectionModel().isEmpty()) {
            messages.add("You haven't selected a customer!");
        }

        if (comicsList == null) {
            messages.add("You haven't selected a comic!");
        }

        if (messages.size() == 0) {
            HelloApplication.getDataSource().createReservationInDatabase(new Reservation(
                    -1,
                    pickedCustomer.getValue(),
                    comicsList.getValue())
            );
        } else {
            String m = String.join("\n", messages);

            var alert = new Alert(Alert.AlertType.ERROR, m);
            alert.setTitle("Error while adding a new reservation!");
            alert.show();
        }

        initialize();
    }
}
