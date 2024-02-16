package com.example.MotorolaScienceCup.Battlezone;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static com.example.MotorolaScienceCup.Battlezone.Bullet generateBullet(double[] dir, double firingAngle, double x, double y, double z, ArrayList<Vertex> points3D, ArrayList<Face> faces3D){
        System.out.println("PPPPPPPPP");
        Bullet bullet = new Bullet(points3D,faces3D);

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

    public static EnemyTank generateEnemyTank(double x, double y, double z, ArrayList<Vertex> points3D, ArrayList<Face> faces3D){
        System.out.println("PPPPPPPPP");
        EnemyTank enemy = new EnemyTank(points3D,faces3D);
        ArrayList<Vertex> hitbox = new ArrayList<>();
        Object3D object3D = Util.convertOBJ("ring.txt");
        ArrayList<Vertex> hitbox1 = new ArrayList<>(object3D.getPoints3D());
        hitbox.add(new Vertex(getMaxX(points3D),0, getMaxZ(points3D)));
        hitbox.add(new Vertex(getMaxX(points3D),0, getMinZ(points3D)));
        hitbox.add(new Vertex(getMinX(points3D),0, getMinZ(points3D)));
        hitbox.add(new Vertex(getMinX(points3D),0, getMaxZ(points3D)));
        enemy.setHitBox2D(hitbox);
        enemy.setCollideHitBox(hitbox1);
        enemy.setForward(new Vertex(-1,0,0));
        enemy.setCenter(new Vertex(0,0,0));
        enemy.scaleTank(1.5,1.15,1.5);
        enemy.moveTank(new Vertex(-enemy.getCenterX(),0,-enemy.getCenterZ()));
        enemy.rotateTank(90);
        enemy.setRotation(0);
        enemy.rotateTank(Math.random()*360);
        enemy.moveTank(new Vertex(x,y,z));
        enemy.setTarget(new Vertex(enemy.getCenter().getX() + Math.random()*60-30,0,enemy.getCenter().getZ() + Math.random()*60-30));
        enemy.setColor(Color.GREEN);
        enemy.setAttackMode(false);
        enemy.setWillShoot(false);
        enemy.setRotating(true);
        enemy.setRotateDir(-1);
        enemy.setTargetRotation(enemy.getLookAt(enemy.getTarget()));
        Main.objectList.add(enemy);
        Main.enemyTankList.add(enemy);
        return enemy;
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
        return object3D;
    }
}
