package com.example.MotorolaScienceCup.Battlezone;

import java.util.ArrayList;

public class Camera{

    private static double H_FOV;

    private static double V_FOV;

    private static double near;

    private static double far;

    private static double x;
    private static double y;

    private static double z;

    private static Vertex forward;
    private static Vertex up;
    private static Vertex right;


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
        this.near = 1 ;
        this.far = 100;
    }

    public static double[][] getTranslateCamMatrix(){
        double[][] matrix = {
                {1,0,0,-x},
                {0,1,0,-y},
                {0,0,1,-z},
                {0,0,0,1}
        };
        return matrix;
    }
    public static double[][] getRotateCamMatrix(){
        double rx = right.getX();
        double ry = right.getY();
        double rz = right.getZ();
        double ux = up.getX();
        double uy = up.getY();
        double uz = up.getZ();
        double fx = forward.getX();
        double fy = forward.getY();
        double fz = forward.getZ();
        double[][] matrix = {
                {rx,ry,rz,0},
                {ux,uy,uz,0},
                {fx,fy,fz,0},
                {0, 0, 0, 1}
        };
        return matrix;
    }

    public static double[][] getCamMatrix(){
        double[][] matrix = Util.multiplyMatrices(getTranslateCamMatrix(),getRotateCamMatrix());
        return matrix;
    }

    public static double[][] getProjectionMatrix(){
        double RIGHT = Math.tan(Math.toRadians(H_FOV/2));
        double LEFT = -RIGHT;
        double TOP = Math.tan(Math.toRadians((25.3125)));
        double BOTTOM = -TOP;
        double a = 2/(RIGHT - LEFT);
        double b = 2/(TOP - BOTTOM);

        System.out.println((25.3125));
        double c = -(far+near)/(far-near);
        double d = (-2 * near * far) / (far - near);
        double [][] matrix = {
                {a, 0, 0, 0},
                {0, b, 0, 0},
                {0, 0, c, d},
                {0, 0, -1, 0}
        };
        return matrix;
    }

}
