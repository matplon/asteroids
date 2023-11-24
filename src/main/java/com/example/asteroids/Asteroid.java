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

    public double getCenterX(){
        double x1 = getPoints().get(0);
        double x2 = getPoints().get(2);
        double x3 = getPoints().get(4);
        return (x1+x2+x3)/3;
    }

    public double getCenterY(){
        double y1 = getPoints().get(1);
        double y2 = getPoints().get(3);
        double y3 = getPoints().get(5);
        return (y1+y2+y3)/3;
    }

    private void updateAngle(){
        angle += rotation;
    }

    private void moveVertices(){
        getPoints().setAll(centerX + Math.cos(Math.toRadians(angle)) * radius, centerY + Math.sin(Math.toRadians(angle)) * radius
                ,centerX + Math.cos(Math.toRadians(angle)) * radius, centerY - Math.sin(Math.toRadians(angle)) * radius,
                centerX - Math.cos(Math.toRadians(angle)) * radius, centerY - Math.sin(Math.toRadians(angle)) * radius,
                centerX - Math.cos(Math.toRadians(angle)) * radius, centerY + Math.sin(Math.toRadians(angle)) * radius);

    }

    private void rotate(double angle) {
        double centerX1 = getCenterX();
        double centerY1 = getCenterY();

        // Convert angle to radians
        double radianAngle = Math.toRadians(angle);

        // Apply rotation to each point
        for (int i = 0; i < getPoints().size(); i += 2) {
            double x = getPoints().get(i);
            double y = getPoints().get(i + 1);

            // Perform rotation
            double rotatedX = centerX1 + (x - centerX1) * Math.cos(radianAngle) - (y - centerY1) * Math.sin(radianAngle);
            double rotatedY = centerY1 + (x - centerX1) * Math.sin(radianAngle) + (y - centerY1) * Math.cos(radianAngle);

            // Update the coordinates
            getPoints().set(i, rotatedX);
            getPoints().set(i + 1, rotatedY);
        }
    }

    public void updatePosition(double FPS, double WINDOW_WIDTH, double WINDOW_HEIGHT){
        centerX += velocity.getX();
        centerY += velocity.getY();
        rotate(rotation);   // Apply rotation to the asteroid
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
