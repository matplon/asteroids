package com.example.MotorolaScienceCup;

import com.example.MotorolaScienceCup.Asteroids.Main;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;
import java.net.MalformedURLException;

import static javafx.application.Application.launch;

public class MainController {
    @FXML
    private Label welcomeText;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ImageView tempest;

    @FXML
    private ImageView first;

    @FXML
    private ImageView battleZone;


    @FXML
    private MediaView mediaView;



    @FXML
    void initialize() {
        first.setImage(new Image("file:images\\first.jpg"));
        tempest.setImage(new Image("file:images\\tempest.jpg"));
        battleZone.setImage(new Image("file:images\\battlezone.jpg"));
    }

    @FXML
    void animation() {
        Media media = null;
        try {
            media = new Media(new File("images\\img.mp4").toURI().toURL().toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        mediaView = new MediaView(mediaPlayer);

        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            }
        });
        anchorPane.getChildren().add(mediaView);


    }

    @FXML
    protected void clickedAsteroids() {
        Main.init();
    }

    @FXML
    protected void clickedBattleZone() {
        com.example.MotorolaScienceCup.Battlezone.Main.init();
    }

    @FXML
    protected void clickedTempest() {
        com.example.MotorolaScienceCup.Tempest.Main.init();
    }


}