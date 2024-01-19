package com.example.MotorolaScienceCup.Battlezone;


import java.util.ArrayList;
import java.util.List;

public class Object3D {

    private double x;
    private double y;

    private double z;

    private double rotation;
    private ArrayList<Vertex> points3D = new ArrayList<>();

    private ArrayList<Face> faces3D = new ArrayList<>();

    public Object3D(double x, double y, double z, ArrayList<Vertex> points3D, ArrayList<Face> faces3D){
        this.x = x;
        this.y = y;
        this.z = z;
        this.points3D = points3D;
        this.faces3D = faces3D;
        this.rotation = 0;

    }

    public Object3D(double x, double y, double z, double rotation, ArrayList<Vertex> points3D, ArrayList<Face> faces3D){
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotation = rotation;
        this.points3D = points3D;
        this.faces3D = faces3D;

    }

    public double getCenterX(){
        double x = 0;
        for (int i = 0; i < this.points3D.size(); i++) {
            x += this.points3D.get(i).getX();
        }
        double average = x/this.points3D.size();
        return average;
    }
    public double getCenterY(){
        double x = 0;
        for (int i = 0; i < this.points3D.size(); i++) {
            x += this.points3D.get(i).getY();
        }
        double average = x/this.points3D.size();
        return average;
    }
    public double getCenterZ(){
        double x = 0;
        for (int i = 0; i < this.points3D.size(); i++) {
            x += this.points3D.get(i).getZ();
        }
        double average = x/this.points3D.size();
        return average;
    }

    public void translate(double x, double y, double z){
        for (int i = 0; i < this.points3D.size(); i++) {
            double [][] translationMatrix = Util.getTranslationMatrix(x,y,z);
            double [] arr = this.points3D.get(i).toArray();
            arr = Util.multiplyTransform(translationMatrix, arr);
            this.points3D.set(i,Util.arrToVert(arr));
            this.x = getCenterX();
            this.y = getCenterY();
            this.z = getCenterZ();
        }
    }

    public void rotX(double angle){
        for (int i = 0; i < this.points3D.size(); i++) {
            double [][] translationMatrix = Util.getRotationXMatrix(angle);
            double [] arr = this.points3D.get(i).toArray();
            arr = Util.multiplyTransform(translationMatrix, arr);
            this.points3D.set(i,Util.arrToVert(arr));
            this.x = getCenterX();
            this.y = getCenterY();
            this.z = getCenterZ();
        }
    }
    public void rotY(double angle){
        for (int i = 0; i < this.points3D.size(); i++) {
            double [][] translationMatrix = Util.getRotationYMatrix(angle);
            double [] arr = this.points3D.get(i).toArray();
            arr = Util.multiplyTransform(translationMatrix, arr);
            this.points3D.set(i,Util.arrToVert(arr));
            this.x = getCenterX();
            this.y = getCenterY();
            this.z = getCenterZ();
        }
    }
    public void rotZ(double angle){
        for (int i = 0; i < this.points3D.size(); i++) {
            double [][] translationMatrix = Util.getRotationZMatrix(angle);
            double [] arr = this.points3D.get(i).toArray();
            arr = Util.multiplyTransform(translationMatrix, arr);
            this.points3D.set(i,Util.arrToVert(arr));
            this.x = getCenterX();
            this.y = getCenterY();
            this.z = getCenterZ();
        }
    }
    public void scale(double x, double y, double z){
        for (int i = 0; i < this.points3D.size(); i++) {
            double [][] translationMatrix = Util.getScaleMatrix(x,y,z);
            double [] arr = this.points3D.get(i).toArray();
            arr = Util.multiplyTransform(translationMatrix, arr);
            this.points3D.set(i,Util.arrToVert(arr));
            this.x = getCenterX();
            this.y = getCenterY();
            this.z = getCenterZ();
        }
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

    public ArrayList<Vertex> getPoints3D() {
        return points3D;
    }

    public ArrayList<Face> getFaces3D() {
        return faces3D;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public void setPoinst3D(ArrayList<Vertex> poinst3D) {
        this.points3D = poinst3D;
    }

    public void setFaces3D(ArrayList<Face> faces3D) {
        this.faces3D = faces3D;
    }
}
