package com.example.MotorolaScienceCup.Battlezone;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Util {
    public static double [] multiplyTransform(double[][] matrix1, double[] array){
        double [] finalArray = new double[4];
        for (int i = 0; i < array.length; i++) {
            double result = 0;
            for (int j = 0; j < array.length; j++) {
                result += array[j]*matrix1[j][i];
            }
            finalArray[i]=result;

        }
        return finalArray;
    }
    public static Object3D convertOBJ(String path){
        ArrayList<Vertex> vertices = new ArrayList<>();
        ArrayList<Face> faces = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(path));
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                if(nextLine.startsWith("v")){
                    nextLine.replaceAll("v ","");
                    String [] cords = nextLine.split(" ");
                    double x = Double.parseDouble(cords[0]);
                    double y = Double.parseDouble(cords[1]);
                    double z = Double.parseDouble(cords[2]);
                    Vertex vertex = new Vertex(x,y,z);
                    vertices.add(vertex);
                }
                if(nextLine.startsWith("f")){
                    nextLine.replaceAll("f ","");
                    String [] prepFaces = nextLine.split(" ");
                    if(prepFaces.length != 0){
                        double a = 0;
                        double b = 0;
                        double c = 0;
                        double d = 0;
                        for (int i = 0; i < prepFaces.length;i++) {
                           String [] finalFace = prepFaces[i].split("/");
                           double result = Double.parseDouble(finalFace[0]);
                           if(i==0){
                               a = result;
                           } else if (i==1) {
                             b = result;
                           } else if (i==2) {
                                c = result;
                           } else if (i==3 && prepFaces.length == 4) {
                                d = result;
                           }
                        }
                        Face face;
                        if(prepFaces.length == 3){
                            face = new Face(a,b,c);
                        } else{
                            face = new Face(a,b,c,d);
                        }
                        faces.add(face);
                    }

                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Object3D object3D = new Object3D(0,0,0, vertices, faces);
        return object3D;
    }
}
