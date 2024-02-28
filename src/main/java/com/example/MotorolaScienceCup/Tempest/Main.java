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
import javafx.scene.shape.Rectangle;
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
    static int flippersNumber = 0;
    static int tankersNumber = 0;
    static int spikersNumber = 1;

    static double bigSideLength;

    static String bullet = "testoctagon.svg";
    static String testMap2 = "testsquareKTORYDZIALA.svg";
    static String testMap3 = "mapa 3.svg";
    static String testMap4 = "map4.svg";
    static String testShip = "ship1.svg";
    static double scale = 1;
    static double a = 1.017;

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

        Graphics.drawMap("mapa 3.svg", defaultPanelColor, 1);

        double bigSideLengthX = panels.get(0).getBigSide().getPoints().getFirst() - panels.get(0).getBigSide().getPoints().get(2);
        double bigSideLengthY = panels.get(0).getBigSide().getPoints().get(1) - panels.get(0).getBigSide().getPoints().getLast();
        bigSideLength = Math.sqrt(Math.pow(bigSideLengthX, 2) + Math.pow(bigSideLengthY, 2));

        player = new Player(panels.getFirst());
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


        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.RIGHT) goRight = true;
            if (keyEvent.getCode() == KeyCode.LEFT) goLeft = true;
            if (keyEvent.getCode() == KeyCode.X) shoot = true;
            if (keyEvent.getCode() == KeyCode.S) timeline.stop();
            if (keyEvent.getCode() == KeyCode.R) timeline.play();
        });
        scene.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.RIGHT) goRight = false;
            if (keyEvent.getCode() == KeyCode.LEFT) goLeft = false;
            if (keyEvent.getCode() == KeyCode.X) shoot = false;
        });
        start();
    }

    public static void start() {

        Enemy.spawnSeeds(flippersNumber, tankersNumber, spikersNumber);

        timeline = new Timeline(new KeyFrame(Duration.millis((double) 1000 / Menu.FPS), actionEvent -> {
            highlightPanel(player);
            double bulletsNumber = 0;
            for (Panel panel : panels) {
                panel.update(false);
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
            if (!Enemy.seedsDone) {
                Enemy.updateSeeds();
            }
            player.shotTimer--;
            if (isLevelFinished()) nextLevel();
//            for (int i = 0; i < player.getCurrentPanel().getFlippers().size(); i++) {
//                System.out.println(player.getCurrentPanel().getFlippers().get(i).reachedTheEdge);
//            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public static void gameOver() {
        System.out.println("you died");
    }

    public static void temp(){
        timeline.stop();
        root.getChildren().clear();
    }

    public static void addPoints() {
        System.out.println("add points");
    }

    public static boolean isLevelFinished() {
        if(!Enemy.seedsDone) return false;
        for (Panel panel : panels) {
            if (!panel.getTankers().isEmpty()) return false;
            for (Spiker spiker : panel.getSpikers()) {
                if (!spiker.isDead) return false;
            }
            for (Flipper flipper : panel.getFlippers()) {
                if (!flipper.reachedTheEdge) return false;
            }
        }
        return true;
    }

    public static void nextLevel() {
        timeline.stop();
        timeline = new Timeline(new KeyFrame(Duration.millis((double) 1000 / Menu.FPS), actionEvent -> {
            root.getChildren().clear();
            Graphics.drawMap(testMap3, defaultPanelColor, scale);
            for (Panel panel : panels){
                if(panel.spikerLine != null){
                    root.getChildren().add(panel.spikerLine);
                }
                for(Player.Bullet bullet1 : panel.getPlayerBullets()){
                    root.getChildren().add(bullet1);
                    System.out.println(bullet1.h);
                }
            }
            scale *= a;
            player.moveDown();
            highlightPanel(player);

            double bulletsNumber = 0;
            for (Panel panel : panels) {
                panel.update(true);
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
            player.shotTimer--;
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public static void highlightPanel(Player player) {
        if (player.getCurrentPanel().getLeftPanel().getColor() == Color.YELLOW) {
            player.getCurrentPanel().getLeftPanel().changeColor(Color.BLUE);
        }
        if (player.getCurrentPanel().getRightPanel().getColor() == Color.YELLOW) {
            player.getCurrentPanel().getRightPanel().changeColor(Color.BLUE);
        }
        player.getCurrentPanel().changeColor(Color.YELLOW);
    }
}