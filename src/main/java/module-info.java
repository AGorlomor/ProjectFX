module com.example.projectfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens Projects to javafx.fxml;
    exports Projects;
}