package com.example.MotorolaScienceCup.Battlezone;


import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Menu;
import com.example.MotorolaScienceCup.Sound;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {

    static int WIDTH = Menu.WIDTH;
    static int HEIGHT = Menu.HEIGHT;
    static Timeline timeline;

    public static boolean wasHit = false;

    public static int score = 0;

    public static int hiScore = Util.getHiScore();

    static double CAMERA_SPEED = 0.1;
    static double CAMERA_ROT_SPEED = 0.5;

    static long frameCount=0;
    
    static double RADAR_ROT = 0;

    static double TEXT_TICK = 0;

    static boolean has_collided = false;

    static boolean isDying = false;

    static double death_ticks = 0;

    static BetterPolygon crack = new BetterPolygon(new ArrayList<>());

    static int impact_ticks = 0;
    static double impact_skip = 0.01;

    static int loadTimer = 0;

    static ArrayList<Object3D> horizon;

    static ArrayList<Object3D> objectList = new ArrayList<>();

    static ArrayList<EnemyTank> enemyTankList = new ArrayList<>();

    static ArrayList<SuperTank> superTankList = new ArrayList<>();

    static ArrayList<Missile> missileList = new ArrayList<>();

    static ArrayList<Mine> mineList = new ArrayList<>();

    static ArrayList<Ufo> ufoList = new ArrayList<>();

    static ArrayList<EnemyTank> fullTankList = new ArrayList<>();

    static ArrayList<Chunk> chunkList = new ArrayList<>();

    static ArrayList<Polyline> lineList = new ArrayList<>();

    static ArrayList<Circle> decals = new ArrayList<>();

    static ArrayList<Polyline> reticle = new ArrayList<>();

    static ArrayList<BetterPolygon> hearts = new ArrayList<>();

    static ArrayList<Text> textList = new ArrayList<>();

    static String cubePath = "Cube.txt";

    static String enemyDir = "";

    static boolean enemyInRange = false;

    static boolean collisionDir = false;

    static boolean forwardPressed = false;
    static boolean rearPressed = false;
    static boolean rightPressed = false;
    static boolean leftPressed = false;
    static boolean rotRightPressed = false;
    static boolean rotLeftPressed = false;

    static Clip isIdling = null;

    static Clip isMoving = null;

    static int playerHP = 3;

    static double H_FOV = 90;

    static double MAX_BULLET_DISTANCE = Camera.getFar()+10;

    public static AnchorPane root = new AnchorPane();
    public static Scene scene = new Scene(root,WIDTH,HEIGHT);

    public static ArrayList<Bullet> allBullets = new ArrayList<>();

    public static ArrayList<Particle> particles = new ArrayList<>();
    public static int MAX_PARTICLE_TICKS = 30;

    public static double PARTICLE_SPEED = 0.05;

    public static double PARTICLE_ROT_SPEED = 1;


    public static Camera camera;

    public static Vertex previousCamPos;




    public static void init(){
        score = 0;
        resetData();
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(0), new Insets(0))));
        AnchorPane rootLoad = new AnchorPane();
        Scene sceneLoad = new Scene(rootLoad,WIDTH,HEIGHT);
        Rectangle rectangle = new Rectangle(0,0,WIDTH,HEIGHT);
        rectangle.setFill(Color.BLACK);
        Text text10 = new Text("Loading...");
        text10.setFont(Font.font(50));
        text10.setLayoutX(WIDTH/2-(text10.getLayoutBounds().getWidth()/2));
        text10.setLayoutY(HEIGHT/2-(text10.getLayoutBounds().getHeight()/2));
        text10.setFill(Color.GREEN);
        text10.setFont(Menu.font);
        rootLoad.getChildren().addAll(rectangle, text10);
        Menu.stage.setScene(sceneLoad);
        ArrayList<Vertex> camHitbox = new ArrayList<>();
        camHitbox.add(new Vertex(2,0,0));
        camHitbox.add(new Vertex(Math.sqrt(2),0,Math.sqrt(2)));
        camHitbox.add(new Vertex(0,0,2));
        camHitbox.add(new Vertex(-Math.sqrt(2),0,Math.sqrt(2)));
        camHitbox.add(new Vertex(-2,0,0));
        camHitbox.add(new Vertex(-Math.sqrt(2),0,-Math.sqrt(2)));
        camHitbox.add(new Vertex(0,0,-2));
        camHitbox.add(new Vertex(Math.sqrt(2),0,-Math.sqrt(2)));
        ArrayList<Vertex> camPos = new ArrayList<>();
        camPos.add(new Vertex(0,0,0));
        camera = new Camera(camPos,new ArrayList<Face>(),0,0,0);
        camera.setHitBox2D(camHitbox);
        previousCamPos = new Vertex(camera.getX(), camera.getY(), camera.getZ());
        objectList.add(camera);
       /* *//*EnemyTank obj1 = Util.generateEnemyTank(0,10);
        SuperTank super1 = Util.generateSuperTank(10,20);*//*
        Missile missile = Util.generateMissile(0,100);
        Ufo ufo = Util.generateUfo(0,0);*/
        generateInitChunks();
        spawnEnemy();
        spawnUfo();
        drawHorizon2();
        start();



    }


    public static void control(){
        double rotation = camera.getRotation();
        Vertex camVert = new Vertex(camera.getX(),camera.getY(),camera.getZ());
        double [] camArr = camVert.toArray();
        Vertex camForward = camera.getForward();
        double [] camF = camForward.toArray();
        Vertex camUp = camera.getUp();
        double [] camU = camUp.toArray();
        Vertex camRight = camera.getRight();
        double [] camR = camRight.toArray();
        scene.setOnKeyPressed(keyEvent -> {

            if (keyEvent.getCode() == KeyCode.S&&!isDying){
                rearPressed = true;
            };
            if (keyEvent.getCode() == KeyCode.W&&!isDying){
                forwardPressed = true;
            }; // Thrust forward
            if (keyEvent.getCode() == KeyCode.D&&!isDying){
                rightPressed = true;
            };   // Rotate right
            if (keyEvent.getCode() == KeyCode.A&&!isDying){
                leftPressed = true;
            }; // Rotate left
            if (keyEvent.getCode() == KeyCode.E&&!isDying){
                rotRightPressed = true;

            };
            if (keyEvent.getCode() == KeyCode.Q&&!isDying){
                rotLeftPressed = true;
            };
            if (keyEvent.getCode() == KeyCode.SPACE && !isDying){
                System.out.println("EEEEEEEEEEEEEEE");
                camera.shootBullet();
            };

        });
        scene.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.S){
                rearPressed = false;
            };
            if (keyEvent.getCode() == KeyCode.W){
                forwardPressed = false;
            }; // Thrust forward
            if (keyEvent.getCode() == KeyCode.D){
                rightPressed = false;
            };   // Rotate right
            if (keyEvent.getCode() == KeyCode.A){
                leftPressed = false;
            }; // Rotate left
            if (keyEvent.getCode() == KeyCode.E){
                rotRightPressed = false;

            };
            if (keyEvent.getCode() == KeyCode.Q){
                rotLeftPressed = false;
            };
        });
        System.out.println("LLLLLLLLLLLL");
        //System.out.println(camera.getPosition().toString());
        System.out.println(camera.getForward().toString());
        System.out.println(camera.getUp().toString());
        System.out.println(camera.getRight().toString());
        System.out.println("WWWWWWWWWWWWW");
        if (rearPressed&&!rightPressed&&!leftPressed){
            System.out.println("lol");
            ArrayList<Vertex> hitbox = camera.getHitBox2D();
            ArrayList<Vertex> lol = new ArrayList<>();
            for (int i = 0; i < hitbox.size(); i++) {
                Vertex vert = hitbox.get(i);
                double[] arr = vert.toArray();
                arr = Util.multiplyTransform(Util.getTranslationMatrix(-camF[0]*CAMERA_SPEED, -camF[1]*CAMERA_SPEED, -camF[2]*CAMERA_SPEED),arr);
                lol.add(Util.arrToVert(arr));
            }
            System.out.println("hihihi");
            ArrayList<Object3D> array = camera.runCollisionCheck(7,lol,camera);
            if(array.size()==0) {
                System.out.println("LALALALA");
                camera.translate(-camF[0] * CAMERA_SPEED, -camF[1] * CAMERA_SPEED, -camF[2] * CAMERA_SPEED);
                collisionDir = false;
            }else{
                if(!collisionDir){
                    impactAnim();
                }
                collisionDir = true;
            }

        };
        if (forwardPressed&&!rightPressed&&!leftPressed){
            ArrayList<Vertex> hitbox = camera.getHitBox2D();
            ArrayList<Vertex> lol = new ArrayList<>();
            for (int i = 0; i < hitbox.size(); i++) {
                Vertex vert = hitbox.get(i);
                double[] arr = vert.toArray();
                arr = Util.multiplyTransform(Util.getTranslationMatrix(camF[0]*CAMERA_SPEED, camF[1]*CAMERA_SPEED, camF[2]*CAMERA_SPEED),arr);
                lol.add(Util.arrToVert(arr));
            }
            ArrayList<Object3D> array = camera.runCollisionCheck(7,lol,camera);
            if(array.size()==0){
                camera.translate(camF[0] * CAMERA_SPEED, camF[1] * CAMERA_SPEED, camF[2] * CAMERA_SPEED);
                collisionDir = false;
            }else{
                if(!collisionDir){
                    impactAnim();
                }
                collisionDir = true;
            }
        }; // Thrust forward
        if (rightPressed){
            camF = Util.multiplyTransform(Util.getRotationYMatrix(CAMERA_ROT_SPEED), camF);
            System.out.println(Arrays.toString(camF)+ " 1MMMMMMMMMMMMM");
            camera.setForward(Util.arrToVert(camF));
            camR = Util.multiplyTransform(Util.getRotationYMatrix(CAMERA_ROT_SPEED), camR);
            camera.setRight(Util.arrToVert(camR));
            camera.rotY(CAMERA_ROT_SPEED);
            collisionDir = false;
            ArrayList<Vertex> hitbox = camera.getHitBox2D();
            ArrayList<Vertex> lol = new ArrayList<>();
            for (int i = 0; i < hitbox.size(); i++) {
                Vertex vert = hitbox.get(i);
                double[] arr = vert.toArray();
                arr = Util.multiplyTransform(Util.getTranslationMatrix(camF[0]*CAMERA_SPEED, camF[1]*CAMERA_SPEED, camF[2]*CAMERA_SPEED),arr);
                lol.add(Util.arrToVert(arr));
            }
            ArrayList<Object3D> array = camera.runCollisionCheck(7,lol,camera);
            if(array.size()==0){
                camera.translate(camF[0] * CAMERA_SPEED, camF[1] * CAMERA_SPEED, camF[2] * CAMERA_SPEED);
                collisionDir = false;
            }else{
                if(!collisionDir){
                    impactAnim();
                }
                collisionDir = true;
            }
            //camera.updateRotation(CAMERA_ROT_SPEED);
        };   // Rotate right
        if (leftPressed){
            camF = Util.multiplyTransform(Util.getRotationYMatrix(-CAMERA_ROT_SPEED), camF);
            System.out.println(Arrays.toString(camF)+ " MMMMMMMMMMMMM");
            camera.setForward(Util.arrToVert(camF));
            camR = Util.multiplyTransform(Util.getRotationYMatrix(-CAMERA_ROT_SPEED), camR);
            camera.setRight(Util.arrToVert(camR));
            camera.rotY(-CAMERA_ROT_SPEED);
            collisionDir = false;
            //camera.updateRotation(-CAMERA_ROT_SPEED);
            ArrayList<Vertex> hitbox = camera.getHitBox2D();
            ArrayList<Vertex> lol = new ArrayList<>();
            for (int i = 0; i < hitbox.size(); i++) {
                Vertex vert = hitbox.get(i);
                double[] arr = vert.toArray();
                arr = Util.multiplyTransform(Util.getTranslationMatrix(camF[0]*CAMERA_SPEED, camF[1]*CAMERA_SPEED, camF[2]*CAMERA_SPEED),arr);
                lol.add(Util.arrToVert(arr));
            }
            ArrayList<Object3D> array = camera.runCollisionCheck(7,lol,camera);
            if(array.size()==0){
                camera.translate(camF[0] * CAMERA_SPEED, camF[1] * CAMERA_SPEED, camF[2] * CAMERA_SPEED);
                collisionDir = false;
            }else{
                if(!collisionDir){
                    impactAnim();
                }
                collisionDir = true;
            }
        }; // Rotate left
        if (rotRightPressed&&!rightPressed&&!leftPressed){
            camF = Util.multiplyTransform(Util.getRotationYMatrix(CAMERA_ROT_SPEED), camF);
            System.out.println(Arrays.toString(camF)+ " 1MMMMMMMMMMMMM");
            camera.setForward(Util.arrToVert(camF));
            camR = Util.multiplyTransform(Util.getRotationYMatrix(CAMERA_ROT_SPEED), camR);
            camera.setRight(Util.arrToVert(camR));
            camera.rotY(CAMERA_ROT_SPEED);
            collisionDir = false;
            //camera.updateRotation(CAMERA_ROT_SPEED);

        };
        if (rotLeftPressed&&!rightPressed&&!leftPressed){
            camF = Util.multiplyTransform(Util.getRotationYMatrix(-CAMERA_ROT_SPEED), camF);
            System.out.println(Arrays.toString(camF)+ " MMMMMMMMMMMMM");
            camera.setForward(Util.arrToVert(camF));
            camR = Util.multiplyTransform(Util.getRotationYMatrix(-CAMERA_ROT_SPEED), camR);
            camera.setRight(Util.arrToVert(camR));
            camera.rotY(-CAMERA_ROT_SPEED);
            collisionDir = false;
            //camera.updateRotation(-CAMERA_ROT_SPEED);
        };
        if((forwardPressed||rearPressed||rightPressed||leftPressed||rotRightPressed||rotLeftPressed)&&isMoving==null) {
            try {
                InputStream in = Sound.class.getResourceAsStream("playerMove.wav");
                InputStream of = new BufferedInputStream(in);
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(of);
                Clip clip = AudioSystem.getClip();
                clip.open(inputStream);
                FloatControl gainControl =
                        (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-5.0f);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
                isMoving=clip;
            }
            catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        }
        else if(!forwardPressed&&!rearPressed&&!rightPressed&&!leftPressed&&!rotRightPressed&&!rotLeftPressed){
            if(isMoving!=null){
                isMoving.close();
                isMoving=null;
            }
            if(isIdling==null){
            try {
                InputStream in = Sound.class.getResourceAsStream("playerIdle.wav");
                InputStream of = new BufferedInputStream(in);
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(of);
                Clip clip = AudioSystem.getClip();
                clip.open(inputStream);
                FloatControl gainControl =
                        (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-10.0f);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
                isIdling=clip;
            }
            catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                throw new RuntimeException(e);
            }}
        }

    }

    public static void drawRadar(){
        Circle circle = new Circle(WIDTH/2, 200, 100, Color.RED);
        Circle circle2 = new Circle(WIDTH/2, 200, 3, Color.RED);
        double x0 = WIDTH/2;
        double y0 = 200;
        double x1 = Math.cos(Math.toRadians(H_FOV/2)) * (0) - Math.sin(Math.toRadians(H_FOV/2)) * (-100) + x0;
        double y1 = Math.sin(Math.toRadians(H_FOV/2)) * (0) + Math.cos(Math.toRadians(H_FOV/2)) * (-100) + y0;
        double x2 = Math.cos(Math.toRadians(-H_FOV/2)) * (0) - Math.sin(Math.toRadians(-H_FOV/2)) * (-100) + x0;
        double y2 = Math.sin(Math.toRadians(-H_FOV/2)) * (0) + Math.cos(Math.toRadians(-H_FOV/2)) * (-100) + y0;
        Polyline polyline1 = new Polyline(x0,y0,x1,y1);
        Polyline polyline2 = new Polyline(x0,y0,x2,y2);
        double mX = Math.cos(Math.toRadians(RADAR_ROT)) * (0) - Math.sin(Math.toRadians(RADAR_ROT)) * (-100) + x0;
        double mY = Math.sin(Math.toRadians(RADAR_ROT)) * (0) + Math.cos(Math.toRadians(RADAR_ROT)) * (-100) + y0;
        Polyline movingLine = new Polyline(x0,y0,mX,mY);
        lineList.add(polyline1);
        lineList.add(polyline2);
        lineList.add(movingLine);
        movingLine.setStroke(Color.RED);
        polyline1.setStroke(Color.RED);
        polyline2.setStroke(Color.RED);
        root.getChildren().addAll(polyline1,polyline2,movingLine);
        enemyInRange=false;
        

        for (Object3D object:fullTankList) {
            Circle circle1 = new Circle();

        double dist = Math.sqrt(Math.pow((camera.getX()-object.getCenterX()),2)+Math.pow((camera.getZ()-object.getCenterZ()),2));
        if(dist < 100){
            enemyInRange = true;
            Vertex vertex = new Vertex(camera.getX(), camera.getY(), camera.getZ());
            Vertex obj = new Vertex(object.getX(), object.getY(), object.getZ());
            double[] arr = vertex.toArray();
            arr = Util.multiplyTransform(Util.getRotationYMatrix(-camera.getRotation()), arr);
            vertex = Util.arrToVert(arr);
            double[] arr1 = obj.toArray();
            arr1 = Util.multiplyTransform(Util.getRotationYMatrix(-camera.getRotation()), arr1);
            obj = Util.arrToVert(arr1);
            double x = obj.getX() - vertex.getX();
            double y = obj.getZ() - vertex.getZ();
            double angle = Math.toDegrees(Math.atan2(x,y));
            if(angle < 0){
                angle+=360;
            }
            System.out.println(angle + " angle");
            if(angle >= 150 && angle <= 210){
                enemyDir = "rear";
            }else if(angle > 210 && angle < 315){
                enemyDir = "left";
            }else if(angle < 150 && angle > 45){
                enemyDir = "right";
            }else{
                enemyDir="";
            }
            circle1 = new Circle(x, -y, 2, Color.RED);
            circle1.setCenterX(circle1.getCenterX()+circle.getCenterX());
            circle1.setCenterY(circle1.getCenterY()+circle.getCenterY());
            decals.add(circle1);
            root.getChildren().addAll(circle1);
            enemyInRange = true;
        }
        }
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.RED);
        decals.add(circle);
        decals.add(circle2);
        root.getChildren().addAll(circle2,circle);
    }


    public static void drawStatusText(){
        if(enemyInRange&&TEXT_TICK>15) {
            Text t = new Text("Enemy in range");
            t.setFont(Menu.font);
            t.setX(100);
            t.setY(300);
            t.setFill(Color.RED);
            textList.add(t);
            root.getChildren().add(t);
        }
        if(!enemyDir.isEmpty() &&TEXT_TICK > 15){
            Text t = new Text("Enemy to "+enemyDir);
            t.setFont(Menu.font);
            t.setX(100);
            t.setY(400);
            t.setFill(Color.RED);
            textList.add(t);
            root.getChildren().add(t);
        }
        if(collisionDir&&TEXT_TICK>10){
            try {
                Sound.play("jump.wav");
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                throw new RuntimeException(e);
            }
            Text t = new Text("Movement blocked by object");
            t.setFont(Menu.font);
            t.setX(100);
            t.setY(500);
            t.setFill(Color.RED);
            textList.add(t);
            root.getChildren().add(t);
        }
        Text s = new Text("Score      " + score);
        s.setFont(Menu.font);
        s.setX(WIDTH-WIDTH/4);
        s.setY(HEIGHT/10);
        s.setFill(Color.RED);
        textList.add(s);
        root.getChildren().add(s);
        Text h = new Text("HiScore   "+hiScore);
        h.setFont(Menu.font);
        h.setX(WIDTH-WIDTH/4);
        h.setY(HEIGHT/7);
        h.setFill(Color.RED);
        textList.add(h);
        root.getChildren().add(h);
    }

    public static void impactAnim(){
        has_collided = true;
    }

    public static void onGotShot(){
        isDying = true;
        wasHit = false;
        has_collided = true;
        CAMERA_SPEED = 0;
        CAMERA_ROT_SPEED = 0;
        playerHP--;
        for (Missile missile:missileList){
            missile.getMissileHum().close();
        }
        try {
            Sound.play("playerDeath1.wav");
            Sound.play("explosion.wav");
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
    }



    public static boolean spawnEnemy(){
        double check = new Random().nextDouble(13);
        if(check >= 4 && score < 10000 && check<8){
            check = new Random().nextDouble(4);
        }else if(check >= 8 && score < 15000){
            check = new Random().nextDouble(8);
        }
        boolean spawned = false;
        if(check<4){
            Vertex vertex = camera.getForward();
            double[] arr = vertex.toArray();
            double offset = Math.random()*90-45;
            arr = Util.multiplyTransform(Util.getRotationYMatrix(offset),arr);
            double scale = Math.random()*50+25;
            for (int i = 0; i < arr.length; i++) {
                arr[i]*=scale;
            }
            vertex = Util.arrToVert(arr);
            EnemyTank enemyTank = Util.generateEnemyTank(vertex.getX()+camera.getX(),vertex.getZ()+camera.getZ());
            boolean notCollided = enemyTank.runCollisionCheck(8, enemyTank.getCollideHitBox(), enemyTank).isEmpty();
            if(!notCollided){
                enemyTank.moveToRandom(45,15);
            }
            spawned = true;
        } else if(check>=4&&check<8&&score>=15000){
            Vertex vertex = camera.getForward();
            double[] arr = vertex.toArray();
            double offset = Math.random()*150-75;
            arr = Util.multiplyTransform(Util.getRotationYMatrix(offset),arr);
            double scale = Math.random()*50+25;
            for (int i = 0; i < arr.length; i++) {
                arr[i]*=scale;
            }
            vertex = Util.arrToVert(arr);
            SuperTank enemyTank = Util.generateSuperTank(vertex.getX()+camera.getX(),vertex.getZ()+camera.getZ());
            boolean notCollided = enemyTank.runCollisionCheck(8, enemyTank.getCollideHitBox(), enemyTank).isEmpty();
            if(!notCollided){
                enemyTank.moveToRandom(75,15);
            }
            spawned = true;
        } else if(check>=8&&score>=10000&&missileList.isEmpty()&&fullTankList.isEmpty()){
            Vertex vertex = camera.getForward();
            double[] arr = vertex.toArray();
            double offset = Math.random()*60-30;
            arr = Util.multiplyTransform(Util.getRotationYMatrix(offset),arr);
            double scale = Camera.getFar()-10;
            for (int i = 0; i < arr.length; i++) {
                arr[i]*=scale;
            }
            vertex = Util.arrToVert(arr);
            Missile enemyTank = Util.generateMissile(vertex.getX()+camera.getX(),vertex.getZ()+camera.getZ());
            boolean notCollided = enemyTank.runCollisionCheck(8, enemyTank.getCollideHitBox(), enemyTank).isEmpty();
            if(!notCollided){
                enemyTank.moveToRandom(30,Camera.getFar()-10);
            }
            spawned = true;
            Clip clip;
            try {
                File music = new File("missileHum.wav");
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(music);
                clip = AudioSystem.getClip();
                clip.open(inputStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
            }
            catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                throw new RuntimeException(e);
            }
            enemyTank.setMissileHum(clip);
        }
        return spawned;


    }

    public static void spawnUfo(){
            Vertex vertex = camera.getForward();
            double[] arr = vertex.toArray();
            double offset = Math.random()*150-75;
            arr = Util.multiplyTransform(Util.getRotationYMatrix(offset),arr);
            double scale = Math.random()*100+50;
            for (int i = 0; i < arr.length; i++) {
                arr[i]*=scale;
            }
            vertex = Util.arrToVert(arr);
            Ufo enemyTank = Util.generateUfo(vertex.getX()+camera.getX(),vertex.getZ()+camera.getZ());
            boolean notCollided = enemyTank.runCollisionCheck(8, enemyTank.getCollideHitBox(), enemyTank).isEmpty();
            if(!notCollided){
                enemyTank.moveToRandom(75,50);
            }
    }

    public static void generateInitChunks(){
        for (int i = -(int)Math.ceil((double) Chunk.chunkHiveSideLength /2)+1; i < (int)Math.ceil((double) Chunk.chunkHiveSideLength /2); i++) {
            for (int j = -(int)Math.ceil((double) Chunk.chunkHiveSideLength /2)+1; j < (int)Math.ceil((double) Chunk.chunkHiveSideLength /2); j++) {
                Chunk chunk = new Chunk((int)Math.round(i*Chunk.getSideLength()), (int)Math.round(j*Chunk.getSideLength()),chunkList);
                if(i==0&&j==0){
                    Chunk.setCenter(chunk);
                }
            }
        }
    }

    public static void start() {
        timeline = new Timeline(new KeyFrame(Duration.millis(1000.0 / (Menu.FPS)), actionEvent -> {
            frameCount++;
            boolean justDied = false;
            if((frameCount%120)==0){
                try {
                    Sound.play("radarPing.wav");
                } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(loadTimer<30){
                loadTimer++;
            }else{
                loadTimer++;
                Menu.stage.setScene(scene);
            }
            if(isDying){
                has_collided = false;
                camera.moveTo(camera.getX(), 0,camera.getZ());
            }
            wasHit = false;
            root.getChildren().remove(crack);
            fullTankList.clear();
            fullTankList.addAll(enemyTankList);
            fullTankList.addAll(superTankList);
            fullTankList.addAll(missileList);
            if(fullTankList.isEmpty()&&Math.random()*100<1){
                double enemyCount = Math.floor(score/20000)+1 < 3 ? Math.floor(score/20000)+1 : 3;
                int count = 0;
                for (int i = 0; i < enemyCount; i++) {
                    boolean spawned = spawnEnemy();
                    if(spawned){
                        count++;
                    }
                }
                if(count>0){
                    try {
                        Sound.play("spawnAlert.wav");
                    } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if(ufoList.isEmpty()&&Math.random()*480<1){
                spawnUfo();
            }
            TEXT_TICK++;
            if(camera.magTimer>=0){
                camera.magTimer--;
            }
            for (EnemyTank enemyTank:fullTankList) {
                if(enemyTank.getMagTimer()>=0 && !(enemyTank instanceof Missile || enemyTank instanceof Ufo)) {
                    enemyTank.setMagTimer(enemyTank.getMagTimer() - 1);
                }
            }
            if(TEXT_TICK>30){
                TEXT_TICK=0;
            }
            previousCamPos = new Vertex(camera.getX(), camera.getY(), camera.getZ());
            control();
            root.getChildren().removeAll(lineList);
            lineList.clear();
            root.getChildren().removeAll(decals);
            decals.clear();
            root.getChildren().removeAll(textList);
            textList.clear();
            Polyline polyline = new Polyline();
            polyline.setStroke(Color.GREEN);
            for (int i = 0; i < fullTankList.size(); i++) {
                fullTankList.get(i).enemyBehavior();
            }
            for (int i = 0; i < ufoList.size(); i++) {
                ufoList.get(i).enemyBehavior();
            }
            if(has_collided){
                if(impact_ticks==1){
                    try {
                        Sound.play("collision1.wav", 3.0f);
                    } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                if(impact_ticks < 5){
                    camera.translate(0,impact_skip*3,0);
                    polyline = new Polyline( 0,(HEIGHT/2)+(2), WIDTH,(HEIGHT/2)+(2));
                    polyline.setStroke(Color.GREEN);
                } else if (impact_ticks >= 5 && impact_ticks < 13) {
                    camera.translate(0,-impact_skip*1.5,0);
                    polyline = new Polyline( 0,(HEIGHT/2)-(1), WIDTH,(HEIGHT/2)-(1));
                    polyline.setStroke(Color.GREEN);
                }else{
                    polyline = new Polyline( 0,(HEIGHT/2), WIDTH,(HEIGHT/2));
                    polyline.setStroke(Color.GREEN);
                    impact_ticks = 0;
                    has_collided = false;
                    camera.moveTo(camera.getX(),0,camera.getZ());
                }
                impact_ticks++;
            }else{
                polyline = new Polyline( 0,(HEIGHT/2), WIDTH,(HEIGHT/2));
                polyline.setStroke(Color.GREEN);
            }
            if(isDying){
                death_ticks++;
                if(isIdling!=null){
                    isIdling.close();
                    isIdling=null;
                }
                if(isMoving!=null){
                    isMoving.close();
                    isMoving=null;
                }
                if(Ufo.ambient!=null){
                    Ufo.ambient.close();
                    Ufo.ambient=null;
                }
                if(death_ticks > 0 && death_ticks < 10){
                    ArrayList<ArrayList<Double>> arr = com.example.MotorolaScienceCup.Util.SVGconverterForLines("zgon1.svg");
                    double xAvg=85.258978;
                    double zAvg=154.94624;
                    for (int i = 0; i < arr.size(); i++) {
                        ArrayList<Double> array = arr.get(i);
                        for (int j = 0; j < array.size()-2; j+=2) {
                            Polyline polyline1 = new Polyline((array.get(j)-xAvg)*15+WIDTH/2-(WIDTH/10), (array.get(j+1)-zAvg)*15+HEIGHT/2, (array.get(j+2)-xAvg)*15+WIDTH/2-(WIDTH/10), (array.get(j+3)-zAvg)*15+HEIGHT/2);
                            polyline1.setStroke(Color.GREEN);
                            root.getChildren().add(polyline1);
                            lineList.add(polyline1);
                        }
                    }
                } else if (death_ticks >= 10 && death_ticks < 20) {
                    ArrayList<ArrayList<Double>> arr = com.example.MotorolaScienceCup.Util.SVGconverterForLines("zgon2.svg");
                    double xAvg=85.258978;
                    double zAvg=154.94624;
                    for (int i = 0; i < arr.size(); i++) {
                        ArrayList<Double> array = arr.get(i);
                        for (int j = 0; j < array.size()-2; j+=2) {
                            Polyline polyline1 = new Polyline((array.get(j)-xAvg)*15+WIDTH/2-(WIDTH/10), (array.get(j+1)-zAvg)*15+HEIGHT/2, (array.get(j+2)-xAvg)*15+WIDTH/2-(WIDTH/10), (array.get(j+3)-zAvg)*15+HEIGHT/2);
                            polyline1.setStroke(Color.GREEN);
                            root.getChildren().add(polyline1);
                            lineList.add(polyline1);
                        }
                    }
                } else if (death_ticks >=20 && death_ticks<30) {
                    ArrayList<ArrayList<Double>> arr = com.example.MotorolaScienceCup.Util.SVGconverterForLines("zgon3.svg");
                    double xAvg=87.397134;
                    double zAvg=153.07535;
                    for (int i = 0; i < arr.size(); i++) {
                        ArrayList<Double> array = arr.get(i);
                        for (int j = 0; j < array.size()-2; j+=2) {
                            Polyline polyline1 = new Polyline((array.get(j)-xAvg)*15+WIDTH/2-(WIDTH/10), (array.get(j+1)-zAvg)*15+HEIGHT/2, (array.get(j+2)-xAvg)*15+WIDTH/2-(WIDTH/10), (array.get(j+3)-zAvg)*15+HEIGHT/2);
                            polyline1.setStroke(Color.GREEN);
                            root.getChildren().add(polyline1);
                            lineList.add(polyline1);
                        }
                    }
                } else if (death_ticks >= 30 && death_ticks < 100) {
                    ArrayList<ArrayList<Double>> arr = com.example.MotorolaScienceCup.Util.SVGconverterForLines("zgon4.svg");
                    double xAvg=87.397134;
                    double zAvg=153.07535;
                    for (int i = 0; i < arr.size(); i++) {
                        ArrayList<Double> array = arr.get(i);
                        for (int j = 0; j < array.size()-2; j+=2) {
                            Polyline polyline1 = new Polyline((array.get(j)-xAvg)*15+WIDTH/2-(WIDTH/10), (array.get(j+1)-zAvg)*15+HEIGHT/2, (array.get(j+2)-xAvg)*15+WIDTH/2-(WIDTH/10), (array.get(j+3)-zAvg)*15+HEIGHT/2);
                            polyline1.setStroke(Color.GREEN);
                            root.getChildren().add(polyline1);
                            lineList.add(polyline1);
                        }
                    }
                } else if (death_ticks == 100) {
                    wasHit = false;
                    CAMERA_SPEED = 0.1;
                    CAMERA_ROT_SPEED = 0.5;
                    isDying = false;
                    death_ticks=0;
                    objectList.removeAll(fullTankList);
                    enemyTankList.clear();
                    missileList.clear();
                    superTankList.clear();
                    objectList.removeAll(ufoList);
                    ufoList.clear();
                    fullTankList.clear();
                    enemyDir = new String();

                    enemyInRange = false;

                    collisionDir = false;
                    textList.clear();
                    try {
                        isIdling = Sound.getClip("playerIdle.wav",-10.0f);
                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                        throw new RuntimeException(e);
                    }
                    isIdling.loop(Clip.LOOP_CONTINUOUSLY);
                    isIdling.start();
                    Vertex preDeathCamPos = new Vertex(camera.getX(), camera.getY(), camera.getZ());
                    camera.moveToRandom();
                    for(Object3D object3D:horizon){
                        object3D.translate(camera.getX()-preDeathCamPos.getX(),0, camera.getZ()-preDeathCamPos.getZ());
                    }
                    justDied = true;
                    if(playerHP<=0){
                        gameOver();
                    }
                }
                crack.setStroke(Color.GREEN);
                crack.scale(15);
                crack.moveTo(WIDTH/2-(WIDTH/10),HEIGHT/2);
                root.getChildren().add(crack);

            }
            lineList.add(polyline);
            root.getChildren().add(polyline);
            for (int i = 0 ; i<allBullets.size() ; i++) {
                Bullet bullet = allBullets.get(i);
                    System.out.println(">>>>>>>>>>>>>>>>>>");
                    bullet.translate(bullet.getDirection().getX(),0,bullet.getDirection().getZ());
                    double travelled = Math.sqrt(bullet.getDirection().getX()*bullet.getDirection().getX()+bullet.getDirection().getZ()*bullet.getDirection().getZ());
                    bullet.setDistanceCovered(bullet.getDistanceCovered()+travelled);
                    bullet.displayObject();
                    if(bullet.getDistanceCovered()>MAX_BULLET_DISTANCE){
                        allBullets.remove(bullet);
                    }
                    System.out.println("<<<<<<<<<<<<<<<<<<");
            }
            //System.out.println(objectList.get(10).getPoints3D().size() + " " + objectList.get(10).getFaces3D().size() + " GGGGGG");
            //objectList.get(10).rotY(1);
            for (int i=0;i<objectList.size();i++) {
                Object3D object = objectList.get(i);
                Vertex away;
                if(true){
                    away = new Vertex(object.getCenterX(), object.getCenterY(), object.getCenterZ());
                }else{
                    away = new Vertex(object.getX(), object.getY(), object.getZ());
                }
                double distance = Util.getDistance(new Vertex(camera.getX(),camera.getY(),camera.getZ()),away);
                if(object.getClass()!= Camera.class && distance < Camera.getFar()) {
                    object.displayObject();
                }
                boolean flying = false;
                boolean grounded = true;
                if(object instanceof Missile){
                    flying = ((Missile) object).isFlying();
                    grounded = ((Missile) object).isGrounded();
                }
                if(!allBullets.isEmpty()&&!(object instanceof Bullet)&&!(object instanceof Mine)&&(!flying||grounded)&&distance<Camera.getFar()+20){
                    for (int j = 0; j < allBullets.size(); j++) {
                        if(!allBullets.get(j).getParent().equals(object)){
                        double dist = Math.sqrt(Math.pow((allBullets.get(j).getX()-object.getX()),2)+Math.pow((allBullets.get(j).getZ()-object.getZ()),2));
                        System.out.println(dist + " TROLOLOLOLOL");
                        if(dist<3){
                            Vertex vertex = allBullets.get(j).checkForHits(object);
                            if(vertex!=null){
                                if(!(object instanceof Ufo)){
                                    try {
                                        Sound.play("explosion.wav");
                                    } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                System.out.println("YYYYYYYYY");
                                if(object instanceof EnemyTank){
                                    ((EnemyTank) object).takeHit(allBullets.get(j).getParent());
                                }
                                if(object.equals(camera)){
                                    onGotShot();
                                    wasHit = false;
                                }
                                allBullets.get(j).explode(vertex);
                                allBullets.remove(allBullets.get(j));
                            }
                        }
                }}}

            }
            if(!particles.isEmpty()){
                for (int i = 0; i < particles.size(); i++) {
                    Particle particle = particles.get(i);
                    particle.displayObject();
                    particle.translate(particle.getDirection().getX()*PARTICLE_SPEED, particle.getDirection().getY()*PARTICLE_SPEED, particle.getDirection().getZ()*PARTICLE_SPEED);
                    if(particle.getRotationVert()!=null){
                        particle.rotX(particle.getRotationVert().getX()*PARTICLE_ROT_SPEED);
                        particle.rotY(particle.getRotationVert().getY()*PARTICLE_ROT_SPEED);
                        particle.rotZ(particle.getRotationVert().getZ()*PARTICLE_ROT_SPEED);
                    }
                    particle.setTickrate(particle.getTickrate()+1);
                    if(particle.getTickrate() > MAX_PARTICLE_TICKS){
                        particles.remove(particle);
                    }
                }
            }
            root.getChildren().removeAll(hearts);
            hearts.clear();
            for (int i = 0; i < playerHP; i++) {
                double x = WIDTH-WIDTH/4.25 + i*100;
                addHeart(x);
            }
            RADAR_ROT+=1;
            if(RADAR_ROT>=360){
                RADAR_ROT=1-(360-RADAR_ROT);
            } else if (RADAR_ROT<0) {
                RADAR_ROT=360+(1-RADAR_ROT);
            }
            if(!justDied){
            for(Object3D object3D:horizon){
                for (int i = 0; i < object3D.getPoints3D().size(); i++) {
                    double [][] translationMatrix = Util.getTranslationMatrix(camera.getX()-previousCamPos.getX(),0,camera.getZ()-previousCamPos.getZ());
                    double [] arr = object3D.getPoints3D().get(i).toArray();
                    arr = Util.multiplyTransform(translationMatrix, arr);
                    System.out.println(Arrays.toString(arr)+"nice");
                    object3D.getPoints3D().set(i,Util.arrToVert(arr));

                }
            }}
            //drawHorizon();
            if(!wasHit) {
                drawRadar();
                drawStatusText();
            }
            root.getChildren().removeAll(reticle);
            reticle.clear();
            boolean check = false;
            for (int i = 0; i < objectList.size(); i++) {
                if(camera.checkReticle(objectList.get(i))){
                    check = true;
                }
            }
            if(check){
                Util.drawScopedReticle();
            }else{
                Util.drawUnscopedReticle();
            }
            for (Mine mine : mineList){
                mine.checkCamera();
            }
            for (Chunk chunk:chunkList){
                chunk.checkHasPlayer();
            }
            ArrayList<Chunk> tempChunk = new ArrayList<>();
            for (int i = 0; i < chunkList.size(); i++) {
                Chunk chunk = chunkList.get(i);
                if(Math.abs(chunk.checkDistanceX(Chunk.center))>Chunk.sideLength*(int)Math.floor((double) Chunk.chunkHiveSideLength /2)){
                    int x = -chunk.checkDistanceX(Chunk.getOldCenter());
                    Chunk chunk1 = new Chunk((int)Math.round(Chunk.getCenter().getX()+x), (int)Math.round(chunk.getZ()), tempChunk);
                    chunk.unloadChunk();
                }else if(Math.abs(chunk.checkDistanceZ(Chunk.center))>Chunk.sideLength*(int)Math.floor((double) Chunk.chunkHiveSideLength /2)){
                    int z = -chunk.checkDistanceZ(Chunk.getOldCenter());
                    Chunk chunk1 = new Chunk((int)Math.round(chunk.getX()), (int)Math.round(Chunk.getCenter().getZ()+z), tempChunk);
                    chunk.unloadChunk();
                }
            }
            chunkList.addAll(tempChunk);
            tempChunk.clear();
            if(wasHit){
                onGotShot();
            }

        }));
        Text back = new Text("←");
        back.setFill(Color.RED);
        back.setFont(Menu.font);
        back.setLayoutX(WIDTH/20);
        back.setLayoutY(100);
        back.setOnMouseClicked(mouseEvent -> {
            try {
                timeline.stop();
                resetData();
                if(Ufo.ambient!=null){
                    Ufo.ambient.close();
                    Ufo.ambient = null;
                }
                if(isMoving!=null){
                    isMoving.close();
                    isMoving = null;
                }
                if(isIdling!=null){
                    isIdling.close();
                    isIdling = null;
                }
                for(Missile missile:missileList){
                    missile.getMissileHum().close();
                }
                Menu.resetMenu();
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        });
        root.getChildren().add(back);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


    }
    
    static void resetData(){
        if(camera!=null) {
            camera.setMagTimer(-1);
        }
        playerHP = 3;
        for(Clip clip: Menu.clips){
            clip.close();
        }
        Timeline timeline;
          WIDTH = Menu.WIDTH;
          HEIGHT = Menu.HEIGHT;
          hiScore = Util.getHiScore();

           wasHit = false;

           score = 0;

          CAMERA_SPEED = 0.1;
          CAMERA_ROT_SPEED = 0.5;

          RADAR_ROT = 0;

          TEXT_TICK = 0;

          has_collided = false;

          isDying = false;

          death_ticks = 0;

          crack = new BetterPolygon(new ArrayList<>());

          frameCount = 0;

          impact_ticks = 0;
          impact_skip = 0.01;

         objectList = new ArrayList<>();

        enemyTankList = new ArrayList<>();

         superTankList = new ArrayList<>();

         missileList = new ArrayList<>();

          mineList = new ArrayList<>();

         ufoList = new ArrayList<>();

          fullTankList = new ArrayList<>();

          chunkList = new ArrayList<>();

          lineList = new ArrayList<>();

          decals = new ArrayList<>();

         reticle = new ArrayList<>();

         textList = new ArrayList<>();

         cubePath = "Cube.txt";

         enemyDir = "";

          enemyInRange = false;

          collisionDir = false;

          forwardPressed = false;
          rearPressed = false;
          rightPressed = false;
          leftPressed = false;
          rotRightPressed = false;
          rotLeftPressed = false;

          H_FOV = 90;

         MAX_BULLET_DISTANCE = Camera.getFar()+10;

        root = new AnchorPane();
        scene = new Scene(root,WIDTH,HEIGHT);

       allBullets = new ArrayList<>();

       particles = new ArrayList<>();
       MAX_PARTICLE_TICKS = 30;

           PARTICLE_SPEED = 0.05;

           PARTICLE_ROT_SPEED = 1;


    }
    public static void gameOver() {
        timeline.stop();
        for (Missile missile:missileList){
            missile.getMissileHum().close();
        }
        if(Ufo.ambient!=null){
            Ufo.ambient.close();
            Ufo.ambient = null;
        }
        if(isMoving!=null){
            isMoving.close();
            isMoving = null;
        }
        if(isIdling!=null){
            isIdling.close();
            isIdling = null;
        }
        Text gameOverText = new Text("Game Over");
        gameOverText.setFont(Menu.font);
        gameOverText.setFill(Color.GREEN);

        gameOverText.setX(WIDTH/2 - gameOverText.getLayoutBounds().getWidth()/2);
        gameOverText.setY(HEIGHT/2);



        AnchorPane newRoot = new AnchorPane();
        newRoot.getChildren().add(gameOverText);
        Button restart = new Button("Restart");
        restart.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(0), new Insets(0))));
        restart.setFont(Menu.font);
        restart.setLayoutX(WIDTH/2 - 275 - restart.getWidth()/2);
        restart.setLayoutY(HEIGHT/2 + 100);


        Button menu = new Button("Menu");
        menu.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(0), new Insets(0))));
        menu.setFont(Menu.font);
        menu.setLayoutX(WIDTH/2 + 75 + menu.getWidth()/2);
        menu.setLayoutY(HEIGHT/2 + 100);
        menu.setOnAction(actionEvent -> {
            try {
                Menu.resetMenu();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        });
        restart.setOnAction(actionEvent -> {
            init();
        });

        newRoot.getChildren().addAll(restart, menu);
        Scene newScene = new Scene(newRoot, WIDTH, HEIGHT);
        newScene.setFill(Color.BLACK);
        newRoot.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(0), new Insets(0))));
        Menu.stage.setScene(newScene);
        InputStream in = Sound.class.getResourceAsStream("battlescore.txt");
        InputStream of = new BufferedInputStream(in);

        Scanner scanner = null;
        scanner = new Scanner(of);
        System.out.println(scanner.hasNextLine());
        if (scanner.hasNextLine()) {

            int highscore = Integer.parseInt(scanner.nextLine());
            System.out.println(highscore);
            /*if (score > highscore) {
                Writer writer = null;
                try {
                    writer = new FileWriter(new File("src/main/resources/com/example/MotorolaScienceCup/battlescore.txt"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    writer.write(score + "");
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }*/
        }
        /*else {
            System.out.println(score);
            Writer writer = null;
            try {
                writer = new FileWriter(new File("src/main/resources/com/example/MotorolaScienceCup/battlescore.txt"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                writer.write(score + "");
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }*/
    }
    public static void addHeart(double x) {
        BetterPolygon heart = new com.example.MotorolaScienceCup.Particle(com.example.MotorolaScienceCup.Util.SVGconverter("heart.svg"), 0, 0, 0, 0);
        heart.setStroke(Color.RED);
        heart.setLayoutX(x);
        heart.setLayoutY((double) HEIGHT /20);
        heart.scale(2);
        hearts.add(heart);
        root.getChildren().add(heart);
    }


    public static void drawHorizon2(){
        horizon = Util.generateHorizon(0,0,0,new Vertex(80,30,80));
    }
}
