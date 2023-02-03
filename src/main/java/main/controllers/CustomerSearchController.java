package main.controllers;

import entity.Customer;
import entity.User;
import exception.DataSourceException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import main.HelloApplication;

import java.util.ArrayList;
import java.util.List;

public class CustomerSearchController {

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
        customerTableView.getSortOrder().addAll(lastNameColumn, firstNameColumn);
    }

    public void search() {
        List<Customer> filtered = allCustomers.stream()
                .filter(c -> c.firstName().contains(firstName.getText()))
                .filter(c -> c.lastName().contains(lastName.getText()))
                .toList();

        customerTableView.setItems(FXCollections.observableList(filtered));
    }
}
