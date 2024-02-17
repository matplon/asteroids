package com.example.MotorolaScienceCup.Battlezone;

import java.util.ArrayList;
import java.util.Vector;

public class SuperTank extends EnemyTank{

    private int HP;
    
    public static double SUPER_SPEED = 0.4;
    public static double SUPER_ROT_SPEED = 1;
    public SuperTank(ArrayList<Vertex> points3D, ArrayList<Face> faces3D){
        super(points3D, faces3D);
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public void takeHit(){
        setHP(getHP()-1);
        if(HP<=0){
            Main.objectList.remove(this);
            Main.superTankList.remove(this);
            Main.fullTankList.remove(this);
            explode();
        }
    }
    
    public void enemyBehavior(){
        if(isMoving()){
            System.out.println("KOKOKOKKO");
            if(Util.getDistance(getTarget(), this.getCenter())<7){
                setWaiting(true);
                setMoving(false);
                setWaitTimer(Math.random()*50);
            }else{
                System.out.println("YOYOOYY");
                moveTank(new Vertex(this.getForward().getX()*SUPER_SPEED*getMoveDir(),0,this.getForward().getZ()*SUPER_SPEED*getMoveDir()));
                Vertex vert1 = getCenter();
                double[] arr = vert1.toArray();
                arr = Util.multiplyTransform(Util.getTranslationMatrix(getForward().getX()*100,0,getForward().getZ()*100),arr);
                Vertex vert2 = Util.arrToVert(arr);
                ArrayList<Vertex> list = new ArrayList<>();
                list.add(vert2);
                list.add(vert1);
                if(!Util.hitBoxIntersect(list, Main.camera.getHitBox2D()).isEmpty()){
                    shootTank();
                }
            }
        }
        if(isWaiting()){
            setWaitTimer(getWaitTimer()-1);
            if(getWaitTimer()<0){
                setWaitTimer(-1);
                double rand = Math.random();
                if(rand<0.85){
                    setWaiting(false);
                    setRotating(true);
                    setWillShoot(true);
                    setMoving(false);
                    setTarget(new Vertex(Main.camera.getX(),0,Main.camera.getZ()));
                    setMoveDir(1);
                    setTargetRotation(getLookAt(getTarget()));
                    setRotateDir(getExactRotationDir());
                }else{
                    setWaiting(false);
                    setRotating(true);
                    setWillShoot(false);
                    setMoving(false);
                    setTarget(new Vertex(Main.camera.getX()+Math.random()*10-5,0,Main.camera.getZ()+Math.random()*10-5));
                    setMoveDir(1);
                    setTargetRotation(getLookAt(getTarget()));
                    setRotateDir(getExactRotationDir());
                }
            }
        }
        if(isRotating()){
            double offset = 0;
            if(this.isWillShoot()){
                setWaiting(false);
                setRotating(true);
                setWillShoot(true);
                if(!(getTarget().getX() == Main.camera.getX() && getTarget().getZ() == Main.camera.getZ())){
                    offset = Math.random()*2-1;
                }
                setTarget(new Vertex(Main.camera.getX(),0,Main.camera.getZ()));
                setMoveDir(1);
                setTargetRotation(getLookAt(getTarget()));
                setRotateDir(getExactRotationDir());
            }
            if(getTargetRotation() < getRotation() + 1 && getTargetRotation() > getRotation() - 1){
                rotateTank(getRotDifference()+offset);
                setTargetRotation(getRotation());
                setRotating(false);
                if(isWillShoot()){
                    shootTank();
                    setWaiting(true);
                    setWaitTimer(Math.random()*50);
                }else{
                    setMoving(true);
                }

            }else{
                rotateTank(SUPER_ROT_SPEED*getRotateDir());
            }
        }
    }


}
