package com.example.MotorolaScienceCup.Asteroids;

import com.example.MotorolaScienceCup.Menu;
import com.example.MotorolaScienceCup.Particle;
import com.example.MotorolaScienceCup.Sound;
import com.example.MotorolaScienceCup.Util;
import com.example.MotorolaScienceCup.Vector;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class Main {
    static int WIDTH = Menu.WIDTH;
    static int HEIGHT = Menu.HEIGHT;
    final static double FPS = 60;
    final static double BULLET_SPEED = 15;
    final static double MAX_BULLET_DISTANCE = WIDTH * 0.6;
    static final double MAX_PARTICLE_DISTANCE = WIDTH * 0.05;

    static final double MAX_EXHAUST_DISTANCE = WIDTH * 0.002;

    static Clip playerEngine = null;

    static final double EXHAUST_SPEED = 1.5;
    static final double PARTICLE_COUNT = 15;
    static final double SPAWNZONE_RADIUS = 100;
    final static double LARGE_SAUCER_RADIUS = Player.PLAYER_RADIUS;
    static final double RESPAWN_COOLDOWN = 2;   // in seconds
    static final double SAUCER_COOLDOWN = 1;  // in seconds
    static AtomicBoolean isAlive = new AtomicBoolean(true);
    static List<Particle> bullets = new ArrayList<>();
    static List<Particle> particlesAll = new ArrayList<>();

    static List<Particle> exhaustAll = new ArrayList<>();
    static HashMap<Particle, Double> bulletsDistanceCovered = new HashMap<>();
    static HashMap<Particle, Double> particlesDistanceCovered = new HashMap<>();

    static HashMap<Particle, Double> exhaustDistanceCovered = new HashMap<>();
    static Timeline timeline;
    static Circle spawnZone;
    static String shipFilePath = "ship1.svg";
    static int HP = 1;
    static int LEVEL = 1;
    static int ASTEROID_COUNT = 4;
    static boolean canShoot = true;
    static int nextPointThreshold = 10000;
    static int respawnTimer = 0;
    static int saucerTimer = 3;
    static double enemyShootTimer = 0;
    static Player player;
    static List<Asteroid> asteroids = new ArrayList<>();


    static AnchorPane root = new AnchorPane();
    public static Scene scene = new Scene(root, WIDTH, HEIGHT);

    public static void init() throws UnsupportedAudioFileException, LineUnavailableException, IOException {

        resetData();
        scene.setFill(Color.BLACK);
        root.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(0), new Insets(0))));

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
            if (keyEvent.getCode() == KeyCode.E && isAlive.get())
                player.hyperSpace();   // Teleport (chance of exploding or colliding with an asteroid)
            if (keyEvent.getCode() == KeyCode.X && canShoot && bullets.size() <= 3 && isAlive.get()) {
                canShoot = false;
                try {
                    player.shootBullet();
                } catch (UnsupportedAudioFileException e) {
                    throw new RuntimeException(e);
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        scene.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.UP) player.stopAcceleration(); // Stop thrusting forward
            if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.LEFT)
                player.stopRotation(); // Stop rotating
            if (keyEvent.getCode() == KeyCode.X) canShoot = true;
        });

        // Spawn the big asteroids
        Asteroid.spawnAsteroids(ASTEROID_COUNT);

        HUD.init(highscore(), Util.SVGconverter("heart.svg"));

        start();
        Menu.stage.setScene(scene);
        Menu.stage.show();

        Clip clip = Menu.clips.get(0);
        clip.close();

    }


    public static void start() {
        timeline = new Timeline(new KeyFrame(Duration.millis(1000.0 / FPS), actionEvent -> {
            if (HP <= 0) {
                try {
                    gameOver();
                } catch (UnsupportedAudioFileException e) {
                    throw new RuntimeException(e);
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

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
            if (!Enemy.enemyList.isEmpty()) {
                Enemy enemy = Enemy.enemyList.get(0);
                if (enemy.getType() == 1 && enemyShootTimer == 90) {
                    Enemy.shootBullet();
                    enemyShootTimer = 0;
                } else if (enemy.getType() == 2 && enemyShootTimer == 60) {
                    Enemy.shootBullet();
                    enemyShootTimer = 0;
                }
            }
            Enemy.collisionDetection();
            Enemy.updateBullet();


            player.updatePosition();

            if(player.isAccelerating()&&isAlive.get()){
                double angle=-player.getAngle();
                if(player.getAngle()>0){
                    angle-=180;
                }else{
                    angle+=180;
                }
                player.exhaustParticles(3,exhaustAll,exhaustDistanceCovered,root,angle+Math.random()*10-5, player.getVelocity().getMagnitude()+6);
                if(playerEngine==null){
                    try {
                        playerEngine = Sound.getClip("asteroidsEngine.wav",1.0f);
                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                        throw new RuntimeException(e);
                    }
                    playerEngine.loop(Clip.LOOP_CONTINUOUSLY);
                    playerEngine.start();
                }

            }else{
                if(playerEngine!=null){
                    playerEngine.stop();
                    playerEngine=null;
                }
            }

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
            for (int i = 0; i < exhaustAll.size(); i++) {   // Update animation particles distances
                Particle particle = exhaustAll.get(i);
                double currentDistance = exhaustDistanceCovered.get(particle);
                exhaustDistanceCovered.remove(particle);
                particle.updatePosition();
                exhaustDistanceCovered.put(particle, currentDistance + EXHAUST_SPEED);
                if (exhaustDistanceCovered.get(particle) > MAX_EXHAUST_DISTANCE) {
                    root.getChildren().remove(particle);
                    exhaustDistanceCovered.remove(particle);
                    exhaustAll.remove(particle);
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
                    player.setVelocity(new Vector(0,0,0));
                    player.setFill(Color.TRANSPARENT);
                    player.setStroke(Color.WHITE);
                    root.getChildren().add(player);
                }
            }
            Asteroid.updateAndCheck();

        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


    }

    public static void resetData() {
        root = new AnchorPane();
        scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(Color.BLACK);
        root.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(0), new Insets(0))));
        timeline = new Timeline();
        isAlive = new AtomicBoolean(true);
        bullets = new ArrayList<>();
        particlesAll = new ArrayList<>();
        bulletsDistanceCovered = new HashMap<>();
        particlesDistanceCovered = new HashMap<>();
        HP = 3;
        LEVEL = 1;
        ASTEROID_COUNT = 4;
        canShoot = true;
        nextPointThreshold = 10000;
        respawnTimer = 0;
        saucerTimer = 0;
        enemyShootTimer = 0;
        Enemy.enemyList.clear();
        Enemy.enemyBullets.clear();
        asteroids.clear();
        bullets.clear();
    }

    public static void gameOver() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        timeline.stop();
        HUD.gameOver();

    }

    public static int highscore() {
        try {
            Scanner scanner = new Scanner(new File("highscore.txt"));
            if (scanner.hasNextLine()) {

                int highscore1 = Integer.parseInt(scanner.nextLine());

                return highscore1;
            }
            else {
                return 0;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}