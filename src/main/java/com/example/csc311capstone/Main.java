package com.example.csc311capstone;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.Objects;

public class Main extends Application {
    private static Stage primaryStage;
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setResizable(false);
        try {
            Parent root = FXMLLoader.load(getClass().getResource("com/example/csc311capstone/splash.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.show();
            Parent newRoot = FXMLLoader.load(getClass().getResource("com/example/csc311capstone/login.fxml"));
            Scene currentScene = primaryStage.getScene();
            Parent currentRoot = currentScene.getRoot();
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), currentRoot);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> {
                Stage login = new Stage();
                Scene newScene = new Scene(newRoot);
                login.setScene(newScene);
                login.initStyle(StageStyle.DECORATED);
                login.show();
                primaryStage.close();

            });

            fadeOut.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}