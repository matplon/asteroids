package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Flipper extends BetterPolygon {
    private Panel currentPanel;
    private boolean goingUp;

    public Flipper(List<Double> points, Panel startPanel) {
        super(null);
        this.currentPanel = startPanel;
        double x = (currentPanel.getSmallSide().getPoints().getFirst() + currentPanel.getSmallSide().getPoints().get(2)) / 2;
        double y = (currentPanel.getSmallSide().getPoints().get(1) + currentPanel.getSmallSide().getPoints().getLast()) / 2;
        moveTo(x, y);
        goingUp = false;
    }

    private void generate(){
        List<Double> points = new ArrayList<>();

    }

    public void moveUp() {

    }

    public void move(boolean left) {
        double pivotX = (currentPanel.getBigSide().getPoints().getFirst() + currentPanel.getBigSide().getPoints().get(2)) / 2;
        double pivotY = (currentPanel.getBigSide().getPoints().get(1) + currentPanel.getBigSide().getPoints().getLast()) / 2;

        rotate(180, pivotX, pivotY);

        if (left) {
            double triangleBaseXLeft = currentPanel.getLeftPanel().getLeftSide().getPoints().get(2);
            double triangleBaseYLeft = currentPanel.getLeftPanel().getLeftSide().getPoints().get(3);
            double triangleBaseXRight = currentPanel.getRightSide().getPoints().get(2);
            double triangleBaseYRight = currentPanel.getRightSide().getPoints().get(3);

            double angle = getMovementAngle(triangleBaseXLeft, triangleBaseYLeft, triangleBaseXRight, triangleBaseYRight);

            for (int i = 0; i < getPoints().size(); i += 2) {
                if (Objects.equals(getPoints().get(i), currentPanel.getLeftSide().getPoints().get(2)) && Objects.equals(getPoints().get(i + 1), currentPanel.getLeftSide().getPoints().getLast())) {
                    rotate(angle, getPoints().get(i), getPoints().get(i + 1));
                    break;
                }
            }
            currentPanel = currentPanel.getLeftPanel();
        } else {
            double triangleBaseXLeft = currentPanel.getRightPanel().getRightSide().getPoints().get(2);
            double triangleBaseYLeft = currentPanel.getRightPanel().getRightSide().getPoints().get(3);
            double triangleBaseXRight = currentPanel.getLeftSide().getPoints().get(2);
            double triangleBaseYRight = currentPanel.getLeftSide().getPoints().get(3);

            double angle = getMovementAngle(triangleBaseXLeft, triangleBaseYLeft, triangleBaseXRight, triangleBaseYRight);

            for (int i = 0; i < getPoints().size(); i += 2) {
                if (Objects.equals(getPoints().get(i), currentPanel.getRightSide().getPoints().get(2)) && Objects.equals(getPoints().get(i + 1), currentPanel.getRightSide().getPoints().getLast())) {
                    rotate(angle, getPoints().get(i), getPoints().get(i + 1));
                    break;
                }
            }
            currentPanel = currentPanel.getRightPanel();
        }
    }

    private double getMovementAngle(double triangleBaseXLeft, double triangleBaseYLeft, double triangleBaseXRight, double triangleBaseYRight) {
        double triangleBaseLengthX = triangleBaseXRight - triangleBaseXLeft;
        double triangleBaseLengthY = triangleBaseYRight - triangleBaseYLeft;
        double triangleBaseLength = Math.sqrt(Math.pow(triangleBaseLengthX, 2) + Math.pow(triangleBaseLengthY, 2));

        double triangleSideLength = Main.bigSideLength;

        double angle = -Math.toDegrees(Math.acos((Math.pow(triangleBaseLength, 2) - Math.pow(triangleSideLength, 2) - Math.pow(triangleSideLength, 2)) /
                (-2 * triangleSideLength * triangleSideLength)));
        if (triangleBaseXLeft == triangleBaseXRight || triangleBaseYLeft == triangleBaseYRight) {
            angle = 180;
        }
        return angle;
    }
}
