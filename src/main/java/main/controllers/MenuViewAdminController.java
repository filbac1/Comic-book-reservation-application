package main.controllers;

import entity.User;
import entity.UserRole;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import main.HelloApplication;

public class MenuViewAdminController {

    User currentUser = User.getUserInstance();

    User helperUser = new User(currentUser.getId(), currentUser.getUsername(), currentUser.getPassword(), currentUser.getRole());
    @FXML
    private Label appUser;

    public void initialize() {
        displayName(currentUser);
    }

    public void displayName(User user) {
        appUser.setText(currentUser.getUsername());
    }
    public void openCustomerSearch() {
        HelloApplication.showWindow("customer-search-view.fxml");
    }

    public void openCustomerAdding() {
        HelloApplication.showWindow("customer-adding-view.fxml");
    }

    public void openCustomerDeletion() {
        HelloApplication.showWindow("customer-deletion-view.fxml");
    }

    public void openCustomerUpdate() { HelloApplication.showWindow("customer-update-view.fxml"); }

    public void openComicSearch() {
        HelloApplication.showWindow("comic-search-view.fxml");
    }

    public void openComicAdding() {
        HelloApplication.showWindow("comic-adding-view.fxml");
    }

    public void openComicDeletion() {
        HelloApplication.showWindow("comic-deletion-view.fxml");
    }

    public void openComicUpdate() {
        HelloApplication.showWindow("comic-update-view.fxml");
    }

    public void openReservationSearch() {
        HelloApplication.showWindow("reservation-search-view.fxml");
    }

    public void openReservationAdding() {
        HelloApplication.showWindow("reservation-adding-view.fxml");
    }

    public void openReservationDeletion() {
        HelloApplication.showWindow("reservation-deletion-view.fxml");
    }

    public void openReservationUpdate() {
        HelloApplication.showWindow("reservation-update-view.fxml");
    }

    public void openUserPasswordChange() {
        if (helperUser.getRole().equals(UserRole.ADMINISTRATION_ROLE)) {
            HelloApplication.showWindow("user-password-change-view.fxml");
        } else {
            var alert = new Alert(Alert.AlertType.ERROR, "Only admin");
            alert.setTitle("Error while logging in the application!");
            alert.show();
        }
    }

    public void openUserChangeTracker() {
        if (helperUser.getRole().equals(UserRole.ADMINISTRATION_ROLE)) {
            HelloApplication.showWindow("user-change-tracker-view.fxml");
        } else {
            var alert = new Alert(Alert.AlertType.ERROR, "Only admin");
            alert.setTitle("Error while logging in the application!");
            alert.show();
        }
    }
}
