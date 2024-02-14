package com.example.MotorolaScienceCup.Battlezone;


import com.example.MotorolaScienceCup.Asteroids.Asteroid;
import com.example.MotorolaScienceCup.Asteroids.Enemy;
import com.example.MotorolaScienceCup.Asteroids.HUD;
import com.example.MotorolaScienceCup.Menu;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
    static Timeline timeline;

    static double CAMERA_SPEED = 0.3;
    static double CAMERA_ROT_SPEED = 2.5;
    
    static double RADAR_ROT = 0;

    static double TEXT_TICK = 0;

    static boolean has_collided = false;

    static int impact_ticks = 0;
    static double impact_skip = 0.01;

    static ArrayList<Object3D> objectList = new ArrayList<>();

    static ArrayList<EnemyTank> enemyTankList = new ArrayList<>();

    static ArrayList<Polyline> lineList = new ArrayList<>();

    static ArrayList<Circle> decals = new ArrayList<>();

    static ArrayList<Polyline> reticle = new ArrayList<>();

    static ArrayList<Text> textList = new ArrayList<>();

    static String cubePath = "Cube.txt";

    static String enemyDir = "";

    static boolean enemyInRange = false;

    static boolean collisionDir = false;

    static double H_FOV = 90;

    static double MAX_BULLET_DISTANCE = 100;


    public static AnchorPane root = new AnchorPane();
    public static Scene scene = new Scene(root,WIDTH,HEIGHT);

    public static ArrayList<Bullet> bullets = new ArrayList<>();

    public static ArrayList<Particle> particles = new ArrayList<>();
    public static int MAX_PARTICLE_TICKS = 30;

    public static double PARTICLE_SPEED = 0.05;


    public static Camera camera;

    public static Text text = new Text();
    public static Text text1 = new Text();


    public static void init(){
        scene.setFill(Color.BLACK);
        root.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(0), new Insets(0))));

        text.setX(100);
        text.setY(100);
        text.setFont(Font.font(50));
        text1.setX(100);
        text1.setY(200);
        text1.setFont(Font.font(50));
        text.setFill(Color.GREEN);
        text1.setFill(Color.GREEN);
        root.getChildren().addAll(text,text1);
        ArrayList<Vertex> cubeHitbox = new ArrayList<>();
        cubeHitbox.add(new Vertex(1.05,0,-1.05));
        cubeHitbox.add(new Vertex(1.05,0,1.05));
        cubeHitbox.add(new Vertex(-1.05,0,1.05));
        cubeHitbox.add(new Vertex(-1.05,0,-1.05));
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
        for (int i = 0; i < 10; i++) {
            ArrayList<Vertex> hitBox = new ArrayList<>();
                for (int j = 0; j < 4; j++) {
                    hitBox.add(cubeHitbox.get(j));
            }
            Object3D obj = Util.convertOBJ(cubePath);
            System.out.println("BRUH");
            Object3D obj1 = Util.generateOBJ(Math.random()*100-50,0,Math.random()*100-50,obj.getPoints3D(),obj.getFaces3D(),Color.GREEN, hitBox);
            obj1.displayObject();
        }
        Object3D obj3 = Util.convertOBJ("normalTank.txt");
        ArrayList<Vertex> hitBox1 = new ArrayList<>();
        ArrayList<Vertex> triangleHitbox = new ArrayList<>();
        triangleHitbox.add(new Vertex(0.6,0,-0.6));
        triangleHitbox.add(new Vertex(0.6,0,0.6));
        triangleHitbox.add(new Vertex(-0.6,0,0.6));
        triangleHitbox.add(new Vertex(-0.6,0,-0.6));
        for (int j = 0; j < 4; j++) {
            hitBox1.add(triangleHitbox.get(j));
        }
        double y = Util.getMinY(obj3.getPoints3D());
        EnemyTank obj1 = Util.generateEnemyTank(0,0.5+y-0.1,10,obj3.getPoints3D(),obj3.getFaces3D());
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
            Vertex camVert = camera.getPoints3D().get(0);
            double [] camArr = camVert.toArray();
            Vertex camForward = camera.getForward();
            double [] camF = camForward.toArray();
            Vertex camUp = camera.getUp();
            double [] camU = camUp.toArray();
            Vertex camRight = camera.getRight();
            double [] camR = camRight.toArray();

            if (keyEvent.getCode() == KeyCode.S){
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
                if(camera.runCollisionCheck(5,lol)) {
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
            if (keyEvent.getCode() == KeyCode.W){
                ArrayList<Vertex> hitbox = camera.getHitBox2D();
                ArrayList<Vertex> lol = new ArrayList<>();
                for (int i = 0; i < hitbox.size(); i++) {
                    Vertex vert = hitbox.get(i);
                    double[] arr = vert.toArray();
                    arr = Util.multiplyTransform(Util.getTranslationMatrix(camF[0]*CAMERA_SPEED, camF[1]*CAMERA_SPEED, camF[2]*CAMERA_SPEED),arr);
                    lol.add(Util.arrToVert(arr));
                }
                if(camera.runCollisionCheck(5,lol)){
                    camera.translate(camF[0] * CAMERA_SPEED, camF[1] * CAMERA_SPEED, camF[2] * CAMERA_SPEED);
                    collisionDir = false;
                }else{
                    if(!collisionDir){
                        impactAnim();
                    }
                    collisionDir = true;
                }
            }; // Thrust forward
            if (keyEvent.getCode() == KeyCode.D){
                ArrayList<Vertex> hitbox = camera.getHitBox2D();
                ArrayList<Vertex> lol = new ArrayList<>();
                for (int i = 0; i < hitbox.size(); i++) {
                    Vertex vert = hitbox.get(i);
                    double[] arr = vert.toArray();
                    arr = Util.multiplyTransform(Util.getTranslationMatrix(camR[0]*CAMERA_SPEED, camR[1]*CAMERA_SPEED, camR[2]*CAMERA_SPEED),arr);
                    lol.add(Util.arrToVert(arr));
                }
                if(camera.runCollisionCheck(5,lol)){
                   // for (int i = 0; i < 4; i++) {
                    camera.translate( camR[0] * CAMERA_SPEED,  camR[1] * CAMERA_SPEED,  camR[2] * CAMERA_SPEED);
                    collisionDir = false;
                }else{
                    if(!collisionDir){
                        impactAnim();
                    }
                    collisionDir = true;
                }
            };   // Rotate right
            if (keyEvent.getCode() == KeyCode.A){
                ArrayList<Vertex> hitbox = camera.getHitBox2D();
                ArrayList<Vertex> lol = new ArrayList<>();
                for (int i = 0; i < hitbox.size(); i++) {
                    Vertex vert = hitbox.get(i);
                    double[] arr = vert.toArray();
                    arr = Util.multiplyTransform(Util.getTranslationMatrix(-camR[0]*CAMERA_SPEED, -camR[1]*CAMERA_SPEED, -camR[2]*CAMERA_SPEED),arr);
                    lol.add(Util.arrToVert(arr));
                }
                if(camera.runCollisionCheck(5,lol)){
                    //for (int i = 0; i < 4; i++) {
                        camera.translate(  -camR[0] * CAMERA_SPEED,   -camR[1] * CAMERA_SPEED,   -camR[2] * CAMERA_SPEED);
                        collisionDir = false;
                }
                else{
                    if(!collisionDir){
                        impactAnim();
                    }
                    collisionDir = true;
                }
            }; // Rotate left
            if (keyEvent.getCode() == KeyCode.UP){
                ArrayList<Vertex> hitbox = camera.getHitBox2D();
                for (int i = 0; i < hitbox.size(); i++) {
                    Vertex vert = hitbox.get(i);
                    double[] arr = vert.toArray();
                    arr = Util.multiplyTransform(Util.getTranslationMatrix( camU[0]*CAMERA_SPEED,  camU[1]*CAMERA_SPEED,  camU[2]*CAMERA_SPEED),arr);
                    hitbox.set(i, Util.arrToVert(arr));
                }
                if(camera.runCollisionCheck(3,hitbox)){
                    //for (int i = 0; i < 4; i++) {
                    camera.translate( camU[0] * CAMERA_SPEED,  camU[1] * CAMERA_SPEED,  camU[2] * CAMERA_SPEED);
                }
            };   // Rotate right
            if (keyEvent.getCode() == KeyCode.DOWN){
                ArrayList<Vertex> hitbox = camera.getHitBox2D();
                for (int i = 0; i < hitbox.size(); i++) {
                    Vertex vert = hitbox.get(i);
                    double[] arr = vert.toArray();
                    arr = Util.multiplyTransform(Util.getTranslationMatrix( -camU[0]*CAMERA_SPEED,  -camU[1]*CAMERA_SPEED,  -camU[2]*CAMERA_SPEED),arr);
                    hitbox.set(i, Util.arrToVert(arr));
                }
                if(camera.runCollisionCheck(3,hitbox)){
                    //for (int i = 0; i < 4; i++) {
                    camera.translate( -camU[0] * CAMERA_SPEED,  -camU[1] * CAMERA_SPEED,  -camU[2] * CAMERA_SPEED);
                }
            }; // Rotate left
            if (keyEvent.getCode() == KeyCode.E){
                camF = Util.multiplyTransform(Util.getRotationYMatrix(CAMERA_ROT_SPEED), camF);
                System.out.println(Arrays.toString(camF)+ " 1MMMMMMMMMMMMM");
                camera.setForward(Util.arrToVert(camF));
                camR = Util.multiplyTransform(Util.getRotationYMatrix(CAMERA_ROT_SPEED), camR);
                camera.setRight(Util.arrToVert(camR));
                camera.rotY(CAMERA_ROT_SPEED);
                collisionDir = false;
                //camera.updateRotation(CAMERA_ROT_SPEED);

            };
            if (keyEvent.getCode() == KeyCode.Q){
                camF = Util.multiplyTransform(Util.getRotationYMatrix(-CAMERA_ROT_SPEED), camF);
                System.out.println(Arrays.toString(camF)+ " MMMMMMMMMMMMM");
                camera.setForward(Util.arrToVert(camF));
                camR = Util.multiplyTransform(Util.getRotationYMatrix(-CAMERA_ROT_SPEED), camR);
                camera.setRight(Util.arrToVert(camR));
                camera.rotY(-CAMERA_ROT_SPEED);
                collisionDir = false;
                //camera.updateRotation(-CAMERA_ROT_SPEED);
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
        //System.out.println(camera.getPosition().toString());
        System.out.println(camera.getForward().toString());
        System.out.println(camera.getUp().toString());
        System.out.println(camera.getRight().toString());
        System.out.println("WWWWWWWWWWWWW");

    }

    public static void drawRadar(){
        Object3D object = objectList.get(10);
        Circle circle = new Circle(WIDTH/2, 200, 100, Color.RED);
        Circle circle1 = new Circle();
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
        }else{
            enemyInRange=false;
        }
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.RED);
        decals.add(circle);
        decals.add(circle1);
        decals.add(circle2);
        root.getChildren().addAll(circle2,circle1,circle);
    }

    public static void drawStatusText(){
        if(enemyInRange&&TEXT_TICK>15) {
            Text t = new Text("Enemy in range");
            t.setFont(Font.font(50));
            t.setX(100);
            t.setY(300);
            t.setFill(Color.RED);
            textList.add(t);
            root.getChildren().add(t);
        }
        if(!enemyDir.equals("")&&TEXT_TICK > 15){
            Text t = new Text("Enemy to "+enemyDir);
            t.setFont(Font.font(50));
            t.setX(100);
            t.setY(400);
            t.setFill(Color.RED);
            textList.add(t);
            root.getChildren().add(t);
        }
        if(collisionDir&&TEXT_TICK>10){
            Text t = new Text("Movement blocked by object");
            t.setFont(Font.font(50));
            t.setX(100);
            t.setY(500);
            t.setFill(Color.RED);
            textList.add(t);
            root.getChildren().add(t);
        }
    }

    public static void impactAnim(){
        has_collided = true;
    }

    public static void onGotShot(){
        //TODO:THIS
    }

    public static void start() {
        timeline = new Timeline(new KeyFrame(Duration.millis(1000.0 / (Menu.FPS)), actionEvent -> {
            TEXT_TICK++;
            if(TEXT_TICK>30){
                TEXT_TICK=0;
            }
            control();
            if(Math.random()*1000<100){
                for(EnemyTank enemyTank:enemyTankList){
                    enemyTank.shootTank();
                }
            }
            for (Polyline polyline:lineList) {
                root.getChildren().remove(polyline);
            }
            lineList.clear();
            for (Circle circle:decals) {
                root.getChildren().remove(circle);
            }
            decals.clear();
            for (Text text:textList) {
                root.getChildren().remove(text);
            }
            textList.clear();
            text.setText(Double.toString(camera.getRotation()));
            text1.setText(Math.round(camera.getX()) + " " + Math.round(camera.getY()) + " " + Math.round(camera.getZ()));
            Polyline polyline = new Polyline();
            polyline.setStroke(Color.GREEN);
            if(has_collided){
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
                }
                impact_ticks++;
            }else{
                polyline = new Polyline( 0,(HEIGHT/2), WIDTH,(HEIGHT/2));
                polyline.setStroke(Color.GREEN);
            }
            lineList.add(polyline);
            root.getChildren().add(polyline);
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
            for (int i = 0; i < enemyTankList.size(); i++) {
                EnemyTank enemyTank = enemyTankList.get(i);

                if(!enemyTank.getThisBullets().isEmpty()){
                    Bullet bullet = enemyTank.getThisBullets().get(0);
                    System.out.println(">>>>>>>>>>>>>>>>>>");
                    bullet.translate(bullet.getDirection().getX(),0,bullet.getDirection().getZ());
                    double travelled = Math.sqrt(bullet.getDirection().getX()*bullet.getDirection().getX()+bullet.getDirection().getZ()*bullet.getDirection().getZ());
                    bullet.setDistanceCovered(bullet.getDistanceCovered()+travelled);
                    bullet.displayObject();
                    if(bullet.getDistanceCovered()>MAX_BULLET_DISTANCE){
                        enemyTank.getThisBullets().remove(bullet);
                        enemyTank.getThisBullets().clear();
                    }
                    System.out.println("<<<<<<<<<<<<<<<<<<");
                }
            }
            //System.out.println(objectList.get(10).getPoints3D().size() + " " + objectList.get(10).getFaces3D().size() + " GGGGGG");
            //objectList.get(10).rotY(1);
            for (Object3D object:objectList) {
                if(object.getClass()!= Camera.class){
                object.displayObject();
                if(!bullets.isEmpty()){
                    double dist = Math.sqrt(Math.pow((bullets.get(0).getX()-object.getX()),2)+Math.pow((bullets.get(0).getZ()-object.getZ()),2));
                    System.out.println(dist + " TROLOLOLOLOL");
                    if(dist<3){
                        Vertex vertex = bullets.get(0).checkForHits(object);
                        if(vertex!=null){
                            System.out.println("YYYYYYYYY");
                            object.setColor(Color.BLUE);
                            bullets.get(0).explode(vertex);
                            bullets.remove(bullets.get(0));
                            bullets.clear();
                    }}
                }
            }}
            if(!particles.isEmpty()){
                for (int i = 0; i < particles.size(); i++) {
                    Particle particle = particles.get(i);
                    particle.displayObject();
                    particle.translate(particle.getDirection().getX()*PARTICLE_SPEED, particle.getDirection().getY()*PARTICLE_SPEED, particle.getDirection().getZ()*PARTICLE_SPEED);
                    particle.setTickrate(particle.getTickrate()+1);
                    if(particle.getTickrate() > MAX_PARTICLE_TICKS){
                        particles.remove(i);
                    }
                }
            }
            RADAR_ROT+=1;
            if(RADAR_ROT>=360){
                RADAR_ROT=1-(360-RADAR_ROT);
            } else if (RADAR_ROT<0) {
                RADAR_ROT=360+(1-RADAR_ROT);
            }
            drawRadar();
            drawStatusText();
            for (int i = 0; i < reticle.size(); i++) {
                root.getChildren().remove(reticle.get(i));
            }
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
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }


}
