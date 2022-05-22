package com.company.schedulingapp.controller;


import com.company.schedulingapp.dbaccess.DBUsers;
import com.company.schedulingapp.util.JDBC;
import com.company.schedulingapp.util.SceneController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    SceneController sceneController = SceneController.getSceneControllerInstance();

    @FXML TextField usernameTextField;
    @FXML TextField passwordTextField;

    public void login(ActionEvent event) throws SQLException, IOException {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();


        if (DBUsers.verifyUsername(username)) {
            if (DBUsers.verifyPassword(password)) {
                System.out.println("User verified.");
                sceneController.setScene(event, "Main.fxml");
            } else {
                System.out.println("Incorrect password.");
                //TODO: onscreen error message
            }
        } else {
            System.out.println("Incorrect username.");
            //TODO: onscreen error message
        }
    }

    public void exit() {
        JDBC.closeConnection();
        Platform.exit();
    }

}