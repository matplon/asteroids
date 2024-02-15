package com.example.MotorolaScienceCup.Battlezone;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;

public class EnemyTank extends Object3D{

    private Vertex forward;

    private Vertex center;

    private ArrayList<Bullet> thisBullets = new ArrayList<>();

    private ArrayList<Vertex> collideHitBox = new ArrayList<>();

    private boolean attackMode;

    private boolean isRotating;

    private Vertex target;

    public static double TANK_SPEED = 0.1;


    public EnemyTank(ArrayList<Vertex> points3D, ArrayList<Face> faces3D){
        super(points3D, faces3D);
    }

    public Vertex getForward() {
        return forward;
    }

    public void setForward(Vertex forward) {
        this.forward = forward;
    }

    public Vertex getCenter() {
        return center;
    }

    public void setCenter(Vertex center) {
        this.center = center;
    }

    public ArrayList<Bullet> getThisBullets() {
        return thisBullets;
    }

    public void setThisBullets(ArrayList<Bullet> thisBullets) {
        this.thisBullets = thisBullets;
    }

    public boolean isAttackMode() {
        return attackMode;
    }

    public void setAttackMode(boolean attackMode) {
        this.attackMode = attackMode;
    }

    public ArrayList<Vertex> getCollideHitBox() {
        return collideHitBox;
    }

    public void setCollideHitBox(ArrayList<Vertex> collideHitBox) {
        this.collideHitBox = collideHitBox;
    }

    public Vertex getTarget() {
        return target;
    }

    public void setTarget(Vertex target) {
        this.target = target;
    }

    public void rotateTank(double angle){
        double x = this.getCenter().getX();
        double y = this.getCenter().getY();
        double z = this.getCenter().getZ();
        Vertex vertex = new Vertex(this.getForward().getX(), 0, this.getForward().getZ());
        double[] arr1 = vertex.toArray();
        arr1 = Util.multiplyTransform(Util.getRotationYMatrix(angle), arr1);
        this.setForward(Util.arrToVert(arr1));
        this.updateRotation(angle);
        System.out.println(getRotation()+" HHHHHHHHHHHHHHHHHHHH");
        this.moveTo(0,0,0);
        for (int i = 0; i < this.getPoints3D().size(); i++) {
            double [][] translationMatrix = Util.getRotationYMatrix(angle);
            double [] arr = this.getPoints3D().get(i).toArray();
            arr = Util.multiplyTransform(translationMatrix, arr);
            this.getPoints3D().set(i,Util.arrToVert(arr));

        }
        for (int i = 0; i < this.getHitBox2D().size(); i++) {
            double [][] translationMatrix = Util.getRotationYMatrix(angle);
            double [] arr = this.getHitBox2D().get(i).toArray();
            System.out.println(Arrays.toString(arr)+"ZZZZZZZZZZ");
            arr = Util.multiplyTransform(translationMatrix, arr);
            System.out.println(Arrays.toString(arr)+"XXXXXXXX");
            this.getHitBox2D().set(i,Util.arrToVert(arr));

        }
        for (int i = 0; i < this.getCollideHitBox().size(); i++) {
            double [][] translationMatrix = Util.getRotationYMatrix(angle);
            double [] arr = this.getCollideHitBox().get(i).toArray();
            System.out.println(Arrays.toString(arr)+"ZZZZZZZZZZ");
            arr = Util.multiplyTransform(Util.getTranslationMatrix(-center.getX(),-center.getY(), -center.getZ()), arr);
            arr = Util.multiplyTransform(translationMatrix, arr);
            arr = Util.multiplyTransform(Util.getTranslationMatrix(center.getX(),center.getY(), center.getZ()), arr);
            System.out.println(Arrays.toString(arr)+"XXXXXXXX");
            this.getCollideHitBox().set(i,Util.arrToVert(arr));

        }
        this.moveTo(x,y,z);
        this.setX(this.getCenter().getX());
        this.setY(this.getCenter().getY());
        this.setZ(this.getCenter().getZ());
    }

