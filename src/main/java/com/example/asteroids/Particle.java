package com.example.asteroids;

import javafx.scene.shape.Polygon;

import java.util.List;
import java.util.Random;

public class Particle extends Polygon {

    private final double ROTATION_SPEED = 360;    // Degrees/second
    private final double THRUST = 5;    // Pixels/second
    private double friction;    // Coefficient of friction
    private double centerX;
    private double centerY;
    private double angle;
    private double rotation;
    private final double radius;
    private Vector velocity;
    private boolean isThrusting = false;
    final double FPS = Main.FPS;
    final double WINDOW_WIDTH = Main.WIDTH;
    final double WINDOW_HEIGHT = Main.HEIGHT;

    public Particle(List<Double> points, double angle, double rotation, double velocity, double friction) {
        super();
        getPoints().setAll(points);
        this.centerX = getCenterX();
        this.centerY = getCenterY();
        this.radius = getRadius();
        this.angle = angle;
        this.rotation = rotation;
        this.velocity = new Vector(velocity, angle);
        this.friction = friction;
    }

    public double getCenterX() {    // Mean average of the X coordinates
        double sum = 0;
        for (int i = 0; i < getPoints().size(); i += 2) {
            sum += getPoints().get(i);
        }
        return sum / ((double) getPoints().size() / 2);
    }

    public double getCenterY() {    // Mean average of the Y coordinates
        double sum = 0;
        for (int i = 1; i < getPoints().size(); i += 2) {
            sum += getPoints().get(i);
        }
        return sum / ((double) getPoints().size() / 2);
    }

    public double getRadius() { // Calculate radius by finding the furthest coordinate
        double maxOffset = 0;
        for (int i = 2; i < getPoints().size(); i += 2) {
            maxOffset = Math.max(maxOffset, Math.sqrt(Math.pow(centerX - getPoints().get(i), 2) + Math.pow(centerY - getPoints().get(i + 1), 2)));
        }
        return maxOffset;
    }

    public void moveTo(double newCenterX, double newCenterY) {
        // Calculate new point coordinates
        for (int i = 0; i < getPoints().size(); i += 2) {
            double newX = newCenterX + getPoints().get(i) - centerX;
            double newY = newCenterY + getPoints().get(i + 1) - centerY;
            getPoints().set(i, newX);
            getPoints().set(i + 1, newY);
        }
        centerX = getCenterX();
        centerY = getCenterY();
    }

    public void hyperSpace() {
        Random random = new Random();
        int randomEvenInteger = random.nextInt((62 / 2) + 1) * 2;   // Random even number from 0 to 62
        if (randomEvenInteger >= Main.asteroids.size() + 44) {
            Main.explode();
        } else {
            centerX = getCenterX();
            centerY = getCenterY();
            moveTo(Math.random() * WINDOW_WIDTH, Math.random() * WINDOW_HEIGHT);    // Teleport
        }
    }

    public void scale(double scale){
        centerX = getCenterX();
        centerY = getCenterY();
        for (int i = 0; i < getPoints().size(); i+=2) {
            double newX = scale * (getPoints().get(i) - centerX) + centerX;
            double newY = scale * (getPoints().get(i+1) - centerY) + centerY;
            getPoints().set(i, newX);
            getPoints().set(i+1, newY);
        }
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void updatePosition() {
        updateAngle();   // Apply the rotation to the "angle" variable
        rotate(-rotation);   // Rotate the ship
        if (isThrusting) {
            velocity.setX(velocity.getX() + THRUST * Math.cos(Math.toRadians(angle)) / FPS);  // Update X component of velocity
            velocity.setY(velocity.getY() - THRUST * Math.sin(Math.toRadians(angle)) / FPS);  // Update Y component of velocity
        } else {
            velocity.setX(velocity.getX() - friction * velocity.getX() / FPS);    // Apply friction to X component of velocity
            velocity.setY(velocity.getY() - friction * velocity.getY() / FPS);    // Apply friction to Y component of velocity
        }
        for (int i = 0; i < getPoints().size(); i += 2) {
            getPoints().set(i, getPoints().get(i) + velocity.getX()); // Apply velocity to the X coordinate of the vertex
            getPoints().set(i + 1, getPoints().get(i + 1) + velocity.getY()); // Apply velocity to the Y coordinate of the vertex
        }
        centerX = getCenterX();
        centerY = getCenterY();
        if (centerX < 0 - radius) { // Goes off to the left of the screen
            centerX = WINDOW_WIDTH + radius;
            for (int i = 0; i < getPoints().size(); i += 2) {
                getPoints().set(i, WINDOW_WIDTH + 2 * radius + getPoints().get(i));
            }
        } else if (centerX > WINDOW_WIDTH + radius) {   // // Goes off to the right of the screen
            centerX = 0 - radius;
            for (int i = 0; i < getPoints().size(); i += 2) {
                getPoints().set(i, getPoints().get(i) - WINDOW_WIDTH - 2 * radius);
            }
        }
        if (centerY < 0 - radius) { // Goes off above of the screen
            centerY = WINDOW_HEIGHT + radius;
            for (int i = 1; i < getPoints().size(); i += 2) {
                getPoints().set(i, WINDOW_HEIGHT + 2 * radius + getPoints().get(i));
            }
        } else if (centerY > WINDOW_HEIGHT + radius) {  // Goes off below of the screen
            centerY = 0 - radius;
            for (int i = 1; i < getPoints().size(); i += 2) {
                getPoints().set(i, getPoints().get(i) - WINDOW_HEIGHT - 2 * radius);
            }
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

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public void accelerate() {
        isThrusting = true;
    }

    public void stopAcceleration() {
        isThrusting = false;
    }

    public void setRotationRight() {
        rotation = -ROTATION_SPEED / FPS;
    }

    public void setRotationLeft() {
        rotation = ROTATION_SPEED / FPS;
    }

    public void stopRotation() {
        rotation = 0;
    }

    private void updateAngle() {
        angle += rotation;
    }

    public double getAngle() {
        return angle;
    }

    public Vector getVelocity() {
        return velocity;
    }
}