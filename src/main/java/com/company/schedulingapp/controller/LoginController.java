package com.company.schedulingapp.controller;


import com.company.schedulingapp.dbaccess.DBAppointments;
import com.company.schedulingapp.dbaccess.DBUsers;
import com.company.schedulingapp.model.Appointment;
import com.company.schedulingapp.util.JDBC;
import com.company.schedulingapp.util.SceneController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    SceneController sceneController = SceneController.getSceneControllerInstance();

    @FXML TextField usernameTextField;
    @FXML PasswordField passwordTextField;
    @FXML Label locationLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            resourceBundle = ResourceBundle.getBundle("locale.properties", Locale.getDefault());
//            LoginLabel.setText(resourceBundle.getString("title"));
//            LoginUsernameLabel.setText(resourceBundle.getString("username"));
//            UsernameTextField.setPromptText(resourceBundle.getString("username"));
//            LoginPasswordLabel.setText(resourceBundle.getString("password"));
//            PasswordTextField.setPromptText(resourceBundle.getString("password"));
//            LoginButton.setText(resourceBundle.getString("signin"));
        } catch (MissingResourceException e) {
            System.out.println("Missing resource");
        }
        setLocationLabel();
    }

    private ZoneId getZoneID() {
        return ZoneId.systemDefault();
    }

    private void setLocationLabel() {
        locationLabel.setText(getZoneID().getId());
    }

    public void login(ActionEvent event) throws SQLException {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();


        if (DBUsers.verifyUsername(username)) {
            if (DBUsers.verifyPassword(password)) {
                System.out.println("User verified.");
                checkForUpcomingAppointments();
                sceneController.setScene(event, "Main.fxml");
            } else {
                System.out.println("Incorrect password.");
                displayIncorrectPasswordError();
            }
        } else {
            System.out.println("Incorrect username.");
            displayIncorrectUsernameError();
        }
    }

    private void displayIncorrectUsernameError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Username");
        alert.setContentText("Invalid username.");
        alert.showAndWait();
    }

    private void displayIncorrectPasswordError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Password");
        alert.setContentText("Invalid password.");
        alert.showAndWait();
    }

    private void checkForUpcomingAppointments() {
        ObservableList<Appointment> userAppointments = FXCollections.observableArrayList();
        Appointment upcomingAppointment = null;
        boolean hasUpcomingAppointment = false;
        try {
            userAppointments = DBAppointments.getUserAppointments(DBUsers.getCurrentUserID());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        LocalDateTime nowDateTime = LocalDateTime.now();
        System.out.println(nowDateTime);

        LocalDateTime nowPlusFifteenMinutes = nowDateTime.plusMinutes(15);
        System.out.println(nowPlusFifteenMinutes);

        Timestamp nowTimestamp = Timestamp.valueOf(LocalDateTime.now());
        Timestamp nowPlusFifteenMinutesTimestamp = Timestamp.valueOf(LocalDateTime.now().plusMinutes(15));

        for (Appointment appointment : userAppointments) {
            Timestamp meetingStart = appointment.getStart();
            if (meetingStart.after(nowTimestamp) && meetingStart.before(nowPlusFifteenMinutesTimestamp)) {
                hasUpcomingAppointment = true;
                upcomingAppointment = appointment;
            }
        }

        if (hasUpcomingAppointment) {
            presentHasUpcomingAppointment(upcomingAppointment);
        } else {
            presentNoUpComingAppointment();
        }
    }

    private void presentHasUpcomingAppointment(Appointment upcomingAppointment) {
        Alert upcomingAppointmentAlert = new Alert(Alert.AlertType.INFORMATION);
        upcomingAppointmentAlert.setTitle("Application Message");
        upcomingAppointmentAlert.setHeaderText("Upcoming Appointment");
        upcomingAppointmentAlert.setContentText("AppointmentID: " + upcomingAppointment.getAppointmentID().toString() + ". Starts at: " + upcomingAppointment.getStart().toString());
        upcomingAppointmentAlert.showAndWait();
    }

    private void presentNoUpComingAppointment() {
        Alert noUpcomingAppointmentAlert = new Alert(Alert.AlertType.INFORMATION);
        noUpcomingAppointmentAlert.setTitle("Application Message");
        noUpcomingAppointmentAlert.setHeaderText("Upcoming Appointment");
        noUpcomingAppointmentAlert.setContentText("You have no upcoming appointments.");
        noUpcomingAppointmentAlert.showAndWait();
    }

    public void exit() {
        JDBC.closeConnection();
        Platform.exit();
    }


}