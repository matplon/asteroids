package com.example.asteroids;

import java.util.List;

public class Asteroid extends Particle{
    private int size;

    public Asteroid(List<Double> points, double angle, double speed, int size){
        super(points, angle, 0, speed, 0);
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
