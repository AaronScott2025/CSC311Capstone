package com.example.csc311capstone.Controllers;

import com.example.csc311capstone.Functions.Invest;
import com.example.csc311capstone.Functions.Locations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.util.List;

/**
 * REFERENCES:
 * https://quickchart.io  | Chart maker for Function 1
 */

public class MainController {
    @FXML
    private Button budget;

    @FXML
    private Button career;

    @FXML
    private Button invest;

    @FXML
    private Label lblUser;
    @FXML
    private MenuItem shutDown;

    @FXML
    private Button relocate;
    private static Stage current;
    private static Stage substage = new Stage();

    @FXML
    void budgetPress(ActionEvent event) {

    }

    @FXML
    void careerPress(ActionEvent event) {

    }

    @FXML
    void investPress(ActionEvent event) throws IOException {
        current = (Stage) invest.getScene().getWindow();
        current.hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/csc311capstone/Invest.fxml"));
        Parent root = loader.load();
        Scene s = new Scene(root);
        substage.setScene(s);
        substage.setTitle("Investments");
        substage.show();
    }

    @FXML
    void relocatePressed(ActionEvent event) throws IOException {
        current = (Stage) invest.getScene().getWindow();
        current.hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/csc311capstone/LocationsQuestions.fxml"));
        Parent root = loader.load();
        Scene s = new Scene(root);
        substage.setScene(s);
        substage.setTitle("Relocation");
        substage.show();

    }
    public void initialize(String username) {
        lblUser.setText("Welcome: " + username);
    }

    /**
     * ========================================================================================================================================================== *
     * Function 1 Controller methods
     * ========================================================================================================================================================== *
     */
    /**
     * Invest.fxml:
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
    private Stage previousStageinvest;

    @FXML
    void showInvestments(ActionEvent event) throws IOException { //Make new scene with graph, showing data.
        Invest i = new Invest(Integer.parseInt(startingTXT.getText()),Integer.parseInt(yearsTXT.getText()),Integer.parseInt(yearlyTXT.getText()));
        makeChartInvest(i); //Makes the png for the chart using API

        FXMLLoader fx = new FXMLLoader(getClass().getResource("/com/example/csc311capstone/InvestmentsResults.fxml"));
        Parent root = fx.load();

        MainController c = fx.getController();
        Path chartpath = Paths.get("chart.png").toAbsolutePath();
        Image image = new Image(chartpath.toUri().toString());

        c.chartView.setImage(image);

        Scene s = new Scene(root);
        substage.setScene(s);
        substage.show();

    }
    private void makeChartInvest(Invest in) throws IOException {
        StringBuilder data = new StringBuilder();
        StringBuilder SP = new StringBuilder();
        StringBuilder HYSA = new StringBuilder();
        StringBuilder Bond = new StringBuilder();
        double[] SP_double = in.eightPercent();
        double[] HYSA_double = in.fivePercent();
        double[] Bond_double = in.threePercent();
        data.append("[");
        SP.append("[");
        HYSA.append("[");
        Bond.append("[");
        for(int i = 0; i < in.getYears();i++) {
            data.append("'" + i + "'");
            SP.append(SP_double[i]);
            HYSA.append(HYSA_double[i]);
            Bond.append(Bond_double[i]);
            if(i + 1 < in.getYears()) {
                data.append(", ");
                SP.append(", ");
                HYSA.append(", ");
                Bond.append(", ");
            }
        }
        data.append("]");
        SP.append("]");
        HYSA.append("]");
        Bond.append("]");
//        System.out.println(data.toString());
//        System.out.println(SP.toString());
//        System.out.println(HYSA.toString());
//        System.out.println(Bond.toString());
        String chartConfig = "{"
                + "  type: 'line',"
                + "  data: {"
                + "    labels: " + data + ","
                + "    datasets: [{"
                + "      label: 'S&P500',"
                + "      borderColor: 'red',"
                + "      fill: false,"
                + "      data: " + SP
                + "    }, {"
                + "      label: 'High Yielding Investment Account',"
                + "      borderColor: 'blue',"
                + "      fill: false,"
                + "      data: " + HYSA
                + "    }, {"
                + "      label: 'Bonds',"
                + "      borderColor: 'green',"
                + "      fill: false,"
                + "      data: " + Bond
                + "    }]"
                + "  }"
                + "}";


        String encodedChartConfig = URLEncoder.encode(chartConfig, StandardCharsets.UTF_8.toString());

        String chartUrl = "https://quickchart.io/chart?c=" + encodedChartConfig;

        URL url = new URL(chartUrl);// Download the chart image
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET"); //Get Req (200 is successful)

        Files.copy(connection.getInputStream(), Paths.get("chart.png"));// Save the image to a file
        connection.disconnect();

        System.out.println("Chart image saved as chart.png");
    }
    /**
     * InvestmentsResults.fxml
     */
    @FXML
    private ImageView chartView;

    @FXML
    private Button exitBTNResult;

    @FXML
    private Button restartBTN;

    @FXML
    void backInvestments(ActionEvent event) throws IOException {
        deleteChart();
        FXMLLoader fx = new FXMLLoader(getClass().getResource("/com/example/csc311capstone/Invest.fxml"));
        Scene s = new Scene(fx.load());
        substage.setScene(s);
    }
    /**
     * Function 2 Controller
     */
    /**
     * Locations.fxml
     */
    @FXML
    private Label bot1;

