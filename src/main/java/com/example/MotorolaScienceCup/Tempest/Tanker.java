package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Util;
import com.example.MotorolaScienceCup.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class Tanker extends BetterPolygon {
    private static final int FRAMES_PER_MOVE = 360;
    private static final double initVelocity = 0.1;

    private static final String filepath = "flipper.svg";
    private static BetterPolygon defTanker = BetterPolygon.rotate(new BetterPolygon(Util.SVGconverter(filepath)), 180);
    private static List<Double> defPoints = BetterPolygon.rotate(new BetterPolygon(Util.SVGconverter(filepath)), 180).getPoints();
    private static final List<Double> pointerPoints = Arrays.asList(0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 0.0, 1.0);

    private Panel currentPanel;
    private int step;
    private double h;
    private double maxH;
    private BetterPolygon pointer;
    private double frameOfMovement;
    private double rotationAngle;
    private double pivotX, pivotY;
    private boolean reachedTheEdge = false;
    private boolean left;
    private final double acceleration;

    static boolean seedsDone = false;

    public Tanker(Panel startPanel) {
        super(null);

        pointer = new BetterPolygon(pointerPoints);

        currentPanel = startPanel;
        step = 0;
        h = 0;
        frameOfMovement = 0;
        maxH = currentPanel.getLength();


        double panelToHorizontalAngle = Math.toDegrees(Math.atan((startPanel.getSmallSide().getPoints().getLast() - startPanel.getSmallSide().getPoints().get(1))
                / (startPanel.getSmallSide().getPoints().get(2) - startPanel.getSmallSide().getPoints().getFirst())));
        if (Double.toString(panelToHorizontalAngle).equals("-0.0")) panelToHorizontalAngle = 180;
        defPoints = BetterPolygon.rotate(new BetterPolygon(defTanker.getPoints()), panelToHorizontalAngle).getPoints();

        double x = (currentPanel.getSmallSide().getPoints().getFirst() + currentPanel.getSmallSide().getPoints().get(2)) / 2;
        double y = (currentPanel.getSmallSide().getPoints().get(1) + currentPanel.getSmallSide().getPoints().getLast()) / 2;
        pointer.moveTo(x, y);
        acceleration = pointerAcceleration();
        getPoints().setAll(getTankerPoints());
    }
    private List<Double> getPointsOnSides() {
        double minX1 = currentPanel.getRightSide().getPoints().getFirst();
        double minY1 = currentPanel.getRightSide().getPoints().get(1);
        double minX2 = currentPanel.getLeftSide().getPoints().getFirst();
        double minY2 = currentPanel.getLeftSide().getPoints().get(1);
        double maxX1 = currentPanel.getRightSide().getPoints().get(2);
        double maxY1 = currentPanel.getRightSide().getPoints().getLast();
        double maxX2 = currentPanel.getLeftSide().getPoints().get(2);
        double maxY2 = currentPanel.getLeftSide().getPoints().getLast();

        double gradX1 = (maxX1 - minX1) / maxH;
        double gradY1 = (maxY1 - minY1) / maxH;
        double gradX2 = (maxX2 - minX2) / maxH;
        double gradY2 = (maxY2 - minY2) / maxH;

        double x1 = minX1 + gradX1 * h;
        double y1 = minY1 + gradY1 * h;
        double x2 = minX2 + gradX2 * h;
        double y2 = minY2 + gradY2 * h;

        return Arrays.asList(x1, y1, x2, y2);
    }
    private List<Double> getTankerPoints() {
        List<java.lang.Double> top = getPointsOnSides();
        double v8 = top.getFirst();
        double v9 = top.get(1);
        double v12 = top.get(2);
        double v13 = top.getLast();

        double topLength = Math.sqrt(Math.pow(v8 - v12, 2) + Math.pow(v9 - v13, 2));
        double defTopLength = Math.sqrt(Math.pow(defPoints.get(8) - defPoints.get(12), 2) + Math.pow(defPoints.get(9) - defPoints.get(13), 2));

        double defTopToCenterLengthX = defPoints.get(12) - defPoints.get(2);
        double ratio1 = defTopToCenterLengthX / defTopLength;
        double v2 = v12 - ratio1 * topLength;
        double v10 = v2;

        double defTopToCenterLengthY = defPoints.get(13) - defPoints.get(3);
        double ratio2 = defTopToCenterLengthY / defTopLength;
        double v3 = v13 - ratio2 * topLength;
        double v11 = v3;

        double defCenterToLeftCenterLengthX = defPoints.get(2) - defPoints.get(6);
        double ratio3 = defCenterToLeftCenterLengthX / defTopLength;
        double v6 = v2 - ratio3 * topLength;

        double defCenterToLeftCenterLengthY = defPoints.get(3) - defPoints.get(7);
        double ratio4 = defCenterToLeftCenterLengthY / defTopLength;
        double v7 = v3 - ratio4 * topLength;

        double defCenterToRightCenterLengthX = defPoints.get(2) - defPoints.get(14);
        double ratio5 = defCenterToRightCenterLengthX / defTopLength;
        double v14 = v2 - ratio5 * topLength;

        double defCenterToRightCenterLengthY = defPoints.get(3) - defPoints.get(15);
        double ratio6 = defCenterToRightCenterLengthY / defTopLength;
        double v15 = v3 - ratio6 * topLength;

        double defLeftCenterToLeftBottomLengthX = defPoints.get(6) - defPoints.get(4);
        double ratio7 = defLeftCenterToLeftBottomLengthX / defTopLength;
        double v4 = v6 - ratio7 * topLength;

        double defLeftCenterToLeftBottomLengthY = defPoints.get(7) - defPoints.get(5);
        double ratio8 = defLeftCenterToLeftBottomLengthY / defTopLength;
        double v5 = v7 - ratio8 * topLength;

        double defRightCenterToRightBottomLengthX = defPoints.get(14) - defPoints.getFirst();
        double ratio9 = defRightCenterToRightBottomLengthX / defTopLength;
        double v0 = v14 - ratio9 * topLength;

        double defRightCenterToRightBottomLengthY = defPoints.get(15) - defPoints.get(1);
        double ratio10 = defRightCenterToRightBottomLengthY / defTopLength;
        double v1 = v15 - ratio10 * topLength;

        return Arrays.asList(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15);
    }
    private double pointerAcceleration() {
        double s = maxH;
        double t = FRAMES_PER_MOVE;
        double v0 = initVelocity;
        return (s - v0 * t) / (0.5 * t * t);
    }
    private void moveUp() {
        updateH();
        updatePointer();
        getPoints().setAll(getTankerPoints());
        frameOfMovement++;
    }
    private void updatePointer() {
        double x = (currentPanel.getSmallSide().getPoints().getFirst() + currentPanel.getSmallSide().getPoints().get(2)) / 2;
        double y = (currentPanel.getSmallSide().getPoints().get(1) + currentPanel.getSmallSide().getPoints().getLast()) / 2;
        Vector tempVector = new Vector(h, currentPanel.getAngle());
        pointer.moveTo(x + tempVector.getX(), y + tempVector.getY());
    }
    private void updateH() {
        h = (initVelocity * frameOfMovement) + (0.5 * acceleration * frameOfMovement * frameOfMovement);
        if (h >= maxH) {
            h = maxH;
            reachedTheEdge = true;
        }
    }
    public void move() {
        if (h < maxH) {
            moveUp();
        }
    }

    public Panel getCurrentPanel() {
        return currentPanel;
    }

    public void setCurrentPanel(Panel currentPanel) {
        this.currentPanel = currentPanel;
    }
}


