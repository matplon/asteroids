package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

public class Player extends BetterPolygon {
    private final int MOVES_PER_SIDE = 6;
    private double moveLength;
    private double sideLength;
    private HashMap<Polyline, Pair<Double, Double>> moveLengthsHashmap; //  <Side, <XInterval, YInterval>>

    public Player(List<Double> points, double sideLength){
        super(points);
        this.moveLength = sideLength / MOVES_PER_SIDE;
        this.sideLength = sideLength;
        calculateMoveLength(Main.bigShape);
    }
    
    public void move(){
        for (Polyline polyline: Main.bigShape) {
            if(Shape.intersect(this, polyline).getLayoutBounds().getWidth() > 0){
                moveTo(getCenterX() + moveLengthsHashmap.get(polyline).getValue(), getCenterX() + moveLengthsHashmap.get(polyline).getKey());
                break;
            }
        }
    }

    private void calculateMoveLength(List<Polyline> sides){
        for (Polyline polyline: sides) {
            double gradient = (polyline.getPoints().get(3) - polyline.getPoints().get(1)) / (polyline.getPoints().get(2) - polyline.getPoints().get(0));
            double angle = Math.atan(gradient);
            double xInterval = moveLength * Math.cos(angle);
            double yInterval = moveLength * Math.sin(angle);
            if (polyline.getPoints().get(0) > polyline.getPoints().get(2)) {
                xInterval  *= -1;
            }
            if ((polyline.getPoints().get(3) > polyline.getPoints().get(1) && gradient < 0) || (gradient > 0 && polyline.getPoints().get(3) < polyline.getPoints().get(1))) {
                yInterval *= -1;
            }
            moveLengthsHashmap.put(polyline, new Pair<>(xInterval, yInterval));
        }
    }
}
