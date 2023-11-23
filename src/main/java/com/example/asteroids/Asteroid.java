package com.example.asteroids;

import javafx.scene.shape.Polygon;


public class Asteroid extends Polygon {
    private double centerX, centerY, radius, angle, rotation;
    private Vector velocity;

    public Asteroid(double centerX, double centerY, double radius, double rotation, double velocity, double angle){
        super(centerX + Math.cos(Math.toRadians(angle)) * radius, centerY + Math.sin(Math.toRadians(angle)) * radius
                ,centerX + Math.cos(Math.toRadians(angle)) * radius, centerY - Math.sin(Math.toRadians(angle)) * radius,
                centerX - Math.cos(Math.toRadians(angle)) * radius, centerY - Math.sin(Math.toRadians(angle)) * radius,
                centerX - Math.cos(Math.toRadians(angle)) * radius, centerY + Math.sin(Math.toRadians(angle)) * radius
        );
        this.centerX = centerX;
        this.centerY = centerY;
        this.angle = angle;
        this.velocity = new Vector(velocity, angle);
        this.radius = radius;
        this.rotation = rotation;
    }

    private void rotate(){
        angle += rotation;
    }

    private void moveVertices(){
        getPoints().setAll(centerX + Math.cos(Math.toRadians(angle)) * radius, centerY + Math.sin(Math.toRadians(angle)) * radius
                ,centerX + Math.cos(Math.toRadians(angle)) * radius, centerY - Math.sin(Math.toRadians(angle)) * radius,
                centerX - Math.cos(Math.toRadians(angle)) * radius, centerY - Math.sin(Math.toRadians(angle)) * radius,
                centerX - Math.cos(Math.toRadians(angle)) * radius, centerY + Math.sin(Math.toRadians(angle)) * radius);

    }

    public void updatePosition(double FPS, double WINDOW_WIDTH, double WINDOW_HEIGHT){
        centerX += velocity.getX();
        centerY += velocity.getY();
        rotate();   // Apply rotation to the asteroid
        if(centerX < 0 - radius){
            centerX = WINDOW_WIDTH + radius;
        }
        else if(centerX > WINDOW_WIDTH + radius){
            centerX = 0 - radius;
        }
        if(centerY < 0 - radius){
            centerY = WINDOW_HEIGHT + radius;
        }
        else if(centerY > WINDOW_HEIGHT + radius){
            centerY = 0 - radius;
        }
        moveVertices();
    }


}
