package com.example.MotorolaScienceCup;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

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
    private Media media;
    @FXML
    private MediaPlayer mediaPlayer;
    @FXML
    private MediaView mediaView;



    @FXML
    void initialize() {
        first = new ImageView(new Image("file:first.jpg"));
        tempest = new ImageView(new Image("file:tempest.jpg"));
        battleZone = new ImageView(new Image("file:battlezone.jpg"));

        anchorPane.getChildren().addAll(first, tempest, battleZone);
        anchorPane.setPrefWidth(800);
        anchorPane.setPrefHeight(800);
       /* Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        anchorPane.setPrefHeight(screenBounds.getHeight());
        anchorPane.setPrefWidth(screenBounds.getWidth());
*/
        //animation();
    }

    @FXML
    void animation() {
        Media media = new Media(getClass().getResource("img.mp4").toString());

        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        MediaView mediaView = new MediaView(mediaPlayer);

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
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}