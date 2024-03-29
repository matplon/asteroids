package com.example.MotorolaScienceCup.Battlezone;

import com.example.MotorolaScienceCup.Sound;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.MotorolaScienceCup.Battlezone.Main.FPS_OFFSET;

public class Ufo extends EnemyTank{

    public static double UFO_SPEED = 0.075*FPS_OFFSET;
    public static double UFO_ROT_SPEED = 1*FPS_OFFSET;

    public static Clip ambient;


    private Clip death;

    {
        try {
            death = Sound.getClip("ufoDeath.wav",5.0f);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
    private Clip ufoPoints;

    {
        try {
            ufoPoints = Sound.getClip("ufoPoints.wav",4.0f);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }





    public Ufo(ArrayList<Vertex> points3D, ArrayList<Face> faces3D) {
        super(points3D, faces3D);
    }

    public void takeHit(Object3D object3D){
        death.start();
        ufoPoints.start();
        if(ambient!=null) {
            ambient.close();
            ambient = null;
        }
        Main.ufoList.remove(this);
        Main.objectList.remove(this);
        if(object3D instanceof Camera) {
            Main.score += 5000;
        }
    }

    public void enemyBehavior(){
            if(isMoving()){
                rotY(1);
                setRotation(getRotation()-1);
               
                if(Util.getDistance(getTarget(), this.getCenter())<7){
                    setWaiting(true);
                    setMoving(false);
                    setWaitTimer(Math.random()*100);
                }else{
                   
                    moveTank(new Vertex(this.getForward().getX()*UFO_SPEED*getMoveDir(),0,this.getForward().getZ()*UFO_SPEED*getMoveDir()));
                }
            }
            if(isWaiting()){
                rotY(1);
                setRotation(getRotation()-1);
                setWaitTimer(getWaitTimer()-1);
                if(getWaitTimer()<0){
                    setWaitTimer(-1);
                    double rand = Math.random();
                        setWaiting(false);
                        setRotating(true);
                        setTarget(new Vertex(getCenter().getX()+Math.random()*50-25,0,getCenter().getZ()+Math.random()*50-25));
                        setMoveDir(1);
                        setTargetRotation(getLookAt(getTarget()));
                        setRotateDir(getExactRotationDir());

                }
            }
            if(isRotating()){
                if(getTargetRotation() < getRotation() + 2 && getTargetRotation()>getRotation() - 2){
                    rotateTank(getRotDifference());
                    setTargetRotation(getTargetRotation()+getRotDifference());
                    setRotating(false);
                    setMoving(true);

                }else{
                    rotateTank(UFO_ROT_SPEED*getRotateDir());
                }
            }
        double distance = Util.getDistance(getCenter(), new Vertex(Main.camera.getX(),0,Main.camera.getZ()));
        if(distance>Camera.getFar()+10){
            Main.ufoList.remove(this);
            Main.objectList.remove(this);
        }
        if(distance<=40&&ambient==null){
                try {
                    ambient = Sound.getClip("ufoAmbient.wav",6.0f);
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                    throw new RuntimeException(e);
                }
                ambient.loop(Clip.LOOP_CONTINUOUSLY);
                ambient.start();
        }
        if(distance>40&&ambient!=null){
            ambient.close();
            ambient=null;
        }
        }
    }

