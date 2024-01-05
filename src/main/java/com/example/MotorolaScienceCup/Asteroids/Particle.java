package com.example.MotorolaScienceCup.Asteroids;

import com.example.MotorolaScienceCup.BetterPolygon;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;

public class Particle extends BetterPolygon {
    static final double ANIMATION_PARTICLE_SPEED = 1;
    protected final double WINDOW_WIDTH = Main.WIDTH;
    protected final double WINDOW_HEIGHT = Main.HEIGHT;
    protected final double radius;
    protected double centerX;
    protected double centerY;
    protected double friction;
    protected double rotation;
    protected double angle;
    protected double terminalVelocity;
    protected double thrust;
    protected boolean isThrusting;
    protected Vector velocity;
    protected final double FPS = Main.FPS;

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
        this.terminalVelocity = 0;
        this.thrust = 0;
        this.isThrusting = false;
    }

    public void updatePosition() {
        updateAngle();   // Apply the rotation to the "angle" variable
        rotate(-rotation);   // Rotate the ship
        velocity.setDirection(-angle);
        if (isThrusting && velocity.getMagnitude() < terminalVelocity) {
            velocity.setX(velocity.getX() + thrust * Math.cos(Math.toRadians(angle)) / FPS);  // Update X component of velocity
            velocity.setY(velocity.getY() - thrust * Math.sin(Math.toRadians(angle)) / FPS);  // Update Y component of velocity

            if (velocity.getMagnitude() > terminalVelocity) {
                velocity.scale(terminalVelocity / velocity.getMagnitude());
            }
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

    void rotate(double angle) {
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

    public void animationParticles(){
        for (int i = 0; i < Main.PARTICLE_COUNT; i++) {
            List<Double> points = Arrays.asList(1.0, 1.0, 1.0, 5.0, 3.0, 5.0, 3.0, 1.0);    // Rectangle particle
            double angle = Math.random() * 360 - 180;
            Particle particle1 = new Particle(points, angle, 0, ANIMATION_PARTICLE_SPEED * (Math.random() * 0.75 + 0.5), 0);
            particle1.setFill(Color.WHITE);
            particle1.moveTo(getCenterX() + getRadius() * Math.cos(Math.toRadians(angle)) * (Math.random() * 0.75 + 0.5), getCenterY() + getRadius() * Math.sin(Math.toRadians(angle)) * (Math.random() * 0.75 + 0.5));
            Main.particlesAll.add(particle1);
            Main.particlesDistanceCovered.put(particle1, 0.0);

            Main.root.getChildren().add(particle1);
        }
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
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