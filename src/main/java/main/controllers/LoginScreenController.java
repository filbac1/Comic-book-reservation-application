package main.controllers;

import entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import main.HelloApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LoginScreenController {

    User currentUser = User.getUserInstance();
    User helperUser = new User();
    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;
    @FXML
    private void login() {
        ArrayList<String> messages = new ArrayList<>();

        Set<User> users = HelloApplication.getDataSource().readAllUsersFromFile();
        List<User> userList = new ArrayList<>(users);

        if (usernameField.getText().isBlank()) {
            messages.add("Please fill in with your username!");
        }

        if (passwordField.getText().isBlank()) {
            messages.add("Please fill in with your password!");
        }

        if (checkIfPassowordIsValid(userList, usernameField.getText(), passwordField.getText()) == false && !usernameField.getText().isBlank() && !passwordField.getText().isBlank()) {
           messages.add("Wrong password for provided username!");
        }

        if (messages.size() == 0 && checkIfPassowordIsValid(userList, usernameField.getText(), passwordField.getText())) {
            helperUser = returnCurrentUser(userList, usernameField.getText(), passwordField.getText());

            currentUser.setId(helperUser.getId());
            currentUser.setUsername(helperUser.getUsername());
            currentUser.setPassword(helperUser.getPassword());
            currentUser.setRole(helperUser.getRole());

            HelloApplication.showWindow("hello-user.fxml");

        } else {
            String m = String.join("\n", messages);

            var alert = new Alert(Alert.AlertType.ERROR, m);
            alert.setTitle("Error while logging in the application!");
            alert.show();
        }
    }

    private boolean checkIfPassowordIsValid(List<User> userList, String username, String password) {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUsername().equals(username)) {
                if (userList.get(i).getPassword().equals(String.valueOf(password.hashCode()))) {
                    return true;
                }
            }
        }

        return false;
    }

    private User returnCurrentUser(List<User> userList, String username, String password) {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUsername().equals(username)) {
                if (userList.get(i).getPassword().equals(String.valueOf(password.hashCode()))) {
                    return userList.get(i);
                }
            }
        }
        return null;
    }
}