    @FXML
    private Label bot2;

    @FXML
    private Label bot3;

    @FXML
    private Label num1;

    @FXML
    private Label num2;

    @FXML
    private Label num3;

    @FXML
    private Button retryLocation;

    @FXML
    void retryLocations(ActionEvent event) throws IOException {
        FXMLLoader fx = new FXMLLoader(getClass().getResource("/com/example/csc311capstone/LocationsQuestions.fxml"));
        Scene s = new Scene(fx.load());
        substage.setScene(s);
    }

    /**
     * LocationsQuestions.fxml
     */
    @FXML
    private MenuButton Question2;

    @FXML
    private MenuButton Question3;

    @FXML
    private MenuButton Question4;

    @FXML
    private MenuButton importanceCost;

    @FXML
    private MenuButton importanceCrime;

    @FXML
    private MenuButton importanceRecreation;

    @FXML
    private MenuItem kinda;

    @FXML
    private MenuItem lux;

    @FXML
    private MenuItem no;

    @FXML
    private MenuItem notalot;

    @FXML
    private MenuItem one,one1,one2;

    @FXML
    private MenuItem simple;

    @FXML
    private MenuItem three,three1,three2;

    @FXML
    private MenuItem two,two1,two2;

    @FXML
    private MenuItem very;

    @FXML
    private MenuItem yes;
    @FXML
    private Label errorHandler;

    @FXML
    void changeText(ActionEvent event) {
        MenuItem m = (MenuItem) event.getSource();
        MenuButton mb = (MenuButton) m.getParentPopup().getOwnerNode();
        mb.setText(m.getText());

    }
    @FXML
    void showLocations(ActionEvent event) throws IOException {
        FXMLLoader fx = new FXMLLoader(getClass().getResource("/com/example/csc311capstone/Locations.fxml"));
        Parent root = fx.load();

        MainController m = fx.getController();
        double initCOL = 5;
        double initREC = 5;
        double initCRIME = 5;

        int q1 = Integer.parseInt(importanceCost.getText()) - 2;
        int q2 = Integer.parseInt(importanceRecreation.getText()) - 2;
        int q3 = Integer.parseInt(importanceCrime.getText()) - 2;

        if(q1 == q2 || q1 == q3 || q2 == q3) {
            errorHandler.setVisible(true);
            errorHandler.setText("Inputs for question 1 cannot be the same. Please make sure only one number is used for each category.");
        } else {
            initCOL = initCOL + q1;
            initREC = initREC + q2;
            initCRIME = initCRIME + q3;

            if (Question2.getText().equals("Luxury")) {
                initCOL = initCOL + 2;
            } else {
                initCOL = initCOL - 2;
            }
            if (Question3.getText().equals("No")) {
                initCRIME = initCRIME + 2;
            } else {
                initCRIME = initCRIME - 2;
            }
            if (Question4.getText().equals("Not alot")) {
                initREC = initREC + 2;
            } else if (Question4.getText().equals("Very")) {
                initREC = initREC - 2;
            }

            Locations l = new Locations("test", initCOL, initREC, initCRIME, 1);
            List<Locations> StateData = l.knn(initCOL, initREC, initCRIME);
            StateData.sort(Locations::compareTo);
            List<Locations> first = StateData.subList(0, Math.min(3, StateData.size()));
            List<Locations> last = StateData.subList(StateData.size() - 3, StateData.size());
            Locations[] firstList = first.toArray(new Locations[3]);
            Locations[] lastList = last.toArray(new Locations[3]);
            m.num1.setText("Number 1 Choice: " + firstList[0].getState() + " | " + Math.round(100 * (1 - (firstList[0].getDist() / 10))) + "%");
            m.num2.setText("Number 2 Choice: " + firstList[1].getState() + " | " + Math.round(100 * (1 - (firstList[1].getDist() / 10))) + "%");
            m.num3.setText("Number 3 Choice: " + firstList[2].getState() + " | " + Math.round(100 * (1 - (firstList[2].getDist() / 10))) + "%");
            m.bot1.setText("Bottom 1 Choice: " + lastList[0].getState() + " | " + Math.round(100 * (1 - (lastList[0].getDist() / 10))) + "%");
            m.bot2.setText("Bottom 2 Choice: " + lastList[1].getState() + " | " + Math.round(100 * (1 - (lastList[1].getDist() / 10))) + "%");
            m.bot3.setText("Bottom 3 Choice: " + lastList[2].getState() + " | " + Math.round(100 * (1 - (lastList[2].getDist() / 10))) + "%");
            Scene s = new Scene(root);
            substage.setScene(s);
            substage.show();
        }
    }
    /**
     * GLOBAL FUNCTIONS
     */
    @FXML
    public void backToMain(ActionEvent event) { //Every function uses this.
        deleteChart();
        substage.close();
        current.show();
    }
    public void deleteChart() {
        File deleteFile = new File("chart.png");
        if(deleteFile.delete()) {
            System.out.println("Chart deleted");
        } else {
            System.out.println("Chart not deleted");
        }
    }
    public void shutdownapp(ActionEvent event) {
        System.exit(0);
    }


    public void viewInfo(ActionEvent actionEvent) {
    }

    public void updateSalary(ActionEvent actionEvent) {
    }

    public void updateLocation(ActionEvent actionEvent) {
    }
}