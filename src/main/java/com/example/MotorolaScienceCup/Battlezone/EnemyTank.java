package com.example.MotorolaScienceCup.Battlezone;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static java.lang.Math.abs;

public class EnemyTank extends Object3D{

    private Vertex forward;

    private Vertex center;

    private ArrayList<Bullet> thisBullets = new ArrayList<>();

    private ArrayList<Vertex> collideHitBox = new ArrayList<>();

    private boolean attackMode;

    private double targetRotation;

    private double rotateDir;

    private double moveDir;

    private boolean isRotating;

    private boolean isWaiting;

    private boolean isMoving;

    private boolean isAiming;

    private double waitTimer;


    private Vertex target;

    private boolean willShoot;

    private double magTimer;

    public static double TANK_SPEED = 0.25*0.25*1.3;

    public static double TANK_ROT_SPEED = 0.75*1.3;


    public EnemyTank(ArrayList<Vertex> points3D, ArrayList<Face> faces3D){
        super(points3D, faces3D);
    }

    public Vertex getForward() {
        return forward;
    }

    public void setForward(Vertex forward) {
        this.forward = forward;
    }

    public Vertex getCenter() {
        return center;
    }

    public void setCenter(Vertex center) {
        this.center = center;
    }

    public ArrayList<Bullet> getThisBullets() {
        return thisBullets;
    }

    public void setThisBullets(ArrayList<Bullet> thisBullets) {
        this.thisBullets = thisBullets;
    }

    public boolean isAttackMode() {
        return attackMode;
    }

    public void setAttackMode(boolean attackMode) {
        this.attackMode = attackMode;
    }

    public ArrayList<Vertex> getCollideHitBox() {
        return collideHitBox;
    }

    public void setCollideHitBox(ArrayList<Vertex> collideHitBox) {
        this.collideHitBox = collideHitBox;
    }

    public Vertex getTarget() {
        return target;
    }

    public void setTarget(Vertex target) {
        this.target = target;
    }

    public void rotateTank(double angle){
        double x = this.getCenter().getX();
        double y = this.getCenter().getY();
        double z = this.getCenter().getZ();
        Vertex vertex = new Vertex(this.getForward().getX(), 0, this.getForward().getZ());
        double[] arr1 = vertex.toArray();
        arr1 = Util.multiplyTransform(Util.getRotationYMatrix(angle), arr1);
        this.setForward(Util.arrToVert(arr1));
        this.updateRotation(angle);
        System.out.println(getRotation()+" HHHHHHHHHHHHHHHHHHHH");
        this.moveTo(0,0,0);
        for (int i = 0; i < this.getPoints3D().size(); i++) {
            double [][] translationMatrix = Util.getRotationYMatrix(angle);
            double [] arr = this.getPoints3D().get(i).toArray();
            arr = Util.multiplyTransform(translationMatrix, arr);
            this.getPoints3D().set(i,Util.arrToVert(arr));

        }
        for (int i = 0; i < this.getHitBox2D().size(); i++) {
            double [][] translationMatrix = Util.getRotationYMatrix(angle);
            double [] arr = this.getHitBox2D().get(i).toArray();
            System.out.println(Arrays.toString(arr)+"ZZZZZZZZZZ");
            arr = Util.multiplyTransform(translationMatrix, arr);
            System.out.println(Arrays.toString(arr)+"XXXXXXXX");
            this.getHitBox2D().set(i,Util.arrToVert(arr));

        }
        for (int i = 0; i < this.getCollideHitBox().size(); i++) {
            double [][] translationMatrix = Util.getRotationYMatrix(angle);
            double [] arr = this.getCollideHitBox().get(i).toArray();
            System.out.println(Arrays.toString(arr)+"ZZZZZZZZZZ");
            arr = Util.multiplyTransform(Util.getTranslationMatrix(-center.getX(),-center.getY(), -center.getZ()), arr);
            arr = Util.multiplyTransform(translationMatrix, arr);
            arr = Util.multiplyTransform(Util.getTranslationMatrix(center.getX(),center.getY(), center.getZ()), arr);
            System.out.println(Arrays.toString(arr)+"XXXXXXXX");
            this.getCollideHitBox().set(i,Util.arrToVert(arr));

        }
        this.moveTo(x,y,z);
        this.setX(this.getCenter().getX());
        this.setY(this.getCenter().getY());
        this.setZ(this.getCenter().getZ());
    }

