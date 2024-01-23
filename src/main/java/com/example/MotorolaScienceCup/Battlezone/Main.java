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

    static double CAMERA_SPEED = 0.1;

    static ArrayList<Object3D> objectList = new ArrayList<>();

    static ArrayList<Polyline> lineList = new ArrayList<>();

    static String cubePath = "Cube.txt";
    static double H_FOV = 90;

    public static AnchorPane root = new AnchorPane();
    public static Scene scene = new Scene(root,WIDTH,HEIGHT);

    public static Camera camera;

    public static void init(){

        camera = new Camera(new ArrayList<Vertex>(),new ArrayList<Face>(),-5,5,40);
        Object3D obj = Util.convertOBJ("cube.txt");
        Object3D obj3 = Util.convertOBJ(cubePath);
        System.out.println("BRUH");
        Object3D obj1 = Util.generateOBJ(10,-5,-50,obj.getPoints3D(),obj.getFaces3D());
        Object3D obj2 = Util.generateOBJ(3,0,-4,obj3.getPoints3D(),obj3.getFaces3D());
        for (int i = 0; i < obj1.getPoints3D().size(); i++) {
            System.out.println(obj1.getPoints3D().get(i).toString() + "01");
        }
        obj1.rotZ(30);
        obj2.displayObject();
        obj1.displayObject();

        start();



        Menu.stage.setScene(scene);
    }

    public static void control(){
        scene.setOnKeyPressed(keyEvent -> {
            Vertex camVert = camera.getPosition();
            double [] camArr = camVert.toArray();
            Vertex camForward = camera.getForward();
            double [] camF = camVert.toArray();
            Vertex camUp = camera.getUp();
            double [] camU = camVert.toArray();
            Vertex camRight = camera.getRight();
            double [] camR = camVert.toArray();

            if (keyEvent.getCode() == KeyCode.S){
                System.out.println("lol");
                for (int i = 0; i < 4; i++) {
                    camArr[i]+=camF[i]*CAMERA_SPEED;
                }
                camera.setPosition(Util.arrToVert(camArr));
            };
            if (keyEvent.getCode() == KeyCode.W){
                System.out.println("xd");
                for (int i = 0; i < 4; i++) {
                    camArr[i]-=camF[i]*CAMERA_SPEED;
                }
                camera.setPosition(Util.arrToVert(camArr));
            }; // Thrust forward
            if (keyEvent.getCode() == KeyCode.RIGHT){
                System.out.println("xd1");
                for (int i = 0; i < 4; i++) {
                    camArr[i]-=camR[i]*CAMERA_SPEED;
                }
                camera.setPosition(Util.arrToVert(camArr));
            };   // Rotate right
            if (keyEvent.getCode() == KeyCode.LEFT){
                System.out.println("xd1");
                for (int i = 0; i < 4; i++) {
                    camArr[i]+=camR[i]*CAMERA_SPEED;
                }
                camera.setPosition(Util.arrToVert(camArr));
            }; // Rotate left
            if (keyEvent.getCode() == KeyCode.UP){
                camArr[1]-=CAMERA_SPEED;
                camera.setPosition(Util.arrToVert(camArr));
            };   // Rotate right
            if (keyEvent.getCode() == KeyCode.DOWN){
                camArr[1]+=CAMERA_SPEED;
                camera.setPosition(Util.arrToVert(camArr));
            }; // Rotate left
            if (keyEvent.getCode() == KeyCode.E){
                double x = camVert.getX();
                double y = camVert.getY();
                double z = camVert.getZ();
                double [] arr = camVert.toArray();
                arr = Util.multiplyTransform(Util.getTranslationMatrix(-x,-y,-z),arr);
                arr = Util.multiplyTransform(Util.getRotationYMatrix(CAMERA_SPEED), arr);
                arr = Util.multiplyTransform(Util.getTranslationMatrix(x,y,z),arr);
                camera.setPosition(Util.arrToVert(arr));
            };
            if (keyEvent.getCode() == KeyCode.Q){
                camArr = Util.multiplyTransform(Util.getRotationYMatrix(-CAMERA_SPEED), camArr);
                camera.setPosition(Util.arrToVert(camArr));
            };
            if (keyEvent.getCode() == KeyCode.X);

        });
    }

    public static void start() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000.0 / Menu.FPS), actionEvent -> {
            control();
            for (Polyline polyline:lineList) {
                root.getChildren().remove(polyline);
            }
            lineList.clear();

            for (Object3D object:objectList) {
                object.rotY(1);
                object.displayObject();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }


}
