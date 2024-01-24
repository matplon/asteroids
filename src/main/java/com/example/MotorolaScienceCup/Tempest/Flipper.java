package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Particle;
import com.example.MotorolaScienceCup.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Flipper extends Particle {
    private Panel currentPanel;
    public int step;
    private boolean goingUp;
    private double pivotX, pivotY;
    private double y;

    public Flipper(List<Double> points, Panel startPanel) {
        super(points, );
        this.currentPanel = startPanel;
        double x = (currentPanel.getSmallSide().getPoints().getFirst() + currentPanel.getSmallSide().getPoints().get(2)) / 2;
        double y = (currentPanel.getSmallSide().getPoints().get(1) + currentPanel.getSmallSide().getPoints().getLast()) / 2;
        moveTo(x, y);
        goingUp = false;
        step = 0;
        y = 0;
    }

    private void generate(){
        List<Double> points = new ArrayList<>();
        double grad = (currentPanel.getRightSide().getPoints().get(3) - currentPanel.getRightSide().getPoints().get(1)) /
                (currentPanel.getRightSide().getPoints().get(2) - currentPanel.getRightSide().getPoints().get(0));
        double x1 = (currentPanel.getRightSide().getPoints().get(0) + y) * grad;
        double x2

    }

    public void moveUp() {

    }




    public void move(boolean left) {
        if(step == 0){
            pivotX = (currentPanel.getBigSide().getPoints().getFirst() + currentPanel.getBigSide().getPoints().get(2)) / 2;
            pivotY = (currentPanel.getBigSide().getPoints().get(1) + currentPanel.getBigSide().getPoints().getLast()) / 2;
            rotate(180, pivotX, pivotY);
        }
        else if(step > 30) {
            if(left){
                currentPanel = currentPanel.getLeftPanel();
            }
            else{
                currentPanel = currentPanel.getRightPanel();
            }
            step = -1;
            pivotX = (currentPanel.getBigSide().getPoints().getFirst() + currentPanel.getBigSide().getPoints().get(2)) / 2;
            pivotY = (currentPanel.getBigSide().getPoints().get(1) + currentPanel.getBigSide().getPoints().getLast()) / 2;
        }
        else{
            if (left) {
                double triangleBaseXLeft = currentPanel.getLeftPanel().getLeftSide().getPoints().get(2);
                double triangleBaseYLeft = currentPanel.getLeftPanel().getLeftSide().getPoints().get(3);
                double triangleBaseXRight = currentPanel.getRightSide().getPoints().get(2);
                double triangleBaseYRight = currentPanel.getRightSide().getPoints().get(3);

                double angle = -getMovementAngle(triangleBaseXLeft, triangleBaseYLeft, triangleBaseXRight, triangleBaseYRight);
                angle /= 30;

                for (int i = 0; i < getPoints().size(); i += 2) {
                    if (Objects.equals(getPoints().get(i), currentPanel.getLeftSide().getPoints().get(2)) && Objects.equals(getPoints().get(i + 1), currentPanel.getLeftSide().getPoints().getLast())) {
                        rotate(angle, getPoints().get(i), getPoints().get(i + 1));
                        break;
                    }
                }
            } else {
                double triangleBaseXLeft = currentPanel.getRightPanel().getRightSide().getPoints().get(2);
                double triangleBaseYLeft = currentPanel.getRightPanel().getRightSide().getPoints().get(3);
                double triangleBaseXRight = currentPanel.getLeftSide().getPoints().get(2);
                double triangleBaseYRight = currentPanel.getLeftSide().getPoints().get(3);

                double angle = -getMovementAngle(triangleBaseXLeft, triangleBaseYLeft, triangleBaseXRight, triangleBaseYRight);
                angle /= 30;

                for (int i = 0; i < getPoints().size(); i += 2) {
                    if (Objects.equals(getPoints().get(i), currentPanel.getRightSide().getPoints().get(2)) && Objects.equals(getPoints().get(i + 1), currentPanel.getRightSide().getPoints().getLast())) {
                        rotate(angle, getPoints().get(i), getPoints().get(i + 1));
                        break;
                    }
                }
            }
        }
        step++;
    }

    private double getMovementAngle(double triangleBaseXLeft, double triangleBaseYLeft, double triangleBaseXRight, double triangleBaseYRight) {
        double triangleBaseLengthX = triangleBaseXRight - triangleBaseXLeft;
        double triangleBaseLengthY = triangleBaseYRight - triangleBaseYLeft;
        double triangleBaseLength = Math.sqrt(Math.pow(triangleBaseLengthX, 2) + Math.pow(triangleBaseLengthY, 2));

        double triangleSideLength = Main.bigSideLength;

        double angle = Math.toDegrees(Math.acos((Math.pow(triangleBaseLength, 2) - Math.pow(triangleSideLength, 2) - Math.pow(triangleSideLength, 2)) /
                (-2 * triangleSideLength * triangleSideLength)));
        if (triangleBaseXLeft == triangleBaseXRight || triangleBaseYLeft == triangleBaseYRight) {
            angle = 180;
        }
        return angle;
    }
}
