package com.example.MotorolaScienceCup.Battlezone;

import java.util.ArrayList;

public class Particle extends Object3D{

    private int tickrate;

    private Vertex rotationVert;
    private Vertex direction;
    public Particle(ArrayList<Vertex> points3D, ArrayList<Face> faces3D, Vertex direction){
        super(points3D, faces3D);
        this.tickrate  = 0;
        this.direction = direction;
        this.rotationVert = null;
    }

    public int getTickrate() {
        return tickrate;
    }

    public void setTickrate(int tickrate) {
        this.tickrate = tickrate;
    }

    public Vertex getDirection() {
        return direction;
    }

    public void setDirection(Vertex direction) {
        this.direction = direction;
    }

    public Vertex getRotationVert() {
        return rotationVert;
    }

    public void setRotationVert(Vertex rotationVert) {
        this.rotationVert = rotationVert;
    }
}
