module com.example.csc311capstone {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;
    requires org.json;
    requires org.apache.pdfbox;


    opens com.example.csc311capstone to javafx.fxml;
    exports com.example.csc311capstone.Controllers;
    opens com.example.csc311capstone.Controllers to javafx.fxml;
    exports com.example.csc311capstone.Functions;
    opens com.example.csc311capstone.Functions to javafx.fxml;
    exports com.example.csc311capstone.App;
    opens com.example.csc311capstone.App to javafx.fxml;
}