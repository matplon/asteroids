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
import javafx.scene.shape.Polygon;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;


public class Main extends Application {

    final int WIDTH = 1800;
    final int HEIGHT = 900;
    final double FPS = 30;
    final double CENTERX = (double) WIDTH / 2, CENTERY = (double) HEIGHT / 2;
    final double RADIUS = 15;
    final double ASTEROID_COUNT = 6;

    int HP = 3;


    AnchorPane root;
    Scene scene;

    Particle player;
    public List<Particle> asteroids;

    public List<Double> SVGconverter(String filepath) {
        List<Double> list = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(filepath));
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                String leftRemoved = nextLine.replaceAll("^\\s+", "");
                nextLine = leftRemoved.replaceAll("\\s+$", "");
                if (nextLine.startsWith("d=")) {
                    String subString = nextLine.substring(5, nextLine.length() - 3);
                    String[] li = subString.split(" ");
                    for (int i = 0; i < li.length; i++) {
                        String[] lili = li[i].split(",");
                        list.add(Double.parseDouble(lili[0]));
                        list.add(Double.parseDouble(lili[1]));
                    }
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return list;
    }


    public void init() {
        root = new AnchorPane();
        scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(Color.BLACK);


        player = new Particle(CENTERX, CENTERY, RADIUS, 0, true);
        player.setFill(Color.TRANSPARENT);
        player.setStroke(Color.WHITE);
        root.getChildren().add(player);


        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.UP) player.accelerate();   // Thrust forward
            if (keyEvent.getCode() == KeyCode.RIGHT) player.setRotationRight(FPS);   // Rotate right
            if (keyEvent.getCode() == KeyCode.LEFT) player.setRotationLeft(FPS); // Rotate left
        });

        scene.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.UP) player.stopAcceleration(); // Stop thrusting forward
            if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.LEFT)
                player.stopRotation(); // Stop rotating
        });

        asteroids = new ArrayList<>();

        for (int i = 0; i < ASTEROID_COUNT; i++) {
            asteroids.add(new Particle(Math.random() * WIDTH, Math.random() * HEIGHT, Math.random() * 150, Math.random() * 6, Math.random() * 20,
                    Math.random() * 360 - 180, false));
            asteroids.get(i).setStroke(Color.WHITE);
            root.getChildren().add(asteroids.get(i));
        }

    }

    public void gameLogic() {

    }

    @Override
    public void start(Stage stage) {
        AtomicBoolean wasNotKilled = new AtomicBoolean(true);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000.0 / FPS), actionEvent -> {
            player.updatePosition(FPS, WIDTH, HEIGHT); // Update player's position
            if (!wasNotKilled.get()) {
                Circle circle = new Circle(WIDTH / 2, HEIGHT / 2, 50);
                boolean newSafe = true;
                for (int i = 0; i < asteroids.size(); i++) {
                    if (Shape.intersect(circle, asteroids.get(i)).getLayoutBounds().getWidth() != -1) {
                        newSafe = false;
                    }
                }
                if (newSafe) {
                    System.out.println("lol");
                    wasNotKilled.set(true);
                    player = new Particle(WIDTH / 2, HEIGHT / 2, RADIUS, 0, true);
                    player.setFill(Color.TRANSPARENT);
                    player.setStroke(Color.WHITE);
                    root.getChildren().add(player);
                    System.out.println(HP);
                }
            }
            for (int i = 0; i < asteroids.size(); i++) {
                asteroids.get(i).updatePosition(FPS, WIDTH, HEIGHT);
                if (Shape.intersect(player, asteroids.get(i)).getLayoutBounds().getWidth() != -1 && root.getChildren().contains(player)) {
                    HP--;
                    root.getChildren().remove(player);
                    System.out.println(HP);
                    if (HP <= 0) {

                    } else {
                        wasNotKilled.set(false);
                    }
                    //fssfs
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