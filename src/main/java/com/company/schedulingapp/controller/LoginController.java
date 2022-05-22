package com.company.schedulingapp.controller;


import com.company.schedulingapp.util.JDBC;
import javafx.application.Platform;

public class LoginController {

    public void exit() {
        JDBC.closeConnection();
        Platform.exit();
    }

}