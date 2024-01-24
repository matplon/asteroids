package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Particle;
import com.example.MotorolaScienceCup.Util;
import javafx.geometry.Point2D;

import java.util.List;
import java.util.Objects;

public class Flipper extends Particle {
    private static String filepath = "flipper.svg";
    private static List<Double> defPoints = Util.SVGconverter(filepath);
    private static final double initVelocity = 1;

    private Panel currentPanel;
    public int step;
    private boolean goingUp;
    private double pivotX, pivotY;
    private double h;

    public Flipper(Panel startPanel) {
        super(null, startPanel.getAngle(), 0, initVelocity, 0);
        getPoints().setAll(defPoints);
        rotate(startPanel.getAngle()-90);
        System.out.println(startPanel.getAngle());

        this.currentPanel = startPanel;
        double x = (currentPanel.getSmallSide().getPoints().getFirst() + currentPanel.getSmallSide().getPoints().get(2)) / 2;
        double y = (currentPanel.getSmallSide().getPoints().get(1) + currentPanel.getSmallSide().getPoints().getLast()) / 2;
        moveTo(x, y);
        goingUp = false;
        step = 0;
        h = 0;
    }

    public void moveUp() {
        updatePosition();
        generate();
    }

    private void generate(){
        Point2D leftWing = getLeftWingCoords();
        Point2D rightWing = getRightWingCoords();
        double topLength = Math.sqrt(Math.pow(rightWing.getX() - leftWing.getX(), 2) + Math.pow(rightWing.getY() - leftWing.getY(), 2));

        double scale = 1.01;
        BetterPolygon tempFlipper = BetterPolygon.scale(this, scale);

        double topLengthTemp = Math.sqrt(Math.pow(tempFlipper.getPoints().get(8) - tempFlipper.getPoints().get(12), 2) + Math.pow(tempFlipper.getPoints().get(9) - tempFlipper.getPoints().get(13), 2));
        while(Math.round(topLength) == Math.round(topLengthTemp)){
            scale += 0.1;
            tempFlipper = BetterPolygon.scale(this, scale);
            topLengthTemp = Math.sqrt(Math.pow(tempFlipper.getPoints().get(8) - tempFlipper.getPoints().get(12), 2) + Math.pow(tempFlipper.getPoints().get(9) - tempFlipper.getPoints().get(13), 2));
        }
    }

    private Point2D getLeftWingCoords(){
        double x1 = currentPanel.getRightSide().getPoints().getFirst();
        double x2 = currentPanel.getRightSide().getPoints().get(2);
        double y1 = currentPanel.getRightSide().getPoints().get(1);
        double y2 = currentPanel.getRightSide().getPoints().getLast();

        double xGrad = (x2 - x1) / currentPanel.getLength();
        double yGrad = (y2 - y1) / currentPanel.getLength();

        double xFinal = x1 + h * xGrad;
        double yFinal = y1 + h * yGrad;

        return new Point2D(xFinal, yFinal);
    }

    private Point2D getRightWingCoords(){
        double x1 = currentPanel.getLeftSide().getPoints().getFirst();
        double x2 = currentPanel.getLeftSide().getPoints().get(2);
        double y1 = currentPanel.getLeftSide().getPoints().get(1);
        double y2 = currentPanel.getLeftSide().getPoints().getLast();

        double xGrad = (x2 - x1) / currentPanel.getLength();
        double yGrad = (y2 - y1) / currentPanel.getLength();

        double xFinal = x1 + h * xGrad;
        double yFinal = y1 + h * yGrad;

        return new Point2D(xFinal, yFinal);
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
