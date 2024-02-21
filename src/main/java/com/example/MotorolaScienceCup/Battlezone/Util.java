package com.example.MotorolaScienceCup.Battlezone;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Util {

    public static double[][] getTranslationMatrix(double Tx, double Ty, double Tz){
        double[][] matrix = {
                {1,0,0,Tx},
                {0,1,0,Ty},
                {0,0,1,Tz},
                {0,0,0,1}
        };
        return matrix;
    }

    public static double[][] getRotationYMatrix(double angle){
        double[][] matrix = {
                {Math.cos(Math.toRadians(angle)),0,Math.sin(Math.toRadians(angle)),0},
                {0,                              1,0,                              0},
                {-Math.sin(Math.toRadians(angle)),0,Math.cos(Math.toRadians(angle)),0},
                {0,                                   0,0,                              1}
        };
        return matrix;
    }

    public static double[][] getRotationXMatrix(double angle){
        double[][] matrix = {
                {1,0,0,0},
                {0,Math.cos(Math.toRadians(angle)),Math.sin(Math.toRadians(angle))*(-1),0},
                {0,Math.sin(Math.toRadians(angle)),Math.cos(Math.toRadians(angle)),0},
                {0,0,0,1}
        };
        return matrix;
    }

    public static double[][] getRotationZMatrix(double angle){
        double[][] matrix = {
                {Math.cos(Math.toRadians(angle)),Math.sin(Math.toRadians(angle))*(-1),0,0},
                {Math.sin(Math.toRadians(angle)),Math.cos(Math.toRadians(angle)),0,0},
                {0,0,1,0},
                {0,0,0,1}
        };
        return matrix;
    }

    public static double[][] getScaleMatrix(double Tx, double Ty, double Tz){
        double[][] matrix = {
                {Tx,0, 0, 0},
                {0, Ty,0, 0},
                {0, 0, Tz,0},
                {0, 0, 0, 1}
        };
        return matrix;
    }

    public static double[][] getDisplayMatrix(){
        double[][] matrix = {
                {(double) Main.WIDTH /2,   0,          0,  (double) Main.WIDTH /2},
                {0,           (double) -Main.HEIGHT /2,0,  (double) Main.HEIGHT /2},
                {0,            0,          1,  0},
                {0,            0,          0,  1}
        };
        return matrix;
    }

    public static com.example.MotorolaScienceCup.Battlezone.Bullet generateBullet(double[] dir, double firingAngle, double x, double y, double z){
        System.out.println("PPPPPPPPP");
        Object3D object3D = Util.convertOBJ("Pyramid.txt");
        Bullet bullet = new Bullet(object3D.getPoints3D(),object3D.getFaces3D());

        bullet.moveTo(0,0,0);
        bullet.scale(0.1,0.2,0.1);
        bullet.rotX(90);
        bullet.rotY(firingAngle);
        bullet.moveTo(x,y,z);
        bullet.setColor(Color.GREEN);
        bullet.setOrigin(new Vertex(x,0,z));
        bullet.setDirection(Util.arrToVert(dir));
        return bullet;
    }

    public static Mine generateMine(double x,double z){
        System.out.println("PPPPPPPPP");
        Object3D object = convertOBJ("Pyramid.txt");
        ArrayList<Vertex> points3D = object.getPoints3D();
        ArrayList<Vertex> triangleHitbox = new ArrayList<>();
        triangleHitbox.add(new Vertex(0.4,0,-0.4));
        triangleHitbox.add(new Vertex(0.4,0,0.4));
        triangleHitbox.add(new Vertex(-0.4,0,0.4));
        triangleHitbox.add(new Vertex(-0.4,0,-0.4));
        Mine enemy = new Mine(object.getPoints3D(),object.getFaces3D());
        ArrayList<Vertex> hitbox = new ArrayList<>(object.getPoints3D());
        hitbox.add(new Vertex(getMaxX(points3D),0, getMaxZ(points3D)));
        hitbox.add(new Vertex(getMaxX(points3D),0, getMinZ(points3D)));
        hitbox.add(new Vertex(getMinX(points3D),0, getMinZ(points3D)));
        hitbox.add(new Vertex(getMinX(points3D),0, getMaxZ(points3D)));
        ArrayList<Vertex> hitbox2 = new ArrayList<>(triangleHitbox);
        enemy.setForward(new Vertex(0,0,1));
        enemy.setHitBox2D(hitbox);
        enemy.setCollideHitBox(hitbox2);
        enemy.setCenter(new Vertex(0,0,0));
        enemy.scale(0.4,0.15,0.4);
        enemy.moveTank(new Vertex(-enemy.getCenterX(),0,-enemy.getCenterZ()));
        enemy.rotateTank(0);
        enemy.setRotation(0);
        enemy.moveTank(new Vertex(x,-0.9,z));
        enemy.setColor(Color.GREEN);
        Main.objectList.add(enemy);
        Main.mineList.add(enemy);
        return enemy;
    }

    public static Ufo generateUfo(double x, double z){
        System.out.println("PPPPPPPPP");
        Object3D object = convertOBJ("UFO.txt");
        ArrayList<Vertex> points3D = object.getPoints3D();
        Object3D ring = convertOBJ("ufoRing.txt");
        Object3D ring2 = convertOBJ("ringUfo.txt");
        Ufo enemy = new Ufo(object.getPoints3D(),object.getFaces3D());
        ArrayList<Vertex> hitbox2 = ring2.getPoints3D();

        enemy.setHitBox2D(hitbox2);
        enemy.setCollideHitBox(new ArrayList<>(ring.getPoints3D()));
        enemy.setForward(new Vertex(0,0,1));
        enemy.setCenter(new Vertex(0,0,0));
        enemy.scale(1,0.7,1);
        enemy.moveTank(new Vertex(-enemy.getCenterX(),0,-enemy.getCenterZ()));
        enemy.rotateTank(0);
        enemy.setRotation(0);
        enemy.rotateTank(Math.random()*360);
        enemy.moveTank(new Vertex(x,0.65,z));
        enemy.setTarget(new Vertex(enemy.getCenter().getX() + Math.random()*50-25,0,enemy.getCenter().getZ() + Math.random()*50-25));
        enemy.setTargetRotation(enemy.getLookAt(enemy.getTarget()));
        enemy.setColor(Color.GREEN);
        enemy.setRotating(true);
        enemy.setMoveDir(1);
        enemy.setRotateDir(enemy.getExactRotationDir());
        Main.objectList.add(enemy);
        Main.ufoList.add(enemy);
        return enemy;
    }

    public static EnemyTank generateEnemyTank(double x, double z){
        System.out.println("PPPPPPPPP");
        Object3D object = convertOBJ("normalTank.txt");
        ArrayList<Vertex> points3D = object.getPoints3D();
        EnemyTank enemy = new EnemyTank(object.getPoints3D(),object.getFaces3D());
        ArrayList<Vertex> hitbox = new ArrayList<>();
        Object3D object3D = Util.convertOBJ("ring.txt");
        Object3D ring = Util.convertOBJ("tankHit.txt");
        ArrayList<Vertex> hitbox1 = new ArrayList<>(object3D.getPoints3D());
        enemy.setHitBox2D(new ArrayList<>(ring.getPoints3D()));
        enemy.setCollideHitBox(hitbox1);
        enemy.setForward(new Vertex(-1,0,0));
        enemy.setCenter(new Vertex(0,0,0));
        enemy.scaleTank(1.5,1.15,1.5);
        enemy.moveTank(new Vertex(-enemy.getCenterX(),0,-enemy.getCenterZ()));
        enemy.rotateTank(90);
        enemy.setRotation(0);
        enemy.rotateTank(Math.random()*360);
        enemy.moveTank(new Vertex(x,-0.3,z));
        enemy.setTarget(new Vertex(enemy.getCenter().getX() + Math.random()*50-25,0,enemy.getCenter().getZ() + Math.random()*50-25));
        enemy.setTargetRotation(enemy.getLookAt(enemy.getTarget()));
        enemy.setColor(Color.GREEN);
        enemy.setAttackMode(false);
        enemy.setWillShoot(false);
        enemy.setRotating(true);
        enemy.setMoveDir(1);
        enemy.setRotateDir(enemy.getExactRotationDir());
        enemy.setTargetRotation(enemy.getLookAt(enemy.getTarget()));
        Main.objectList.add(enemy);
        Main.enemyTankList.add(enemy);
        return enemy;
    }

    public static Missile generateMissile(double x, double z){
        System.out.println("PPPPPPPPP");
        Object3D object = convertOBJ("missile.txt");
        ArrayList<Vertex> points3D = object.getPoints3D();
        Missile enemy = new Missile(object.getPoints3D(),object.getFaces3D());
        ArrayList<Vertex> hitbox1 = new ArrayList<>();
        hitbox1.add(object.getPoints3D().get(1));
        hitbox1.add(object.getPoints3D().get(3));
        hitbox1.add(object.getPoints3D().get(7));
        ArrayList<Vertex> hitbox = new ArrayList<>();
        hitbox.add(new Vertex(getMaxX(points3D),0, getMaxZ(points3D)));
        hitbox.add(new Vertex(getMaxX(points3D),0, getMinZ(points3D)));
        hitbox.add(new Vertex(getMinX(points3D),0, getMinZ(points3D)));
        hitbox.add(new Vertex(getMinX(points3D),0, getMaxZ(points3D)));
        enemy.setHitBox2D(hitbox1);
        enemy.setCollideHitBox(hitbox);
        enemy.scale(0.75,0.75,0.75);
        enemy.setForward(new Vertex(1,0,0));
        enemy.setCenter(new Vertex(0,0,0));
        enemy.moveTank(new Vertex(-enemy.getCenterX(),0,-enemy.getCenterZ()));
        enemy.rotateTank(270);
        enemy.setRotation(0);
        enemy.moveTank(new Vertex(x,50,z));
        Vertex vertex = enemy.getCenter().getVertDif(new Vertex(Main.camera.getX(),Main.camera.getY(),Main.camera.getZ()));
        double[] arr = vertex.toArray();
        arr = Util.multiplyTransform(Util.getRotationYMatrix(new Random().nextDouble(-30,30)),arr);
        double offset = -Math.random()/2 - 0.25;
        for (int i = 0; i < arr.length; i++) {
            arr[i]*=offset;
        }
        enemy.setTarget(enemy.getCenter().getVertSum(Util.arrToVert(arr)));
        enemy.setTargetRotation(enemy.getLookAt(enemy.getTarget()));
        enemy.setColor(Color.GREEN);
        enemy.setRotating(true);
        enemy.setWaiting(false);
        enemy.setMoving(false);
        enemy.setMoveDir(1);
        enemy.setRotateDir(enemy.getExactRotationDir());
        enemy.setTargetRotation(enemy.getLookAt(enemy.getTarget()));
        enemy.setHasSpawned(true);
        enemy.setFlying(true);
        Main.objectList.add(enemy);
        Main.missileList.add(enemy);
        return enemy;
    }

    public static SuperTank generateSuperTank(double x, double z){
        System.out.println("PPPPPPPPP");
        Object3D object = convertOBJ("superTank.txt");
        ArrayList<Vertex> points3D = object.getPoints3D();
        SuperTank enemy = new SuperTank(object.getPoints3D(),object.getFaces3D());
        Object3D ring = Util.convertOBJ("superTankHit.txt");
        ArrayList<Vertex> hitbox = new ArrayList<>(ring.getPoints3D());
        Object3D object3D = Util.convertOBJ("superRing.txt");
        ArrayList<Vertex> hitbox1 = new ArrayList<>(object3D.getPoints3D());
        enemy.setHitBox2D(hitbox);
        enemy.setCollideHitBox(hitbox1);
        enemy.setForward(new Vertex(-1,0,0));
        enemy.setCenter(new Vertex(0,0,0));
        enemy.scaleTank(2,1.5,2);
        enemy.moveTank(new Vertex(-enemy.getCenterX(),0,-enemy.getCenterZ()));
        enemy.rotateTank(90);
        enemy.setRotation(0.5);
        enemy.rotateTank(Math.random()*360);
        enemy.moveTank(new Vertex(x,-0.25,z));
        enemy.setTarget(new Vertex(Main.camera.getX() + Math.random()*30-15,0,Main.camera.getZ() + Math.random()*30-15));
        enemy.setTargetRotation(enemy.getLookAt(enemy.getTarget()));
        enemy.setColor(Color.GREEN);
        enemy.setAttackMode(true);
        enemy.setWillShoot(false);
        enemy.setRotating(true);
        enemy.setMoving(false);
        enemy.setMoveDir(1);
        enemy.setRotateDir(enemy.getExactRotationDir());
        enemy.setTargetRotation(enemy.getLookAt(enemy.getTarget()));
        enemy.setHP(2);
        Main.objectList.add(enemy);
        Main.superTankList.add(enemy);
        return enemy;
    }

    public static Object3D generateCube(double x, double z){
        System.out.println("PPPPPPPPP");
        Object3D object = convertOBJ("Cube.txt");
        ArrayList<Vertex> cubeHitbox = new ArrayList<>();
        cubeHitbox.add(new Vertex(1.05,0,-1.05));
        cubeHitbox.add(new Vertex(1.05,0,1.05));
        cubeHitbox.add(new Vertex(-1.05,0,1.05));
        cubeHitbox.add(new Vertex(-1.05,0,-1.05));
        object.setColor(Color.GREEN);
        object.setHitBox2D(cubeHitbox);
        object.moveTo(x,0,z);
        Main.objectList.add(object);
        return object;
    }
    public static Object3D generateCone(double x, double z){
        System.out.println("PPPPPPPPP");
        Object3D object = convertOBJ("Pyramid.txt");
        ArrayList<Vertex> triangleHitbox = new ArrayList<>();
        triangleHitbox.add(new Vertex(0.6,0,-0.6));
        triangleHitbox.add(new Vertex(0.6,0,0.6));
        triangleHitbox.add(new Vertex(-0.6,0,0.6));
        triangleHitbox.add(new Vertex(-0.6,0,-0.6));
        object.setColor(Color.GREEN);
        object.setHitBox2D(triangleHitbox);
        object.moveTo(x,-0.6,z);
        Main.objectList.add(object);
        return object;
    }
    public static Object3D generateHalfCube(double x, double z){
        System.out.println("PPPPPPPPP");
        Object3D object = convertOBJ("Half-Cube.txt");
        ArrayList<Vertex> cubeHitbox = new ArrayList<>();
        cubeHitbox.add(new Vertex(1.05,0,-1.05));
        cubeHitbox.add(new Vertex(1.05,0,1.05));
        cubeHitbox.add(new Vertex(-1.05,0,1.05));
        cubeHitbox.add(new Vertex(-1.05,0,-1.05));
        object.setColor(Color.GREEN);
        object.setHitBox2D(cubeHitbox);
        object.moveTo(x,-0.6,z);
        Main.objectList.add(object);
        return object;
    }

    public static void drawUnscopedReticle(){
        Polyline polyline = new Polyline(Main.WIDTH/2, Main.HEIGHT/2 -100, Main.WIDTH/2, Main.HEIGHT/2 -200);
        Polyline polyline1 = new Polyline(Main.WIDTH/2, Main.HEIGHT/2 +100, Main.WIDTH/2, Main.HEIGHT/2 +200);
        Polyline polyline2 = new Polyline(Main.WIDTH/2 -100, Main.HEIGHT/2 -100, Main.WIDTH/2 +100, Main.HEIGHT/2 -100);
        Polyline polyline3 = new Polyline(Main.WIDTH/2 -100, Main.HEIGHT/2 +100, Main.WIDTH/2+100, Main.HEIGHT/2 +100);
        Polyline polyline4 = new Polyline(Main.WIDTH/2 -100, Main.HEIGHT/2 -100, Main.WIDTH/2 -100, Main.HEIGHT/2 -75);
        Polyline polyline5 = new Polyline(Main.WIDTH/2 +100, Main.HEIGHT/2 -100, Main.WIDTH/2 +100, Main.HEIGHT/2 -75);
        Polyline polyline6 = new Polyline(Main.WIDTH/2 -100, Main.HEIGHT/2 +100, Main.WIDTH/2 -100, Main.HEIGHT/2 +75);
        Polyline polyline7 = new Polyline(Main.WIDTH/2 +100, Main.HEIGHT/2 +100, Main.WIDTH/2 +100, Main.HEIGHT/2 +75);
        polyline.setStroke(Color.GREEN);
        polyline1.setStroke(Color.GREEN);
        polyline2.setStroke(Color.GREEN);
        polyline3.setStroke(Color.GREEN);
        polyline4.setStroke(Color.GREEN);
        polyline5.setStroke(Color.GREEN);
        polyline6.setStroke(Color.GREEN);
        polyline7.setStroke(Color.GREEN);
        Main.reticle.add(polyline);
        Main.reticle.add(polyline1);
        Main.reticle.add(polyline2);
        Main.reticle.add(polyline3);
        Main.reticle.add(polyline4);
        Main.reticle.add(polyline5);
        Main.reticle.add(polyline6);
        Main.reticle.add(polyline7);
        Main.root.getChildren().addAll(polyline,polyline1,polyline2,polyline3,polyline4,polyline5,polyline6,polyline7);
    }

    public static void drawScopedReticle(){
        Polyline polyline = new Polyline(Main.WIDTH/2, Main.HEIGHT/2 -50, Main.WIDTH/2, Main.HEIGHT/2 -200);
        Polyline polyline1 = new Polyline(Main.WIDTH/2, Main.HEIGHT/2 +50, Main.WIDTH/2, Main.HEIGHT/2 +200);
        Polyline polyline2 = new Polyline(Main.WIDTH/2 -100, Main.HEIGHT/2 -100, Main.WIDTH/2 +100, Main.HEIGHT/2 -100);
        Polyline polyline3 = new Polyline(Main.WIDTH/2 -100, Main.HEIGHT/2 +100, Main.WIDTH/2+100, Main.HEIGHT/2 +100);
        Polyline polyline4 = new Polyline(Main.WIDTH/2 -100, Main.HEIGHT/2 -100, Main.WIDTH/2 -50, Main.HEIGHT/2 -50);
        Polyline polyline5 = new Polyline(Main.WIDTH/2 +100, Main.HEIGHT/2 -100, Main.WIDTH/2 +50, Main.HEIGHT/2 -50);
        Polyline polyline6 = new Polyline(Main.WIDTH/2 -100, Main.HEIGHT/2 +100, Main.WIDTH/2 -50, Main.HEIGHT/2 +50);
        Polyline polyline7 = new Polyline(Main.WIDTH/2 +100, Main.HEIGHT/2 +100, Main.WIDTH/2 +50, Main.HEIGHT/2 +50);
        polyline.setStroke(Color.GREEN);
        polyline1.setStroke(Color.GREEN);
        polyline2.setStroke(Color.GREEN);
        polyline3.setStroke(Color.GREEN);
        polyline4.setStroke(Color.GREEN);
        polyline5.setStroke(Color.GREEN);
        polyline6.setStroke(Color.GREEN);
        polyline7.setStroke(Color.GREEN);
        Main.reticle.add(polyline);
        Main.reticle.add(polyline1);
        Main.reticle.add(polyline2);
        Main.reticle.add(polyline3);
        Main.reticle.add(polyline4);
        Main.reticle.add(polyline5);
        Main.reticle.add(polyline6);
        Main.reticle.add(polyline7);
        Main.root.getChildren().addAll(polyline,polyline1,polyline2,polyline3,polyline4,polyline5,polyline6,polyline7);
    }

    public static double getMaxX(ArrayList<Vertex> points){
        double x = points.get(0).getX();
        for (int i = 0; i < points.size(); i++) {
            if(x<points.get(i).getX()){
                x = points.get(i).getX();
            }
        }
        return x;
    }

    public static double getDistance(Vertex vertex1, Vertex vertex2){
        double dist = Math.sqrt(Math.pow((vertex1.getX() - vertex2.getX()), 2) + Math.pow((vertex1.getZ() - vertex2.getZ()), 2));
        return dist;
    }
    public static double getMaxZ(ArrayList<Vertex> points){
        double z = points.get(0).getZ();
        for (int i = 0; i < points.size(); i++) {
            if(z<points.get(i).getZ()){
                z = points.get(i).getZ();
            }
        }
        return z;
    }

    public static double getMinX(ArrayList<Vertex> points){
        double x = points.get(0).getX();
        for (int i = 0; i < points.size(); i++) {
            if(x>points.get(i).getX()){
                x = points.get(i).getX();
            }
        }
        return x;
    }

    public static double getMinZ(ArrayList<Vertex> points){
        double z = points.get(0).getZ();
        for (int i = 0; i < points.size(); i++) {
            if(z>points.get(i).getZ()){
                z = points.get(i).getZ();
            }
        }
        return z;
    }

    public static double getMaxY(ArrayList<Vertex> points){
        double y = points.get(0).getY();
        for (int i = 0; i < points.size(); i++) {
            if(y<points.get(i).getY()){
                y = points.get(i).getY();
            }
        }
        return y;
    }

    public static double getMinY(ArrayList<Vertex> points){
        double y = points.get(0).getY();
        for (int i = 0; i < points.size(); i++) {
            if(y>points.get(i).getY()){
                y = points.get(i).getY();
            }
        }
        return y;
    }

    public static ArrayList<Vertex> hitBoxIntersect(ArrayList<Vertex> hitbox1, ArrayList<Vertex> hitbox2){
        ArrayList<Vertex> result = new ArrayList<>();
        Vertex a1;
        Vertex a2;
        Vertex b1;
        Vertex b2;
        for (int i = 0; i < hitbox1.size(); i++) {
            if(i+1<hitbox1.size()){
                  a1 = hitbox1.get(i);
                  a2 = hitbox1.get(i+1);
            }else{
                  a1 = hitbox1.get(hitbox1.size()-1);
                  a2 = hitbox1.get(0);
            }
            for (int j = 0; j < hitbox2.size(); j++) {
                if(j+1<hitbox2.size()){
                      b1 = hitbox2.get(j);
                      b2 = hitbox2.get(j+1);
                }else{
                      b1 = hitbox2.get(hitbox2.size()-1);
                      b2 = hitbox2.get(0);
                }
                Vertex vert = lineIntersect(a1,a2,b1,b2);
                if(vert!=null){
                    result.add(vert);
                }
            }
        }
        
        
        return result;
    }

    public static Vertex lineIntersect(Vertex a1, Vertex a2, Vertex b1, Vertex b2){
        double ax = a2.getX() - a1.getX();
        double az = a2.getZ() - a1.getZ();
        double bx = b2.getX() - b1.getX();
        double bz = b2.getZ() - b1.getZ();
        double delta = (-bx*az + ax*bz);
        if(delta==0){
            return null;
        }else{
            double resultX;
            double resultZ;
            double factor1 = (-az*(a1.getX()-b1.getX())+ax*(a1.getZ()-b1.getZ()))/delta;
            double factor2 = (bx*(a1.getZ()-b1.getZ())-bz*(a1.getX()-b1.getX()))/delta;
            if(factor1 > 0 && factor1 <= 1 && factor2 > 0 && factor2 <= 1){
                resultX = a1.getX() + (factor2 * ax);
                resultZ = a1.getZ() + (factor2 * az);
                return new Vertex(resultX, 0, resultZ);
            }else{
                return  null;
            }
        }
    }



    public static double [] multiplyTransform(double[][] matrix1, double[] array){
        double [] finalArray = new double[4];
        for (int i = 0; i < array.length; i++) {
            double result = 0;
            for (int j = 0; j < array.length; j++) {
                result += array[j]*matrix1[i][j];
            }
            finalArray[i]=result;

        }
        return finalArray;
    }

    public static double [][] multiplyMatrices(double[][] matrix1, double[][] matrix2){
        double [][] finalMatrix = new double[4][4];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[0].length; j++) {
                double result = 0;
                for (int k = 0; k < matrix2.length; k++) {
                    result += matrix2[i][k]*matrix1[k][j];
                }
                finalMatrix[i][j]=result;
            }

        }
        return finalMatrix;
    }

    public static Vertex arrToVert(double[] arr){
        Vertex vertex = new Vertex(arr[0], arr[1], arr[2], arr[3]);
        return vertex;
    }

    public static Object3D generateOBJ (double x, double y, double z, ArrayList<Vertex> points3D, ArrayList<Face> faces3D, Color color, ArrayList<Vertex> hitboxBounds){
        Object3D obj = new Object3D(points3D,faces3D);
        obj.setHitBox2D(hitboxBounds);
        obj.convertVertecesToCentralOrigin();
        obj.moveTo(x,y,z);
        //obj.scale(1,1,5);
        Main.objectList.add(obj);
        obj.setColor(color);
        System.out.println(Arrays.toString(obj.getHitBox2D().get(0).toArray())+"{{{{{{{");
        return obj;
    }



    public static Object3D convertOBJ(String path){
        ArrayList<Vertex> vertices = new ArrayList<>();
        ArrayList<Face> faces = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(path));
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                System.out.println(nextLine);
                if(nextLine.startsWith("v ")){
                    String line = nextLine.replace("v ", "");
                    System.out.println(line);
                    String [] cords = line.split(" ");
                    System.out.println(Arrays.toString(cords) +" JJJJJJJJJ");
                    double x = Double.parseDouble(cords[0]);
                    double y = Double.parseDouble(cords[1]);
                    double z = Double.parseDouble(cords[2]);
                    if(path.equals("untitled.txt")){
                        System.out.println(x+ " " + y + " " + z + " coords");
                        /*y = y*5;
                        z = z*5;
                        x = x*5;*/
                    }
                    Vertex vertex = new Vertex(x,y,z);
                    System.out.println(vertex.getW()+"WWWW");
                    vertices.add(vertex);
                }
                if(nextLine.startsWith("f")){
                    String line = nextLine.replace("f ","");
                    String [] prepFaces = line.split(" ");
                    System.out.println(Arrays.toString(prepFaces));
                    if(prepFaces.length != 0){
                        ArrayList<Integer> list = new ArrayList<>();
                        for (int i = 0; i < prepFaces.length;i++) {
                            String [] finalFace = prepFaces[i].split("/");
                            System.out.println(Arrays.toString(finalFace));
                            System.out.println("lol");
                            int result = Integer.parseInt(finalFace[0])-1;
                            list.add(result);
                        }
                        Face face;
                        face = new Face(list);
                        faces.add(face);
                    }

                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Object3D object3D = new Object3D(vertices,faces);
        if(path.equals("Half-Cube.txt")){
            object3D.setHalfCube(true);
        }else{
            object3D.setHalfCube(false);
        }
        return object3D;
    }
}
