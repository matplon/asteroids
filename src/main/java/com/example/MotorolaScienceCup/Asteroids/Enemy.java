package com.example.MotorolaScienceCup.Asteroids;

import com.example.MotorolaScienceCup.Particle;
import com.example.MotorolaScienceCup.Sound;
import com.example.MotorolaScienceCup.Vector;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.example.MotorolaScienceCup.Asteroids.Main.*;
import static com.example.MotorolaScienceCup.Util.SVGconverter;

public class Enemy extends Particle {
    static List<Enemy> enemyList = new ArrayList<>();
    static final double ENEMY_SPEED = 4;

    static Clip clip;
    private final HashMap<Integer, Integer> pointsMapping = new HashMap<>() {{
        put(2, 1000);
        put(1, 200);
    }};
    private final int type;

    static List<Particle> enemyBullets = new ArrayList<>();
    static HashMap<Particle, Double> enemyBulletDistanceCovered = new HashMap<>();



    public Enemy(List<Double> points, double angle, double speed, int type) {
        super(points, angle, 0, speed, 0);
        this.type = type;
    }

    private static double probability(){
        double baseProb = 0.001;
        double changeRate = 0.002;

        double adjustedProb = baseProb + ((double) HUD.getPoints() / 1000) * changeRate;
        adjustedProb =Math.max(0, Math.min(1, adjustedProb));

        return adjustedProb;
    }

    public static void spawnEnemy() {

        if (enemyList.isEmpty()&&Math.random()*10<1) {
            if(clip==null){
            try {
                clip = Sound.getClip("enemyAmbient.wav",1.0f);
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                throw new RuntimeException(e);
            }
            clip.loop(Clip.LOOP_CONTINUOUSLY);}
            double pick = Math.random();
            int type = 1;
            if (pick < probability()){
                type = 2;
            }
            Enemy enemy = new Enemy(SVGconverter("enemy" + type + ".svg"), 0, ENEMY_SPEED, type);
            if (enemy.getType() == 2) {
                enemy.setStroke(Color.RED);
                }
            boolean check = Math.random() < 0.5;
            double x;
            if (check) {
                x = 0 - enemy.getRadius();
                enemy.setAngle(0);
                enemy.setVelocity(new Vector(ENEMY_SPEED, 0));
            } else {
                x = WIDTH + enemy.getRadius();
                enemy.setAngle(180);
                enemy.setVelocity(new Vector(ENEMY_SPEED, 180));
            }
            if (enemy.getType() == 2) {
                enemy.setVelocity(new Vector(ENEMY_SPEED * 1, enemy.getAngle()));
            }
            double y = Math.random() * (HEIGHT - enemy.getRadius() - (0 + enemy.getRadius())) + 0 + enemy.getRadius();
            enemy.moveTo(x, y);
            enemy.scale(LARGE_SAUCER_RADIUS / enemy.radius);
            enemy.setFill(Color.TRANSPARENT);
            enemy.setStroke(Color.WHITE);
            root.getChildren().add(enemy);
                enemyList.add(enemy); //all
            }
    }

    static void updateEnemy(List<Double> rightDirections, List<Double> leftDirections) {
        if (!enemyList.isEmpty()) {
            Enemy enemy = enemyList.get(0);
            boolean goingRight = enemy.getAngle() < 90 && enemy.getAngle() > -90;
            double originalX = enemy.getCenterX();
            checkDirections(rightDirections, enemy);
            checkDirections(leftDirections, enemy);
            enemy.updatePosition();
            if ((goingRight && originalX > enemy.getCenterX()) || (!goingRight && originalX < enemy.getCenterX())) {
                clip.stop();
                clip = null;
                root.getChildren().remove(enemy);
                enemyList.remove(enemy);
                enemyShootTimer=0;
            }
        }
    }

    private static void checkDirections(List<Double> directions, Enemy enemy) {
        if (directions.contains(enemy.getAngle()) && Math.random() * 1000 < 10) {
            directions.remove(enemy.getAngle());
            int index = Math.random() < 0.5 ? 0 : 1;
            enemy.setVelocity(new Vector(ENEMY_SPEED, directions.get(index)));
            enemy.setAngle(directions.get(index));
        }
    }

    static void updateBullet() {
        for (Particle playerBullet : enemyBullets) {   // Update bullet distances
            double currentDistance = enemyBulletDistanceCovered.get(playerBullet);
            enemyBulletDistanceCovered.remove(playerBullet);
            playerBullet.updatePosition();
            enemyBulletDistanceCovered.put(playerBullet, currentDistance + BULLET_SPEED);
        }
        for (int i = 0; i < enemyBullets.size(); i++) {    // Delete bullets which have exceeded the max distance
            if (enemyBulletDistanceCovered.get(enemyBullets.get(i)) > Main.MAX_BULLET_DISTANCE) {
                root.getChildren().remove(enemyBullets.get(i));
                enemyBulletDistanceCovered.remove(enemyBullets.get(i));
                enemyBullets.remove(enemyBullets.get(i));
            }
        }
    }

