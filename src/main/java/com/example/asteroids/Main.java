package com.example.asteroids;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class Main extends Application {

    final int WIDTH = 1800;
    final int HEIGHT = 900;
    final double FPS = 30;
    final double CENTERX = (double) WIDTH /2, CENTERY = (double) HEIGHT /2;
    final double RADIUS = 15;
    final double ASTEROID_COUNT = 6;

    int HP = 3;


    AnchorPane root;
    Scene scene;

    Particle player;
    public List<Particle> asteroids;


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

        asteroids = new ArrayList<>();

        for (int i = 0; i < ASTEROID_COUNT; i++) {
            asteroids.add(new Particle(Math.random() * WIDTH, Math.random() * HEIGHT, Math.random() * 15, Math.random() * 6, Math.random() * 15,
                    Math.random() * 360 - 180, false));
            asteroids.get(i).setStroke(Color.WHITE);
            root.getChildren().add(asteroids.get(i));
        }

    }

    public void gameLogic(){

    }

    @Override
    public void start(Stage stage){
        AtomicBoolean isSafe = new AtomicBoolean(true);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000.0/FPS), actionEvent -> {
            player.updatePosition(FPS, WIDTH, HEIGHT); // Update player's position
            if(!isSafe.get()) {
                Circle circle = new Circle(WIDTH / 2, HEIGHT / 2, 1);
                boolean newSafe = true;
                for (int i = 0; i < asteroids.size(); i++) {
                    if(!asteroids.get(i).intersects(player.getLayoutBounds())){
                        if(asteroids.get(i).intersects(player.getLayoutBounds())){
                            newSafe = false;
                        }
                    }
                }
                if(newSafe){
                    System.out.println("lol");
                    isSafe.set(true);
                    player = new Particle(WIDTH/2,HEIGHT/2,RADIUS,0,true);
                    player.setFill(Color.TRANSPARENT);
                    player.setStroke(Color.WHITE);
                    root.getChildren().add(player);
                }
            }
            for (int i = 0; i < asteroids.size(); i++) {
                asteroids.get(i).updatePosition(FPS, WIDTH, HEIGHT);
                if(asteroids.get(i).intersects(player.getLayoutBounds())){
                    HP--;
                    root.getChildren().remove(player);
                    if(HP<=0){

                    }else{
                        isSafe.set(false);
                        }
                    }
                }

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