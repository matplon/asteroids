package com.example.MotorolaScienceCup.Battlezone;

import java.util.ArrayList;

public class Mine extends EnemyTank{

    public static double MINE_SPEED = 1;
    public Mine(ArrayList<Vertex> points3D, ArrayList<Face> faces3D) {
        super(points3D, faces3D);
    }

    public void takeHit(Object3D object3D){
        Main.objectList.remove(this);
        Main.mineList.remove(this);
        Main.onGotShot();
        explode();
    }

    public void checkCamera(){
        if(!Util.hitBoxIntersect(getCollideHitBox(), Main.camera.getHitBox2D()).isEmpty()){
            takeHit(Main.camera);
        }
    }
}
