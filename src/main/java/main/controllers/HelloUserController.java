package main.controllers;

import entity.User;
import entity.UserRole;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.HelloApplication;

public class HelloUserController {

    User currentUser = User.getUserInstance();
    @FXML
    private Label usernameWelcomeField;

    public void initialize() {
        displayName(currentUser);
    }

    public void displayName(User user) {
        usernameWelcomeField.setText("Hello " + currentUser.getUsername() + " :-)");
    }

    public void enterApplication() {
        HelloApplication.showWindow("blank-menu-view-admin.fxml");
    }

}
