package main.controllers;

import entity.Change;
import entity.Customer;
import entity.User;
import exception.DataSourceException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import main.HelloApplication;
import sorter.CustomerSorter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CustomerAddingController {

    User currentUser = User.getUserInstance();
    User helperUser = new User(currentUser.getId(), currentUser.getUsername(), currentUser.getPassword(), currentUser.getRole());

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

            Change changeOne = new Change("firstName", null, firstName.getText(), helperUser, LocalDateTime.now());
            Change changeTwo = new Change("lastName", null, lastName.getText(), helperUser, LocalDateTime.now());

            List<Change> changeList = HelloApplication.getDataSource().loadAllChanges();
            changeList.add(changeOne);
            changeList.add(changeTwo);
            HelloApplication.getDataSource().writeChanges(changeList);

        } else {
            String m = String.join("\n", messages);

            var alert = new Alert(Alert.AlertType.ERROR, m);
            alert.setTitle("Error while adding a new customer!");
            alert.show();
        }

        initialize();
    }
}

