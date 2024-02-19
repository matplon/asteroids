package com.example.MotorolaScienceCup.Battlezone;

import java.util.ArrayList;

public class Chunk {

    public static int sideLength = 40;

    public static int chunkHiveSideLenght = 3;

    public static int chunkObjCount = 1;
    public static int chunkMineCount = 1;

    public static Chunk center;
    public static Chunk oldCenter;
    private int x;
    private int z;
    private ArrayList<Object3D> chunkObjects = new ArrayList<>();

    private ArrayList<Mine> chunkMines = new ArrayList<>();

    public Chunk(int x, int z, ArrayList<Chunk> arr){
        this.x = x;
        this.z = z;
        fillChunk();
        arr.add(this);
    }

    public int checkDistanceX(Chunk chunk){
        double x = this.getX() - chunk.getX();
        return (int) x/sideLength;

    }

    public int checkDistanceZ(Chunk chunk){
        double z = this.getZ() - chunk.getZ();
        return (int) z/sideLength;

    }
    public void unloadChunk(ArrayList<Chunk> arr){
        for(Object3D object3D: chunkObjects){
            Main.objectList.remove(object3D);
        }
        for(Mine mine: chunkMines){
            Main.objectList.remove(mine);
            Main.mineList.remove(mine);
        }
        Main.chunkList.remove(this);
        int x = -checkDistanceX(oldCenter);
        int z = -checkDistanceZ(oldCenter);
        x = (int)(center.getX() + x);
        z = (int)(center.getZ() + z);
        Chunk chunk = new Chunk(x,z,arr);

    }


    public void fillChunk(){

    }

    public void checkHasPlayer(){
        if(Main.camera.getX()<=x+sideLength/2 && Main.camera.getX()>=x-sideLength/2 && Main.camera.getZ()<=z+sideLength/2 && Main.camera.getZ()>=z-sideLength/2){
            if(!this.equals(center)){
                setOldCenter(getCenter());
                setCenter(this);
            }
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
}
