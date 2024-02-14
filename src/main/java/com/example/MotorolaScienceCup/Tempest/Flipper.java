package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Particle;
import com.example.MotorolaScienceCup.Util;
import com.example.MotorolaScienceCup.Vector;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Arrays;
import java.util.List;

public class Flipper extends BetterPolygon {
    /*  Points:
     * Left top = 8, 9
     * Right top - 12, 13
     * Left center = 6, 7
     * Right center = 14, 15
     * Left bottom = 4, 5
     * Right bottom = 0, 1
     * Center (left) = 2, 3
     * Center (right) = 10, 11
     * */

    private static final int FRAMES_PER_MOVE = 360;
    private static final double initVelocity = 0.1;
    private static String filepath = "flipper.svg";
    private static BetterPolygon defFlipper = BetterPolygon.rotate(new BetterPolygon(Util.SVGconverter(filepath)), 180);
    private static List<Double> defPoints = BetterPolygon.rotate(new BetterPolygon(Util.SVGconverter(filepath)), 180).getPoints();
    private static final List<Double> pointerPoints = Arrays.asList(0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 0.0, 1.0);
    private Panel currentPanel;
    private int step;
    public double h;
    public double maxH, tempMaxH;
    private Particle pointer;
    private double frameOfMovement;
    private double rotationAngle;
    private double pivotX, pivotY;
    private boolean isChangingPanel;
    private boolean reachedTheEdge = false;

    public Flipper(Panel startPanel) {
        super(null);
        pointer = new Particle(pointerPoints, startPanel.getAngle(), 0, initVelocity, 0);
        currentPanel = startPanel;
        step = 0;
        h = 0;
        frameOfMovement = 0;
        maxH = currentPanel.getLength();
        isChangingPanel = false;

        double panelToHorizontalAngle = Math.toDegrees(Math.atan((startPanel.getSmallSide().getPoints().getLast() - startPanel.getSmallSide().getPoints().get(1))
                / (startPanel.getSmallSide().getPoints().get(2) - startPanel.getSmallSide().getPoints().getFirst())));
        if (Double.toString(panelToHorizontalAngle).equals("-0.0")) panelToHorizontalAngle = 180;
        defPoints = BetterPolygon.rotate(new BetterPolygon(defFlipper.getPoints()), panelToHorizontalAngle).getPoints();

        double x = (currentPanel.getSmallSide().getPoints().getFirst() + currentPanel.getSmallSide().getPoints().get(2)) / 2;
        double y = (currentPanel.getSmallSide().getPoints().get(1) + currentPanel.getSmallSide().getPoints().getLast()) / 2;
        pointer.moveTo(x, y);
        pointer.setAcceleration(pointerAcceleration());
        pointer.setAccelerating(true);
        pointer.setTerminalVelocity(Integer.MAX_VALUE);
        getPoints().setAll(getFlipperPoints());

        pointer.setStroke(Color.GREEN);
        Main.root.getChildren().add(pointer);
    }

    private void updatePointer() {
        pointer.setVelocity(new Vector(pointer.getVelocity().getMagnitude() + pointer.getAcceleration(), currentPanel.getAngle()));
        for (int i = 0; i < pointer.getPoints().size(); i += 2) {
            pointer.getPoints().set(i, pointer.getPoints().get(i) + pointer.getVelocity().getX());
            pointer.getPoints().set(i + 1, pointer.getPoints().get(i + 1) + pointer.getVelocity().getY());
        }
        if (h == maxH)
            pointer.moveTo((currentPanel.getBigSide().getPoints().getFirst() + currentPanel.getBigSide().getPoints().get(2)) / 2,
                    (currentPanel.getBigSide().getPoints().get(1) + currentPanel.getBigSide().getPoints().getLast()) / 2);
    }

    public void moveUp() {
        if (h < maxH) {
//            System.out.println(getPointsOnSides());
//            System.out.println(pointer.getCenterX()+" "+pointer.getCenterY());
            updateH();

            updatePointer();
            getPoints().setAll(getFlipperPoints());
            frameOfMovement++;
        }
    }

    private double pointerAcceleration() {
        double s = maxH;
        double t = FRAMES_PER_MOVE;
        double v0 = initVelocity;
        return (s - v0 * t) / (0.5 * t * t);
    }

    private void updateH() {
        h = (initVelocity * frameOfMovement) + (0.5 * pointer.getAcceleration() * frameOfMovement * frameOfMovement);
        if(h >= maxH) h = maxH;
    }

