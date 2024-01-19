package com.example.MotorolaScienceCup.Battlezone;

import java.util.ArrayList;

public class Camera{

    private double H_FOV;

    private double V_FOV;

    private double near;

    private double far;

    private double x;
    private double y;

    private double z;

    private Vertex forward;
    private Vertex up;
    private Vertex right;


    private ArrayList<Vertex> rotation;



    public Camera(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
        this.H_FOV = Main.H_FOV;
        this.V_FOV = H_FOV*(Main.HEIGHT/Main.WIDTH);
        this.forward = new Vertex(0,0,1);
        this.up = new Vertex(0,1,0);
        this.right = new Vertex(1,0,0);
        this.near = 0.001;
        this.far = 10000;
    }

}