    public void scaleTank(double x, double y, double z){
            for (int i = 0; i < this.getPoints3D().size(); i++) {
                double [][] translationMatrix = Util.getScaleMatrix(x,y,z);
                double [] arr = this.getPoints3D().get(i).toArray();
                arr = Util.multiplyTransform(translationMatrix, arr);
                this.getPoints3D().set(i,Util.arrToVert(arr));

            }
            for (int i = 0; i < this.getHitBox2D().size(); i++) {
                double [][] translationMatrix = Util.getScaleMatrix(x,y,z);
                double [] arr = this.getHitBox2D().get(i).toArray();
                System.out.println(Arrays.toString(arr)+"ZZZZZZZZZZ");
                arr = Util.multiplyTransform(translationMatrix, arr);
                System.out.println(Arrays.toString(arr)+"XXXXXXXX");
                this.getHitBox2D().set(i,Util.arrToVert(arr));

            }
                for (int i = 0; i < this.getCollideHitBox().size(); i++) {
                    double [][] translationMatrix = Util.getScaleMatrix(x,y,z);
                    double [] arr = this.getCollideHitBox().get(i).toArray();
                    System.out.println(Arrays.toString(arr)+"ZZZZZZZZZZ");
                    arr = Util.multiplyTransform(translationMatrix, arr);
                    System.out.println(Arrays.toString(arr)+"XXXXXXXX");
                    this.getCollideHitBox().set(i,Util.arrToVert(arr));

                }

    }