    public void scaleTank(double x, double y, double z){
            for (int i = 0; i < this.getPoints3D().size(); i++) {
                double [][] translationMatrix = Util.getScaleMatrix(x,y,z);
                double [] arr = this.getPoints3D().get(i).toArray();
                arr = Util.multiplyTransform(translationMatrix, arr);
                this.getPoints3D().set(i,Util.arrToVert(arr));

            }
            for (int i = 0; i < this.getHitBox2D().size(); i++) {
                double [][] translationMatrix = Util.getScaleMatrix(x,y,z);
                double [] arr = this.getHitBox2D().get(i).toArray();
                System.out.println(Arrays.toString(arr)+"ZZZZZZZZZZ");
                arr = Util.multiplyTransform(translationMatrix, arr);
                System.out.println(Arrays.toString(arr)+"XXXXXXXX");
                this.getHitBox2D().set(i,Util.arrToVert(arr));

            }
                for (int i = 0; i < this.getCollideHitBox().size(); i++) {
                    double [][] translationMatrix = Util.getScaleMatrix(x,y,z);
                    double [] arr = this.getCollideHitBox().get(i).toArray();
                    System.out.println(Arrays.toString(arr)+"ZZZZZZZZZZ");
                    arr = Util.multiplyTransform(translationMatrix, arr);
                    System.out.println(Arrays.toString(arr)+"XXXXXXXX");
                    this.getCollideHitBox().set(i,Util.arrToVert(arr));

                }

    }

    public boolean moveTank(Vertex direction){
        ArrayList<Vertex> hitbox = this.getCollideHitBox();
        ArrayList<Vertex> lol = new ArrayList<>();
        for (int i = 0; i < hitbox.size(); i++) {
            Vertex vert = hitbox.get(i);
            double[] arr = vert.toArray();
            arr = Util.multiplyTransform(Util.getTranslationMatrix(direction.getX()*TANK_SPEED,direction.getY()*TANK_SPEED,direction.getZ()*TANK_SPEED),arr);
            lol.add(Util.arrToVert(arr));
        }
        System.out.println("hihihi");
        if(this.runCollisionCheck(15,lol,this)) {
            System.out.println(getRotation() + " HHHHHHHHHHHHHHHHHHHH");
            Vertex vert = this.getCenter();
            double[] arr1 = vert.toArray();
            arr1 = Util.multiplyTransform(Util.getTranslationMatrix(direction.getX()*TANK_SPEED,direction.getY()*TANK_SPEED,direction.getZ()*TANK_SPEED),arr1);
            this.setCenter(Util.arrToVert(arr1));
            for (int i = 0; i < this.getPoints3D().size(); i++) {
                double[][] translationMatrix = Util.getTranslationMatrix(direction.getX()*TANK_SPEED, direction.getY()*TANK_SPEED, direction.getZ()*TANK_SPEED);
                double[] arr = this.getPoints3D().get(i).toArray();
                arr = Util.multiplyTransform(translationMatrix, arr);
                this.getPoints3D().set(i, Util.arrToVert(arr));

            }
            for (int i = 0; i < this.getHitBox2D().size(); i++) {
                double[][] translationMatrix = Util.getTranslationMatrix(direction.getX()*TANK_SPEED, direction.getY()*TANK_SPEED, direction.getZ()*TANK_SPEED);
                double[] arr = this.getHitBox2D().get(i).toArray();
                System.out.println(Arrays.toString(arr) + "ZZZZZZZZZZ");
                arr = Util.multiplyTransform(translationMatrix, arr);
                System.out.println(Arrays.toString(arr) + "XXXXXXXX");
                this.getHitBox2D().set(i, Util.arrToVert(arr));

            }
            for (int i = 0; i < this.getCollideHitBox().size(); i++) {
                double [][] translationMatrix = Util.getTranslationMatrix(direction.getX()*TANK_SPEED, direction.getY()*TANK_SPEED, direction.getZ()*TANK_SPEED);
                double [] arr = this.getCollideHitBox().get(i).toArray();
                System.out.println(Arrays.toString(arr)+"ZZZZZZZZZZ");
                arr = Util.multiplyTransform(translationMatrix, arr);
                System.out.println(Arrays.toString(arr)+"XXXXXXXX");
                this.getCollideHitBox().set(i,Util.arrToVert(arr));

            }
            this.setX(this.getCenter().getX());
            this.setY(this.getCenter().getY());
            this.setZ(this.getCenter().getZ());
            return true;
        }else{
            return false;
        }
    }
    public void shootTank(){
        if(thisBullets.isEmpty()||thisBullets.isEmpty()){
            Object3D obj = Util.convertOBJ("Pyramid.txt");
            double[] dir = this.getForward().toArray();
            Bullet bullet = Util.generateBullet(dir, this.getRotation(), this.getX()+this.getForward().getX()*0.5, this.getY()+0.25, this.getZ()+this.getForward().getZ()*0.5, obj.getPoints3D(),obj.getFaces3D());
            thisBullets.add(bullet);
        }
    }

    public void lookAtTarget(){

    }


}
