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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    static int WIDTH = Menu.WIDTH;
    static int HEIGHT = Menu.HEIGHT;

    static double CAMERA_SPEED = 0.5;
    static double CAMERA_ROT_SPEED = 2.5;

    static ArrayList<Object3D> objectList = new ArrayList<>();

    static ArrayList<Polyline> lineList = new ArrayList<>();

    static ArrayList<Text> textList = new ArrayList<>();

    static String cubePath = "Cube.txt";
    static double H_FOV = 90;

    static double MAX_BULLET_DISTANCE = 100;


    public static AnchorPane root = new AnchorPane();
    public static Scene scene = new Scene(root,WIDTH,HEIGHT);

    public static ArrayList<Bullet> bullets = new ArrayList<>();


    public static Camera camera;

    public static Text text = new Text();
    public static Text text1 = new Text();


    public static void init(){

        text.setX(100);
        text.setY(100);
        text.setFont(Font.font(50));
        text1.setX(100);
        text1.setY(200);
        text1.setFont(Font.font(50));
        root.getChildren().addAll(text,text1);

        camera = new Camera(new ArrayList<Vertex>(),new ArrayList<Face>(),0,0,0);
        Vertex camForward = camera.getForward();
        double [] camF = camForward.toArray();
        Vertex camUp = camera.getUp();
        double [] camU = camUp.toArray();
        Vertex camRight = camera.getRight();
        double [] camR = camRight.toArray();
        for (int i = 0; i < 10; i++) {
            ArrayList<Vertex> cubeHitbox = new ArrayList<>();
            cubeHitbox.add(new Vertex(1,0,-1));
            cubeHitbox.add(new Vertex(1,0,1));
            cubeHitbox.add(new Vertex(-1,0,1));
            cubeHitbox.add(new Vertex(-1,0,-1));
            ArrayList<Vertex> hitBox = new ArrayList<>();
                for (int j = 0; j < 4; j++) {
                    hitBox.add(cubeHitbox.get(j));
            }
            Object3D obj = Util.convertOBJ(cubePath);
            System.out.println("BRUH");
            Object3D obj1 = Util.generateOBJ(Math.random()*100-50,0,Math.random()*100-50,obj.getPoints3D(),obj.getFaces3D(),Color.BLACK, hitBox);
            obj1.displayObject();
        }
        Object3D obj3 = Util.convertOBJ("lowPolyTank.txt");
        ArrayList<Vertex> hitBox1 = new ArrayList<>();
        ArrayList<Vertex> triangleHitbox = new ArrayList<>();
        triangleHitbox.add(new Vertex(0.5,0,-0.5));
        triangleHitbox.add(new Vertex(0.5,0,0.5));
        triangleHitbox.add(new Vertex(-0.5,0,0.5));
        triangleHitbox.add(new Vertex(-0.5,0,-0.5));
        for (int j = 0; j < 4; j++) {
            hitBox1.add(triangleHitbox.get(j));
        }
        Object3D obj1 = Util.generateOBJ(0,-0.6,0,obj3.getPoints3D(),obj3.getFaces3D(), Color.RED, hitBox1);
        for (int i = 0; i < obj1.getPoints3D().size(); i++) {
            System.out.println(obj1.getPoints3D().get(i).toString() + "01");
        }
        obj1.displayObject();

        start();



        Menu.stage.setScene(scene);
    }


    public static void control(){
        scene.setOnKeyPressed(keyEvent -> {
            double rotation = camera.getRotation();
            Vertex camVert = camera.getPosition();
            double [] camArr = camVert.toArray();
            Vertex camForward = camera.getForward();
            double [] camF = camForward.toArray();
            Vertex camUp = camera.getUp();
            double [] camU = camUp.toArray();
            Vertex camRight = camera.getRight();
            double [] camR = camRight.toArray();

            if (keyEvent.getCode() == KeyCode.S){
                System.out.println("lol");
                for (int i = 0; i < 4; i++) {
                    camArr[i]-=camF[i]*CAMERA_SPEED;
                }
                camera.setPosition(Util.arrToVert(camArr));
            };
            if (keyEvent.getCode() == KeyCode.W){
                System.out.println("xd");
                for (int i = 0; i < 4; i++) {
                    camArr[i]+=camF[i]*CAMERA_SPEED;
                }
                camera.setPosition(Util.arrToVert(camArr));
            }; // Thrust forward
            if (keyEvent.getCode() == KeyCode.D){
                System.out.println("xd1");
                for (int i = 0; i < 4; i++) {
                    camArr[i]+=camR[i]*CAMERA_SPEED;
                }
                camera.setPosition(Util.arrToVert(camArr));
            };   // Rotate right
            if (keyEvent.getCode() == KeyCode.A){
                System.out.println("xd1");
                for (int i = 0; i < 4; i++) {
                    camArr[i]-=camR[i]*CAMERA_SPEED;
                }
                camera.setPosition(Util.arrToVert(camArr));
            }; // Rotate left
            if (keyEvent.getCode() == KeyCode.UP){
                //for (int i = 0; i < 4; i++) {
                    camArr[1]+=camU[1]*CAMERA_SPEED;
                //}
                camera.setPosition(Util.arrToVert(camArr));
            };   // Rotate right
            if (keyEvent.getCode() == KeyCode.DOWN){
                //for (int i = 0; i < 4; i++) {
                    camArr[1]-=camU[1]*CAMERA_SPEED;
               // }
                camera.setPosition(Util.arrToVert(camArr));
            }; // Rotate left
            if (keyEvent.getCode() == KeyCode.E){
                camF = Util.multiplyTransform(Util.getRotationYMatrix(CAMERA_ROT_SPEED), camF);
                System.out.println(Arrays.toString(camF)+ " 1MMMMMMMMMMMMM");
                camera.setForward(Util.arrToVert(camF));
                camR = Util.multiplyTransform(Util.getRotationYMatrix(CAMERA_ROT_SPEED), camR);
                camera.setRight(Util.arrToVert(camR));
                camera.updateRotation(CAMERA_ROT_SPEED);

            };
            if (keyEvent.getCode() == KeyCode.Q){
                camF = Util.multiplyTransform(Util.getRotationYMatrix(-CAMERA_ROT_SPEED), camF);
                System.out.println(Arrays.toString(camF)+ " MMMMMMMMMMMMM");
                camera.setForward(Util.arrToVert(camF));
                camR = Util.multiplyTransform(Util.getRotationYMatrix(-CAMERA_ROT_SPEED), camR);
                camera.setRight(Util.arrToVert(camR));
                camera.updateRotation(-CAMERA_ROT_SPEED);
            };
            /*if (keyEvent.getCode() == KeyCode.R){
                camF = Util.multiplyTransform(Util.getRotationXMatrix(-1), camF);
                System.out.println(Arrays.toString(camF)+ " 1MMMMMMMMMMMMM");
                camera.setForward(Util.arrToVert(camF));
                camU = Util.multiplyTransform(Util.getRotationXMatrix(-1), camU);
                camera.setUp(Util.arrToVert(camU));
            };
            if (keyEvent.getCode() == KeyCode.F){
                camF = Util.multiplyTransform(Util.getRotationXMatrix(1), camF);
                System.out.println(Arrays.toString(camF)+ " 1MMMMMMMMMMMMM");
                camera.setForward(Util.arrToVert(camF));
                camU = Util.multiplyTransform(Util.getRotationXMatrix(1), camU);
                camera.setUp(Util.arrToVert(camU));

            };*/
            if (keyEvent.getCode() == KeyCode.SPACE){
                System.out.println("EEEEEEEEEEEEEEE");
                camera.shootBullet();
            };

        });
        System.out.println("LLLLLLLLLLLL");
        System.out.println(camera.getPosition().toString());
        System.out.println(camera.getForward().toString());
        System.out.println(camera.getUp().toString());
        System.out.println(camera.getRight().toString());
        System.out.println("WWWWWWWWWWWWW");

    }

    public static void start() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000.0 / (Menu.FPS/2)), actionEvent -> {
            control();
            Vertex camForward = camera.getForward();
            double [] camF = camForward.toArray();
            System.out.println(Arrays.toString(camF)+ " MMMMMMMMMMMMM");
            for (Polyline polyline:lineList) {
                root.getChildren().remove(polyline);
            }
            lineList.clear();
            for (Text text:textList) {
                root.getChildren().remove(text);
            }
            textList.clear();
            text.setText(Double.toString(camera.getRotation()));
            text1.setText(Math.round(camera.getPosition().getX()) + " " + Math.round(camera.getPosition().getY()) + " " + Math.round(camera.getPosition().getZ()));
            if(!bullets.isEmpty()){
                Bullet bullet = bullets.get(0);
                System.out.println(">>>>>>>>>>>>>>>>>>");
                bullet.translate(bullet.getDirection().getX(),0,bullet.getDirection().getZ());
                double travelled = Math.sqrt(bullet.getDirection().getX()*bullet.getDirection().getX()+bullet.getDirection().getZ()*bullet.getDirection().getZ());
                bullet.setDistanceCovered(bullet.getDistanceCovered()+travelled);
                bullet.displayObject();
                if(bullet.getDistanceCovered()>MAX_BULLET_DISTANCE){
                    bullets.remove(bullet);
                    bullets.clear();
                }
                System.out.println("<<<<<<<<<<<<<<<<<<");
            }
            Polyline polyline = new Polyline(0,HEIGHT/2, WIDTH,HEIGHT/2);
            root.getChildren().add(polyline);

            for (Object3D object:objectList) {
                object.displayObject();
                if(!bullets.isEmpty()){
                    double dist = Math.sqrt(Math.pow((bullets.get(0).getX()-object.getX()),2)+Math.pow((bullets.get(0).getZ()-object.getZ()),2));
                    System.out.println(dist + " TROLOLOLOLOL");
                    if(dist<3){
                        Vertex vertex = bullets.get(0).checkForHits(object);
                        if(vertex!=null){
                            System.out.println("YYYYYYYYY");
                            object.setColor(Color.GREEN);
                            bullets.get(0).explode(vertex);
                            bullets.remove(bullets.get(0));
                            bullets.clear();
                    }}
                }
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }


}
