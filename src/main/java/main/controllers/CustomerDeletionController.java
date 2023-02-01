package main.controllers;

import entity.Customer;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.HelloApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDeletionController {

    @FXML
    private TableView<Customer> customerTableView;
    @FXML
    private TableColumn<Customer, String> firstNameColumn;
    @FXML
    private TableColumn<Customer, String> lastNameColumn;

    private List<Customer> allCustomers = new ArrayList<>();

    public void initialize() {
        firstNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().firstName()));
        lastNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().lastName()));

        allCustomers = HelloApplication.getDataSource().readAllCustomersFromDatabase();

        customerTableView.setItems(FXCollections.observableList(allCustomers));
    }

    public void delete() {
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        ArrayList<String> messages = new ArrayList<>();

        if (selectedCustomer == null) {
            messages.add("You didn't select a customer!");
        }

        if (messages.size() == 0) {
            var confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Would you like to delete selected person?");

            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.get() == ButtonType.OK) {
                HelloApplication.getDataSource().deleteCustomerInDatabase(selectedCustomer);
                initialize();
            } else {
                String allMessages = String.join("\n", messages);
                var alert = new Alert(Alert.AlertType.ERROR, allMessages);
                alert.setTitle("Error while deleting a customer");
                alert.show();
            }
        }
    }
}
