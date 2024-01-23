package com.example.MotorolaScienceCup.Battlezone;

import java.util.ArrayList;

public class EnemyTank extends Object3D{

    private float rotation;
    public float ENEMY_SPEED = 10;



    public EnemyTank(float rotation, ArrayList<Vertex> points3D, ArrayList<Face> faces3D){
        super(points3D, faces3D);
        this.rotation = rotation;
    }

}
