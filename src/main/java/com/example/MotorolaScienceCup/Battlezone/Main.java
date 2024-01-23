package com.example.MotorolaScienceCup.Battlezone;


import com.example.MotorolaScienceCup.Asteroids.Asteroid;
import com.example.MotorolaScienceCup.Asteroids.Enemy;
import com.example.MotorolaScienceCup.Asteroids.HUD;
import com.example.MotorolaScienceCup.Menu;
import com.example.MotorolaScienceCup.Particle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    static int WIDTH = Menu.WIDTH;
    static int HEIGHT = Menu.HEIGHT;

    static double CAMERA_SPEED = 0.01;

    static ArrayList<Object3D> objectList = new ArrayList<>();

    static ArrayList<Polyline> lineList = new ArrayList<>();

    static String cubePath = "Cube.txt";
    static double H_FOV = 90;

    public static AnchorPane root = new AnchorPane();
    public static Scene scene = new Scene(root,WIDTH,HEIGHT);

    public static void init(){

        Camera camera = new Camera(new ArrayList<Vertex>(),new ArrayList<Face>(),2,-2,-10);
        Object3D obj = Util.convertOBJ(cubePath);
        System.out.println("BRUH");
        Object3D obj1 = Util.generateOBJ(0,0,0,obj.getPoints3D(),obj.getFaces3D());
        for (int i = 0; i < obj1.getPoints3D().size(); i++) {
            System.out.println(obj1.getPoints3D().get(i).toString() + "0");
        }
        obj1.rotX(0);
        obj1.displayObject();

        scene.setOnKeyPressed(keyEvent -> {
            Vertex camVert = Camera.getPosition();
            double [] camArr = camVert.toArray();
            Vertex camForward = Camera.getForward();
            double [] camF = camVert.toArray();
            Vertex camUp = Camera.getUp();
            double [] camU = camVert.toArray();
            Vertex camRight = Camera.getRight();
            double [] camR = camVert.toArray();

            if (keyEvent.getCode() == KeyCode.UP){
                System.out.println("lol");
                double[] newArr = new double[4];
                for (int i = 0; i < camArr.length; i++) {
                    newArr[i] = camArr[i] + camR[i]*CAMERA_SPEED;
                }
                Camera.setPosition(Util.arrToVert(newArr));
            };
            if (keyEvent.getCode() == KeyCode.DOWN){
                System.out.println("xd");
                double[] newArr = new double[4];
                for (int i = 0; i < camArr.length; i++) {
                    newArr[i] = camArr[i] -  camR[i]*CAMERA_SPEED;
                }
                Camera.setPosition(Util.arrToVert(newArr));
            }; // Thrust forward
            if (keyEvent.getCode() == KeyCode.RIGHT);   // Rotate right
            if (keyEvent.getCode() == KeyCode.LEFT) ; // Rotate left
            if (keyEvent.getCode() == KeyCode.E);
            if (keyEvent.getCode() == KeyCode.X);

        });
        start();



        Menu.stage.setScene(scene);
    }

    public static void start() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000.0 / Menu.FPS), actionEvent -> {
            for (Polyline polyline:lineList) {
                root.getChildren().remove(polyline);
            }

            for (Object3D object:objectList) {
                object.displayObject();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }


}
