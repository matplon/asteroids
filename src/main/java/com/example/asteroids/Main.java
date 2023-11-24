package com.example.asteroids;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Main extends Application {

    final int WIDTH = 1800;
    final int HEIGHT = 900;
    final double FPS = 30;
    final double CENTERX = (double) WIDTH /2, CENTERY = (double) HEIGHT /2;
    final double RADIUS = 15;


    AnchorPane root;
    Scene scene;

    Particle player;
    List


    public void init(){
        root = new AnchorPane();
        scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(Color.BLACK);

        player = new Particle(CENTERX, CENTERY, RADIUS, 0, true);
        player.setFill(Color.TRANSPARENT);
        player.setStroke(Color.WHITE);
        root.getChildren().add(player);

        scene.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.UP) player.accelerate();   // Thrust forward
            if(keyEvent.getCode() == KeyCode.RIGHT) player.setRotationRight(FPS);   // Rotate right
            if(keyEvent.getCode() == KeyCode.LEFT) player.setRotationLeft(FPS); // Rotate left
        });

        scene.setOnKeyReleased(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.UP) player.stopAcceleration(); // Stop thrusting forward
            if(keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.LEFT) player.stopRotation(); // Stop rotating
        });

    }

    public void gameLogic(){

    }

    @Override
    public void start(Stage stage){
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000.0/FPS), actionEvent -> {
            player.updatePosition(FPS, WIDTH, HEIGHT); // Update player's position
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}