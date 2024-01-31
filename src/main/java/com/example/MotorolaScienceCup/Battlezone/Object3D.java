package com.example.MotorolaScienceCup.Battlezone;


import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Object3D {

    private Color color;

    private double x;
    private double y;

    private double z;

    private double rotation;
    private ArrayList<Vertex> points3D = new ArrayList<>();

    private ArrayList<Face> faces3D = new ArrayList<>();

    public Object3D(ArrayList<Vertex> points3D, ArrayList<Face> faces3D, double x, double y, double z){
        this.points3D = points3D;
        this.faces3D = faces3D;
        this.rotation = 0;
        this.color = Color.BLACK;
        this.x = 0;
        this.y = 0;
        this.z = 0;

    }


    public Object3D(ArrayList<Vertex> points3D, ArrayList<Face> faces3D){
        this.points3D = points3D;
        this.faces3D = faces3D;
        this.rotation = 0;
        this.color = Color.BLACK;
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
        }
        double average = x/this.points3D.size();
        return average;
    }

    public void updateRotation(double angle){
        double rot1 = this.getRotation();
        rot1+=angle;
        if(rot1>=360){
            rot1=angle-(360-this.getRotation());
        } else if (rot1<0) {
            rot1=360+(angle-this.getRotation());
        }
        this.setRotation(rot1);
    }

    //DOES NOT WORK. DO NOT USE UNDER ANY CIRCUMSTANCES. ONLY USE Object.moveTo(0,0,0)
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

    //

    public ArrayList<Vertex> toScreen(){
        int zeroedArrCount = 0;
        ArrayList<Vertex> arrlist = new ArrayList<>();
        for (int i = 0; i < this.points3D.size(); i++) {
            System.out.println(this.points3D.get(i).toString()+"0");
        }
        for (int i = 0; i < this.points3D.size(); i++) {

            double [][] camMatrixMatrix = Main.camera.getCamMatrix();
            double [] arr = new double[4];
            double[] arr1 = this.points3D.get(i).toArray();
            System.out.println(Arrays.toString(arr1)+"1");
            for (int j = 0; j < arr.length; j++) {
                arr[j] = arr1[j];
            }
            arr = Util.multiplyTransform(Util.getRotationYMatrix(0), arr);
            System.out.println(Arrays.toString(arr)+"2");
            arr = Util.multiplyTransform(camMatrixMatrix, arr);
            System.out.println(Arrays.toString(arr)+"3");
            double [][] projectionMatrix = Main.camera.getProjectionMatrix();
            arr = Util.multiplyTransform(projectionMatrix, arr);
            System.out.println(Arrays.toString(arr)+"4");
            for (int j = 0; j < arr.length; j++) {
                arr[j] = arr[j] / arr[arr.length-1];
            }
            if(arr[2]>1||arr[2]<-1){
                zeroedArrCount++;
                Arrays.fill(arr, 0);
            }
            System.out.println(Arrays.toString(arr)+"5");
            double [][] displayMatrix = Util.getDisplayMatrix();
            arr = Util.multiplyTransform(displayMatrix, arr);
            System.out.println(Arrays.toString(arr)+"6");
            arrlist.add(Util.arrToVert(arr));
        }
        if(zeroedArrCount == 0 || zeroedArrCount == arrlist.size()){
        return arrlist;}else{return new ArrayList<>();}
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void displayObject(){
        ArrayList<Vertex> arrlist=this.toScreen();
        for (int i = 0; i < arrlist.size(); i++) {
            System.out.println(arrlist.get(i).toString()+"heh");
        }
        for (int i = 0; i < this.faces3D.size(); i++) {
            Face face = this.faces3D.get(i);
            for (int j = 0; j < face.getIndexes().size(); j++) {
                double ax;
                double ay;
                double bx;
                double by;
                if(arrlist.size()>0){
                if(j+1<face.getIndexes().size()){
                    ax = arrlist.get(face.getIndexes().get(j)).getX();
                    ay = arrlist.get(face.getIndexes().get(j)).getY();
                    bx = arrlist.get(face.getIndexes().get(j+1)).getX();
                    by = arrlist.get(face.getIndexes().get(j+1)).getY();
                }else{
                    ax = arrlist.get(face.getIndexes().get(j)).getX();
                    ay = arrlist.get(face.getIndexes().get(j)).getY();
                    bx = arrlist.get(face.getIndexes().get(0)).getX();
                    by = arrlist.get(face.getIndexes().get(0)).getY();
                }


                //if(ax > Main.WIDTH+Main.WIDTH || bx > Main.WIDTH+Main.WIDTH || ax < 0-Main.WIDTH || bx < 0-Main.WIDTH ||
              //          ay > Main.HEIGHT+Main.HEIGHT|| by > Main.HEIGHT+Main.HEIGHT || ay < 0-Main.HEIGHT || by < 0-Main.HEIGHT){

              //  }else{
                    Polyline polyline1 = new Polyline(ax,ay,bx,by);
                    polyline1.setStroke(this.getColor());
                    if(i==0 && j==0){
                        Text text = new Text();
                        text.setText(Math.round(this.getCenterX()) + " " + Math.round(this.getCenterY()) + " " + Math.round(this.getCenterZ()));
                        text.setX(ax);
                        text.setY(ay);
                        Main.textList.add(text);
                        Main.root.getChildren().add(text);
                    }
                    Main.lineList.add(polyline1);
                    Main.root.getChildren().add(polyline1);

            }}


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
        System.out.println(tx+" "+ty+" "+tz + " CFFF");
        System.out.println(x+" "+y+" "+z);
        translate(tx,ty,tz);
    }

    public void rotX(double angle){
        double x = this.getCenterX();
        double y = this.getCenterY();
        double z = this.getCenterZ();
        System.out.println(rotation+" HHHHHHHHHHHHHHHHHHHH");
        this.moveTo(0,0,0);
        for (int i = 0; i < this.points3D.size(); i++) {
            double [][] translationMatrix = Util.getRotationXMatrix(angle);
            double [] arr = this.points3D.get(i).toArray();
            arr = Util.multiplyTransform(translationMatrix, arr);
            this.points3D.set(i,Util.arrToVert(arr));

        }
        this.moveTo(x,y,z);
        this.x = getCenterX();
        this.y = getCenterY();
        this.z = getCenterZ();
    }
    public void rotY(double angle){
        double x = this.getCenterX();
        double y = this.getCenterY();
        double z = this.getCenterZ();
        this.rotation += angle;
        if(rotation == 360){
            rotation = 0;
        } else if (rotation == -1) {
            rotation = 359;
        }
        System.out.println(rotation+" HHHHHHHHHHHHHHHHHHHH");
        this.moveTo(0,0,0);
        for (int i = 0; i < this.points3D.size(); i++) {
            double [][] translationMatrix = Util.getRotationYMatrix(angle);
            double [] arr = this.points3D.get(i).toArray();
            arr = Util.multiplyTransform(translationMatrix, arr);
            for (int j = 0; j < arr.length; j++) {
                arr[j] = Math.round(arr[j] * 10000.0) / 10000.0;
            }
            this.points3D.set(i,Util.arrToVert(arr));

        }
        this.moveTo(x,y,z);
        this.x = getCenterX();
        this.y = getCenterY();
        this.z = getCenterZ();
    }
    public void rotZ(double angle){
        double x = this.getCenterX();
        double y = this.getCenterY();
        double z = this.getCenterZ();
        System.out.println(rotation+" HHHHHHHHHHHHHHHHHHHH");
        this.moveTo(0,0,0);
        for (int i = 0; i < this.points3D.size(); i++) {
            double [][] translationMatrix = Util.getRotationZMatrix(angle);
            double [] arr = this.points3D.get(i).toArray();
            arr = Util.multiplyTransform(translationMatrix, arr);
            for (int j = 0; j < arr.length; j++) {
                arr[j] = Math.round(arr[j] * 10000.0) / 10000.0;
            }
            this.points3D.set(i,Util.arrToVert(arr));

        }
        this.moveTo(x,y,z);
        this.x = getCenterX();
        this.y = getCenterY();
        this.z = getCenterZ();
    }
    public void scale(double x, double y, double z){
        for (int i = 0; i < this.points3D.size(); i++) {
            double [][] translationMatrix = Util.getScaleMatrix(x,y,z);
            double [] arr = this.points3D.get(i).toArray();
            arr = Util.multiplyTransform(translationMatrix, arr);
            for (int j = 0; j < arr.length; j++) {
                arr[j] = Math.round(arr[j] * 10000.0) / 10000.0;
            }
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
