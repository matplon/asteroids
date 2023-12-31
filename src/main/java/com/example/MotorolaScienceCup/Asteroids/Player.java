package com.example.MotorolaScienceCup.Asteroids;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Player extends Particle {
    private final double ROTATION_SPEED = 270;
    private final double SHIP_TERMINAL_VELOCITY = 10;
    private final double SHIP_THRUST = 4;
    final static double PLAYER_RADIUS = Asteroid.BIG_ASTEROID_RADIUS * 0.5;
    static final double FRICTION = 0.7;
    public Player(List<Double> points, double angle, double rotation, double velocity) {
        super(points, angle, rotation, velocity, FRICTION);
        this.thrust = SHIP_THRUST;
        this.terminalVelocity = SHIP_TERMINAL_VELOCITY;
    }

    public void hyperSpace() {
        Random random = new Random();
        int randomEvenInteger = random.nextInt((62 / 2) + 1) * 2;   // Random even number from 0 to 62
        if (randomEvenInteger >= Main.asteroids.size() + 44) {
            explode();
        } else {
            centerX = getCenterX();
            centerY = getCenterY();
            moveTo(Math.random() * WINDOW_WIDTH, Math.random() * WINDOW_HEIGHT);    // Teleport
        }
    }

    public void shootBullet() {
        List<Double> points = Arrays.asList(1.0, 1.0, 1.0, 5.0, 3.0, 5.0, 3.0, 1.0);    // Rectangle bullet
        Particle bullet = new Particle(points, -getAngle(), 0, Main.BULLET_SPEED, 0);
        bullet.setFill(Color.WHITE);
        // Spawn the bullet at the nose of the ship
        bullet.moveTo(getCenterX() + getRadius() * Math.cos(Math.toRadians(-getAngle())), getCenterY() + getRadius() * Math.sin(Math.toRadians(-getAngle())));
        Main.bullets.add(bullet);
        Main.bulletsDistanceCovered.put(bullet, 0.0);

        Main.root.getChildren().add(bullet);
    }

    public void checkForHits() {
        List<Particle> bulletsToRemove = new ArrayList<>();

        // Check every bullet and asteroid for intersection
        for (int i = 0; i < Main.bullets.size(); i++) {
            if (!bulletsToRemove.contains(Main.bullets.get(i)) && Main.isAlive.get() && Main.bullets.get(i).getLayoutBounds().intersects(this.getLayoutBounds())) {
                if (intersect(Main.bullets.get(i), this).getLayoutBounds().getWidth() > 0) {
                    bulletsToRemove.add(Main.bullets.get(i));
                    explode();
                }
            }
            if (!bulletsToRemove.contains(Main.bullets.get(i)) && !Enemy.enemyList.isEmpty() && Main.bullets.get(i).getLayoutBounds().intersects(Enemy.enemyList.get(0).getLayoutBounds())) {
                if (intersect(Main.bullets.get(i), Enemy.enemyList.get(0)).getLayoutBounds().getWidth() > 0) {
                    bulletsToRemove.add(Main.bullets.get(i));
                    Enemy.explode();
                    continue;
                }
            }
            for (int j = 0; j < Main.asteroids.size(); j++) {
                if (!bulletsToRemove.contains(Main.bullets.get(i)) && Main.bullets.get(i).getLayoutBounds().intersects(Main.asteroids.get(j).getLayoutBounds())) {
                    if (intersect(Main.bullets.get(i), Main.asteroids.get(j)).getLayoutBounds().getWidth() > 0) {
                        Main.asteroids.get(j).destroy(true);
                        bulletsToRemove.add(Main.bullets.get(i));
                    }
                }
            }
        }
        // Destroy bullets which hit the target
        for (Particle bullet :
                bulletsToRemove) {
            Main.root.getChildren().remove(bullet);
            Main.bullets.remove(bullet);
        }
    }


    public void explode() {
        Main.isAlive.set(false);
        Main.HP--;
        HUD.removeHeart();
        Main.root.getChildren().remove(this);
        animationParticles();
    }

    public void accelerate() {
        isThrusting = true;
    }

    public void stopAcceleration() {
        isThrusting = false;
    }

    public void setRotationRight() {
        rotation = -ROTATION_SPEED / FPS;
    }

    public void setRotationLeft() {
        rotation = ROTATION_SPEED / FPS;
    }

}
