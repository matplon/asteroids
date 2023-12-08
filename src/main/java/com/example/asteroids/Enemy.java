package com.example.asteroids;

import java.util.List;

public class Enemy extends Particle{
    private int type;


    public Enemy(List<Double> points, double angle, double speed, int type){
        super(points, angle, 0, speed, 0);
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
