package com.example.MotorolaScienceCup.Battlezone;

import java.util.ArrayList;
import java.util.Arrays;

public class Bullet extends Object3D{

    private double distanceCovered;

    private Vertex direction;
    public Bullet(ArrayList<Vertex> points3D, ArrayList<Face> faces3D){
        super(points3D, faces3D);
        this.direction = new Vertex(0,0,0);
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

    public boolean checkForHits(Object3D object){
        double x = object.getCenterX();
        double z = object.getCenterZ();
        double rotation = object.getRotation();
        object.translate(-x,0,-z);
        object.rotY(-rotation);
        this.translate(-x,0,-z);
        this.rotY(-rotation);
        Vertex vertex = this.getPoints3D().get(4);
        ArrayList<Vertex> hitbox = object.getHitBox2D();
        if(Util.getMaxX(hitbox)>vertex.getX()&&Util.getMaxZ(hitbox)>vertex.getZ()&&Util.getMinX(hitbox)<vertex.getX()&&Util.getMinZ(hitbox)<vertex.getZ()){
            object.translate(x,0,z);
            object.rotY(rotation);
            this.translate(x,0,z);
            this.rotY(rotation);
            return true;
        }else{
            object.translate(x,0,z);
            object.rotY(rotation);
            this.translate(x,0,z);
            this.rotY(rotation);
            return false;
        }

    }
}
