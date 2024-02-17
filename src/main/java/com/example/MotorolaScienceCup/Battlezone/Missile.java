package com.example.MotorolaScienceCup.Battlezone;

import java.util.ArrayList;
import java.util.Arrays;

public class Missile extends EnemyTank {


    public static double MISSILE_SPEED = 0.25;
    public static double MISSILE_ROT_SPEED = 10;

    private boolean isFlying;

    private boolean hasSpawned;

    public Missile(ArrayList<Vertex> points3D, ArrayList<Face> faces3D) {
        super(points3D, faces3D);
    }

    public void takeHit(){
        Main.missileList.remove(this);
        Main.fullTankList.remove(this);
        Main.objectList.remove(this);
        explode();
    }

    public boolean isFlying() {
        return isFlying;
    }

    public void setFlying(boolean flying) {
        isFlying = flying;
    }

    public boolean isHasSpawned() {
        return hasSpawned;
    }

    public void setHasSpawned(boolean hasSpawned) {
        this.hasSpawned = hasSpawned;
    }

    public void moveTank(Vertex direction){
        ArrayList<Vertex> hitbox = this.getCollideHitBox();
        ArrayList<Vertex> lol = new ArrayList<>();
        for (int i = 0; i < hitbox.size(); i++) {
            Vertex vert = hitbox.get(i);
            double[] arr = vert.toArray();
            arr = Util.multiplyTransform(Util.getTranslationMatrix(direction.getX()*2,direction.getY()*2,direction.getZ()*2),arr);
            lol.add(Util.arrToVert(arr));
        }
        System.out.println("hihihi");
        ArrayList<Object3D> array = this.runCollisionCheck(5,lol,this);
            System.out.println(getRotation() + " HHHHHHHHHHHHHHHHHHHH");
            Vertex vert = this.getCenter();
            double[] arr1 = vert.toArray();
            arr1 = Util.multiplyTransform(Util.getTranslationMatrix(direction.getX(),direction.getY(),direction.getZ()),arr1);
            this.setCenter(Util.arrToVert(arr1));
            for (int i = 0; i < this.getPoints3D().size(); i++) {
                double[][] translationMatrix = Util.getTranslationMatrix(direction.getX(), direction.getY(), direction.getZ());
                double[] arr = this.getPoints3D().get(i).toArray();
                arr = Util.multiplyTransform(translationMatrix, arr);
                this.getPoints3D().set(i, Util.arrToVert(arr));

            }
            for (int i = 0; i < this.getHitBox2D().size(); i++) {
                double[][] translationMatrix = Util.getTranslationMatrix(direction.getX(), direction.getY(), direction.getZ());
                double[] arr = this.getHitBox2D().get(i).toArray();
                System.out.println(Arrays.toString(arr) + "ZZZZZZZZZZ");
                arr = Util.multiplyTransform(translationMatrix, arr);
                System.out.println(Arrays.toString(arr) + "XXXXXXXX");
                this.getHitBox2D().set(i, Util.arrToVert(arr));

            }
            for (int i = 0; i < this.getCollideHitBox().size(); i++) {
                double [][] translationMatrix = Util.getTranslationMatrix(direction.getX(), direction.getY(), direction.getZ());
                double [] arr = this.getCollideHitBox().get(i).toArray();
                System.out.println(Arrays.toString(arr)+"ZZZZZZZZZZ");
                arr = Util.multiplyTransform(translationMatrix, arr);
                System.out.println(Arrays.toString(arr)+"XXXXXXXX");
                this.getCollideHitBox().set(i,Util.arrToVert(arr));

            }
            this.setX(this.getCenter().getX());
            this.setY(this.getCenter().getY());
            this.setZ(this.getCenter().getZ());
        if(!array.isEmpty()&&!isFlying&&!hasSpawned){
            double maxY = Util.getMaxY(array.get(0).getPoints3D());
            double currentY = Util.getMinY(getPoints3D());
            for (int i = 0; i < array.size(); i++) {
                if(Util.getMaxY(array.get(i).getPoints3D())>maxY){
                    maxY = Util.getMaxY(array.get(i).getPoints3D());
                }
            }
            if(maxY>currentY) {
                moveTank(new Vertex(0, maxY - currentY, 0));
            }else{
                setFlying(true);
            }

        }
        if(array.isEmpty()&&isFlying&&!hasSpawned){
            if(getPoints3D().get(1).getY() > 0){
                moveTank(new Vertex(0,-MISSILE_SPEED-100000,0));
            }else{
                setFlying(false);
            }

        }
    }

    public void enemyBehavior(){
        System.out.println("6969696969691");
        if(isMoving()){
            System.out.println("QQQQQQQQQQQQQQ");
            if(getPoints3D().get(1).getY() > 1 && hasSpawned){
                moveTank(new Vertex(0,-MISSILE_SPEED,0));
            }else if(getPoints3D().get(1).getY() < 1 && getPoints3D().get(1).getY() !=0){
                moveTank(new Vertex(0,-getPoints3D().get(1).getY(),0));
                setHasSpawned(false);
                setFlying(false);
            }
            if(Util.getDistance(getTarget(), this.getCenter())<7){
                setWaiting(true);
                setMoving(false);
                setWaitTimer(Math.random()*2);
            }else{
                System.out.println("YOYOOYY");
                moveTank(new Vertex(this.getForward().getX()*MISSILE_SPEED*getMoveDir(),0,this.getForward().getZ()*MISSILE_SPEED*getMoveDir()));
            }
        }
        if(isWaiting()){
            System.out.println("RRRRRRRRRRR");
            setWaitTimer(getWaitTimer()-1);
            if(getWaitTimer()<0){
                setWaitTimer(-1);
                double rand = Math.random();
                if(Util.getDistance(this.getCenter(), new Vertex(Main.camera.getX(), Main.camera.getY(), Main.camera.getZ()))<50){
                    setWaiting(false);
                    setRotating(true);
                    setMoving(false);
                    setTarget(new Vertex(Main.camera.getX(),0,Main.camera.getZ()));
                    setMoveDir(1);
                    setTargetRotation(getLookAt(getTarget()));
                    setRotateDir(getExactRotationDir());
                }else{
                    setWaiting(false);
                    setRotating(true);
                    setMoving(false);
                    Vertex vertex = getCenter();
                    double[] arr = vertex.toArray();
                    arr = Util.multiplyTransform(Util.getTranslationMatrix(getForward().getX()*20-getCenter().getX(), 0,getForward().getZ()*20-getCenter().getZ()),arr);
                    arr = Util.multiplyTransform(Util.getRotationYMatrix(Math.random()*160-80),arr);
                    setTarget(Util.arrToVert(arr));
                    setMoveDir(1);
                    setTargetRotation(getLookAt(getTarget()));
                    setRotateDir(getExactRotationDir());
                }
            }
        }
        if(isRotating()){
            System.out.println("?????????????");
            if(getTargetRotation() < getRotation() + 5 && getTargetRotation() > getRotation() - 5){
                rotateTank(getRotDifference());
                setTargetRotation(getRotation());
                setRotating(false);
                setMoving(true);

            }else{
                rotateTank(MISSILE_ROT_SPEED*getRotateDir());
            }
        }



}}