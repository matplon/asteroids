package com.example.MotorolaScienceCup.Battlezone;

import javax.sound.sampled.Clip;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import static com.example.MotorolaScienceCup.Battlezone.Main.FPS_OFFSET;

public class Missile extends EnemyTank {


    public static double MISSILE_SPEED = 0.6*FPS_OFFSET;
    public static double MISSILE_ROT_SPEED = 10*FPS_OFFSET;

    private boolean isFlying;

    private boolean isGrounded;
    private boolean hasSpawned;

    private Clip missileHum;

    public Missile(ArrayList<Vertex> points3D, ArrayList<Face> faces3D) {
        super(points3D, faces3D);
    }

    public void takeHit(Object3D object3D){
        Main.missileList.remove(this);
        Main.fullTankList.remove(this);
        Main.objectList.remove(this);
        if(object3D instanceof Camera) {
            Main.score += 2000;
        }
        this.getMissileHum().stop();
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
       
        ArrayList<Object3D> array = this.runCollisionCheck(5,lol,this);
        boolean hasMine = false;
        for (Object3D object3D:array){
            if(object3D instanceof Mine){
                hasMine = true;
            }
        }
           
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
               
                arr = Util.multiplyTransform(translationMatrix, arr);
               
                this.getHitBox2D().set(i, Util.arrToVert(arr));

            }
            for (int i = 0; i < this.getCollideHitBox().size(); i++) {
                double [][] translationMatrix = Util.getTranslationMatrix(direction.getX(), direction.getY(), direction.getZ());
                double [] arr = this.getCollideHitBox().get(i).toArray();
               
                arr = Util.multiplyTransform(translationMatrix, arr);
               
                this.getCollideHitBox().set(i,Util.arrToVert(arr));

            }
            this.setX(this.getCenter().getX());
            this.setY(this.getCenter().getY());
            this.setZ(this.getCenter().getZ());
        if(array.contains(Main.camera)&&(isGrounded&&!isFlying)){
            if(!Main.isDying){
                Main.wasHit=true;
            }
            this.setForward(new Vertex(0,0,0));
            this.getMissileHum().stop();
        }else{
        if(!array.isEmpty()&&!isFlying&&!hasSpawned&&!(array.size()==1&&hasMine)){
            double maxY = Util.getMaxY(array.get(0).getPoints3D());
            double currentY = Util.getMinY(getPoints3D());
            for (int i = 0; i < array.size(); i++) {
                if(Util.getMaxY(array.get(i).getPoints3D())>maxY){
                    maxY = Util.getMaxY(array.get(i).getPoints3D());
                }
            }
            if(maxY-0.0001>currentY) {
                setGrounded(false);
                moveTank(new Vertex(0, (maxY - currentY), 0));
            }else{
                setFlying(true);
            }

        }
        if(array.isEmpty()&&!isGrounded&&!hasSpawned&&!(array.size()==1&&hasMine)){
            if(getPoints3D().get(1).getY() > 0.00001){
                setFlying(false);
                moveTank(new Vertex(0,-getPoints3D().get(1).getY(),0));
            }else{
                setGrounded(true);
            }

        }}
    }

    public boolean isGrounded() {
        return isGrounded;
    }

    public void setGrounded(boolean grounded) {
        isGrounded = grounded;
    }

    public void enemyBehavior(){
       
        if(isMoving()){
           
            if(getPoints3D().get(1).getY() > 1 && hasSpawned){
                moveTank(new Vertex(0,-MISSILE_SPEED,0));
            }else if(getPoints3D().get(1).getY() < 1 && getPoints3D().get(1).getY() !=0 && hasSpawned){
                moveTank(new Vertex(0,-getPoints3D().get(1).getY(),0));
                setHasSpawned(false);
                setGrounded(true);
                setFlying(false);
            }
            if(Util.getDistance(getTarget(), this.getCenter())<0.3){
                setWaiting(true);
                setMoving(false);
                setWaitTimer(0);
            }else{
               
                moveTank(new Vertex(this.getForward().getX()*MISSILE_SPEED*getMoveDir(),0,this.getForward().getZ()*MISSILE_SPEED*getMoveDir()));
            }
        }
        if(isWaiting()){
           
            setWaitTimer(getWaitTimer()-1);
            if(getWaitTimer()<0){
                setWaitTimer(-1);
                double rand = Math.random();
                double dist = Util.getDistance(this.getCenter(), new Vertex(Main.camera.getX(), Main.camera.getY(), Main.camera.getZ()));
                if(dist<5){
                    setWaiting(false);
                    setRotating(true);
                    setMoving(false);
                    Vertex vertex = getCenter().getVertDif(new Vertex(Main.camera.getX(),Main.camera.getY(),Main.camera.getZ()));
                    double[] arr = vertex.toArray();
                    double offset = -1000;
                    for (int i = 0; i < arr.length; i++) {
                        arr[i]*=offset;
                    }
                    setTarget(getCenter().getVertSum(Util.arrToVert(arr)));
                    setMoveDir(1);
                    setTargetRotation(getLookAt(getTarget()));
                    setRotateDir(getExactRotationDir());
                } else if (dist>=5&&dist<40) {
                    setWaiting(false);
                    setRotating(true);
                    setMoving(false);
                    Vertex vertex = getCenter().getVertDif(new Vertex(Main.camera.getX(),Main.camera.getY(),Main.camera.getZ()));
                    double[] arr = vertex.toArray();
                    double offset = -1;
                    for (int i = 0; i < arr.length; i++) {
                        arr[i]*=offset;
                    }
                    setTarget(getCenter().getVertSum(Util.arrToVert(arr)));
                    setMoveDir(1);
                    setTargetRotation(getLookAt(getTarget()));
                    setRotateDir(getExactRotationDir());
                } else{
                    setWaiting(false);
                    setRotating(true);
                    setMoving(false);
                    Vertex vertex = getCenter().getVertDif(new Vertex(Main.camera.getX(),Main.camera.getY(),Main.camera.getZ()));
                    double[] arr = vertex.toArray();
                    arr = Util.multiplyTransform(Util.getRotationYMatrix(new Random().nextDouble(-45,45)),arr);
                    double offset = -Math.random()/4 - 0.25;
                    for (int i = 0; i < arr.length; i++) {
                        arr[i]*=offset;
                    }
                    setTarget(getCenter().getVertSum(Util.arrToVert(arr)));
                    setMoveDir(1);
                    setTargetRotation(getLookAt(getTarget()));
                    setRotateDir(getExactRotationDir());
                }
            }
        }
        if(isRotating()){
           
            if(getTargetRotation() < getRotation() + 12 && getTargetRotation() > getRotation() - 12){
                rotateTank(getRotDifference());
                setTargetRotation(getRotation());
                setRotating(false);
                setMoving(true);

            }else{
                rotateTank(MISSILE_ROT_SPEED*getRotateDir());
            }
        }


        double distance = Util.getDistance(getCenter(), new Vertex(Main.camera.getX(),0,Main.camera.getZ()));
        if(distance>Camera.getFar()+10){
            this.getMissileHum().stop();
            Main.objectList.remove(this);
            Main.missileList.remove(this);
            Main.fullTankList.remove(this);
        }
        }

    public Clip getMissileHum() {
        return missileHum;
    }

    public void setMissileHum(Clip missileHum) {
        this.missileHum = missileHum;
    }
}