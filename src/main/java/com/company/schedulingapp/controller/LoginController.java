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
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Presents login screen to user
 * Tests for valid username and password
 * Sets zoneID and locale
 * Checks for upcoming appointments
 */
public class LoginController implements Initializable {

    SceneController sceneController = SceneController.getSceneControllerInstance();
    ResourceBundle rb;

    @FXML TextField usernameTextField;
    @FXML PasswordField passwordTextField;
    @FXML Label locationLabel;
    @FXML Text titleText;
    @FXML Button loginButton;
    @FXML Button exitButton;

    /**
     * Sets up locale translations
     *
     * From https://docs.oracle.com/javase/8/javafx/api/javafx/fxml/Initializable.html:
     * @param url the location used to resolve relative paths for the root object, or null if the location is not known
     * @param resourceBundle  The resources used to localize the root object, or null if the root object was not localized.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            resourceBundle = ResourceBundle.getBundle("login", Locale.getDefault());
            rb = resourceBundle;
            titleText.setText(resourceBundle.getString("title"));
            usernameTextField.setPromptText(resourceBundle.getString("username"));
            passwordTextField.setPromptText(resourceBundle.getString("password"));
            loginButton.setText(resourceBundle.getString("login"));
            exitButton.setText(resourceBundle.getString("exit"));


        } catch (MissingResourceException e) {
            e.printStackTrace();
        }
        setLocationLabel();
    }

    private ZoneId getZoneID() {
        return ZoneId.systemDefault();
    }

    private void setLocationLabel() {
        locationLabel.setText(getZoneID().getId());
    }

    /**
     * Verify correct username and password entry
     * @param event login button
     * @throws SQLException throws error if SQL query fails
     */
    public void login(ActionEvent event) throws SQLException {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();


        if (DBUsers.verifyUsername(username)) {
            if (DBUsers.verifyPassword(password)) {
                recordLoginAttempt(username, true);
                checkForUpcomingAppointments();
                sceneController.setScene(event, "Main.fxml");
            } else {
                recordLoginAttempt(username, false);
                displayIncorrectPasswordError();
            }
        } else {
            recordLoginAttempt(username, false);
            displayIncorrectUsernameError();
        }
    }

    private void displayIncorrectUsernameError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(rb.getString("username"));
        alert.setContentText(rb.getString("userLoginError"));
        alert.showAndWait();
    }

    private void displayIncorrectPasswordError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(rb.getString("password"));
        alert.setContentText(rb.getString("passwordLoginError"));
        alert.showAndWait();
    }

    /**
     * Checks if logged-in user has any appointments within the next 15 minutes
     */
    private void checkForUpcomingAppointments() {
        ObservableList<Appointment> userAppointments = FXCollections.observableArrayList();
        Appointment upcomingAppointment = null;
        boolean hasUpcomingAppointment = false;
        try {
            userAppointments = DBAppointments.getUserAppointments(DBUsers.getCurrentUserID());
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

    /**
     * Records each login attempt, the username, the date and time, and whether it was successful
     * @param username String of username
     * @param loginAttempt result of login attempt
     */
    private void recordLoginAttempt(String username, Boolean loginAttempt) {
        Logger log = Logger.getLogger("login_activity.txt");

        try {
            FileHandler fileHandler = new FileHandler("login_activity.txt", true);
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);
            log.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.setLevel(Level.INFO);

        if (loginAttempt) {
            log.log(Level.INFO, "Login attempt: " + username + " at " + Timestamp.valueOf(LocalDateTime.now()) + " successful." );
        } else {
            log.log(Level.INFO, "Login attempt: " + username + " at " + Timestamp.valueOf(LocalDateTime.now()) + " failed." );
        }

    }

    public void exit() {
        JDBC.closeConnection();
        Platform.exit();
    }


}