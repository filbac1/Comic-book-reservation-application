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

    /**
     * Function that displays received User info on screen
     * @param user
     */
    public void displayName(User user) {
        appUser.setText(currentUser.getUsername());
    }

    /**
     * Opens Customer search FXML
     */
    public void openCustomerSearch() {
        HelloApplication.showWindow("customer-search-view.fxml");
    }

    /**
     * Opens Customer adding FXML
     */

    public void openCustomerAdding() {
        HelloApplication.showWindow("customer-adding-view.fxml");
    }

    /**
     * Opens Customer deletion FXML
     */

    public void openCustomerDeletion() {
        HelloApplication.showWindow("customer-deletion-view.fxml");
    }

    /**
     * Opens Customer update FXML
     */

    public void openCustomerUpdate() { HelloApplication.showWindow("customer-update-view.fxml"); }

    /**
     * Opens Comic search FXML
     */

    public void openComicSearch() {
        HelloApplication.showWindow("comic-search-view.fxml");
    }

    /**
     * Opens Comic adding FXML
     */

    public void openComicAdding() {
        HelloApplication.showWindow("comic-adding-view.fxml");
    }

    /**
     * Opens Comic deletion FXML
     */

    public void openComicDeletion() {
        HelloApplication.showWindow("comic-deletion-view.fxml");
    }

    /**
     * Opens Comic update FXML
     */

    public void openComicUpdate() {
        HelloApplication.showWindow("comic-update-view.fxml");
    }

    /**
     * Opens Reservation search FXML
     */

    public void openReservationSearch() {
        HelloApplication.showWindow("reservation-search-view.fxml");
    }

    /**
     * Opens Reservation adding FXML
     */

    public void openReservationAdding() {
        HelloApplication.showWindow("reservation-adding-view.fxml");
    }

    /**
     * Opens Reservation deletion FXML
     */

    public void openReservationDeletion() {
        HelloApplication.showWindow("reservation-deletion-view.fxml");
    }

    /**
     * Opens Reservation update FXML
     */

    public void openReservationUpdate() {
        HelloApplication.showWindow("reservation-update-view.fxml");
    }

    /**
     * Opens Change tracker FXML if the User has administration role
     */


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
