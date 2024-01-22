package com.example.MotorolaScienceCup.Battlezone;

import java.util.ArrayList;

public class EnemyTank extends Object3D{

    private double rotation;
    public double ENEMY_SPEED = 10;



    public EnemyTank(double rotation, ArrayList<Vertex> points3D, ArrayList<Face> faces3D){
        super(points3D, faces3D);
        this.rotation = rotation;
    }

}
