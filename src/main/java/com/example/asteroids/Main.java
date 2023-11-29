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
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;


public class Main extends Application {

    final static int WIDTH = 1800;
    final static int HEIGHT = 900;
    final static double FPS = 30;
    final int INIT_ASTEROID_COUNT = 15;
    final double FRICTION = 0.7;

    static AtomicBoolean isAlive = new AtomicBoolean(true);
    List<Particle> bullets = new ArrayList<>();

    String shipFilePath = "ship1.svg";


    static int HP = 3;


    static AnchorPane root;
    Scene scene;
    static Particle player;
    public static List<Particle> asteroids;

    public static void explode() {
        isAlive.set(false);
        HP--;
        root.getChildren().remove(player);
    }

    public void gameOver() {
        System.out.println("Game Over");
    }
    public void bullet(Particle particle) {
        List<Double> points = Arrays.asList(1.0, 1.0, 1.0, 4.0, 2.4, 4.0, 2.4, 1.0);
        Particle bullet = new Particle(points, -particle.getAngle(), 0, 10, 0);
        bullet.setFill(Color.WHITE);
        bullet.moveTo(particle.getCenterX(), particle.getCenterY());
        bullets.add(bullet);

        root.getChildren().add(bullet);
    }

    public List<Double> SVGconverter(String filepath) { // Convert .svg file to a list of coordinates
        List<Double> list = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(filepath));
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                String leftRemoved = nextLine.replaceAll("^\\s+", "");  // Remove whitespaces from the sides
                nextLine = leftRemoved.replaceAll("\\s+$", "");
                if (nextLine.startsWith("d=")) {    // Find the path line in the .svg file
                    String subString = nextLine.substring(5, nextLine.length() - 3);
                    String[] li = subString.split(" ");
                    for (String s : li) {
                        String[] lili = s.split(",");
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


        player = new Particle(SVGconverter(shipFilePath), -90, 0, 0, FRICTION);
        player.moveTo((double) WIDTH / 2, (double) HEIGHT / 2);
        player.setFill(Color.TRANSPARENT);
        player.setStroke(Color.WHITE);
        root.getChildren().add(player);


        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.UP) player.accelerate();   // Thrust forward
            if (keyEvent.getCode() == KeyCode.RIGHT) player.setRotationRight();   // Rotate right
            if (keyEvent.getCode() == KeyCode.LEFT) player.setRotationLeft(); // Rotate left
            if (keyEvent.getCode() == KeyCode.E) player.hyperSpace();   // Teleport (chance of exploding colliding with an asteroid)
            if (keyEvent.getCode() == KeyCode.X) bullet(player);
        });

        scene.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.UP) player.stopAcceleration(); // Stop thrusting forward
            if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.LEFT)
                player.stopRotation(); // Stop rotating
        });

        asteroids = new ArrayList<>();

//        for (int i = 0; i < INIT_ASTEROID_COUNT; i++) {
//            asteroids.add();
//            asteroids.get(i).setStroke(Color.WHITE);
//            root.getChildren().add(asteroids.get(i));
//        }
    }

    @Override
    public void start(Stage stage) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000.0 / FPS), actionEvent -> {
            player.updatePosition(); // Update player's position
            for (int i = 0; i < bullets.size(); i++) {
                bullets.get(i).updatePosition();
                System.out.println(bullets.get(i));
            }

            if (!isAlive.get()) {  // If the player is dead
                Circle circle = new Circle((double) WIDTH / 2, (double) HEIGHT / 2, 75);    // Safe zone
                boolean newSafe = true;
                for (Particle asteroid : asteroids) {
                    if (Shape.intersect(circle, asteroid).getLayoutBounds().getWidth() != -1) {
                        newSafe = false;
                    }
                }
                if (newSafe) {  // If no asteroids in the spawn zone
                    isAlive.set(true);
                    player = new Particle(SVGconverter(shipFilePath), -90, 0, 0, FRICTION);
                    player.moveTo((double) WIDTH / 2, (double) HEIGHT / 2);
                    player.setFill(Color.TRANSPARENT);
                    player.setStroke(Color.WHITE);
                    root.getChildren().add(player);
                }
            }
            for (Particle asteroid : asteroids) {   // Update asteroids and check for collision
                asteroid.updatePosition();
                if (Shape.intersect(player, asteroid).getLayoutBounds().getWidth() != -1 && root.getChildren().contains(player)) {
                    explode();
                    if (HP <= 0) {  // You lost
                        gameOver();
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