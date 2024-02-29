package com.example.MotorolaScienceCup.Battlezone;

import java.util.ArrayList;
import java.util.Vector;

import static com.example.MotorolaScienceCup.Battlezone.Main.FPS_OFFSET;

public class SuperTank extends EnemyTank{

    private int HP;
    
    public static double SUPER_SPEED = 0.13*1.3*FPS_OFFSET;
    public static double SUPER_ROT_SPEED = 1*FPS_OFFSET;
    public SuperTank(ArrayList<Vertex> points3D, ArrayList<Face> faces3D){
        super(points3D, faces3D);
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public void takeHit(Object3D object3D){
        setHP(getHP()-1);
        if(HP<=0){
            Main.objectList.remove(this);
            Main.superTankList.remove(this);
            Main.fullTankList.remove(this);
            if(object3D instanceof Camera) {
                Main.score += 3000;
            }
            explode();
        }
    }
    
    public void enemyBehavior(){
        if(isMoving()){
           
            if(Util.getDistance(getTarget(), this.getCenter())<5){
                setWaiting(true);
                setMoving(false);
                setWaitTimer(Math.random()*50);
            }else{
               
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
                if(rand<0.9){
                    setWaiting(false);
                    setRotating(true);
                    setWillShoot(true);
                    setMoving(false);
                    Vertex vertex = new Vertex(Main.camera.getX(), 0, Main.camera.getZ()).getVertDif(new Vertex(getX(), 0, getZ()));
                    double[] arr = vertex.toArray();
                    double offset = Math.random()*25-12.5;
                    arr = Util.multiplyTransform(Util.getRotationYMatrix(offset),arr);
                    double scale = Math.random()*0.25 + 0.5;
                    for (int i = 0; i < arr.length; i++) {
                        arr[i]*=scale;
                    }
                    vertex = Util.arrToVert(arr);
                    setTarget(vertex.getVertSum(new Vertex(getX(),0,getZ())));
                    setMoveDir(1);
                    setTargetRotation(getLookAt(getTarget()));
                    setRotateDir(getExactRotationDir());
                }else{
                    setWaiting(false);
                    setRotating(true);
                    setWillShoot(false);
                    setMoving(false);
                    setTarget(new Vertex(Main.camera.getX()+Math.random()*20-10,0,Main.camera.getZ()+Math.random()*20-10));
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
                    offset = Math.random()*4-2;
                }
                setTarget(new Vertex(Main.camera.getX(),0,Main.camera.getZ()));
                setMoveDir(1);
                setTargetRotation(getLookAt(getTarget()));
                setRotateDir(getExactRotationDir());
            }
            if(getTargetRotation() < getRotation() + 2 && getTargetRotation() > getRotation() - 2){
                rotateTank(getRotDifference()+offset);
                setTargetRotation(getRotation()+offset);
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
        double distance = Util.getDistance(getCenter(), new Vertex(Main.camera.getX(),0,Main.camera.getZ()));
        if(distance>Camera.getFar()+10){
            Main.objectList.remove(this);
            Main.superTankList.remove(this);
            Main.fullTankList.remove(this);
        }
    }


}
