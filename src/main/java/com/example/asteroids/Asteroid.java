package com.example.asteroids;

import java.util.List;

public class Asteroid extends Particle{
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
    private int size;

    public Asteroid(List<Double> points, double angle, double rotation, double velocity, double friction, int size){
        super(points, angle, rotation, velocity, friction);
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
