module com.example.MotorolaScienceCup {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;


    opens com.example.MotorolaScienceCup to javafx.fxml;
    exports com.example.MotorolaScienceCup.Asteroids;
    exports com.example.MotorolaScienceCup;
}