    private void setNewH() {
        double minX = (currentPanel.getSmallSide().getPoints().getFirst() + currentPanel.getSmallSide().getPoints().get(2)) / 2;
        double minY = (currentPanel.getSmallSide().getPoints().get(1) + currentPanel.getSmallSide().getPoints().get(3)) / 2;
        double maxX = (currentPanel.getBigSide().getPoints().getFirst() + currentPanel.getBigSide().getPoints().get(2)) / 2;
        double maxY = (currentPanel.getBigSide().getPoints().get(1) + currentPanel.getBigSide().getPoints().get(3)) / 2;

        double gradX = (maxX - minX) / maxH;
        double gradY = (maxY - minY) / maxH;

        double c = (-gradX * minX) - (gradY * minY);

        h = gradX * pointer.getCenterX() + gradY * pointer.getCenterY() + c;

        if (h >= maxH) reachedTheEdge = true;
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

    private List<Double> getFlipperPoints() {
        List<Double> top = getPointsOnSides();
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

    private double changePanelAngle(double triangleBaseXLeft, double triangleBaseYLeft, double triangleBaseXRight, double triangleBaseYRight) {
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

    public void changePanel(boolean left) {
        if (step == 0) {
            rotate(180, pointer.getCenterX(), pointer.getCenterY());
            isChangingPanel = true;
//            tempMaxH = (left) ? currentPanel.getLeftPanel().getLength() : currentPanel.getRightPanel().getLength();

        } else {
            BetterPolygon temp = new BetterPolygon(getFlipperPoints());
            temp.rotate(180, pointer.getCenterX(), pointer.getCenterY());
            if (left) {
                double triangleBaseXLeft = currentPanel.getLeftPanel().getLeftSide().getPoints().get(2);
                double triangleBaseYLeft = currentPanel.getLeftPanel().getLeftSide().getPoints().get(3);
                double triangleBaseXRight = currentPanel.getRightSide().getPoints().get(2);
                double triangleBaseYRight = currentPanel.getRightSide().getPoints().get(3);

                double angle = changePanelAngle(triangleBaseXLeft, triangleBaseYLeft, triangleBaseXRight, triangleBaseYRight);
                angle /= 30;

                pivotX = temp.getPoints().get(8);
                pivotY = temp.getPoints().get(9);
                rotationAngle += angle;
            } else {
                double triangleBaseXLeft = currentPanel.getRightPanel().getRightSide().getPoints().get(2);
                double triangleBaseYLeft = currentPanel.getRightPanel().getRightSide().getPoints().get(3);
                double triangleBaseXRight = currentPanel.getLeftSide().getPoints().get(2);
                double triangleBaseYRight = currentPanel.getLeftSide().getPoints().get(3);

                double angle = changePanelAngle(triangleBaseXLeft, triangleBaseYLeft, triangleBaseXRight, triangleBaseYRight);
                angle /= 30;

                pivotX = temp.getPoints().get(12);
                pivotY = temp.getPoints().get(13);
                rotationAngle += angle;
            }
            getPoints().setAll(temp.getPoints());
            rotate(-rotationAngle, pivotX, pivotY);
        }
        step++;

        if (step == 31) {
            if (left) {
                currentPanel = currentPanel.getLeftPanel();
            } else {
                currentPanel = currentPanel.getRightPanel();
            }
            step = 0;
            maxH = currentPanel.getLength();

            double newX = (getPoints().get(8) + getPoints().get(12)) / 2;
            double newY = (getPoints().get(9) + getPoints().get(13)) / 2;
            // you have to take the points from a new flipper!!!
//            Circle circle = new Circle(getPoints().get(8), getPoints().get(9), 5, Color.PINK);
//            Main.root.getChildren().add(circle);

            if(Math.round(rotationAngle) == 90) rotationAngle *= -1;

//            pointer.rotate(rotationAngle, getPoints().get(8), getPoints().get(9));
//            pointer.moveTo(newX, newY);
            Circle circle = new Circle(getPointsOnSides().get(2), getPointsOnSides().get(3), 10, Color.PINK);
            pointer.rotate(rotationAngle, getPointsOnSides().get(2), getPointsOnSides().get(3));

            Main.root.getChildren().add(circle);
            pointer.setAngle(currentPanel.getAngle());

            pointer.setVelocity(new Vector(pointer.getVelocity().getMagnitude(), currentPanel.getAngle()));
            setNewH();

            double panelToHorizontalAngle = Math.toDegrees(Math.atan((currentPanel.getSmallSide().getPoints().getLast() - currentPanel.getSmallSide().getPoints().get(1))
                    / (currentPanel.getSmallSide().getPoints().get(2) - currentPanel.getSmallSide().getPoints().getFirst())));
            if (Double.toString(panelToHorizontalAngle).equals("-0.0")) panelToHorizontalAngle = 180;
            defPoints = BetterPolygon.rotate(new BetterPolygon(defFlipper.getPoints()), panelToHorizontalAngle).getPoints();
            getPoints().setAll(getFlipperPoints());

            rotationAngle = 0;

            isChangingPanel = false;
        }
    }
}