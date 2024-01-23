package com.example.MotorolaScienceCup.Battlezone;

import java.util.ArrayList;

public class Camera extends Object3D{

    private static double H_FOV;

    private static double V_FOV;

    private static double near;

    private static double far;

    private static Vertex position;

    private static Vertex forward;
    private static Vertex up;
    private static Vertex right;





    public Camera(ArrayList<Vertex> points3D, ArrayList<Face> faces3D, double x, double y, double z){
        super(points3D,faces3D);
        Vertex vertex = new Vertex(x,y,z);
        this.position = vertex;
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
                {1,0,0,-position.getX()},
                {0,1,0,-position.getY()},
                {0,0,1,-position.getZ()},
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
                {0, 0, 1, 0}
        };
        return matrix;
    }

    public static double gethFov() {
        return H_FOV;
    }

    public static double getvFov() {
        return V_FOV;
    }

    public static double getNear() {
        return near;
    }

    public static double getFar() {
        return far;
    }

    public static Vertex getPosition() {
        return position;
    }

    public static Vertex getForward() {
        return forward;
    }

    public static Vertex getUp() {
        return up;
    }

    public static Vertex getRight() {
        return right;
    }

    public static void setPosition(Vertex position) {
        Camera.position = position;
    }

    public static void setForward(Vertex forward) {
        Camera.forward = forward;
    }

    public static void setUp(Vertex up) {
        Camera.up = up;
    }

    public static void setRight(Vertex right) {
        Camera.right = right;
    }
}
