package com.example.asteroids;

import javafx.scene.shape.Polygon;

import java.util.Random;

public class Asteroid extends Polygon {

    private double centerX, centerY, radius, angle, rotation;
    private Vector velocity;

    public Asteroid(double centerX, double centerY, double rotation, double velocity, double radius, double angle) {
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
        double sideLength = 2*radius/Math.sqrt(2);

    }
}
