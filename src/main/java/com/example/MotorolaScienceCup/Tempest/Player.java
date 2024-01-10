package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

public class Player extends BetterPolygon {
    private final double MOVE_SIZE = 1; // pixel
    private double sideLength;
    private int sideIndex;
    private int rightSideIndex;
    private int leftSideIndex;
    private HashMap<Integer, Pair<Double, Double>> moveLengthsHashmap; //  <Side, <XInterval, YInterval>>

    public Player(List<Double> points, double sideLength, int sideIndex, int rightSideIndex, int leftSideInex){
        super(points);
        this.sideLength = sideLength;
        this.sideIndex = sideIndex;
        this.rightSideIndex = rightSideIndex;
        this.leftSideIndex = leftSideInex;
    }
    
    public void move(boolean left){

    }
}
