package com.example.MotorolaScienceCup.Battlezone;


import java.util.ArrayList;
import java.util.List;

public class Object3D {

    private double x;
    private double y;

    private double z;

    private double rotation;
    private ArrayList<Vertex> poinst3D = new ArrayList<>();

    private ArrayList<Face> faces3D = new ArrayList<>();

    public Object3D(double x, double y, double z, ArrayList<Vertex> poinst3D, ArrayList<Face> faces3D){
        this.x = x;
        this.y = y;
        this.z = z;
        this.poinst3D = poinst3D;
        this.faces3D = faces3D;
        this.rotation = 0;

    }

    public Object3D(double x, double y, double z, double rotation, ArrayList<Vertex> poinst3D, ArrayList<Face> faces3D){
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotation = rotation;
        this.poinst3D = poinst3D;
        this.faces3D = faces3D;

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

    public double getRotation() {
        return rotation;
    }

    public ArrayList<Vertex> getPoinst3D() {
        return poinst3D;
    }

    public ArrayList<Face> getFaces3D() {
        return faces3D;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public void setPoinst3D(ArrayList<Vertex> poinst3D) {
        this.poinst3D = poinst3D;
    }

    public void setFaces3D(ArrayList<Face> faces3D) {
        this.faces3D = faces3D;
    }
}
