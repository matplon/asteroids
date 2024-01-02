package com.example.MotorolaScienceCup;

import com.example.MotorolaScienceCup.Asteroids.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

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

//        animation();
    }

    @FXML
    void animation() {
        Media media = new Media(getClass().getResource("file:images\\img.mp4").toString());

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
//        anchorPane.getChildren().add(mediaView);


    }

    @FXML
    private void clicked(MouseEvent event)
    {
        Main.main();
    }




    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}