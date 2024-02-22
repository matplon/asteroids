package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.Menu;
import com.example.MotorolaScienceCup.Util;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Main {
    final static int WIDTH = Menu.WIDTH;
    final static int HEIGHT = Menu.HEIGHT;
    final static double BULLET_SPEED = 15;
    final static double BULLET_RADIUS = 7;

    static Scene scene;
    static AnchorPane root;
    static Stage stage = Menu.stage;
    static Timeline timeline;
    static Color defaultPanelColor = Color.BLUE;
    static Color activePanelColor = Color.RED;
    static final double glowV = 0.9;
    static Player player;

    static List<Polyline> smallShape;
    static List<Polyline> bigShape;
    static List<Polyline> connectors;
    static List<Panel> panels;

    static boolean shoot;
    static boolean goRight;
    static boolean goLeft;
    static int LEVEL = 0;
    static int flippersNumber = 5;

    static double bigSideLength;

    static String bullet = "testoctagon.svg";
    static String testMap2 = "testsquareKTORYDZIALA.svg";
    static String testMap3 = "mapa 3.svg";
    static String testMap4 = "map4.svg";
    static String testShip = "ship1.svg";
    static double scale = 1;
    static double a = 1.02;

    public static void init() {
        connectors = new ArrayList<>();
        bigShape = new ArrayList<>();
        smallShape = new ArrayList<>();
        panels = new ArrayList<>();
        root = new AnchorPane();
        scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);
        scene.setFill(Color.BLACK);
        root.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(0), new Insets(0))));

        shoot = false;
        goLeft = false;
        goRight = false;

        Graphics.drawMap(testMap3, defaultPanelColor, 1);

        double bigSideLengthX = panels.get(0).getBigSide().getPoints().getFirst() - panels.get(0).getBigSide().getPoints().get(2);
        double bigSideLengthY = panels.get(0).getBigSide().getPoints().get(1) - panels.get(0).getBigSide().getPoints().getLast();
        bigSideLength = Math.sqrt(Math.pow(bigSideLengthX, 2) + Math.pow(bigSideLengthY, 2));

        player = new Player(Util.SVGconverter(testShip), panels.getFirst());
        player.scale(20 / player.getRadius());
        player.setStroke(Color.RED);
        player.setFill(Color.RED);
        player.moveTo(panels.getFirst().getRightSide().getPoints().get(2), panels.getFirst().getRightSide().getPoints().get(3));
        root.getChildren().add(player);

        double x1 = panels.get(0).getBigSide().getPoints().get(0);
        double x2 = panels.get(0).getBigSide().getPoints().get(2);
        List<Double> points = new ArrayList<>();
        points.add(x1);
        points.add(0.0);
        points.add(x2);
        points.add(0.0);
        points.add(x2);
        points.add(10.0);
        points.add(x1);
        points.add(10.0);

//        Flipper flipper = new Flipper(panels.get(14));
//        root.getChildren().add(flipper);
//        flipper.setStroke(Color.RED);
//        panels.get(14).addFlipper(flipper);
//        flippers.add(flipper);


        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.RIGHT) goRight = true;
            if (keyEvent.getCode() == KeyCode.LEFT) goLeft = true;
            if (keyEvent.getCode() == KeyCode.X) shoot = true;
            if(keyEvent.getCode() == KeyCode.S) timeline.stop();
            if(keyEvent.getCode() == KeyCode.R) timeline.play();
        });
        scene.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.RIGHT) goRight = false;
            if (keyEvent.getCode() == KeyCode.LEFT) goLeft = false;
            if (keyEvent.getCode() == KeyCode.X) shoot = false;
        });
        start();
//        nextLevel();
    }

    public static void start() {
        //Flipper.spawnSeeds(flippersNumber);
//        Tanker tanker = new Tanker(panels.get(1));
//        root.getChildren().add(tanker);
//        tanker.setStroke(Color.RED);

        Spiker spiker = new Spiker(panels.get(10));
        root.getChildren().add(spiker);

        timeline = new Timeline(new KeyFrame(Duration.millis((double) 1000 / Menu.FPS), actionEvent -> {
            highlightPanel(player);
            double bulletsNumber = 0;
            for (Panel panel : panels) {
                panel.update();
                bulletsNumber += panel.getPlayerBullets().size();
            }
            if (goRight) {
                player.move(false);
            }
            if (goLeft) {
                player.move(true);
            }
            if (shoot && bulletsNumber < 5) {
                player.shoot();
            }
            Player.shotTimer--;
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public static void gameOver(){
        System.out.println("You died");
    }

    public static void addPoints(){
        System.out.println("add points");
    }

    public static void nextLevel(){
        if(timeline != null) timeline.stop();
        timeline = new Timeline(new KeyFrame(Duration.millis((double) 1000 / Menu.FPS), actionEvent -> {
            root.getChildren().clear();
            Graphics.drawMap(testMap3, defaultPanelColor, scale);
            scale *= a;
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public static void highlightPanel(Player player) {
        if (player.getCurrentPanel().getLeftPanel().getColor() == Color.YELLOW){
            player.getCurrentPanel().getLeftPanel().changeColor(Color.BLUE);
        }
        if (player.getCurrentPanel().getRightPanel().getColor() == Color.YELLOW){
            player.getCurrentPanel().getRightPanel().changeColor(Color.BLUE);
        }

        player.getCurrentPanel().changeColor(Color.YELLOW);
    }
}