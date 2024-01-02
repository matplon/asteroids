package com.example.MotorolaScienceCup.Asteroids;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class ShopController {
    @FXML
    private ImageView menuButton;

    @FXML
    private Text coins;

    @FXML
    void initialize(){
        menuButton = new ImageView(new Image("@Asteroids/img1.jpg"));
    }

}
