package com.example.MotorolaScienceCup.Battlezone;


import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Object3D {

    private double x;
    private double y;

    private double z;

    private double rotation;
    private ArrayList<Vertex> points3D = new ArrayList<>();

    private ArrayList<Face> faces3D = new ArrayList<>();

    public Object3D(ArrayList<Vertex> points3D, ArrayList<Face> faces3D){
        this.points3D = points3D;
        this.faces3D = faces3D;
        this.rotation = 0;
        this.x = 0;
        this.y = 0;
        this.z = 0;

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
            System.out.println(x+"IIIII");
            System.out.println(this.points3D.get(i).getZ()+ " xxxxxxxxxxxx");
        }
        double average = x/this.points3D.size();
        return average;
    }

    public void convertVertecesToCentralOrigin(){
        for (int i = 0; i < this.points3D.size(); i++) {
            Vertex vertex = this.points3D.get(i);
            double x = vertex.getX() - this.getCenterX();
            double y = vertex.getY() - this.getCenterX();
            double z = vertex.getZ() - this.getCenterZ();
            this.points3D.set(i, new Vertex(x,y,z));
        }
        this.x = getCenterX();
        this.y = getCenterY();
        this.z = getCenterZ();
    }

    public ArrayList<Vertex> toScreen(){
        ArrayList<Vertex> arrlist= new ArrayList<>();
        for (int i = 0; i < this.points3D.size(); i++) {
            System.out.println(this.points3D.get(i).toString()+"0");
        }
        for (int i = 0; i < this.points3D.size(); i++) {
            double [][] camMatrixMatrix = Camera.getCamMatrix();
            double [] arr = new double[4];
            double[] arr1 = this.points3D.get(i).toArray();
            System.out.println(Arrays.toString(arr1)+"1");
            for (int j = 0; j < arr.length; j++) {
                arr[j] = arr1[j];
            }
            System.out.println(Arrays.toString(arr)+"2");
            arr = Util.multiplyTransform(camMatrixMatrix, arr);
            System.out.println(Arrays.toString(arr)+"3");
            double [][] projectionMatrix = Camera.getProjectionMatrix();
            arr = Util.multiplyTransform(projectionMatrix, arr);
            System.out.println(Arrays.toString(arr)+"4");
            for (int j = 0; j < arr.length; j++) {
                arr[j] = arr[j] / arr[3];
                if(arr[j]>2||arr[j]<-2){
                    arr[j]=0;
                }
            }
            System.out.println(Arrays.toString(arr)+"5");
            double [][] displayMatrix = Util.getDisplayMatrix();
            arr = Util.multiplyTransform(displayMatrix, arr);
            System.out.println(Arrays.toString(arr)+"6");
            arrlist.add(Util.arrToVert(arr));
        }
        return arrlist;
    }

    public void displayObject(){
        ArrayList<Vertex> arrlist=this.toScreen();
        for (int i = 0; i < arrlist.size(); i++) {
            System.out.println(arrlist.get(i).toString()+"heh");
        }
        for (int i = 0; i < this.faces3D.size(); i++) {
            Face face = this.faces3D.get(i);
            double ax = arrlist.get(face.getA()).getX();
            double ay = arrlist.get(face.getA()).getY();
            System.out.println(face.toString());
            double bx = arrlist.get(face.getB()).getX();
            double by = arrlist.get(face.getB()).getY();
            double cx = arrlist.get(face.getC()).getX();
            double cy = arrlist.get(face.getC()).getY();
            double dx = arrlist.get(face.getD()).getX();
            double dy = arrlist.get(face.getD()).getY();
            Polygon polygon = new Polygon(ax,ay,bx,by,cx,cy,dx,dy);
            polygon.setStroke(Color.BLACK);
            polygon.setFill(Color.WHITE);
            Main.root.getChildren().add(polygon);
        }
    }

    public void translate(double x, double y, double z){
        for (int i = 0; i < this.points3D.size(); i++) {
            double [][] translationMatrix = Util.getTranslationMatrix(x,y,z);
            double [] arr = this.points3D.get(i).toArray();
            arr = Util.multiplyTransform(translationMatrix, arr);
            System.out.println(Arrays.toString(arr)+"nice");
            this.points3D.set(i,Util.arrToVert(arr));

        }
        System.out.println(this.getCenterZ());
        this.x = this.getCenterX();
        this.y = this.getCenterY();
        this.z = this.getCenterZ();
    }

    public void moveTo(double x, double y, double z){
        double tx = x - this.getCenterX();
        double ty = y - this.getCenterY();
        double tz = z - this.getCenterZ();
        System.out.println(tx+" "+ty+" "+tz + "CFFF");
        System.out.println(x+" "+y+" "+z);
        translate(tx,ty,tz);
    }

    public void rotX(double angle){
        for (int i = 0; i < this.points3D.size(); i++) {
            double [][] translationMatrix = Util.getRotationXMatrix(angle);
            double [] arr = this.points3D.get(i).toArray();
            arr = Util.multiplyTransform(translationMatrix, arr);
            this.points3D.set(i,Util.arrToVert(arr));

        }
        this.x = getCenterX();
        this.y = getCenterY();
        this.z = getCenterZ();
    }
    public void rotY(double angle){
        for (int i = 0; i < this.points3D.size(); i++) {
            double [][] translationMatrix = Util.getRotationYMatrix(angle);
            double [] arr = this.points3D.get(i).toArray();
            arr = Util.multiplyTransform(translationMatrix, arr);
            this.points3D.set(i,Util.arrToVert(arr));

        }
        this.x = getCenterX();
        this.y = getCenterY();
        this.z = getCenterZ();
    }
    public void rotZ(double angle){
        for (int i = 0; i < this.points3D.size(); i++) {
            double [][] translationMatrix = Util.getRotationZMatrix(angle);
            double [] arr = this.points3D.get(i).toArray();
            arr = Util.multiplyTransform(translationMatrix, arr);
            this.points3D.set(i,Util.arrToVert(arr));

        }
        this.x = getCenterX();
        this.y = getCenterY();
        this.z = getCenterZ();
    }
    public void scale(double x, double y, double z){
        for (int i = 0; i < this.points3D.size(); i++) {
            double [][] translationMatrix = Util.getScaleMatrix(x,y,z);
            double [] arr = this.points3D.get(i).toArray();
            arr = Util.multiplyTransform(translationMatrix, arr);
            this.points3D.set(i,Util.arrToVert(arr));

        }
        this.x = getCenterX();
        this.y = getCenterY();
        this.z = getCenterZ();
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
