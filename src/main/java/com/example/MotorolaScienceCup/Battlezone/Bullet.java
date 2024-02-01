package com.example.MotorolaScienceCup.Battlezone;

import java.util.ArrayList;
import java.util.Arrays;

public class Bullet extends Object3D{

    private double distanceCovered;

    private Vertex origin;

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

    public void explode(Vertex vertex){
        System.out.println("lol");
    }

    public Vertex checkForHits(Object3D object){
        Vertex vertex = new Vertex(this.getPoints3D().get(4).getX(), 0, this.getPoints3D().get(4).getZ());
        Vertex vertex1 = new Vertex(this.getPoints3D().get(4).getX(), 0, this.getPoints3D().get(4).getZ());
        double[] arr1 = vertex1.toArray();
        arr1 = Util.multiplyTransform(Util.getTranslationMatrix(-this.getDirection().getX(),0,-this.getDirection().getZ()),arr1);
        vertex1 = Util.arrToVert(arr1);

        ArrayList<Vertex> hitbox = object.getHitBox2D();
        ArrayList<Vertex> list = new ArrayList<>();
        for (int i = 0; i < hitbox.size(); i++) {
            if(i+1<hitbox.size()){
                Vertex vert = Util.lineIntersect(vertex,vertex1,hitbox.get(i),hitbox.get(i+1));
                if(vert!=null){
                    list.add(vert);
                }
            }else {
                Vertex vert = Util.lineIntersect(vertex,vertex1,hitbox.get(hitbox.size()-1),hitbox.get(0));
                if(vert!=null){
                    list.add(vert);
                }
            }
        }
        if(list.isEmpty()){
            System.out.println("frick");
            return null;
        } else if (list.size()==1) {
            System.out.println("heck");
            return list.get(0);
        }else{
            System.out.println("omg");
            double maxDistance = 0;
            int ind = -1;
            for (int i = 0; i < list.size(); i++) {
                double travelled = Math.sqrt(Math.pow((list.get(i).getX()-this.getOrigin().getX()),2)+Math.pow((list.get(i).getZ()-this.getOrigin().getZ()),2));
                if(travelled>maxDistance){
                    maxDistance = travelled;
                    ind = i;
                }
            }
            return list.get(ind);
        }
        

    }
}
