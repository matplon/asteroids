package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Vector;

import java.util.Arrays;
import java.util.List;

public class Enemy extends BetterPolygon {
    protected static final int FRAMES_PER_MOVE = 360;
    protected static final double initVelocity = 0.1;
    protected static final List<Double> pointerPoints = Arrays.asList(0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 0.0, 1.0);
    protected Panel currentPanel;
    protected BetterPolygon pointer;
    protected double h;
    protected double maxH;
    protected double frameOfMovement;
    protected double acceleration;
    protected boolean reachedTheEdge;

    public Enemy(Panel startPanel) {
        super(null);
        pointer = new BetterPolygon(pointerPoints);

        currentPanel = startPanel;
        h = 0;
        frameOfMovement = 0;
        maxH = currentPanel.getLength();
        acceleration = getPointerAcceleration();
    }

    protected void moveUp() {
        updateH();
        updatePointer();
        updatePoints();
        frameOfMovement++;
    }

    protected void updateH() {
        h = (initVelocity * frameOfMovement) + (0.5 * acceleration * frameOfMovement * frameOfMovement);
        if (h >= maxH) {
            h = maxH;
            reachedTheEdge = true;
        }
    }

    protected void updatePointer() {
        double x = (currentPanel.getSmallSide().getPoints().getFirst() + currentPanel.getSmallSide().getPoints().get(2)) / 2;
        double y = (currentPanel.getSmallSide().getPoints().get(1) + currentPanel.getSmallSide().getPoints().getLast()) / 2;
        Vector tempVector = new Vector(h, currentPanel.getAngle());
        pointer.moveTo(x + tempVector.getX(), y + tempVector.getY());
    }

    protected double getPointerAcceleration() {
        double s = maxH;
        double t = FRAMES_PER_MOVE;
        double v0 = initVelocity;
        return (s - v0 * t) / (0.5 * t * t);
    }

    protected void updatePoints(){}

}
