package com.example.csc311capstone.App;

import com.example.csc311capstone.db.ConnDbOps;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Main extends Application {
    private static Stage primaryStage;
    private static ConnDbOps cdbop;
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        applaunch();


    }

    private void applaunch() {
        cdbop = new ConnDbOps();
        try {
            cdbop.connectToDatabase();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/csc311capstone/splash.fxml")); //Get Splash
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.UNDECORATED); //No top bar
            primaryStage.show(); //Splash visible
            Parent newRoot = FXMLLoader.load(getClass().getResource("/com/example/csc311capstone/login.fxml")); //Load Login
            Scene currentScene = primaryStage.getScene();
            Parent currentRoot = currentScene.getRoot();

            //Transition Code
            PauseTransition pause = new PauseTransition(Duration.seconds(3)); //Hold for 3 seconds
            pause.setOnFinished(event -> {
                //After Pause
                TranslateTransition chacha = new TranslateTransition(Duration.seconds(1), currentRoot); //Cha Cha real smooth
                chacha.setFromX(0); //From origin (middle)
                chacha.setToX(-currentScene.getWidth()); //To left
                chacha.setOnFinished(e -> {
                    //After splash slides out
                    Stage login = new Stage(); //New stage must be declared, to give primary stage its top bar back.
                    Scene newScene = new Scene(newRoot); //Login.fxml as new scene
                    TranslateTransition reversereverse = new TranslateTransition(Duration.seconds(1), newRoot); //Reverse Reverse
                    reversereverse.setFromX(-currentScene.getWidth()); //From left
                    reversereverse.setToX(0); //To origin (middle)
                    login.setScene(newScene); //Set scene
                    login.initStyle(StageStyle.DECORATED); //Has bar
                    primaryStage.close(); //Close splash
                    login.show(); //Open Login
                    reversereverse.play(); //Play transition after cha cha
                });
            chacha.play(); //Play transition after pause
            });
            pause.play(); //play the pause (ironic)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}