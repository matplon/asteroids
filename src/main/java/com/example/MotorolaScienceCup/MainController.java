package com.example.MotorolaScienceCup;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.example.MotorolaScienceCup.Asteroids.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MainController {

    public Button Exit;
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
    void asteroids(ActionEvent event) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
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
        Asteroids.setLayoutX(((double) Menu.WIDTH / 3) + 60);
        Asteroids.setLayoutY(((double) Menu.HEIGHT / 3));

        Tempest.setLayoutX(((double) Menu.WIDTH / 3) + 60);
        Tempest.setLayoutY(((double) Menu.HEIGHT / 3) + 40);

        Battlezone.setLayoutX(((double) Menu.WIDTH / 3) + 60);
        Battlezone.setLayoutY(((double) Menu.HEIGHT / 3) + 80);
        Exit.setLayoutX(((double) Menu.WIDTH /2));
        Exit.setLayoutY(((double) Menu.HEIGHT /2));

    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
    }
}
