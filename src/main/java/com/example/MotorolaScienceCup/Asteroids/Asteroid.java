package com.example.MotorolaScienceCup.Asteroids;

import com.example.MotorolaScienceCup.*;
import com.example.MotorolaScienceCup.Vector;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.*;

public class Asteroid extends Particle {
    static final double BIG_ASTEROID_SPEED = 1.5;
    static final double BIG_ASTEROID_RADIUS = Main.WIDTH * 0.025;

    private boolean isHealing;

    private BetterPolygon cross;
    static final HashMap<Integer, Integer> pointsMapping = new HashMap<>() {{
        put(3, 20);
        put(2, 50);
        put(1, 100);
    }};
    private final int size;

    public Asteroid(List<Double> points, double angle, double speed, int size) {
        super(points, angle, 0, speed, 0);
        this.size = size;
    }

    public Asteroid(List<Double> points, double angle, double speed, int size, BetterPolygon cross) {
        super(points, angle, 0, speed, 0);
        this.size = size;
    }

    public void destroy(boolean destroyedByPlayer) {
        try {
            Sound.play("asteroidsBoom.wav");
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
        if (getSize() > 1) {   // Multiply only the big and medium asteroids
            // 2 smaller and faster asteroids from 1 asteroid

            int type1 = (int) (Math.random() * 4 + 1);
            int type2 = (int) (Math.random() * 4 + 1);
            List<Double> points1 = Util.SVGconverter("asteroidVar" + type1 + ".svg");
            List<Double> points2 = Util.SVGconverter("asteroidVar" + type2 + ".svg");
            Asteroid asteroid1 = new Asteroid(points1, 0, 0, getSize() - 1);
            asteroid1.setHealing(false);
            asteroid1.scale((getRadius()) * 0.8 / 45);
            asteroid1.moveTo(centerX, getCenterY());
            Asteroid asteroid2 = new Asteroid(points2, 0, 0, getSize() - 1);
            asteroid2.setHealing(false);
            asteroid2.moveTo(centerX, getCenterY());
            asteroid2.scale((getRadius()) * 0.8 / 45);
            asteroid1.setAngle(Math.random() * 360 - 180);
            asteroid2.setAngle(Math.random() * 360 - 180);
            asteroid1.setStroke(Color.WHITE);
            asteroid2.setStroke(Color.WHITE);
            asteroid1.setFill(Color.TRANSPARENT);
            asteroid2.setFill(Color.TRANSPARENT);

            // Random, higher velocity
            Random random = new Random();
            com.example.MotorolaScienceCup.Vector newVelocity1 = new Vector(random.nextDouble(getVelocity().getMagnitude() * 1.2, getVelocity().getMagnitude() * 2), asteroid1.getAngle());
            com.example.MotorolaScienceCup.Vector newVelocity2 = new Vector(random.nextDouble(getVelocity().getMagnitude() * 1.2, getVelocity().getMagnitude() * 2), asteroid2.getAngle());

            asteroid1.setVelocity(newVelocity1);
            asteroid2.setVelocity(newVelocity2);

            Main.asteroids.add(asteroid1);
            Main.asteroids.add(asteroid2);
            Main.root.getChildren().addAll(asteroid1, asteroid2);
        }
        // Add points for asteroid
        if (destroyedByPlayer) {
            if (this.isHealing) {
                HUD.addHeart();
                Main.HP++;
            }
            HUD.addPoints(pointsMapping.get(getSize()));
        }
        // Remove the big asteroid
        Main.root.getChildren().remove(this);
        if(this.isHealing){
            Main.root.getChildren().remove(this.getCross());
        }
        animationParticles(Main.PARTICLE_COUNT, Main.particlesAll, Main.particlesDistanceCovered, Main.root);
        Main.asteroids.remove(this);
    }

    public static void updateAndCheck() {
        HashMap<Asteroid, Boolean> asteroidsToDestroy = new HashMap<>();

        for (int i = 0; i < Main.asteroids.size(); i++) {   // Update asteroids and check for collision
            Main.asteroids.get(i).updatePosition();
            if(Main.asteroids.get(i).isHealing){
                Main.asteroids.get(i).getCross().moveTo(Main.asteroids.get(i).getCenterX(), Main.asteroids.get(i).getCenterY());
            }
            if (Main.isAlive.get() && Main.player.getLayoutBounds().intersects(Main.asteroids.get(i).getLayoutBounds())){
                if (intersect(Main.player, Main.asteroids.get(i)).getLayoutBounds().getWidth() > 0 && Main.root.getChildren().contains(Main.player)) {
                    Main.player.explode();
                    asteroidsToDestroy.put(Main.asteroids.get(i), true);
                }
            }
            if (!Enemy.enemyList.isEmpty()) {
                if (Enemy.enemyList.get(0).getLayoutBounds().intersects(Main.asteroids.get(i).getLayoutBounds())) {
                    if (intersect(Enemy.enemyList.get(0), Main.asteroids.get(i)).getLayoutBounds().getWidth() > 0 && Main.root.getChildren().contains(Enemy.enemyList.get(0))) {
                        Enemy.enemyList.get(0).animationParticles(Main.PARTICLE_COUNT, Main.particlesAll, Main.particlesDistanceCovered, Main.root);
                        Main.enemyShootTimer = 0;
                        Main.root.getChildren().remove(Enemy.enemyList.get(0));
                        Enemy.enemyList.get(0).explode();
                        asteroidsToDestroy.put(Main.asteroids.get(i), false);
                    }
                }
            }
        }
        for (Map.Entry<Asteroid, Boolean> entry : asteroidsToDestroy.entrySet()) {
            entry.getKey().destroy(entry.getValue());
        }
    }

    static void spawnAsteroids(int count) {
        int healingIndex = -1;
        if(Math.random()<0.5){
            healingIndex = new Random().nextInt(count);
        }
        for (int i = 0; i < count; i++) {
            int type = (int) (Math.random() * 4 + 1);
            Asteroid asteroid = new Asteroid(Util.SVGconverter("asteroidVar" + type + ".svg"), Math.random() * 360 - 180, BIG_ASTEROID_SPEED, 3);
            asteroid.setStroke(Color.WHITE);
            asteroid.setFill(Color.TRANSPARENT);
            asteroid.scale(BIG_ASTEROID_RADIUS / asteroid.getRadius());
            if(i==healingIndex){
                asteroid.setHealing(true);
                asteroid.setCross(new BetterPolygon(Util.SVGconverter("heart.svg")));
                asteroid.getCross().setStroke(Color.RED);
                asteroid.getCross().setFill(Color.RED);
                asteroid.getCross().scale(0.7);
                asteroid.setStroke(Color.RED);
            }

            // Spawn asteroids outside the spawn zone
            do {
                asteroid.moveTo(Math.random() * Main.WIDTH, Math.random() * Main.HEIGHT);

            } while (Shape.intersect(asteroid, Main.spawnZone).getLayoutBounds().getWidth() > 0);
            if(asteroid.isHealing){
                asteroid.getCross().moveTo(asteroid.getCenterX(), asteroid.getCenterY());
                Main.root.getChildren().add(asteroid.getCross());
            }
            Main.asteroids.add(asteroid);
            Main.root.getChildren().add(asteroid);
        }
    }

    public int getSize() {
        return size;
    }

    public boolean isHealing() {
        return isHealing;
    }

    public void setHealing(boolean healing) {
        isHealing = healing;
    }

    public BetterPolygon getCross() {
        return cross;
    }

    public void setCross(BetterPolygon cross) {
        this.cross = cross;
    }
}
