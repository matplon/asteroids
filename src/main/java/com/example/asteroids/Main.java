package com.example.asteroids;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class Main extends Application {

    static Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    final static int WIDTH = (int) screenBounds.getWidth();
    final static int HEIGHT = (int) screenBounds.getHeight();
    final static double FPS = 60;
    final int INIT_ASTEROID_COUNT = 15;
    final double FRICTION = 0.7;
    final static double BULLET_SPEED = 15;
    static final double PARTICLE_SPEED = 1;
    final static double SHIP_TERMINAL_VELOCITY = 10;
    final static double MAX_BULLET_DISTANCE = WIDTH * 0.6;

    final double MAX_PARTICLE_DISTANCE = WIDTH * 0.05;

    static final double PARTICLE_COUNT = 15;
    final double BIG_ASTEROID_SPEED = 1;
    final double SPAWNZONE_RADIUS = 100;

    static final double ENEMY_SPEED = 4;
    final static double PLAYER_RADIUS = 20;
    final double BIG_ASTEROID_RADIUS = 45;


    static AtomicBoolean isAlive = new AtomicBoolean(true);
    static List<Particle> bullets = new ArrayList<>();

    static List<Particle> particlesAll = new ArrayList<>();

    static HashMap<Particle, Double> bulletsDistanceCovered = new HashMap<>();

    static HashMap<Particle, Double> particlesDistanceCovered = new HashMap<>();

    static Timeline timeline;

    Circle spawnZone;
    String shipFilePath = "ship1.svg";

    static String enemyFilePath = "enemy1.svg";


    static int HP = 3;
    boolean canShoot = true;
    int nextPointThreshold = 10000;


    static AnchorPane root;
    static Scene scene;
    static Stage stage1;
    static Player player;
    static List<Asteroid> asteroids;

    public static void main(String[] args) {
        launch();
    }

    public void init() {
        System.out.println(screenBounds.getHeight());
        System.out.println(screenBounds.getWidth());
        root = new AnchorPane();
        scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(Color.BLACK);

        // Spawn the player
        player = new Player(Util.SVGconverter(shipFilePath), -90, 0, 0, FRICTION);
        player.moveTo((double) WIDTH / 2, (double) HEIGHT / 2);
        player.setFill(Color.TRANSPARENT);
        player.setStroke(Color.WHITE);
        root.getChildren().add(player);
        player.scale(PLAYER_RADIUS / player.getRadius());

        spawnZone = new Circle((double) WIDTH / 2, (double) HEIGHT / 2, SPAWNZONE_RADIUS);

        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.UP) player.accelerate();   // Thrust forward
            if (keyEvent.getCode() == KeyCode.RIGHT) player.setRotationRight();   // Rotate right
            if (keyEvent.getCode() == KeyCode.LEFT) player.setRotationLeft(); // Rotate left
            if (keyEvent.getCode() == KeyCode.E)
                player.hyperSpace();   // Teleport (chance of exploding or colliding with an asteroid)
            if (keyEvent.getCode() == KeyCode.X && canShoot && bullets.size() <= 3 && isAlive.get()) {
                canShoot = false;
                player.shootBullet();
            }
        });

        scene.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.UP) player.stopAcceleration(); // Stop thrusting forward
            if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.LEFT)
                player.stopRotation(); // Stop rotating
            if (keyEvent.getCode() == KeyCode.X) canShoot = true;
        });

        // Spawn the big asteroids
        asteroids = new ArrayList<>();
        for (int i = 0; i < INIT_ASTEROID_COUNT; i++) {
            int type = (int) (Math.random() * 4 + 1);
            Asteroid asteroid = new Asteroid(Util.SVGconverter("asteroidVar" + type + ".svg"), Math.random() * 360 - 180, BIG_ASTEROID_SPEED, 3);
            asteroid.setStroke(Color.WHITE);
            asteroid.setFill(Color.TRANSPARENT);
            asteroid.scale(BIG_ASTEROID_RADIUS / asteroid.getRadius());

            // Spawn asteroids outside the spawn zone
            do {
                asteroid.moveTo(Math.random() * WIDTH, Math.random() * HEIGHT);

            } while (Shape.intersect(asteroid, spawnZone).getLayoutBounds().getWidth() > 0);

            asteroids.add(asteroid);
            root.getChildren().add(asteroid);
        }

        HUD.init(0, Util.SVGconverter(shipFilePath));
    }

    @Override
    public void start(Stage stage) {
        timeline = new Timeline(new KeyFrame(Duration.millis(1000.0 / FPS), actionEvent -> {
            if (HP <= 0)
                gameOver();

                if (HUD.getPoints() >= nextPointThreshold) {
                    HUD.addHeart();
                    nextPointThreshold += 10000;
                }

            List<Double> leftDirections = new ArrayList<>(Arrays.asList(-45.0, 0.0, 45.0));
            List<Double> rightDirections = new ArrayList<>(Arrays.asList(-135.0, 180.0, 135.0));

            Enemy.spawnEnemy();
            Enemy.updateEnemy(leftDirections, rightDirections);
            Enemy.shootBullet();
            Enemy.collisionDetection();
            Enemy.updateBullet();


            player.updatePosition(); // Update player's position

            for (Particle playerBullet : bullets) {   // Update bullet distances
                double currentDistance = bulletsDistanceCovered.get(playerBullet);
                bulletsDistanceCovered.remove(playerBullet);
                playerBullet.updatePosition();
                bulletsDistanceCovered.put(playerBullet, currentDistance + BULLET_SPEED);
            }
            for (int i = 0; i < bullets.size(); i++) {    // Delete bullets which have exceeded the max distance
                if (bulletsDistanceCovered.get(bullets.get(i)) > MAX_BULLET_DISTANCE) {
                    root.getChildren().remove(bullets.get(i));
                    bulletsDistanceCovered.remove(bullets.get(i));
                    bullets.remove(bullets.get(i)); //commit
                }
            }
            for (int i = 0; i < particlesAll.size(); i++) {   // Update animation particles distances
                Particle particle = particlesAll.get(i);
                double currentDistance = particlesDistanceCovered.get(particle);
                particlesDistanceCovered.remove(particle);
                particle.updatePosition();
                particlesDistanceCovered.put(particle, currentDistance + PARTICLE_SPEED);
                if (particlesDistanceCovered.get(particle) > MAX_PARTICLE_DISTANCE) {
                    root.getChildren().remove(particle);
                    particlesDistanceCovered.remove(particle);
                    particlesAll.remove(particle);
                }
            }
            player.checkForHits();
            if (!Enemy.enemyList.isEmpty())
                Enemy.enemyList.get(0).checkForHits();

            if (!isAlive.get() && HP > 0) {  // If the player is dead check for asteroids in the spawn zone
                boolean newSafe = true;
                for (Particle asteroid : asteroids) {
                    if (Shape.intersect(spawnZone, asteroid).getLayoutBounds().getWidth() > 0) {
                        newSafe = false;
                    }
                }
                if (newSafe) {  // If no asteroids in the spawn zone
                    isAlive.set(true);
                    player.moveTo((double) WIDTH / 2, (double) HEIGHT / 2);
                    player.setFill(Color.TRANSPARENT);
                    player.setStroke(Color.WHITE);
                    root.getChildren().add(player);
                }
            }
            Asteroid.updateAndCheck();

        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        stage1 = new Stage();
        stage = stage1;
        stage.setScene(scene);
        stage.show();
    }

    void addPoint(Enemy enemyShip) {
        if (enemyShip.getType() == 1) HUD.addPoints(200);
        else HUD.addPoints(1000);
    }


    public static void gameOver() {
        HUD.gameOver();
    }

}