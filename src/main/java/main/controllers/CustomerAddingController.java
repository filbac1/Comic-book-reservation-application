package main.controllers;

import entity.Customer;
import exception.DataSourceException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import main.HelloApplication;

import java.util.ArrayList;
import java.util.List;

public class CustomerAddingController {

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;

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

    public void add() {
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

        initialize();
    }
}

