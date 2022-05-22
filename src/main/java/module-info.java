module com.company.schedulingapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.company.schedulingapp to javafx.fxml;
    exports com.company.schedulingapp;
    exports com.company.schedulingapp.controller;
    opens com.company.schedulingapp.controller to javafx.fxml;
}