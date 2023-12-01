package com.example.asteroids;

import java.util.List;

public class Asteroid extends Particle{
    private int size;

    public Asteroid(List<Double> points, double angle, double rotation, double velocity, double friction, int size){
        super(points, angle, rotation, velocity, friction);
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
