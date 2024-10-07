module com.example.csc311capstone {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.csc311capstone to javafx.fxml;
    exports com.example.csc311capstone;
}