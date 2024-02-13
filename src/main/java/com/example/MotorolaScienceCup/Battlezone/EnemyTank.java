package com.example.MotorolaScienceCup.Battlezone;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;

public class EnemyTank extends Object3D{

    private Vertex forward;


    public EnemyTank(ArrayList<Vertex> points3D, ArrayList<Face> faces3D){
        super(points3D, faces3D);
    }

    public Vertex getForward() {
        return forward;
    }

    public void setForward(Vertex forward) {
        this.forward = forward;
    }

    public void rotateTank(double angle){
        //TODO:ADD FORWARD VECTOR ROTATION AND TRANSLATION FOR THIS FUNCTION
        double x = this.getCenterX();
        double y = this.getCenterY();
        double z = this.getCenterZ();
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
        this.moveTo(x,y,z);
        this.setX(this.getCenterX());
        this.setY(this.getCenterY());
        this.setZ(this.getCenterZ());
    }
}
