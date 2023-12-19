module com.example.MotorolaScienceCup {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.MotorolaScienceCup to javafx.fxml;
    exports com.example.MotorolaScienceCup.Asteroids;
}