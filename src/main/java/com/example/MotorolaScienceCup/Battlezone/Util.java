package com.example.MotorolaScienceCup.Battlezone;

import javafx.scene.paint.Color;

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

    public static Bullet generateBullet(double[] dir,double firingAngle, double x, double y, double z, ArrayList<Vertex> points3D, ArrayList<Face> faces3D){
        System.out.println("PPPPPPPPP");
        Bullet bullet = new Bullet(points3D,faces3D);
        bullet.moveTo(0,0,0);
        bullet.scale(0.1,0.2,0.1);
        bullet.rotX(90);
        bullet.rotY(firingAngle);
        bullet.moveTo(x,y,z);
        bullet.setOrigin(new Vertex(x,0,z));
        bullet.setDirection(Util.arrToVert(dir));
        return bullet;
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

    public static double getMaxZ(ArrayList<Vertex> points){
        double z = points.get(0).getZ();
        for (int i = 0; i < points.size(); i++) {
            if(z<points.get(i).getX()){
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
            if(z>points.get(i).getX()){
                z = points.get(i).getZ();
            }
        }
        return z;
    }

    public static Vertex lineIntersect(Vertex a1, Vertex a2, Vertex b1, Vertex b2){
        ArrayList<Vertex> pointsA = new ArrayList<>();
        ArrayList<Vertex> pointsB = new ArrayList<>();
        pointsA.add(a1);
        pointsA.add(a2);
        pointsB.add(b1);
        pointsB.add(b2);
        double XmaxA = Util.getMaxX(pointsA);
        System.out.println(XmaxA);
        double ZmaxA = Util.getMaxZ(pointsA);
        System.out.println(ZmaxA);
        double XminA = Util.getMinX(pointsA);
        System.out.println(XminA);
        double ZminA = Util.getMinZ(pointsA);
        System.out.println(ZminA);
        double XmaxB = Util.getMaxX(pointsB);
        System.out.println(XmaxB);
        double ZmaxB = Util.getMaxZ(pointsB);
        System.out.println(ZmaxB);
        double XminB = Util.getMinX(pointsB);
        System.out.println(XminB);
        double ZminB = Util.getMinZ(pointsB);
        System.out.println(ZminB);
        double intersectX = 0;
        double intersectZ = 0;
        if(a1.getX()!=a2.getX()&&b1.getX()!=b2.getX()){
            System.out.println("%");
            double slopeA = (a1.getZ()-a2.getZ())/(a1.getX()-a2.getX());
            double slopeB = (b1.getZ()-b2.getZ())/(b1.getX()-b2.getX());
            if(slopeA==slopeB){
                System.out.println("!");
                return null;
            }
            double intersectA = -slopeA*a1.getX()+a1.getZ();
            double intersectB = -slopeB*b1.getX()+b1.getZ();
            intersectX = (intersectB-intersectA)/(slopeA-slopeB);
            intersectZ = slopeA*intersectX + intersectA;
        } else if (a1.getX()==a2.getX()&&b1.getX()!=b2.getX()) {
            System.out.println("rofl");
            double slopeB = (b1.getZ()-b2.getZ())/(b1.getX()-b2.getX());
            System.out.println(slopeB);
            double intersectB = -slopeB*b1.getX()+b1.getZ();
            intersectZ = a1.getZ();
            intersectX = (intersectZ-intersectB)/slopeB;
        } else if (a1.getX()!=a2.getX()&&b1.getX()==b2.getX()) {
            System.out.println("kek");
            double slopeA = (a1.getZ()-a2.getZ())/(a1.getX()-a2.getX());
            double intersectA = -slopeA*b1.getX()+a1.getZ();
            intersectZ = b1.getZ();
            intersectX = (intersectZ-intersectA)/slopeA;
        } else if(a1.getX()==a2.getX()&&b1.getX()==b2.getX()){
            System.out.println("*");
            return null;
        }
        System.out.println(intersectX + " " + intersectZ+" dab");
        if((intersectX<=XmaxA && intersectX>=XminA && intersectX<=XmaxB && intersectX>=XminB)&&
                (intersectZ<=ZmaxA && intersectZ>=ZminA && intersectZ<=ZmaxB && intersectZ>=ZminB)){
            System.out.println("?");
            return new Vertex(intersectX,0,intersectZ);
        }else{
            System.out.println("^");
            return null;
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
        //obj.scale(0.1,0.1,0.1);
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
                    System.out.println(Arrays.toString(cords));
                    double x = Double.parseDouble(cords[0]);
                    double y = Double.parseDouble(cords[1]);
                    double z = Double.parseDouble(cords[2]);
                    Vertex vertex = new Vertex(Math.round(x),Math.round(y),Math.round(z));
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
