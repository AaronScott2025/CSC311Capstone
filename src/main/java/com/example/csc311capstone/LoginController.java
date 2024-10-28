package com.example.csc311capstone;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController {

    @FXML
    private Label boolbl;
    @FXML
    private Button createacct;
    @FXML
    private Rectangle rect;
    @FXML
    private ImageView enterbtn;

    @FXML
    private Pane loginPane;

    @FXML
    private PasswordField passTxt;

    @FXML
    private TextField userTxt;
    @FXML
    private ImageView enterbtnCREATE;

    @FXML
    private PasswordField passTxtCREATE;

    @FXML
    private TextField userTxtCREATE;

/********************************************************************************************************************************/
    /**
     * Login Functions
     */
    @FXML
    void createAccount(ActionEvent event) {
        Stage stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/csc311capstone/createAccount.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void enteredLogin(MouseEvent event) {
        String pass = passTxt.getText();
        String user = userTxt.getText();
        System.out.println(userTxt.getText());

    }
/********************************************************************************************************************************/
    /**
     * Create Account Functions
     */
    @FXML
    void enteredCreate(MouseEvent event) {
        String user = userTxtCREATE.getText();
        String pass = passTxtCREATE.getText();
        System.out.println(userTxtCREATE.getText());
    }
}