package com.example.MotorolaScienceCup.Battlezone;


import java.util.ArrayList;
import java.util.List;

public class Object3D {

    private double x;
    private double y;
    private ArrayList<Double> poinst3D = new ArrayList<>();

    public Object3D(double x, double y, ArrayList<Double> poinst3D){
        this.x = x;
        this.y = y;
        this.poinst3D = poinst3D;

    }

}
