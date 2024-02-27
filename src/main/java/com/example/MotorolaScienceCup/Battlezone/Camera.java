package com.example.MotorolaScienceCup.Battlezone;

import com.example.MotorolaScienceCup.Sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;

public class Camera extends Object3D{

    private static double H_FOV;

    private static double V_FOV;

    private static double near = 0.5;

    private static double far = 125;

    private double x;
    private double y;
    private double z;

    double magTimer;

    private final AudioInputStream stream;

    {
        try {
            stream = Sound.getStream("tankShot.wav");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }


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
        if(magTimer<0){
            double[] dir = this.getForward().toArray();
            Bullet bullet = Util.generateBullet(dir, this.getRotation(), this.getX(), this.getY()-0.1, this.getZ());
            bullet.setParent(this);
            Main.allBullets.add(bullet);
            try {
                Sound.play("tankShot.wav", 3.0f);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                throw new RuntimeException(e);
            }
            this.magTimer = 120;
        }
    }
    
    public boolean checkReticle(Object3D object3D){

        Vertex origin = new Vertex(this.getX(), this.getY(), this.getZ());
        Vertex endpoint = new Vertex(this.getX()+this.getForward().getX()*100, this.getY()+this.getForward().getY()*100, this.getZ()+this.getForward().getZ()*100);
        Vertex a1;
        Vertex a2;
        ArrayList<Vertex> result = new ArrayList<>();
        if(object3D instanceof EnemyTank && object3D.getClass() != Mine.class && !object3D.isHalfCube()){
        for (int i = 0; i < object3D.getHitBox2D().size(); i++) {
            if (i + 1 < object3D.getHitBox2D().size()) {
                a1 = object3D.getHitBox2D().get(i);
                a2 = object3D.getHitBox2D().get(i + 1);
            } else {
                a1 = object3D.getHitBox2D().get(object3D.getHitBox2D().size() - 1);
                a2 = object3D.getHitBox2D().get(0);
            }
            Vertex vertex = Util.lineIntersect(origin, endpoint, a1, a2);
            if (vertex != null) {
                result.add(vertex);
            }
        }}
        if(result.isEmpty()){
            return false;
        }else{
            return true;
        }
    }

    public void moveToRandom(){
        Vertex vertex = new Vertex(Chunk.getCenter().getX(), 0 ,Chunk.getCenter().getZ());
        double[] arr = vertex.toArray();
        double offsetX = Math.random()*(Chunk.sideLength-4) - ((Chunk.sideLength/2)-2);
        double offsetZ = Math.random()*(Chunk.sideLength-4) - ((Chunk.sideLength/2)-2);
        arr = Util.multiplyTransform(Util.getTranslationMatrix(offsetX,0,offsetZ),arr);
        vertex = Util.arrToVert(arr);
        this.moveTo(vertex.getX(), this.getY(), vertex.getZ());
        boolean notCollided = this.runCollisionCheck(5, this.getHitBox2D(), this).isEmpty();
        if(!notCollided){
            this.moveToRandom();
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

    public static void sethFov(double hFov) {
        H_FOV = hFov;
    }

    public static void setvFov(double vFov) {
        V_FOV = vFov;
    }

    public static void setNear(double near) {
        Camera.near = near;
    }

    public static void setFar(double far) {
        Camera.far = far;
    }

    public double getMagTimer() {
        return magTimer;
    }

    public void setMagTimer(double magTimer) {
        this.magTimer = magTimer;
    }
}
