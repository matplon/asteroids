package com.example.MotorolaScienceCup.Asteroids;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;


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