    public static void shootBullet() {
        if (!enemyList.isEmpty()) {
            try {
                Sound.play("enemyShoot.wav");
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                throw new RuntimeException(e);
            }
            Enemy enemy = enemyList.get(0);
            List<Double> points = Arrays.asList(1.0, 1.0, 1.0, 5.0, 3.0, 5.0, 3.0, 1.0);    // Rectangle bullet
            double angle = Math.random() * 360 - 180;
            Particle bullet = new Particle(points, angle, 0, Main.BULLET_SPEED, 0);
            bullet.moveTo(enemy.getCenterX() + enemy.getRadius() * Math.cos(Math.toRadians(angle)), enemy.getCenterY() + enemy.getRadius() * Math.sin(Math.toRadians(angle)));
            if (enemy.getType() == 2) {
                bullet.setStroke(Color.RED);
                double dir = getDir(bullet);
                double offset = Math.random() * 10 - 5;
                offset = offset/LEVEL;
                dir = dir + offset;
                Vector vector = new Vector(BULLET_SPEED, dir);
                bullet.setVelocity(vector);
                bullet.setAngle(dir);
            }
            bullet.setFill(Color.WHITE);
            // Spawn the bullet at the nose of the ship

            enemyBullets.add(bullet);
            enemyBulletDistanceCovered.put(bullet, 0.0);


            Main.root.getChildren().add(bullet);
        }
    }

    private static double getDir(Particle bullet) {
        double x = player.getCenterX(), y = player.getCenterY();

        double a = Math.pow(player.getVelocity().getX(), 2) + Math.pow(player.getVelocity().getY(), 2) - Math.pow(BULLET_SPEED, 2);
        double b = 2 * (player.getVelocity().getX() * (player.getCenterX() - bullet.getCenterX()) +
                player.getVelocity().getY() * (player.getCenterY() - bullet.getCenterY()));
        double c = Math.pow(player.getCenterX() - bullet.getCenterX(), 2) + Math.pow(player.getCenterY() - bullet.getCenterY(), 2);

        double disc = Math.pow(b, 2) - 4 * a * c;
        if (disc >= 0) {
            double t1 = (-b + Math.sqrt(disc)) / (2 * a);
            double t2 = (-b - Math.sqrt(disc)) / (2 * a);
            double t = 0;
            if(t1 >= 0 && t1 < t2) t = t1;
            else if(t2 >= 0) t = t2;
            x = t * player.getVelocity().getX() + player.getCenterX();
            y = t * player.getVelocity().getY() + player.getCenterY();
        }
        return Math.toDegrees(Math.atan2(y - bullet.getCenterY(), x - bullet.getCenterX()));
    }

    public void checkForHits() {
        List<Particle> bulletsToRemove = new ArrayList<>();
        boolean playerKilled = false;

        // Check every bullet and asteroid for intersection
        for (int i = 0; i < enemyBullets.size(); i++) {
            if (isAlive.get() && enemyBullets.get(i).getLayoutBounds().intersects(player.getLayoutBounds())) {
                if (Shape.intersect(enemyBullets.get(i), player).getLayoutBounds().getWidth() >= 0) {
                    playerKilled = true;
                    if (!bulletsToRemove.contains(enemyBullets.get(i)))
                        bulletsToRemove.add(enemyBullets.get(i));
                }
            }
            for (int j = 0; j < Main.asteroids.size(); j++) {
                if (enemyBullets.get(i).getLayoutBounds().intersects(asteroids.get(j).getLayoutBounds())) {
                    if (Shape.intersect(enemyBullets.get(i), Main.asteroids.get(j)).getLayoutBounds().getWidth() > 0) {
                        if (!bulletsToRemove.contains(enemyBullets.get(i))) {    // Make sure that one bullet doesn't hit 2 asteroids
                            Main.asteroids.get(j).destroy(false);
                            bulletsToRemove.add(enemyBullets.get(i));
                        }
                    }
                }
            }
        }
        // Destroy bullets which hit the target
        for (Particle bullet :
                bulletsToRemove) {
            Main.root.getChildren().remove(bullet);
            enemyBullets.remove(bullet);
        }
        if (playerKilled)
            player.explode();
    }

    public void explode() {
        if (!enemyList.isEmpty()) {
            clip.stop();
            clip = null;
            try {
                Sound.play("asteroidsBoom.wav");
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                throw new RuntimeException(e);
            }
            HUD.addPoints(enemyList.get(0).pointsMapping.get(enemyList.get(0).type));
            enemyList.get(0).animationParticles(Main.PARTICLE_COUNT, Main.particlesAll, Main.particlesDistanceCovered, Main.root);
            root.getChildren().remove(enemyList.get(0));
            enemyList.remove(enemyList.get(0));
        }
    }

    static void collisionDetection() {
        if (!enemyList.isEmpty()) {
            Enemy enemy = enemyList.get(0);
            if (enemy.getLayoutBounds().intersects(player.getLayoutBounds())) {
                if (intersect(enemy, player).getLayoutBounds().getWidth() > 0) {
                    player.explode();
                    enemy.explode();
                }
            }
        }
    }

    public int getType() {
        return type;
    }

}