    public void moveToRandom(double rotOffset, double scaleOffset){
        Vertex vertex = Main.camera.getForward();
        double[] arr = vertex.toArray();
        double offset = Math.random()*rotOffset*2-rotOffset;
        arr = Util.multiplyTransform(Util.getRotationYMatrix(offset),arr);
        double scale;
        if(!(this instanceof Missile)){
            scale = Math.random()*scaleOffset*2+scaleOffset;
        }else{
           scale = scaleOffset;
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i]*=scale;
        }
        vertex = Util.arrToVert(arr);
        this.moveTo(vertex.getX(), this.getY(), vertex.getZ());
        ArrayList<Vertex> hitbox = new ArrayList<>();
        if(this instanceof EnemyTank){
            hitbox = this.getCollideHitBox();
        }else{
            hitbox = this.getHitBox2D();
        }
        boolean notCollided = this.runCollisionCheck(5, hitbox, this).isEmpty();
        if(!notCollided){
            this.moveToRandom(rotOffset,scaleOffset);
        }
    }

    public void moveTank(Vertex direction){
        ArrayList<Vertex> hitbox = this.getCollideHitBox();
        ArrayList<Vertex> lol = new ArrayList<>();
        double multi = 1;
        if(this instanceof Ufo){
            multi = 5;
        }
        for (int i = 0; i < hitbox.size(); i++) {
            Vertex vert = hitbox.get(i);
            double[] arr = vert.toArray();
            arr = Util.multiplyTransform(Util.getTranslationMatrix(direction.getX(),direction.getY(),direction.getZ()),arr);
            lol.add(Util.arrToVert(arr));
        }
        System.out.println("hihihi");
        ArrayList<Object3D> array = this.runCollisionCheck(10,lol,this);
        if(array.isEmpty()) {
            System.out.println(getRotation() + " HHHHHHHHHHHHHHHHHHHH");
            Vertex vert = this.getCenter();
            double[] arr1 = vert.toArray();
            arr1 = Util.multiplyTransform(Util.getTranslationMatrix(direction.getX(),direction.getY(),direction.getZ()),arr1);
            this.setCenter(Util.arrToVert(arr1));
            for (int i = 0; i < this.getPoints3D().size(); i++) {
                double[][] translationMatrix = Util.getTranslationMatrix(direction.getX(), direction.getY(), direction.getZ());
                double[] arr = this.getPoints3D().get(i).toArray();
                arr = Util.multiplyTransform(translationMatrix, arr);
                this.getPoints3D().set(i, Util.arrToVert(arr));

            }
            for (int i = 0; i < this.getHitBox2D().size(); i++) {
                double[][] translationMatrix = Util.getTranslationMatrix(direction.getX(), direction.getY(), direction.getZ());
                double[] arr = this.getHitBox2D().get(i).toArray();
                System.out.println(Arrays.toString(arr) + "ZZZZZZZZZZ");
                arr = Util.multiplyTransform(translationMatrix, arr);
                System.out.println(Arrays.toString(arr) + "XXXXXXXX");
                this.getHitBox2D().set(i, Util.arrToVert(arr));

            }
            for (int i = 0; i < this.getCollideHitBox().size(); i++) {
                double [][] translationMatrix = Util.getTranslationMatrix(direction.getX(), direction.getY(), direction.getZ());
                double [] arr = this.getCollideHitBox().get(i).toArray();
                System.out.println(Arrays.toString(arr)+"ZZZZZZZZZZ");
                arr = Util.multiplyTransform(translationMatrix, arr);
                System.out.println(Arrays.toString(arr)+"XXXXXXXX");
                this.getCollideHitBox().set(i,Util.arrToVert(arr));

            }
            this.setX(this.getCenter().getX());
            this.setY(this.getCenter().getY());
            this.setZ(this.getCenter().getZ());
        }else{
                System.out.println("]]]]]]]]]]]]]]]]]]");
                if(!array.contains(Main.camera)||this instanceof Ufo) {
                    this.setTarget(new Vertex(this.getCenter().getX() - this.getForward().getX() * 10, 0, this.getCenter().getZ() - this.getForward().getZ() * 10));
                    this.setAttackMode(true);
                    this.setMoving(true);
                    this.setWaiting(false);
                    this.setRotating(false);
                    this.setMoveDir(-1);
                }else{
                    this.setTarget(new Vertex(Main.camera.getX(), 0, Main.camera.getZ()));
                    this.setAttackMode(true);
                    this.setMoving(false);
                    this.setRotating(true);
                    setWillShoot(true);
                    this.setTargetRotation(this.getLookAt(this.getTarget()));
                    setRotateDir(getExactRotationDir());
                }
        }
    }
    public void shootTank(){
        if(magTimer<0&&!Main.isDying){
            double[] dir = this.getForward().toArray();
            Bullet bullet = Util.generateBullet(dir, this.getRotation(), this.getX()+this.getForward().getX()*0.5, this.getY()+0.25, this.getZ()+this.getForward().getZ()*0.5);
            bullet.setParent(this);
            Main.allBullets.add(bullet);
            setMagTimer(120);
        }
    }

    public double getLookAt(Vertex obj){
        double x = obj.getX() - this.getX();
        double y = obj.getZ() - this.getZ();
        double angle = Math.toDegrees(Math.atan2(x,y));
        if(angle < 0){
            angle+=360;
        }
        return angle;
    }

    public double getRotDifference(){
        double a = getRotation()-getTargetRotation();
        double b = getTargetRotation()-getRotation();
        if(abs(a)<abs(b)){
            return a;
        }else{
            return b;
        }
    }

    public double getExactRotationDir(){
        if(this.getRotation() < this.getTargetRotation()) {
            if(abs(this.getRotation() - this.getTargetRotation())<180)
                return 1;
            else return -1;
        }

        else {
            if(abs(this.getRotation() - this.getTargetRotation())<180)
                return  -1;
            else return  1;
        }
    }

    public void explode(){
        ArrayList<Face> faces = this.getFaces3D();
        System.out.println(faces.size()+"///////////////");
        for (int i = 0; i < 5; i++) {
            int pick = new Random().nextInt(faces.size());
            System.out.println(faces.size());
            Face face = faces.get(pick);
            faces.remove(pick);
            ArrayList<Vertex> points = new ArrayList<>();
            ArrayList<Integer> faceInd = new ArrayList<>();
            for (int j = 0; j < face.getIndexes().size(); j++) {
                points.add(this.getPoints3D().get(face.getIndexes().get(j)));
            }
            for (int j = 0; j < points.size(); j++) {
                faceInd.add(j);
            }
            Face face1 = new Face(faceInd);
            ArrayList<Face> facesFinal = new ArrayList<>();
            facesFinal.add(face1);
            Particle particle = new Particle(points,facesFinal, new Vertex(Math.random()*10-5, Math.random()*7, Math.random()*10-5));
            particle.rotX(Math.random()*360);
            particle.rotY(Math.random()*360);
            particle.rotZ(Math.random()*360);
            particle.setRotationVert(new Vertex(Math.random()*10-5, Math.random()*10, Math.random()*10-5));
            particle.setColor(Color.GREEN);
            Main.particles.add(particle);
        }
    }
    public void takeHit(Object3D object3D){
        System.out.println("###########");
        Main.enemyTankList.remove(this);
        Main.fullTankList.remove(this);
        Main.objectList.remove(this);
        if(object3D instanceof Camera) {
            System.out.println("============");
            Main.score += 1000;
        }
        explode();
    }
    public void enemyBehavior(){
        System.out.println(this.getForward().toString() + " {{{{{{{{{");
        if(!attackMode){
            if(isMoving){
                System.out.println("KOKOKOKKO");
                if(Util.getDistance(target, this.getCenter())<5){
                    setWaiting(true);
                    setMoving(false);
                    setWaitTimer(Math.random()*200);
                }else{
                    System.out.println("YOYOOYY");
                    moveTank(new Vertex(this.getForward().getX()*TANK_SPEED*moveDir,0,this.getForward().getZ()*TANK_SPEED*moveDir));
                }
            }
            if(isWaiting){
                setWaitTimer(getWaitTimer()-1);
                if(getWaitTimer()<0){
                    setWaitTimer(-1);
                    double rand = Math.random();
                    if(rand<0.8){
                        setWaiting(false);
                        setRotating(true);
                        setAttackMode(true);
                        setWillShoot(false);
                        Vertex vertex = new Vertex(Main.camera.getX(), 0, Main.camera.getZ()).getVertDif(new Vertex(getX(), 0, getZ()));
                        double[] arr = vertex.toArray();
                        double offset = Math.random()*25-12.5;
                        arr = Util.multiplyTransform(Util.getRotationYMatrix(offset),arr);
                        double scale = Math.random()*0.25 + 0.5;
                        for (int i = 0; i < arr.length; i++) {
                            arr[i]*=scale;
                        }
                        vertex = Util.arrToVert(arr);
                        setTarget(vertex.getVertSum(new Vertex(getX(),0,getZ())));
                        setMoveDir(1);
                        setTargetRotation(getLookAt(getTarget()));
                        setRotateDir(getExactRotationDir());
                    }else{
                        setWaiting(false);
                        setRotating(true);
                        setTarget(new Vertex(getCenter().getX()+Math.random()*30-15,0,getCenter().getZ()+Math.random()*30-15));
                        setMoveDir(1);
                        setTargetRotation(getLookAt(getTarget()));
                        setRotateDir(getExactRotationDir());
                    }
                }
            }
            if(isRotating){
                if(targetRotation < getRotation() + 1 && targetRotation>getRotation() - 1){
                    double offset = 0;
                    rotateTank(getRotDifference()+offset);
                    setTargetRotation(getTargetRotation()+getRotDifference()+offset);
                    setTargetRotation(getTargetRotation()+getRotDifference());
                    setRotating(false);
                    setMoving(true);

                }else{
                    rotateTank(TANK_ROT_SPEED*rotateDir);
                }
            }
        }else{
            if(isMoving){
                System.out.println("KOKOKOKKO");
                if(Util.getDistance(target, this.getCenter())<5){
                    setWaiting(true);
                    setMoving(false);
                    setWaitTimer(Math.random()*100);
                }else{
                    System.out.println("YOYOOYY");
                    moveTank(new Vertex(this.getForward().getX()*TANK_SPEED*moveDir,0,this.getForward().getZ()*TANK_SPEED*moveDir));
                }
            }
            if(isWaiting){
                setWaitTimer(getWaitTimer()-1);
                if(getWaitTimer()<0){
                    setWaitTimer(-1);
                    double rand = Math.random();
                    if(rand<0.8){
                        setWaiting(false);
                        setRotating(true);
                        setWillShoot(true);
                        setTarget(new Vertex(Main.camera.getX(),0,Main.camera.getZ()));
                        setMoveDir(1);
                        setTargetRotation(getLookAt(getTarget()));
                        setRotateDir(getExactRotationDir());
                    }else{
                        setWaiting(false);
                        setRotating(true);
                        setWillShoot(false);
                        setTarget(new Vertex(Main.camera.getX()+Math.random()*20-10,0,Main.camera.getZ()+Math.random()*20-10));
                        setMoveDir(1);
                        setTargetRotation(getLookAt(getTarget()));
                        setRotateDir(getExactRotationDir());
                    }
                }
            }
            if(isRotating){
                double offset = 0;
                if(willShoot){
                    setWaiting(false);
                    setRotating(true);
                    setWillShoot(true);
                    if((target.getX() == Main.camera.getX() && target.getZ() == Main.camera.getZ())){
                        offset = Math.random()*6-3;
                    }
                    setTarget(new Vertex(Main.camera.getX(),0,Main.camera.getZ()));
                    setMoveDir(1);
                    setTargetRotation(getLookAt(getTarget()));
                    setRotateDir(getExactRotationDir());
                }
                if(targetRotation < getRotation() + 1 && targetRotation > getRotation() - 1){
                    rotateTank(getRotDifference()+offset);
                    setTargetRotation(getTargetRotation()+getRotDifference()+offset);
                    setRotating(false);
                    if(willShoot){
                        shootTank();
                        setWaiting(true);
                        setWaitTimer(Math.random()*100);
                    }else{
                        setMoving(true);
                    }

                }else{
                    rotateTank(TANK_ROT_SPEED*rotateDir);
                }
            }
        }
        double distance = Util.getDistance(getCenter(), new Vertex(Main.camera.getX(),0,Main.camera.getZ()));
        if(distance>Camera.getFar()+10){
            Main.objectList.remove(this);
            Main.enemyTankList.remove(this);
            Main.fullTankList.remove(this);
        }
    }

    public double getTargetRotation() {
        return targetRotation;
    }

    public void setTargetRotation(double targetRotation) {
        this.targetRotation = targetRotation;
    }

    public double getRotateDir() {
        return rotateDir;
    }

    public void setRotateDir(double rotateDir) {
        this.rotateDir = rotateDir;
    }

    public boolean isRotating() {
        return isRotating;
    }

    public void setRotating(boolean rotating) {
        isRotating = rotating;
    }

    public boolean isWillShoot() {
        return willShoot;
    }

    public void setWillShoot(boolean willShoot) {
        this.willShoot = willShoot;
    }

    public double getMoveDir() {
        return moveDir;
    }

    public void setMoveDir(double moveDir) {
        this.moveDir = moveDir;
    }

    public boolean isAiming() {
        return isAiming;
    }

    public void setAiming(boolean aiming) {
        isAiming = aiming;
    }

    public double getWaitTimer() {
        return waitTimer;
    }

    public void setWaitTimer(double waitTimer) {
        this.waitTimer = waitTimer;
    }

    public boolean isWaiting() {
        return isWaiting;
    }

    public void setWaiting(boolean waiting) {
        isWaiting = waiting;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public double getMagTimer() {
        return magTimer;
    }

    public void setMagTimer(double magTimer) {
        this.magTimer = magTimer;
    }
}
