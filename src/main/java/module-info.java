module com.example.javaprojekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;


    opens main to javafx.fxml;
    exports main;
    exports main.controllers;
    opens main.controllers to javafx.fxml;
}