package com.example.MotorolaScienceCup;

import com.example.MotorolaScienceCup.Asteroids.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController {

    @FXML
    private Button Exit;

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
    void exit(ActionEvent actionEvent) {
        Platform.exit();
    }


    @FXML
    void initialize() {
        Asteroids.setLayoutX((double) (3 * Menu.WIDTH) / 8);
        Asteroids.setLayoutY(((double) Menu.HEIGHT / 3));
        Asteroids.setFont(Menu.font);

        Tempest.setLayoutX((double) (3 * Menu.WIDTH) / 8);
        Tempest.setLayoutY(((double) Menu.HEIGHT / 3) + 40);
        Tempest.setFont(Menu.font);

        Battlezone.setLayoutX((double) (3 * Menu.WIDTH) / 8);
        Battlezone.setLayoutY(((double) Menu.HEIGHT / 3) + 80);
        Battlezone.setFont(Menu.font);

        Exit.setLayoutX((double) (3 * Menu.WIDTH) / 8);
        Exit.setLayoutY(((double) Menu.HEIGHT /2));
        Exit.setFont(Menu.font);

    }


}
