package com.example.asteroids;

import javafx.scene.shape.Polygon;

public class Particle extends Polygon {

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
    public Particle(double centerX, double centerY, double radius, double angle){
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
        double sum = 0;
        for (int i = 0; i < getPoints().size(); i+=2) {
            sum+=getPoints().get(i);
        }
        return sum/getPoints().size()/2;
    }

    public double getCenterY(){
        double sum = 0;
        for (int i = 1; i < getPoints().size(); i+=2) {
            sum+=getPoints().get(i);
        }
        return sum/getPoints().size()/2;
    }
}