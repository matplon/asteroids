package com.example.asteroids;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.example.asteroids.Main.*;
import static com.example.asteroids.Main.ENEMY_SPEED;
import static com.example.asteroids.Util.SVGconverter;

public class Enemy extends Particle {
    private final int type;
    static List<Enemy> enemyList = new ArrayList<>();
    private static List<Particle> enemyBullets = new ArrayList<>();
    static HashMap<Particle, Double> enemyBulletDistanceCovered = new HashMap<>();
    HashMap<Integer, Integer> pointsMapping = new HashMap<>() {{
        put(2, 1000);
        put(1, 200);
    }};


    public Enemy(List<Double> points, double angle, double speed, int type) {
        super(points, angle, 0, speed, 0);
        this.type = type;
    }

    public static void spawnEnemy() {
        if (Math.random() * Main.FPS * 300 < 1000 && enemyList.isEmpty()) {
            int type = Math.random() < 0.5 ? 1 : 2;
            Enemy enemy = new Enemy(SVGconverter("enemy" + "1" + ".svg"), 0, Main.ENEMY_SPEED, type);
            if(enemy.getType() == 2){
                System.out.println("lol");
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
            if(enemy.getType()==2){
                enemy.setVelocity(new Vector(ENEMY_SPEED*1, enemy.getAngle()));
            }
            double y = Math.random() * (HEIGHT - enemy.getRadius() - (0 + enemy.getRadius())) + 0 + enemy.getRadius();
            enemy.moveTo(x, y);
            enemy.setFill(Color.TRANSPARENT);
            enemy.setStroke(Color.WHITE);
            root.getChildren().add(enemy);
            enemy.scale(PLAYER_RADIUS / player.getRadius());
            enemyList.add(enemy); //all
        }
    }

    public void updateEnemy(List<Double> rightDirections, List<Double> leftDirections) {
        if (!enemyList.isEmpty()) {
            Enemy enemy = enemyList.get(0);
            boolean goingRight = getAngle() < 90 && getAngle() > -90;
            double originalX = getCenterX();
            if (rightDirections.contains(getAngle()) && Math.random() * 1000 < 10) {
                rightDirections.remove(getAngle());
                int index = Math.random() < 0.5 ? 0 : 1;
                setVelocity(new Vector(enemy.getVelocity().getMagnitude(), rightDirections.get(index)));
                setAngle(rightDirections.get(index));
            }
            if (leftDirections.contains(getAngle()) && Math.random() * 1000 < 10) {
                leftDirections.remove(getAngle());
                int index = Math.random() < 0.5 ? 0 : 1;
                setVelocity(new Vector(enemy.getVelocity().getMagnitude(), leftDirections.get(index)));
                setAngle(leftDirections.get(index));
            }
            updatePosition();
            if ((goingRight && originalX > getCenterX()) || (!goingRight && originalX < getCenterX())) {
                root.getChildren().remove(this);
                enemyList.remove(this);
            }
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

    public void shootBullet() {
        if (enemyBullets.isEmpty() && !enemyList.isEmpty()) {
            Enemy enemy = enemyList.get(0);
            List<Double> points = Arrays.asList(1.0, 1.0, 1.0, 5.0, 3.0, 5.0, 3.0, 1.0);    // Rectangle bullet
            double angle = Math.random() * 360 - 180;
            Particle bullet = new Particle(points, angle, 0, Main.BULLET_SPEED, 0);
            if(enemy.getType() == 2){
                bullet.setStroke(Color.RED);
                double x = player.getCenterX() - enemy.getCenterX();
                double y = player.getCenterY() - enemy.getCenterY();
                System.out.println(x/y + " lol");
                double dir = Math.toDegrees(Math.atan2(x,y))/2;
                System.out.println(dir + " lmao");
                Vector vector = new Vector(BULLET_SPEED, dir);
                bullet.setVelocity(vector);
            }
            bullet.setFill(Color.WHITE);
            // Spawn the bullet at the nose of the ship
            bullet.moveTo(getCenterX() + getRadius() * Math.cos(Math.toRadians(angle)), getCenterY() + getRadius() * Math.sin(Math.toRadians(angle)));
            enemyBullets.add(bullet);
            enemyBulletDistanceCovered.put(bullet, 0.0);


            Main.root.getChildren().add(bullet);
        }
    }

    public void checkForHits() {
        List<Particle> bulletsToRemove = new ArrayList<>();
        boolean playerKilled = false;

        // Check every bullet and asteroid for intersection
        for (int i = 0; i < enemyBullets.size(); i++) {
            if (!playerKilled && enemyBullets.get(i).getLayoutBounds().intersects(player.getLayoutBounds())) {
                if(Shape.intersect(enemyBullets.get(i), player).getLayoutBounds().getWidth() >= 0){
                    playerKilled = true;
                    if(!bulletsToRemove.contains(enemyBullets.get(i)))
                        bulletsToRemove.add(enemyBullets.get(i));
                }
            }
            for (int j = 0; j < Main.asteroids.size(); j++) {
                if(enemyBullets.get(i).getLayoutBounds().intersects(asteroids.get(j).getLayoutBounds())){
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
        if(playerKilled)
            player.explode();
    }

    public static void explode(){
        if(!enemyList.isEmpty()){
            HUD.addPoints(enemyList.get(0).pointsMapping.get(enemyList.get(0).type));
            enemyList.get(0).animationParticles();
            root.getChildren().remove(enemyList.get(0));
            enemyList.remove(enemyList.get(0));
        }
    }

    public void collisionDetection(){
        if(!enemyList.isEmpty()){
            if(getLayoutBounds().intersects(player.getLayoutBounds())){
                if(Shape.intersect(this, player).getLayoutBounds().getWidth() > 0){
                    player.explode();
                    explode();
                }
            }
        }
    }

    public int getType() {
        return type;
    }

}
