package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;

import java.util.List;

public class Flipper extends BetterPolygon {
    private Panel currentPanel;
    private boolean movedUp;
    public Flipper(List<Double> points, Panel startPanel){
        super(points);
        this.currentPanel = startPanel;
        double x = (currentPanel.getSmallSide().getPoints().getFirst() + currentPanel.getSmallSide().getPoints().get(2))/2;
        double y = (currentPanel.getSmallSide().getPoints().get(1) + currentPanel.getSmallSide().getPoints().getLast())/2;
        moveTo(x, y);
        movedUp = false;
    }

    public void moveUp(){

    }

    public void move(boolean left){
        rotate(180, getCenterX(), getCenterY());

        if(left){
            double triangleBaseXLeft = currentPanel.getLeftPanel().getLeftSide().getPoints().get(2);
            double triangleBaseYLeft = currentPanel.getLeftPanel().getLeftSide().getPoints().get(3);
            double triangleBaseXRight = currentPanel.getRightSide().getPoints().get(2);
            double triangleBaseYRight = currentPanel.getRightSide().getPoints().get(3);
        }
    }

    void rotate(double angle, double pivotX, double pivotY) {
        double centerX1 = pivotX;
        double centerY1 = pivotY;

        // Convert angle to radians
        double radianAngle = Math.toRadians(angle);

        // Apply rotation to each point
        for (int i = 0; i < getPoints().size(); i += 2) {
            double x = getPoints().get(i);
            double y = getPoints().get(i + 1);

            // Perform rotation
            double rotatedX = centerX1 + (x - centerX1) * Math.cos(radianAngle) - (y - centerY1) * Math.sin(radianAngle);
            double rotatedY = centerY1 + (x - centerX1) * Math.sin(radianAngle) + (y - centerY1) * Math.cos(radianAngle);

            // Update the coordinates
            getPoints().set(i, rotatedX);
            getPoints().set(i + 1, rotatedY);
        }
    }
}
