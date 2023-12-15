package com.example.MotorolaScienceCup.Asteroids;

public class Vector {
    private double direction, magnitude;
    private double x, y;

    public Vector(double x, double y, double direction) {
        this.x = x;
        this.y = y;
        this.magnitude = Math.sqrt(x * x + y * y);
    }

    public Vector(double magnitude, double direction) {
        this.magnitude = magnitude;
        this.direction = direction;
        this.x = magnitude * Math.cos(Math.toRadians(direction));
        this.y = magnitude * Math.sin(Math.toRadians(direction));
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getMagnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
        this.x = magnitude * Math.cos(Math.toRadians(direction));
        this.y = magnitude * Math.sin(Math.toRadians(direction));
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public void scale(double scale) {
        this.x *= scale;
        this.y *= scale;
    }

    public static double inverseDirection(double direction) {
        if (direction < 0) return (180 - Math.abs(direction));
        return -(180 - direction);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "direction=" + direction +
                ", magnitude=" + magnitude +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
