package com.example.csc311capstone.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private Button budget;

    @FXML
    private Button career;

    @FXML
    private Button invest;

    @FXML
    private Button relocate;

    @FXML
    void budgetPress(ActionEvent event) {

    }

    @FXML
    void careerPress(ActionEvent event) {

    }

    @FXML
    void investPress(ActionEvent event) {

    }

    @FXML
    void relocatePressed(ActionEvent event) {

    }
    @FXML
    void backToMain(ActionEvent event) { //Every function uses this.

    }

    /**
     * ========================================================================================================================================================== *
     * Function 1 Controller methods
     * ========================================================================================================================================================== *
     */
    @FXML
    private Button exitBTN;

    @FXML
    private Button goBTN;

    @FXML
    private TextField startingTXT;

    @FXML
    private TextField yearlyTXT;

    @FXML
    private TextField yearsTXT;


    @FXML
    void showInvestments(ActionEvent event) { //Make new scene with graph, showing data.
        Invest i = new Invest(Integer.getInteger(startingTXT.getText()),Integer.getInteger(yearsTXT.getText()),Integer.getInteger(yearlyTXT.getText()));
        Stage stage = new Stage();
//        Scene func1Scene = new Scene();
//        stage.setScene();
    }


}