package com.example.MotorolaScienceCup.Battlezone;


import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.MotorolaScienceCup.Battlezone.Chunk.sideLength;

public class Object3D {

    private Color color;

    private double x;
    private double y;

    private double z;

    private double rotation;
    private ArrayList<Vertex> points3D = new ArrayList<>();

    private ArrayList<Face> faces3D = new ArrayList<>();

    private ArrayList<Vertex> hitBox2D = new ArrayList<>();

    private boolean isHalfCube;

    public void setPoints3D(ArrayList<Vertex> points3D) {
        this.points3D = points3D;
    }

    public boolean isHalfCube() {
        return isHalfCube;
    }

    public void setHalfCube(boolean halfCube) {
        isHalfCube = halfCube;
    }

    public Object3D(ArrayList<Vertex> points3D, ArrayList<Face> faces3D, double x, double y, double z){
        this.points3D = points3D;
        this.faces3D = faces3D;
        this.hitBox2D = new ArrayList<>();
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

    public ArrayList<Object3D> runCollisionCheck(double range, ArrayList<Vertex> myHitBox, Object3D current){
        ArrayList<Vertex> arr = new ArrayList<>();
        ArrayList<Object3D> objArr = new ArrayList<>();
        for (Object3D object:Main.objectList) {
            boolean flying = false;
            if(object instanceof Missile){
                flying = ((Missile) object).isFlying();
            }
            if (!(object.equals(current))&&!flying&&!((current instanceof Camera)&&(object instanceof Mine))) {
                double dist = Math.sqrt(Math.pow((this.getX() - object.getX()), 2) + Math.pow((this.getZ() - object.getZ()), 2));
                System.out.println(dist + " TROLOLOLOLOL");
                if (dist < range) {
                    if(!(object instanceof EnemyTank)){
                        ArrayList<Vertex> res = Util.hitBoxIntersect(object.getHitBox2D(), myHitBox);
                        for (Vertex vert : res) {
                            objArr.add(object);
                            arr.add(vert);
                        }
                    }else{
                        EnemyTank enemyTank = (EnemyTank) object;
                        ArrayList<Vertex> res = Util.hitBoxIntersect(enemyTank.getCollideHitBox(), myHitBox);
                        for (Vertex vert : res) {
                            objArr.add(object);
                            arr.add(vert);
                    }
                }}
            }
        }
        return objArr;
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
            if(arr[2]>1.0001||arr[2]<-1.0001){
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
            if(this.faces3D.size() >= 1 && this.getFaces3D().get(0).getIndexes().size() > 1){
            for (int j = 0; j < face.getIndexes().size(); j++) {
                double ax;
                double ay;
                double bx;
                double by;
                if(arrlist.size()>0) {
                    if (j + 1 < face.getIndexes().size()) {
                        ax = arrlist.get(face.getIndexes().get(j)).getX();
                        ay = arrlist.get(face.getIndexes().get(j)).getY();
                        bx = arrlist.get(face.getIndexes().get(j + 1)).getX();
                        by = arrlist.get(face.getIndexes().get(j + 1)).getY();
                    } else {
                        ax = arrlist.get(face.getIndexes().get(j)).getX();
                        ay = arrlist.get(face.getIndexes().get(j)).getY();
                        bx = arrlist.get(face.getIndexes().get(0)).getX();
                        by = arrlist.get(face.getIndexes().get(0)).getY();
                    }


                    //if(ax > Main.WIDTH+Main.WIDTH || bx > Main.WIDTH+Main.WIDTH || ax < 0-Main.WIDTH || bx < 0-Main.WIDTH ||
                    //          ay > Main.HEIGHT+Main.HEIGHT|| by > Main.HEIGHT+Main.HEIGHT || ay < 0-Main.HEIGHT || by < 0-Main.HEIGHT){

                    //  }else{
                    Polyline polyline1 = new Polyline(ax, ay, bx, by);
                    polyline1.setStroke(this.getColor());
                    Main.lineList.add(polyline1);
                    Main.root.getChildren().add(polyline1);
                }
            }}else{
                double ax = arrlist.get(face.getIndexes().get(0)).getX();
                double ay = arrlist.get(face.getIndexes().get(0)).getY();
                Circle circle = new Circle(ax, ay, 1, Color.GREEN);
                Main.decals.add(circle);
                Main.root.getChildren().add(circle);
            }


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
        for (int i = 0; i < this.hitBox2D.size(); i++) {
            double [][] translationMatrix = Util.getTranslationMatrix(x,y,z);
            double [] arr = this.hitBox2D.get(i).toArray();
            System.out.println(Arrays.toString(arr)+"ZZZZZZZZZZ");
            arr = Util.multiplyTransform(translationMatrix, arr);
            System.out.println(Arrays.toString(arr)+"XXXXXXXX");
            this.hitBox2D.set(i,Util.arrToVert(arr));

        }
       /* if(this instanceof EnemyTank){
            for (int i = 0; i < ((EnemyTank) this).getCollideHitBox().size(); i++) {
                double [][] translationMatrix = Util.getTranslationMatrix(x,y,z);
                double [] arr = ((EnemyTank) this).getCollideHitBox().get(i).toArray();
                System.out.println(Arrays.toString(arr)+"ZZZZZZZZZZ");
                arr = Util.multiplyTransform(translationMatrix, arr);
                System.out.println(Arrays.toString(arr)+"XXXXXXXX");
                ((EnemyTank) this).getCollideHitBox().set(i,Util.arrToVert(arr));

            }
        }*/

        this.setX(this.getCenterX());
        this.setY(this.getCenterY());
        this.setZ(this.getCenterZ());
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
        this.updateRotation(angle);
        System.out.println(rotation+" HHHHHHHHHHHHHHHHHHHH");
        this.moveTo(0,0,0);
        for (int i = 0; i < this.points3D.size(); i++) {
            double [][] translationMatrix = Util.getRotationYMatrix(angle);
            double [] arr = this.points3D.get(i).toArray();
            arr = Util.multiplyTransform(translationMatrix, arr);
            this.points3D.set(i,Util.arrToVert(arr));

        }
        for (int i = 0; i < this.hitBox2D.size(); i++) {
            double [][] translationMatrix = Util.getRotationYMatrix(angle);
            double [] arr = this.hitBox2D.get(i).toArray();
            System.out.println(Arrays.toString(arr)+"ZZZZZZZZZZ");
            arr = Util.multiplyTransform(translationMatrix, arr);
            System.out.println(Arrays.toString(arr)+"XXXXXXXX");
            this.hitBox2D.set(i,Util.arrToVert(arr));

        }
        /*if(this instanceof Ufo){
            for (int i = 0; i < ((EnemyTank) this).getCollideHitBox().size(); i++) {
                double [][] translationMatrix = Util.getRotationYMatrix(angle);
                double [] arr = ((EnemyTank) this).getCollideHitBox().get(i).toArray();
                System.out.println(Arrays.toString(arr)+"ZZZZZZZZZZ");
                arr = Util.multiplyTransform(translationMatrix, arr);
                System.out.println(Arrays.toString(arr)+"XXXXXXXX");
                ((EnemyTank) this).getCollideHitBox().set(i,Util.arrToVert(arr));

            }
        }*/
        this.moveTo(x,y,z);
        this.setX(this.getCenterX());
        this.setY(this.getCenterY());
        this.setZ(this.getCenterZ());
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
            this.points3D.set(i,Util.arrToVert(arr));

        }
        for (int i = 0; i < this.hitBox2D.size(); i++) {
            double [][] translationMatrix = Util.getScaleMatrix(x,y,z);
            double [] arr = this.hitBox2D.get(i).toArray();
            System.out.println(Arrays.toString(arr)+"ZZZZZZZZZZ");
            arr = Util.multiplyTransform(translationMatrix, arr);
            System.out.println(Arrays.toString(arr)+"XXXXXXXX");
            this.hitBox2D.set(i,Util.arrToVert(arr));

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

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
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

    public ArrayList<Vertex> getHitBox2D() {
        return hitBox2D;
    }

    public void setHitBox2D(ArrayList<Vertex> hitBox2D) {
        this.hitBox2D = hitBox2D;
    }
}
