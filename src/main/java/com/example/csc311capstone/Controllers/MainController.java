package com.example.csc311capstone.Controllers;

import com.example.csc311capstone.Functions.Invest;
import com.example.csc311capstone.Functions.Locations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
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
    void relocatePressed(ActionEvent event) throws FileNotFoundException {
        Locations l = new Locations("test",1,1,1,1);
        List<Locations> StateData = l.knn(5.6,2.3,1.1);
        StateData.sort(Locations::compareTo);
        List<Locations> first = StateData.subList(0,Math.min(3, StateData.size()));
        List<Locations> last = StateData.subList(StateData.size() - 3,StateData.size());
        for(Locations loc : first) {
            System.out.println(loc.getState() + "  " + loc.getDist());
        }
        for(Locations loc : last) {
            System.out.println(loc.getState() + "  " + loc.getDist());
        }

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