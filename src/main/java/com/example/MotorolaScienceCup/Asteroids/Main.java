package com.example.MotorolaScienceCup.Asteroids;

import com.example.MotorolaScienceCup.Menu;
import com.example.MotorolaScienceCup.Util;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
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


public class Main {

    static Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    final static int WIDTH = (int) screenBounds.getWidth();
    final static int HEIGHT = (int) screenBounds.getHeight();
    final static double FPS = 60;
    final static double BULLET_SPEED = 15;
    final static double MAX_BULLET_DISTANCE = WIDTH * 0.6;
    static final double MAX_PARTICLE_DISTANCE = WIDTH * 0.05;
    static final double PARTICLE_COUNT = 15;
    static final double SPAWNZONE_RADIUS = 100;
    final static double LARGE_SAUCER_RADIUS = Player.PLAYER_RADIUS;
    static final double RESPAWN_COOLDOWN = 2;   // in seconds
    static final double SAUCER_COOLDOWN = 1;  // in seconds
    static AtomicBoolean isAlive = new AtomicBoolean(true);
    static List<Particle> bullets = new ArrayList<>();
    static List<Particle> particlesAll = new ArrayList<>();
    static HashMap<Particle, Double> bulletsDistanceCovered = new HashMap<>();
    static HashMap<Particle, Double> particlesDistanceCovered = new HashMap<>();
    static Timeline timeline;
    static Circle spawnZone;
    static String shipFilePath = "ship1.svg";
    static int HP = 3;
    static int LEVEL = 1;
    static int ASTEROID_COUNT = 4;
    static boolean canShoot = true;
    static int nextPointThreshold = 10000;
    static int respawnTimer = 0;
    static int saucerTimer = 0;
    static double enemyShootTimer = 0;
    static Player player;
    static List<Asteroid> asteroids;

    static Scene scene = Menu.scene;
    static AnchorPane root = Menu.root;
    static Stage stage = Menu.stage;


    public static void init() {

        root = new AnchorPane();
        scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(Color.BLACK);

        // Spawn the player
        player = new Player(Util.SVGconverter(shipFilePath), -90, 0, 0);
        player.moveTo((double) WIDTH / 2, (double) HEIGHT / 2);
        player.setFill(Color.TRANSPARENT);
        player.setStroke(Color.WHITE);
        root.getChildren().add(player);
        player.scale(Player.PLAYER_RADIUS / player.getRadius());

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
        Asteroid.spawnAsteroids(ASTEROID_COUNT);

        HUD.init(0, Util.SVGconverter(shipFilePath));

        start();

    }


    public static void start() {
        timeline = new Timeline(new KeyFrame(Duration.millis(1000.0 / FPS), actionEvent -> {
            if (HP <= 0)
                gameOver();

            if (!isAlive.get())
                respawnTimer++;

            if (Enemy.enemyList.isEmpty())
                saucerTimer++;

            if (asteroids.isEmpty() && Enemy.enemyList.isEmpty()) {
                LEVEL++;
                ASTEROID_COUNT = (int) Math.round(ASTEROID_COUNT * 1.5);
                player.moveTo((double) WIDTH / 2, (double) HEIGHT / 2);
                for (Particle particle :
                        particlesAll) {
                    root.getChildren().remove(particle);
                }
                particlesAll.clear();
                timeline.stop();
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(pauseEvent -> {
                    Asteroid.spawnAsteroids(ASTEROID_COUNT);
                    saucerTimer = 0;
                    timeline.play();
                });
                pause.play();
            }

            if (HUD.getPoints() >= nextPointThreshold) {
                HUD.addHeart();
                nextPointThreshold += 10000;
            }

            List<Double> leftDirections = new ArrayList<>(Arrays.asList(-45.0, 0.0, 45.0));
            List<Double> rightDirections = new ArrayList<>(Arrays.asList(-135.0, 180.0, 135.0));

            if (saucerTimer >= SAUCER_COOLDOWN * FPS) {
                saucerTimer = 0;
                Enemy.spawnEnemy();
            }
            enemyShootTimer++;
            Enemy.updateEnemy(leftDirections, rightDirections);
            if(!Enemy.enemyList.isEmpty()){
                Enemy enemy = Enemy.enemyList.get(0);
                if(enemy.getType() == 1 && enemyShootTimer == 90){
                    Enemy.shootBullet();
                    enemyShootTimer = 0;
                }else if(enemy.getType() == 2 && enemyShootTimer == 60){
                    Enemy.shootBullet();
                    enemyShootTimer = 0;
                }
            }
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
                    bullets.remove(bullets.get(i));
                }
            }
            for (int i = 0; i < particlesAll.size(); i++) {   // Update animation particles distances
                Particle particle = particlesAll.get(i);
                double currentDistance = particlesDistanceCovered.get(particle);
                particlesDistanceCovered.remove(particle);
                particle.updatePosition();
                particlesDistanceCovered.put(particle, currentDistance + Particle.ANIMATION_PARTICLE_SPEED);
                if (particlesDistanceCovered.get(particle) > MAX_PARTICLE_DISTANCE) {
                    root.getChildren().remove(particle);
                    particlesDistanceCovered.remove(particle);
                    particlesAll.remove(particle);
                }
            }
            player.checkForHits();
            if (!Enemy.enemyList.isEmpty())
                Enemy.enemyList.get(0).checkForHits();

            if (!isAlive.get() && HP > 0 && respawnTimer >= RESPAWN_COOLDOWN * FPS) {  // If the player is dead check for asteroids in the spawn zone
                respawnTimer = 0;
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

        stage.setScene(scene);
        stage.show();
    }

    public static void gameOver() {
        HUD.gameOver();
    }

}