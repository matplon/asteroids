package com.example.MotorolaScienceCup.Battlezone;

import java.util.ArrayList;
import java.util.Random;

public class Chunk {

    public static int sideLength = 65;

    public static int chunkHiveSideLength = 5;

    public static int chunkObjCount = 1;
    public static int chunkMineCount = 1;

    public static Chunk center;
    public static Chunk oldCenter;
    private int x;
    private int z;

    private int indexX;
    private int indexZ;
    private ArrayList<Object3D> chunkObjects = new ArrayList<>();

    private ArrayList<Mine> chunkMines = new ArrayList<>();

    public Chunk(int x, int z, ArrayList<Chunk> thisArray){
        this.x = x;
        this.z = z;
        fillChunk();
        thisArray.add(this);
    }

    public int checkDistanceX(Chunk chunk){
        double x = this.getX() - chunk.getX();
        return (int) Math.round(x);

    }

    public int checkDistanceZ(Chunk chunk){
        double z = this.getZ() - chunk.getZ();
        return (int) Math.round(z);

    }
    public void unloadChunk(){
        Main.objectList.removeAll(chunkObjects);
        Main.objectList.removeAll(chunkMines);
        Main.mineList.removeAll(chunkMines);
        chunkMines.clear();
        chunkObjects.clear();
        Main.chunkList.remove(this);

    }

    public void moveToRandom(Object3D object3D){
        double x = getX()+Math.random()*(getSideLength()-4)-(getSideLength()-4)/2;
        double z = getZ()+Math.random()*(getSideLength()-4)-(getSideLength()-4)/2;
        object3D.moveTo(x,0,z);
        boolean notCollided = object3D.runCollisionCheck(5, object3D.getHitBox2D(), object3D).isEmpty();
        if(notCollided){
            return;
        }else{
            moveToRandom(object3D);
        }
    }



    public void fillChunk(){
        int random = new Random().nextInt(chunkObjCount);
        for (int i = 0; i < chunkObjCount; i++) {
            double check = new Random().nextDouble(10);
            if(check<4){
                double x = getX()+Math.random()*(getSideLength()-4)-(getSideLength()-4)/2;
                double z = getZ()+Math.random()*(getSideLength()-4)-(getSideLength()-4)/2;
                Object3D object3D = Util.generateCube(x,z);
                boolean notCollided = object3D.runCollisionCheck(7, object3D.getHitBox2D(), object3D).isEmpty();
                if(!notCollided){
                    moveToRandom(object3D);
                }
                this.getChunkObjects().add(object3D);
            } else if(check>=4&&check<8){
                double x = getX()+Math.random()*(getSideLength()-4)-(getSideLength()-4)/2;
                double z = getZ()+Math.random()*(getSideLength()-4)-(getSideLength()-4)/2;
                Object3D object3D = Util.generateCone(x,z);
                boolean notCollided = object3D.runCollisionCheck(7, object3D.getHitBox2D(), object3D).isEmpty();
                if(!notCollided){
                    moveToRandom(object3D);
                }
                this.getChunkObjects().add(object3D);
            } else if(check>=8){
                double x = getX()+Math.random()*(getSideLength()-4)-(getSideLength()-4)/2;
                double z = getZ()+Math.random()*(getSideLength()-4)-(getSideLength()-4)/2;
                Object3D object3D = Util.generateHalfCube(x,z);
                boolean notCollided = object3D.runCollisionCheck(7, object3D.getHitBox2D(), object3D).isEmpty();
                if(!notCollided){
                    moveToRandom(object3D);
                }
                this.getChunkObjects().add(object3D);
            }
        }
        int random2 = new Random().nextInt(chunkMineCount+1);
        for (int i = 0; i < random2; i++) {
            double x = getX()+Math.random()*(getSideLength()-4)-(getSideLength()-4)/2;
            double z = getZ()+Math.random()*(getSideLength()-4)-(getSideLength()-4)/2;
            Mine object3D = Util.generateMine(x,z);
            boolean notCollided = object3D.runCollisionCheck(7, object3D.getHitBox2D(), object3D).isEmpty();
            if(!notCollided){
                moveToRandom(object3D);
            }
            this.getChunkMines().add(object3D);
        }
    }

    public void checkHasPlayer(){
        System.out.println("_______________");
        if(Main.camera.getX()<x+ (double) sideLength /2 && Main.camera.getX()>x- (double) sideLength /2 && Main.camera.getZ()<z+ (double) sideLength /2 && Main.camera.getZ()>z- (double) sideLength /2){
               if(!this.equals(center)){
                   setOldCenter(center);
                   setCenter(this);
               }
            System.out.println("^^^^^^^^^");

        }
    }

    public static double getSideLength() {
        return sideLength;
    }

    public static void setSideLength(int sideLength) {
        Chunk.sideLength = sideLength;
    }

    public static Chunk getCenter() {
        return center;
    }

    public static void setCenter(Chunk center) {
        Chunk.center = center;
    }

    public static Chunk getOldCenter() {
        return oldCenter;
    }

    public static void setOldCenter(Chunk oldCenter) {
        Chunk.oldCenter = oldCenter;
    }

    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public ArrayList<Object3D> getChunkObjects() {
        return chunkObjects;
    }

    public void setChunkObjects(ArrayList<Object3D> chunkObjects) {
        this.chunkObjects = chunkObjects;
    }

    public ArrayList<Mine> getChunkMines() {
        return chunkMines;
    }

    public void setChunkMines(ArrayList<Mine> chunkMines) {
        this.chunkMines = chunkMines;
    }

    public int getIndexX() {
        return indexX;
    }

    public void setIndexX(int indexX) {
        this.indexX = indexX;
    }

    public int getIndexZ() {
        return indexZ;
    }

    public void setIndexZ(int indexZ) {
        this.indexZ = indexZ;
    }
}
