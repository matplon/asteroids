package com.example.MotorolaScienceCup.Battlezone;

import java.util.ArrayList;

public class Camera extends Object3D{

    private static double H_FOV;

    private static double V_FOV;

    private static double near;

    private static double far;

    private double x;
    private double y;
    private double z;

    private Vertex forward;
    private Vertex up;
    private  Vertex right;


    public Camera(ArrayList<Vertex> points3D, ArrayList<Face> faces3D, double x, double y, double z){
        super(points3D,faces3D);
        this.x=x;
        this.y=y;
        this.z=z;
        double rotation = 0;
        this.H_FOV = Main.H_FOV;
        this.V_FOV = H_FOV*(Main.HEIGHT/Main.WIDTH);
        this.forward = new Vertex(0,0,1);
        this.up = new Vertex(0,1,0);
        this.right = new Vertex(1,0,0);
        this.near = 0.5;
        this.far = 100;
    }

    public double[][] getTranslateCamMatrix(){
        double[][] matrix = {
                {1,0,0,-this.getX()},
                {0,1,0,-this.getY()},
                {0,0,1,-this.getZ()},
                {0,0,0,1}
        };
        return matrix;
    }
    public double[][] getRotateCamMatrix(){
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
        double[][] matrix1 = {
                {rx,fx,ux,0},
                {ry,fy,uy,0},
                {rz,fz,uz,0},
                {0, 0, 0, 1}
        };
        return matrix;
    }

    public  double[][] getCamMatrix(){
        double[][] matrix = Util.multiplyMatrices(getTranslateCamMatrix(), getRotateCamMatrix());
        return matrix;
    }

    public static double[][] getProjectionMatrix(){
        double RIGHT = Math.tan(Math.toRadians(H_FOV/2));
        double LEFT = -RIGHT;
        double TOP = Math.tan(Math.toRadians((25.3125)));
        double BOTTOM = -TOP;
        double a = 2/(RIGHT - LEFT);
        double b = 2/(TOP - BOTTOM);

        System.out.println((V_FOV/2));
        double c = (far+near)/(far-near);
        double d = (-2 * near * far) / (far - near);
        double [][] matrix = {
                {a, 0, 0, 0},
                {0, b, 0, 0},
                {0, 0, c, d},
                {0, 0, 1, 0}
        };
        return matrix;
    }

    public void shootBullet(){
        if(Main.bullets.isEmpty()||Main.bullets.isEmpty()){
            Object3D obj = Util.convertOBJ("Pyramid.txt");
            Vertex position = this.getPoints3D().get(0);
            double[] dir =this.getForward().toArray();
            Bullet bullet = Util.generateBullet(dir, this.getRotation(), this.getX(), this.getY()-0.1, this.getZ(), obj.getPoints3D(),obj.getFaces3D());
            Main.bullets.add(bullet);
        }
    }

    @Override
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    @Override
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
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


    public Vertex getForward() {
        return forward;
    }

    public  Vertex getUp() {
        return up;
    }

    public  Vertex getRight() {
        return right;
    }

    public  void setForward(Vertex forward) { this.forward = forward;
    }

    public  void setUp(Vertex up) {
        this.up = up;
    }

    public  void setRight(Vertex right) {
        this.right = right;
    }
}
