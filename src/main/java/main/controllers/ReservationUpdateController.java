package main.controllers;

import entity.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.HelloApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReservationUpdateController {
    User currentUser = User.getUserInstance();
    User helperUser = new User(currentUser.getId(), currentUser.getUsername(), currentUser.getPassword(), currentUser.getRole());

    @FXML
    private ChoiceBox<Customer> pickedCustomer;
    @FXML
    private ChoiceBox<Comic> comicsList;

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

        allCustomers.sort((c1, c2) -> c1.lastName().compareTo(c2.lastName()));
        pickedCustomer.setItems(FXCollections.observableList(allCustomers));

        allComics.sort((c1, c2) -> {
            int comp = c1.getBookName().compareTo(c2.getBookName());
            if (comp == 0) {
                comp = c1.getComicID().compareTo(c2.getComicID());
            }
            return comp;
        });
        comicsList.setItems(FXCollections.observableList(allComics));

        customerNameColumn.setCellValueFactory(data -> new SimpleStringProperty((data.getValue().getCustomer().toString())));
        comicNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getComic().toString()));

        reservationTableView.setItems(FXCollections.observableList(allReservations));
        reservationTableView.getSortOrder().addAll(customerNameColumn, comicNameColumn);
    }

    /**
     * Set of actions performed when trying to update a Reservation
     */
    public void update() {
        Reservation selectedReservation = reservationTableView.getSelectionModel().getSelectedItem();

        ArrayList<String> messages = new ArrayList<>();

        if (pickedCustomer.getValue() == null && comicsList.getValue() == null) {
            messages.add("All fields are empty! Please select at least one to update!");
        }

        if (selectedReservation == null) {
            messages.add("You didn't select a reservation which you would like to update!");
        }

        if (messages.size() == 0) {
            var confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Would you like to update selected reservation?");

            Optional<ButtonType> result = confirmationAlert.showAndWait();

            Customer customerDB;
            Comic comicDB;

            if (pickedCustomer.getValue() == null) {
                customerDB = selectedReservation.getCustomer();
            } else {
                customerDB = pickedCustomer.getValue();
            }

            if (comicsList.getValue() == null) {
                comicDB = selectedReservation.getComic();
            } else {
                comicDB = comicsList.getValue();
            }

            Reservation reservationForDatabase = new Reservation(selectedReservation.getReservationID(), customerDB, comicDB);

            if (result.get() == ButtonType.OK) {
                HelloApplication.getDataSource().updateReservationInDatabase(reservationForDatabase);

                Change changeOne = new Change("reservationCustomer", selectedReservation.getCustomer(), customerDB, helperUser, LocalDateTime.now());
                Change changeTwo = new Change("reservationComic", selectedReservation.getComic(), comicDB, helperUser, LocalDateTime.now());

                List<Change> changeList = HelloApplication.getDataSource().loadAllChanges();
                changeList.add(changeOne);
                changeList.add(changeTwo);
                HelloApplication.getDataSource().writeChanges(changeList);

                pickedCustomer.getSelectionModel().clearSelection();
                comicsList.getSelectionModel().clearSelection();

                initialize();
            }
        } else {
            String allMessages = String.join("\n", messages);
            var alert = new Alert(Alert.AlertType.ERROR, allMessages);
            alert.setTitle("Error while updating a reservation!");
            alert.show();
        }
    }
}
