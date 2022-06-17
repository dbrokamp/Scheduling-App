package com.company.schedulingapp.controller;


import com.company.schedulingapp.dbaccess.DBUsers;
import com.company.schedulingapp.util.JDBC;
import com.company.schedulingapp.util.SceneController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    SceneController sceneController = SceneController.getSceneControllerInstance();

    @FXML TextField usernameTextField;
    @FXML PasswordField passwordTextField;
    @FXML Label locationLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

    public void exit() {
        JDBC.closeConnection();
        Platform.exit();
    }


}