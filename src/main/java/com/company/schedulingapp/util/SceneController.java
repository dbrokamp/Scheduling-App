package com.company.schedulingapp.util;




import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Manages scene changes for application, utilizes Singleton Pattern
 */
public class SceneController {

    /**
     * Create single instance of SceneController object
     */
    private static SceneController instance = new SceneController();


    /**
     * Protected constructor to ensure only one instance is created.
     */
    protected SceneController() {}

    /**
     * Gets the single instance of SceneController or creates one if the single instance is null.
     * @return instance - returns the single instance of the SceneController.
     */
    public static SceneController getSceneControllerInstance() {
        if (instance == null) {
            instance = new SceneController();
        }
        return instance;
    }

    public void setInitialStageAndScene(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("LoginForm.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add("style.css");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setScene(ActionEvent event, String fxmlFileName)  {

        try {
            Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource(fxmlFileName));
            Scene scene = new Scene(parent);
            scene.getStylesheets().add("style.css");
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
