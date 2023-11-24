package com.example.asteroids;

import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

public class Player extends Polygon {
    private final double ROTATION_SPEED = 360;    // Degrees/second
    private final double THRUST = 5;    // Pixels/second
    private final double FRICTION = 0.7;    // Coefficient of friction
    private double centerX;
    private double centerY;
    private double angle;
    private double rotation;
    private final double radius;
    private Vector velocity;
    private boolean isThrusting = false;

    public Player(double centerX, double centerY, double radius, double angle){
        super(centerX+(4.0/3)*radius*Math.cos(Math.toRadians(angle)), centerY-(4.0/3)*radius*Math.sin(Math.toRadians(angle)),   // Nose of the ship
                centerX-radius*((2.0/3)*Math.cos(Math.toRadians(angle))+Math.sin(Math.toRadians(angle))),   // Rear left X
                centerY+radius*((2.0/3)*Math.sin(Math.toRadians(angle))-Math.cos(Math.toRadians(angle))),   // Rear left Y
                centerX-radius*((2.0/3)*Math.cos(Math.toRadians(angle))-Math.sin(Math.toRadians(angle))),   // Rear right X
                centerY+radius*((2.0/3)*Math.sin(Math.toRadians(angle))+Math.cos(Math.toRadians(angle)))    // Rear right Y
        );
        this.centerX = centerX;
        this.centerY = centerY;
        this.angle = angle;
        this.rotation = 0;
        this.radius = radius;
        this.velocity = new Vector(0, 0, angle);
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

    public void updatePosition(double FPS, double WINDOW_WIDTH, double WINDOW_HEIGHT){
        updateAngle();   // Apply the rotation to the "angle" variable
        rotate(-rotation);   // Rotate the ship
        if(isThrusting){
            velocity.setX(velocity.getX()+THRUST*Math.cos(Math.toRadians(angle))/FPS);  // Update X component of velocity
            velocity.setY(velocity.getY()-THRUST*Math.sin(Math.toRadians(angle))/FPS);  // Update Y component of velocity
        }
        else{
            velocity.setX(velocity.getX()-FRICTION*velocity.getX()/FPS);    // Apply friction to X component of velocity
            velocity.setY(velocity.getY()-FRICTION*velocity.getY()/FPS);    // Apply friction to Y component of velocity
        }
        centerX = getCenterX();
        centerY = getCenterY();
        for (int i = 0; i < getPoints().size(); i+=2) {
            getPoints().set(i, getPoints().get(i)+velocity.getX()); // Apply velocity to the X coordinate of the vertex
            getPoints().set(i+1, getPoints().get(i+1)+velocity.getY()); // Apply velocity to the Y coordinate of the vertex
        }
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

    public void accelerate(){
        isThrusting = true;
    }

    public void stopAcceleration(){
        isThrusting = false;
    }

    public void setRotationRight(double FPS){
        rotation = -ROTATION_SPEED/FPS;
    }

    public void setRotationLeft(double FPS){
        rotation = +ROTATION_SPEED/FPS;
    }

    public void stopRotation(){
        rotation = 0;
    }

    private void updateAngle(){
        angle += rotation;
    }
}
