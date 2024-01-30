package com.example.MotorolaScienceCup.Battlezone;

import java.util.ArrayList;

public class Bullet extends Object3D{

    private double distanceCovered;

    private Vertex direction;
    public Bullet(ArrayList<Vertex> points3D, ArrayList<Face> faces3D){
        super(points3D, faces3D);
        this.direction = new Vertex(0,0,0);
        int rotation = 0;
    }

    public double getDistanceCovered() {
        return distanceCovered;
    }

    public void setDistanceCovered(double distanceCovered) {
        this.distanceCovered = distanceCovered;
    }

    public Vertex getDirection() {
        return direction;
    }

    public void setDirection(Vertex direction) {
        this.direction = direction;
    }
}
