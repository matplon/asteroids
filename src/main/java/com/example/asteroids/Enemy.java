package com.example.asteroids;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import static com.example.asteroids.Main.*;
import static com.example.asteroids.Main.ENEMY_SPEED;
import static com.example.asteroids.Util.SVGconverter;

public class Enemy extends Particle{
    private final int type;
    private static List<Enemy> enemyList = new ArrayList<>();


    public Enemy(List<Double> points, double angle, double speed, int type){
        super(points, angle, 0, speed, 0);
        this.type = type;
    }

    public static void spawnEnemy(){
        if(Math.random() * Main.FPS * 300 < 1000 && enemyList.isEmpty()) {
            int type = Math.random() < 0.5 ? 1 : 2;
            Enemy enemy = new Enemy(SVGconverter(enemyFilePath), 0, Main.ENEMY_SPEED, type);
            boolean check = Math.random() < 0.5;
            double x;
            if(check) {
                x = 0 - enemy.getRadius();
                enemy.setAngle(0);
                enemy.setVelocity(new Vector(ENEMY_SPEED, 0));
            }else {
                x = WIDTH + enemy.getRadius();
                enemy.setAngle(180);
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
    }

    public static void updateEnemy(List<Double> rightDirections, List<Double> leftDirections){
        if(!enemyList.isEmpty()){
            Enemy enemy = enemyList.get(0);
            boolean goingRight = enemy.getAngle() < 90 && enemy.getAngle() > -90;
            double originalX = enemy.getCenterX();
            if(rightDirections.contains(enemy.getAngle()) && Math.random() * 1000 < 10 ){
                rightDirections.remove(enemy.getAngle());
                int index = Math.random() < 0.5 ? 0 : 1;
                enemy.setVelocity(new Vector(ENEMY_SPEED, rightDirections.get(index)));
                enemy.setAngle(rightDirections.get(index));
            }
            if(leftDirections.contains(enemy.getAngle()) && Math.random() * 1000 < 10){
                leftDirections.remove(enemy.getAngle());
                int index = Math.random() <0.5 ? 0 : 1;
                enemy.setVelocity(new Vector(ENEMY_SPEED, leftDirections.get(index)));
                enemy.setAngle(leftDirections.get(index));
            }
            enemy.updatePosition();
            if((goingRight && originalX > enemy.getCenterX()) || (!goingRight && originalX < enemy.getCenterX())){
                root.getChildren().remove(enemy);
                enemyList.remove(enemy);
            }
        }
    }

    public int getType() {
        return type;
    }

}
