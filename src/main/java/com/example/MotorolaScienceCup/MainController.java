package com.example.MotorolaScienceCup;

import java.net.URL;
import java.util.ResourceBundle;

import com.example.MotorolaScienceCup.Asteroids.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button Asteroids;

    @FXML
    private Button Battlezone;

    @FXML
    private Button Tempest;

    @FXML
    void asteroids(ActionEvent event) {
        Main.init();
    }

    @FXML
    void battlezone(ActionEvent event) {
        com.example.MotorolaScienceCup.Battlezone.Main.init();
    }

    @FXML
    void tempest(ActionEvent event) {
        com.example.MotorolaScienceCup.Tempest.Main.init();
    }

    @FXML
    void initialize() {


    }
}
