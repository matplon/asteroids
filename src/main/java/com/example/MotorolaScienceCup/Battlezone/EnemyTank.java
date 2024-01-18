package com.example.MotorolaScienceCup.Battlezone;

import java.util.ArrayList;

public class EnemyTank extends Object3D{
    private double rotation;

    public EnemyTank(double x, double y, ArrayList<Double> points3D, double rotation){
        super(x,y,points3D);
        this.rotation = rotation;
    }

}
