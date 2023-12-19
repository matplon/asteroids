package com.example.MotorolaScienceCup.Asteroids;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

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
    private MediaView mediaView;



    @FXML
    void initialize() {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        anchorPane.setPrefHeight(screenBounds.getHeight());
        anchorPane.setPrefWidth(screenBounds.getWidth());
    }




    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}