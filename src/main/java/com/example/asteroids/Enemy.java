package com.example.asteroids;

import java.util.List;

public class Enemy extends Particle{
    private int type;

    private int behavior;

    public Enemy (List<Double> points, double angle, double speed, int type){
        super(points, angle,0,speed,0);
        this.type = type;
        this.behavior = behavior;
    }

    public int getType() {
        return type;
    }

}
