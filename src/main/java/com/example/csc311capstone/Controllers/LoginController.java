package com.example.csc311capstone.Controllers;

import com.example.csc311capstone.App.Main;
import com.example.csc311capstone.Functions.Login;
import com.example.csc311capstone.Functions.User;
import com.example.csc311capstone.db.ConnDbOps;
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

import java.io.IOException;

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
    private PasswordField passTxt;
    private static ConnDbOps cd;
    private int x = 3;



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
    void enteredLogin(MouseEvent event) throws IOException {
        boolbl.setText("");
        cd = new ConnDbOps(); //New DB connection
        String pass = passTxt.getText(); //Password
        String user = userTxt.getText(); //Username
        Login login = new Login(user, pass); //Login Class entered in
        String encryption = login.encryption(); //Login encryption
        User u = cd.getUser(user); //Get Username from SQL Query
        if(u.getUsername().isEmpty() || !u.getPassword().equals(encryption)) { //Fails if user not found, or password !=
            System.out.println(u.getUsername()); //Testing Statement
            x--;
            boolbl.setText("Login Failed: Username or Password is incorrect");
            boolbl.setVisible(true);
            if(x == 0) {
                System.exit(0);
            }
        } else {
            System.out.println("User Accepted, Welcome!");
            Stage stage = Main.getPrimaryStage();
            FXMLLoader f = new FXMLLoader(getClass().getResource("/com/example/csc311capstone/MainInterface.fxml"));
            Scene s = new Scene(f.load());
            stage.setScene(s);
            stage.setTitle("Future Link");
            MainController mc = f.getController();
            mc.initialize(user);
            stage.show();
        }
    }
/********************************************************************************************************************************/
    /**
     * Create Account Functions
     */
    @FXML
    private TextField userTxt;
    @FXML
    private ImageView enterbtnCREATE;

    @FXML
    private PasswordField passTxtCREATE;

    @FXML
    private TextField userTxtCREATE;

    @FXML
    void enteredCreate(MouseEvent event) {
        cd = new ConnDbOps();
        String user = userTxtCREATE.getText();
        String pass = passTxtCREATE.getText();
        Login login = new Login(user, pass);
        pass = login.encryption();
        login.setPassword(pass);
        User s = new User(0,"",login.getPassword(),login.getUsername());
        try {
            cd.registerUser(s);
        } catch (Exception e) {
            System.out.println("Unable to register user");
        }
        System.out.println(userTxtCREATE.getText());
    }
}