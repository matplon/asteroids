package com.example.asteroids;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.stage.Screen;

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