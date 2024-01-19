package com.example.MotorolaScienceCup.Battlezone;

public class Vertex {
    private double x;
    private double y;
    private double z;

    private double w;

    public Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1;

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getW(){
        return w;
    }
}
