package com.example.asteroids;

import javafx.scene.paint.Color;

import java.util.List;
import java.util.Objects;

import static com.example.asteroids.Main.*;
import static com.example.asteroids.Main.ENEMY_SPEED;
import static com.example.asteroids.Util.SVGconverter;

public class Enemy extends Particle{
    private int type;


    public Enemy(List<Double> points, double angle, double speed, int type){
        super(points, angle, 0, speed, 0);
        this.type = type;
    }

    public static void spawnEnemy(){
        if(Math.random() * Main.FPS * 300 < 1000 && enemyList.size() < 1) {
            int type = Math.random() < 0.5 ? 1 : 2;
            Enemy enemy = new Enemy(SVGconverter(enemyFilePath), 0, Main.ENEMY_SPEED, type);
            boolean check = Math.random() < 0.5;
            double x = 0;
            if(check) {
                x = 0 - enemy.getRadius();
                enemy.setAngle(0);
                enemy.setVelocity(new Vector(ENEMY_SPEED, 0));
                System.out.println(enemy.getAngle());
            }else {
                x = WIDTH + enemy.getRadius();
                enemy.setAngle(180);
                enemy.setVelocity(new Vector(ENEMY_SPEED, 180));
                System.out.println(enemy.getAngle());
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

    public static void updateEnemy(List<Double> rightDirections, List<Double> leftDirections){
        if(enemyList.size() > 0){
            Enemy enemy = enemyList.get(0);
            if(rightDirections.contains(enemy.getAngle()) && Math.random() * 1000 < 10 ){
                List<Double> newDir = rightDirections;
                newDir.remove(newDir.indexOf(enemy.getAngle()));
                int index = Math.random() < 0.5 ? 0 : 1;
                enemy.setVelocity(new Vector(ENEMY_SPEED, newDir.get(index)));
                enemy.setAngle(newDir.get(index));
            }
            if(leftDirections.contains(enemy.getAngle()) && Math.random() * 1000 < 10){
                List<Double> newDir = leftDirections;
                newDir.remove(newDir.indexOf(enemy.getAngle()));
                int index = Math.random() <0.5 ? 0 : 1;
                enemy.setVelocity(new Vector(ENEMY_SPEED, newDir.get(index)));
                enemy.setAngle(newDir.get(index));
            }
            enemy.updatePosition();
        }
    }










    public int getType() {
        return type;
    }

}
