package com.example.asteroids;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.util.*;

public class Asteroid extends Particle{
    HashMap<Integer, Integer> pointsMapping = new HashMap<>() {{
        put(3, 20);
        put(2, 50);
        put(1, 100);
    }};
    private int size;

    public Asteroid(List<Double> points, double angle, double speed, int size){
        super(points, angle, 0, speed, 0);
        this.size = size;
    }

    public void destroy(boolean destroyedByPlayer){
        if (getSize() > 1.1) {   // Multiply only the big and medium asteroids
            // 2 smaller and faster asteroids from 1 asteroid

            int type1 = (int) (Math.random() * 4 + 1);
            int type2 = (int) (Math.random() * 4 + 1);
            List<Double> points1 = Util.SVGconverter("asteroidVar" + type1 + ".svg");
            List<Double> points2 = Util.SVGconverter("asteroidVar" + type2 + ".svg");
            Asteroid asteroid1 = new Asteroid(points1, 0, 0, getSize() - 1);
            asteroid1.scale((getRadius()) * 0.8 / 45);
            asteroid1.moveTo(centerX, getCenterY());
            Asteroid asteroid2 = new Asteroid(points2, 0, 0, getSize() - 1);
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
            Vector newVelocity1 = new Vector(random.nextDouble(getVelocity().getMagnitude() * 1.2, getVelocity().getMagnitude() * 2), asteroid1.getAngle());
            Vector newVelocity2 = new Vector(random.nextDouble(getVelocity().getMagnitude() * 1.2, getVelocity().getMagnitude() * 2), asteroid2.getAngle());

            asteroid1.setVelocity(newVelocity1);
            asteroid2.setVelocity(newVelocity2);

            Main.asteroids.add(asteroid1);
            Main.asteroids.add(asteroid2);
            Main.root.getChildren().addAll(asteroid1, asteroid2);
        }
        // Add points for asteroid
        if(destroyedByPlayer)
            HUD.addPoints(pointsMapping.get(getSize()));

        // Remove the big asteroid
        Main.root.getChildren().remove(this);
        animationParticles();
        Main.asteroids.remove(this);
    }

    public static void updateAndCheck(){
        HashMap<Asteroid, Boolean> asteroidsToDestroy = new HashMap<>();

        for (int i = 0; i < Main.asteroids.size(); i++) {   // Update asteroids and check for collision
            Main.asteroids.get(i).updatePosition();
            if(Main.player.getLayoutBounds().intersects(Main.asteroids.get(i).getLayoutBounds())){
                if (Shape.intersect(Main.player, Main.asteroids.get(i)).getLayoutBounds().getWidth() > 0 && Main.root.getChildren().contains(Main.player)) {
                    Main.player.explode();
                    asteroidsToDestroy.put(Main.asteroids.get(i), true);
                }
            }
            if(!Enemy.enemyList.isEmpty()){
                if(Enemy.enemyList.get(0).getLayoutBounds().intersects(Main.asteroids.get(i).getLayoutBounds())){
                    if(Shape.intersect(Enemy.enemyList.get(0), Main.asteroids.get(i)).getLayoutBounds().getWidth() > 0 && Main.root.getChildren().contains(Enemy.enemyList.get(0))){
                        Enemy.enemyList.get(0).animationParticles();
                        Main.root.getChildren().remove(Enemy.enemyList.get(0));
                        Enemy.enemyList.remove(Enemy.enemyList.get(0));
                        asteroidsToDestroy.put(Main.asteroids.get(i), false);
                    }
                }
            }
        }
        for (Map.Entry<Asteroid, Boolean> entry : asteroidsToDestroy.entrySet()) {
            entry.getKey().destroy(entry.getValue());
        }
    }

    public int getSize() {
        return size;
    }
}
