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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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

    private Polygon convertSVGPathToPolygon(String svgPathData) {
        Polygon polygon = new Polygon();

        // Use Batik's PathParser to parse SVG path data
        PathParser pathParser = new PathParser();
        AWTPathProducer pathProducer = new AWTPathProducer(new Path2D.Double());
        pathParser.setPathHandler(pathProducer);

        pathParser.parse(svgPathData);

        // Get the path from the producer
        Path2D path = (Path2D) pathProducer.getShape();

        // Extract the coordinates from the path and add them to the Polygon
        double[] coords = new double[6];
        for (Path2D.PathIterator iterator = path.getPathIterator(null); !iterator.isDone(); iterator.next()) {
            int type = iterator.currentSegment(coords);
            switch (type) {
                case Path2D.SEG_MOVETO:
                case Path2D.SEG_LINETO:
                    polygon.getPoints().addAll(coords[0], coords[1]);
                    break;
                case Path2D.SEG_CLOSE:
                    // Close the path if needed
                    polygon.getPoints().addAll(polygon.getPoints().get(0), polygon.getPoints().get(1));
                    break;
            }
        }

        return polygon;
    }

    public void init() {
        root = new AnchorPane();
        scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(Color.BLACK);

        player = new Particle(CENTERX, CENTERY, RADIUS, 0, true);
        player.setFill(Color.TRANSPARENT);
        player.setStroke(Color.WHITE);
//        root.getChildren().add(player);

        SVGPath svgPath = new SVGPath();
        svgPath.setContent("m 106.74569,134.60057 -29.195403,12.77299 2.737069,27.37069 23.721264,15.51006 9.57974,-14.14153 8.66739,11.40446 10.94827,-16.87859 -9.12356,-20.9842 z");

        Polygon polygon = convertSVGPathToPolygon(svgPath);
        polygon.setStroke(Color.WHITE);
        root.getChildren().add(polygon);


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

//        for (int i = 0; i < ASTEROID_COUNT; i++) {
//            asteroids.add(new Particle(WIDTH/2, HEIGHT/2, 150, 20, Math.random() * 15,
//                    90, false));
//            asteroids.get(i).setStroke(Color.WHITE);
//            root.getChildren().add(asteroids.get(i));
//        }

    }

    public void gameLogic() {

    }

    @Override
    public void start(Stage stage) {
//        AtomicBoolean isSafe = new AtomicBoolean(true);
//        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000.0 / FPS), actionEvent -> {
//            player.updatePosition(FPS, WIDTH, HEIGHT); // Update player's position
//            if (!isSafe.get()) {
//                Circle circle = new Circle(WIDTH / 2, HEIGHT / 2, 10);
//                boolean newSafe = true;
//                for (int i = 0; i < asteroids.size(); i++) {
//                    if (!asteroids.get(i).intersects(circle.getBoundsInLocal())) {
//                        if (asteroids.get(i).intersects(circle.getBoundsInLocal())) {
//                            newSafe = false;
//                        }
//                    }
//                }
//                if (newSafe) {
//                    System.out.println("lol");
//                    isSafe.set(true);
//                    player = new Particle(WIDTH / 2, HEIGHT / 2, RADIUS, 0, true);
//                    player.setFill(Color.TRANSPARENT);
//                    player.setStroke(Color.WHITE);
//                    root.getChildren().add(player);
//                }
//            }
//            for (int i = 0; i < asteroids.size(); i++) {
//                asteroids.get(i).updatePosition(FPS, WIDTH, HEIGHT);
//                if (asteroids.get(i).intersects(player.getBoundsInLocal())) {
//                    HP--;
//                    root.getChildren().remove(player);
//                    if (HP <= 0) {
//
//                    } else {
//                        isSafe.set(false);
//                    }
//                }
//            }
//
//            asteroids.get(0).updatePosition(FPS, WIDTH, HEIGHT);
//            System.out.println(asteroids.get(0));
//
//        }));
//        timeline.setCycleCount(Animation.INDEFINITE);
//        timeline.play();

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}