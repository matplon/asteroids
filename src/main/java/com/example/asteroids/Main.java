package com.example.asteroids;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class Main extends Application {

    final static int WIDTH = 1800;
    final static int HEIGHT = 900;
    final static double FPS = 60;
    final int INIT_ASTEROID_COUNT = 15;
    final double FRICTION = 0.7;
    final double BULLET_SPEED = 15;
    static final double PARTICLE_SPEED = 1;
    final static double SHIP_TERMINAL_VELOCITY = 10;
    final double MAX_BULLET_DISTANCE = WIDTH * 0.6;

    final double MAX_PARTICLE_DISTANCE = WIDTH * 0.05;

    static final double PARTICLE_COUNT = 15;
    final double BIG_ASTEROID_SPEED = 1;
    final double SPAWNZONE_RADIUS = 100;

    final double ENEMY_SPEED = 4;
    final double PLAYER_RADIUS = 20;
    final double BIG_ASTEROID_RADIUS = 45;

    public Integer points = 0;


    static AtomicBoolean isAlive = new AtomicBoolean(true);
    List<Particle> playerBullets = new ArrayList<>();

    static List<Particle> particlesAll = new ArrayList<>();

    HashMap<Particle, Double> bulletsDistanceCovered = new HashMap<>();

    static HashMap<Particle, Double> particlesDistanceCovered = new HashMap<>();

    List<Particle> enemyList = new ArrayList<>();
    Circle spawnZone;
    String shipFilePath = "ship1.svg";

    String enemyFilePath = "enemy1.svg";


    static int HP = 3;
    boolean canShoot = true;


    static AnchorPane root;
    Scene scene;
    static Particle player;
    public static List<Asteroid> asteroids;

    public static void main(String[] args) {
        launch();
    }

    public void init() {
        root = new AnchorPane();
        scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(Color.BLACK);

        // Spawn the player
        player = new Particle(Util.SVGconverter(shipFilePath), -90, 0, 0, FRICTION);
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
            if (keyEvent.getCode() == KeyCode.X && canShoot && playerBullets.size() <= 3 && isAlive.get()) {
                canShoot = false;
                shootBullet(player);
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

        ChangeListener<Integer> pointsListener = (observableValue, integer, t1) -> {
            if (integer.intValue() % 100 == 0 && integer.intValue() > 0)
                HUD.addHeart();
        };
    }

    @Override
    public void start(Stage stage) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000.0 / FPS), actionEvent -> {

            if (Math.random() * FPS * 300 < 1000 && enemyList.size() < 1) {
                Particle enemy = new Particle(Util.SVGconverter(enemyFilePath), -90, 0, ENEMY_SPEED, 0);
                boolean check = Math.random() < 0.5;
                double x = 0;
                if (check) {
                    x = 0 - enemy.getRadius();
                    enemy.setVelocity(new Vector(ENEMY_SPEED, 0));
                } else {
                    x = WIDTH + enemy.getRadius();
                    enemy.setVelocity(new Vector(ENEMY_SPEED, 180));
                }
                double y = Math.random() * (HEIGHT - enemy.getRadius() - (0 + enemy.getRadius())) + 0 + enemy.getRadius();
                enemy.moveTo(x, y);
                enemy.setFill(Color.TRANSPARENT);
                enemy.setStroke(Color.WHITE);
                root.getChildren().add(enemy);
                enemy.scale(PLAYER_RADIUS / player.getRadius());
                enemyList.add(enemy); //all
            }

            if (enemyList.size() > 0) {
                enemyList.get(0).updatePosition();
            }

            player.updatePosition(); // Update player's position

            for (Particle playerBullet : playerBullets) {   // Update bullet distances
                double currentDistance = bulletsDistanceCovered.get(playerBullet);
                bulletsDistanceCovered.remove(playerBullet);
                playerBullet.updatePosition();
                bulletsDistanceCovered.put(playerBullet, currentDistance + BULLET_SPEED);
            }
            for (int i = 0; i < playerBullets.size(); i++) {    // Delete bullets which have exceeded the max distance
                if (bulletsDistanceCovered.get(playerBullets.get(i)) > MAX_BULLET_DISTANCE) {
                    root.getChildren().remove(playerBullets.get(i));
                    bulletsDistanceCovered.remove(playerBullets.get(i));
                    playerBullets.remove(playerBullets.get(i)); //commit
                }
            }
            for (int i = 0; i < particlesAll.size(); i++) {   // Update bullet distances
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
            checkForHits();

            if (!isAlive.get()) {  // If the player is dead check for asteroids in the spawn zone
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
            for (Particle asteroid : asteroids) {   // Update asteroids and check for collision
                asteroid.updatePosition();
                if (Shape.intersect(player, asteroid).getLayoutBounds().getWidth() > 0 && root.getChildren().contains(player)) {
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


    public static void explode() {
        isAlive.set(false);
        HP--;
        root.getChildren().remove(player);
        spawnParticle(player);
        HUD.removeHeart();
    }

    public void gameOver() {
        System.out.println("Game Over");
    }

    public void moveEnemy(Particle particle) {

    }

    public void checkForHits() {
        List<Particle> bulletsToRemove = new ArrayList<>();

        // Check every bullet and asteroid for intersection
        for (int i = 0; i < playerBullets.size(); i++) {
            for (int j = 0; j < asteroids.size(); j++) {
                if (Shape.intersect(playerBullets.get(i), asteroids.get(j)).getLayoutBounds().getWidth() > 0) {
                    if (!bulletsToRemove.contains(playerBullets.get(i))) {    // Make sure that one bullet doesn't hit 2 asteroids
                        destroyAsteroid(asteroids.get(j));
                        bulletsToRemove.add(playerBullets.get(i));

                    }
                }
            }
        }
        // Destroy bullets which hit the target
        for (Particle bullet :
                bulletsToRemove) {
            root.getChildren().remove(bullet);
            playerBullets.remove(bullet);
        }
    }

    public void destroyAsteroid(Asteroid asteroid) {
        if (asteroid.getSize() > 1.1) {   // Multiply only the big and medium asteroids
            // 2 smaller and faster asteroids from 1 asteroid

            int type1 = (int) (Math.random() * 4 + 1);
            int type2 = (int) (Math.random() * 4 + 1);
            List<Double> points1 = Util.SVGconverter("asteroidVar" + type1 + ".svg");
            List<Double> points2 = Util.SVGconverter("asteroidVar" + type2 + ".svg");
            Asteroid asteroid1 = new Asteroid(points1, 0, 0, asteroid.getSize() - 1);
            asteroid1.scale((asteroid.getRadius()) * 0.8 / 45);
            asteroid1.moveTo(asteroid.centerX, asteroid.getCenterY());
            Asteroid asteroid2 = new Asteroid(points2, 0, 0, asteroid.getSize() - 1);
            asteroid2.moveTo(asteroid.centerX, asteroid.getCenterY());
            asteroid2.scale((asteroid.getRadius()) * 0.8 / 45);
            asteroid1.setAngle(Math.random() * 360 - 180);
            asteroid2.setAngle(Math.random() * 360 - 180);
            asteroid1.setStroke(Color.WHITE);
            asteroid2.setStroke(Color.WHITE);
            asteroid1.setFill(Color.TRANSPARENT);
            asteroid2.setFill(Color.TRANSPARENT);

            // Random, higher velocity
            Random random = new Random();
            Vector newVelocity1 = new Vector(random.nextDouble(asteroid.getVelocity().getMagnitude() * 1.2, asteroid.getVelocity().getMagnitude() * 2), asteroid1.getAngle());
            Vector newVelocity2 = new Vector(random.nextDouble(asteroid.getVelocity().getMagnitude() * 1.2, asteroid.getVelocity().getMagnitude() * 2), asteroid2.getAngle());

            asteroid1.setVelocity(newVelocity1);
            asteroid2.setVelocity(newVelocity2);

            asteroids.add(asteroid1);
            asteroids.add(asteroid2);
            root.getChildren().addAll(asteroid1, asteroid2);
        }
        // Remove the big asteroid
        root.getChildren().remove(asteroid);
        spawnParticle(asteroid);
        asteroids.remove(asteroid);
    }

    public void shootBullet(Particle particle) {
        List<Double> points = Arrays.asList(1.0, 1.0, 1.0, 5.0, 3.0, 5.0, 3.0, 1.0);    // Rectangle bullet
        Particle bullet = new Particle(points, -particle.getAngle(), 0, BULLET_SPEED, 0);
        bullet.setFill(Color.WHITE);
        // Spawn the bullet at the nose of the ship
        bullet.moveTo(particle.getCenterX() + particle.getRadius() * Math.cos(Math.toRadians(-particle.getAngle())), particle.getCenterY() + particle.getRadius() * Math.sin(Math.toRadians(-particle.getAngle())));
        playerBullets.add(bullet);
        bulletsDistanceCovered.put(bullet, 0.0);

        root.getChildren().add(bullet);
    }

    public static void spawnParticle(Particle particle) {
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            List<Double> points = Arrays.asList(1.0, 1.0, 1.0, 5.0, 3.0, 5.0, 3.0, 1.0);    // Rectangle particle
            double angle = Math.random() * 360 - 180;
            Particle particle1 = new Particle(points, angle, 0, PARTICLE_SPEED * (Math.random() * 0.75 + 0.5), 0);
            particle1.setFill(Color.WHITE);
            particle1.moveTo(particle.getCenterX() + particle.getRadius() * Math.cos(Math.toRadians(angle)) * (Math.random() * 0.75 + 0.5), particle.getCenterY() + particle.getRadius() * Math.sin(Math.toRadians(angle)) * (Math.random() * 0.75 + 0.5));
            particlesAll.add(particle1);
            particlesDistanceCovered.put(particle1, 0.0);

            root.getChildren().add(particle1);
        }
    }


}