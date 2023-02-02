package main.controllers;

import entity.Change;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.HelloApplication;

import java.util.ArrayList;
import java.util.List;

public class UserChangeTrackerController {

    @FXML
    private TableView<Change> changesTableView;
    @FXML
    private TableColumn<Change, String> fieldValueColumn;
    @FXML
    private TableColumn<Change, String> oldValueColumn;

    @FXML
    private TableColumn<Change, String> newValueColumn;
    @FXML
    private TableColumn<Change, String> userNameColumn;

    @FXML
    private TableColumn<Change, String> localDateTimeColumn;

    List<Change> changeList = new ArrayList<>();


    public void initialize() {
        changeList = HelloApplication.getDataSource().loadAllChanges();

        fieldValueColumn.setCellValueFactory(data -> new SimpleStringProperty((data.getValue().getObjectChanged().toString())));
        oldValueColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOldValue().toString()));
        newValueColumn.setCellValueFactory(data -> new SimpleStringProperty((data.getValue().getNewValue().toString())));
        userNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUser().getUsername()));
        localDateTimeColumn.setCellValueFactory(data -> new SimpleStringProperty((data.getValue().getLocalDateTime().toString())));

        changesTableView.setItems(FXCollections.observableList(changeList));
    }
}
