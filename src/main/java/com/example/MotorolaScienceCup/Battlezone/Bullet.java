package com.example.MotorolaScienceCup.Battlezone;

import com.example.MotorolaScienceCup.Sound;
import javafx.scene.paint.Color;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.example.MotorolaScienceCup.Battlezone.Main.FPS_OFFSET;

public class Bullet extends Object3D{

    public static int PARTICLE_COUNT = 12;

    private double distanceCovered;

    private Vertex origin;

    private Object3D parent;

    private Vertex direction;
    public Bullet(ArrayList<Vertex> points3D, ArrayList<Face> faces3D){
        super(points3D, faces3D);
        this.direction = new Vertex(0,0,0);
    }

    public double getDistanceCovered() {
        return distanceCovered;
    }

    public Vertex getOrigin() {
        return origin;
    }

    public void setOrigin(Vertex origin) {
        this.origin = origin;
    }

    public void setDistanceCovered(double distanceCovered) {
        this.distanceCovered = distanceCovered;
    }

    public Vertex getDirection() {
        return direction;
    }

    public void setDirection(Vertex direction) {
        this.direction = direction;
    }

    public Object3D getParent() {
        return parent;
    }

    public void setParent(Object3D parent) {
        this.parent = parent;
    }

    public void explode(Vertex vertex){
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            ArrayList<Vertex> arr = new ArrayList<>();
            arr.add(new Vertex(vertex.getX(),vertex.getY(),vertex.getZ()));
            ArrayList<Integer> arr1 = new ArrayList<>();
            arr1.add(0);
            ArrayList<Face> faces = new ArrayList<>();
            Face face = new Face(arr1);
            faces.add(face);
            Particle particle = new Particle(arr,faces, new Vertex(Math.random()*2-1, Math.random()*2-1, Math.random()*2-1));
            particle.setColor(Color.RED);
            Main.particles.add(particle);
        }
    }

    public Vertex checkForHits(Object3D object){
        ArrayList<Vertex> list = new ArrayList<>();
        ArrayList<Vertex> hitbox = new ArrayList<>();
        hitbox = object.getHitBox2D();

        if(!(object instanceof Bullet)&&(!object.isHalfCube())){
            Vertex vertex = new Vertex(this.getPoints3D().get(4).getX(), 0, this.getPoints3D().get(4).getZ());
            Vertex vertex1 = new Vertex(this.getPoints3D().get(4).getX(), 0, this.getPoints3D().get(4).getZ());
            double[] arr1 = vertex1.toArray();
            if(!(object instanceof Missile)){
                arr1 = Util.multiplyTransform(Util.getTranslationMatrix(-this.getDirection().getX()*FPS_OFFSET, 0, -this.getDirection().getZ()*FPS_OFFSET), arr1);
            }else{
                arr1 = Util.multiplyTransform(Util.getTranslationMatrix(-this.getDirection().getX()*2*FPS_OFFSET, 0, -this.getDirection().getZ()*2*FPS_OFFSET), arr1);
            }
            vertex1 = Util.arrToVert(arr1);


            for (int i = 0; i < hitbox.size(); i++) {
                if (i + 1 < hitbox.size()) {
                   
                    Vertex vert = Util.lineIntersect(vertex, vertex1, hitbox.get(i), hitbox.get(i + 1));
                    if (vert != null) {
                        list.add(vert);
                    }
                } else {
                   
                    Vertex vert = Util.lineIntersect(vertex, vertex1, hitbox.get(hitbox.size() - 1), hitbox.get(0));
                    if (vert != null) {
                        list.add(vert);
                    }
                }
            }}
            if (list.isEmpty()) {
               
                return null;
            } else if (list.size() == 1) {
               
                return list.get(0);
            } else {
               
                double maxDistance = 0;
                int ind = -1;
                for (int i = 0; i < list.size(); i++) {
                    double travelled = Math.sqrt(Math.pow((list.get(i).getX() - this.getOrigin().getX()), 2) + Math.pow((list.get(i).getZ() - this.getOrigin().getZ()), 2));
                    if (travelled > maxDistance) {
                        maxDistance = travelled;
                        ind = i;
                    }
                }
                return list.get(ind);
            }
        }

    }

