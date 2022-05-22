package com.company.schedulingapp;

import com.company.schedulingapp.util.JDBC;
import com.company.schedulingapp.util.SceneController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launch extends Application {

    SceneController sceneController = SceneController.getSceneControllerInstance();

    @Override
    public void start(Stage stage) {

        sceneController.setInitialStageAndScene(stage);

    }

    public static void main(String[] args) {
        JDBC.openConnection();
        launch();
    }
}