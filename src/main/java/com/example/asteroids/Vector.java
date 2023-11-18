package com.example.asteroids;

public class Vector {
    private double direction, magnitude;
    private double x, y;

    public Vector(double x, double y, double direction){
        this.x = x;
        this.y = y;
        this.magnitude = Math.sqrt(x*x+y*y);
    }

    public Vector(double magnitude, double direction){
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

    public double getMagnitude(){
        return magnitude;
    }

    public void setMagnitude(double magnitude){
        this.magnitude = magnitude;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public static double inverseDirection(double direction){
        if(direction<0) return (180-Math.abs(direction));
        return -(180-direction);
    }
}
