module com.example.MotorolaScienceCup {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.MotorolaScienceCup to javafx.fxml;
    exports com.example.MotorolaScienceCup.Asteroids;
}