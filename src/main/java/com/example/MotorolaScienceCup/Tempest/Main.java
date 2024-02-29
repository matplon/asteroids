package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.Menu;
import com.example.MotorolaScienceCup.Vector;
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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.*;

public class Main {
    final static double WIDTH = Menu.WIDTH;
    final static double HEIGHT = Menu.HEIGHT;
    final static double BULLET_SPEED = 15;
    final static double BULLET_RADIUS = 7;

    static Scene scene;
    static AnchorPane root;
    static Stage stage = Menu.stage;
    static Timeline timeline;
    static Color defaultPanelColor = Color.BLUE;
    static Color activePanelColor = new Color(1.0 - defaultPanelColor.getRed(), 1.0 -defaultPanelColor.getGreen(), 1.0 - defaultPanelColor.getBlue(), 1.0);
    static final double glowV = 0.9;
    static Player player;
    static double nextLevelSpikelineSpeed = 8;

    static List<Polyline> smallShape;
    static List<Polyline> bigShape;
    static List<Polyline> connectors;
    static List<Panel> panels;
    static List<Panel> tempPanels;

    static boolean shoot;
    static boolean goRight;
    static boolean goLeft;
    static int LEVEL = 1;
    static int flippersNumber = 1;
    static int tankersNumber = 0;
    static int spikersNumber = 1;
    static int fuseballNumber = 0;

    static int maxFlipper = 4;
    static int maxTanker = 4;
    static int maxSpiker = 4;
    static int maxFuseball = 4;

    static double bigSideLength;
    static String map;

    static List<String> maps = Arrays.asList("map1.svg", "map2.svg", "map3.svg", "map4.svg", "map5.svg", "map6.svg", "map7.svg", "map8.svg", "map9.svg", "map10.svg",
            "map11.svg", "map12.svg", "map13.svg", "map14.svg");
    static HashMap<Integer, Boolean> isMapOpen = new HashMap<>() {{
        put(0, false);
        put(1, false);
        put(2, false);
        put(3, false);
        put(4, false);
        put(5, false);
        put(6, true);
        put(7, false);
        put(8, true);
        put(9, false);
        put(10, true);
        put(11, true);
        put(12, true);
        put(13, true);
    }};

    static double scale = 1;
    static double a = 1.006;
    static double mapSpeed = 4;
    static double mapDistanceCovered = 0;

    public static void init() {
        if(timeline != null){
            timeline.stop();
        }
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

        if(LEVEL <= 13){
            map = "map"+LEVEL+".svg";
        }
        else{
            Random random = new Random();
            map = "map"+random.nextInt(1, 15)+".svg";
            defaultPanelColor = new Color(Math.random(), Math.random(), Math.random(), 1.0);
            activePanelColor = new Color(1.0 - defaultPanelColor.getRed(), 1.0 -defaultPanelColor.getGreen(), 1.0 - defaultPanelColor.getBlue(), 1.0);
        }

        Graphics.drawMap(map, defaultPanelColor, 1);

        double bigSideLengthX = panels.get(0).getBigSide().getPoints().get(0) - panels.get(0).getBigSide().getPoints().get(2);
        double bigSideLengthY = panels.get(0).getBigSide().getPoints().get(1) - panels.get(0).getBigSide().getPoints().get(3);
        bigSideLength = Math.sqrt(Math.pow(bigSideLengthX, 2) + Math.pow(bigSideLengthY, 2));

        player = new Player(panels.get(0));
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
        for(Clip clip:Menu.clips){
            clip.close();
        }
    }

    public static void start() {

        Enemy.spawnSeeds(flippersNumber, tankersNumber, spikersNumber, fuseballNumber);

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
                player.shoot(false);
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

    public static void addPoints() {
        System.out.println("add points");
    }

    public static boolean isLevelFinished() {
        if(!Enemy.seedsDone) return false;
        for (Panel panel : panels) {
            if (!panel.getTankers().isEmpty()) return false;
            if(!panel.getSpikers().isEmpty()) return false;
            for (Flipper flipper : panel.getFlippers()) {
                if (!flipper.reachedTheEdge) return false;
            }
        }
        return true;
    }

    public static void nextLevel() {
        timeline.stop();
        removeAllEnemies();
        mapDistanceCovered = 0;
        tempPanels = new ArrayList<>(panels);
        timeline = new Timeline(new KeyFrame(Duration.millis((double) 1000 / Menu.FPS), actionEvent -> {
            root.getChildren().clear();
            root.getChildren().add(player);
            for (Panel panel : tempPanels){
                if(panel.spikerLine != null) root.getChildren().add(panel.spikerLine);
                for (Player.Bullet bullet1 : panel.playerBullets){
                    root.getChildren().add(bullet1);
                }
            }
            Graphics.drawMap(map, defaultPanelColor, scale);
            highlightPanel(player);
            if(isMapOpen.get(map.indexOf(map))){
                scale *= a;
            }
            else{
                moveMap();
            }
            double bulletsNumber = 0;
            for (Panel panel : tempPanels) {
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
                player.shoot(true);
            }
            player.shotTimer--;
        }));
        Text back = new Text("←");
        back.setFill(Color.RED);
        back.setFont(Font.font("Public Pixel", 40));
        back.setLayoutX(300);
        back.setLayoutY(100);
        back.setOnMouseClicked(mouseEvent -> {
            try {
                Menu.resetMenu();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        });
        root.getChildren().add(back);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

    public static void newLevel(){
        init();
    }

    public static void highlightPanel(Player player) {
        if (player.getCurrentPanel().getLeftPanel() != null && player.getCurrentPanel().getLeftPanel().getColor() == activePanelColor) {
            player.getCurrentPanel().getLeftPanel().changeColor(defaultPanelColor);
        }
        if (player.getCurrentPanel().getRightPanel() != null && player.getCurrentPanel().getRightPanel().getColor() == activePanelColor) {
            player.getCurrentPanel().getRightPanel().changeColor(defaultPanelColor);
        }
        player.getCurrentPanel().changeColor(activePanelColor);
    }

    public static void removeAllEnemies(){
        List<Flipper> flippersToDestroy = new ArrayList<>();
        List<Tanker> tankersToDestroy = new ArrayList<>();
        List<Fuseball> fuseballsToDestroy = new ArrayList<>();
        for (Panel panel : panels){
            for (Flipper flipper : panel.getFlippers()) flippersToDestroy.add(flipper);
            for (Tanker tanker : panel.getTankers()) tankersToDestroy.add(tanker);
            for (Fuseball fuseball : panel.getFuseBalls()) fuseballsToDestroy.add(fuseball);
        }
        for(Flipper flipper : flippersToDestroy) flipper.currentPanel.getFlippers().remove(flipper);
        for(Tanker tanker : tankersToDestroy) tanker.currentPanel.getTankers().remove(tanker);
        for (Fuseball fuseball : fuseballsToDestroy) fuseball.currentPanel.getFuseBalls().remove(fuseball);
    }

    public static void moveMap(){
        Vector vector = new Vector(mapSpeed, 90);
        for (Panel panel : panels){
            moveLine(panel.smallSide, vector);
            moveLine(panel.bigSide, vector);
            moveLine(panel.leftSide, vector);
            moveLine(panel.rightSide, vector);
        }
        mapDistanceCovered += mapSpeed;
    }

    public static void moveLine(Polyline line, Vector vector){
        List<Double> tempPoints = line.getPoints();
        line.getPoints().setAll(tempPoints.get(0) + vector.getX(), tempPoints.get(1) + vector.getY(),
                tempPoints.get(2) + vector.getX(), tempPoints.get(3) + vector.getY());
    }
}