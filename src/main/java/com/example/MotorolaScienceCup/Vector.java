package com.example.MotorolaScienceCup;

public class Vector {
    private double direction;
    private double x, y;

    public Vector(double x, double y, double direction) {
        this.direction = direction;
        this.x = x;
        this.y = y;
    }

    public Vector(double magnitude, double direction) {
        this.direction = direction;
        this.x = magnitude * Math.cos(Math.toRadians(direction));
        this.y = magnitude * Math.sin(Math.toRadians(direction));
    }
    public Vector (){

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

    @Override
    public String toString() {
        return "Vector{" +
                "direction=" + direction +
                ", magnitude=" + getMagnitude() +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
