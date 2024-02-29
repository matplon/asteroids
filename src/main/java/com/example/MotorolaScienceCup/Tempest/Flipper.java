package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Util;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.*;

public class Flipper extends Enemy {
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

    private static final String filepath = "flipper.svg";
    static Color flipperColor = Color.RED;
    private BetterPolygon defFlipper = BetterPolygon.rotate(new BetterPolygon(Util.SVGconverter(filepath)), 180);
    private List<Double> defPoints;
    private double fullRotationAngle;
    private double tempRotationAngle;
    private double pivotX, pivotY;
    private int step;
    private boolean left;

    public Flipper(Panel startPanel) {
        super(startPanel);

        double panelToHorizontalAngle = Math.toDegrees(Math.atan((currentPanel.getSmallSide().getPoints().get(3) - currentPanel.getSmallSide().getPoints().get(1))
                / (currentPanel.getSmallSide().getPoints().get(2) - currentPanel.getSmallSide().getPoints().get(0))));
        if (Double.toString(panelToHorizontalAngle).equals("-0.0")) panelToHorizontalAngle = 180;

        defFlipper.rotate(panelToHorizontalAngle);

        defPoints = defFlipper.getPoints();

        double x = (currentPanel.getSmallSide().getPoints().get(0) + currentPanel.getSmallSide().getPoints().get(2)) / 2;
        double y = (currentPanel.getSmallSide().getPoints().get(1) + currentPanel.getSmallSide().getPoints().get(3)) / 2;
        pointer.moveTo(x, y);
        acceleration = getPointerAcceleration();
        getPoints().setAll(getFlipperPoints());

        currentPanel.addFlipper(this);
        setStroke(flipperColor);
    }

    public boolean move() {
        if (h < maxH) {
            moveUp();
        }
        if (!reachedTheEdge) {
            bulletTimer--;
            if (bulletTimer <= 0) shoot();
        }
        if (!(Main.LEVEL == 1 && !reachedTheEdge)) {
            if (changePanel()) return false;
        }
        return true;
    }

    @Override
    protected void updatePoints() {
        getPoints().setAll(getFlipperPoints());
    }

    @Override
    protected void uniqueDestroyMethod() {
        if (Main.root.getChildren().contains(this)) Main.root.getChildren().remove(this);
        currentPanel.getFlippers().remove(this);
    }

    private List<Double> getPointsOnSides() {
        double minX1 = currentPanel.getRightSide().getPoints().get(0);
        double minY1 = currentPanel.getRightSide().getPoints().get(1);
        double minX2 = currentPanel.getLeftSide().getPoints().get(0);
        double minY2 = currentPanel.getLeftSide().getPoints().get(1);
        double maxX1 = currentPanel.getRightSide().getPoints().get(2);
        double maxY1 = currentPanel.getRightSide().getPoints().get(3);
        double maxX2 = currentPanel.getLeftSide().getPoints().get(2);
        double maxY2 = currentPanel.getLeftSide().getPoints().get(3);

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
        double v8 = top.get(0);
        double v9 = top.get(1);
        double v12 = top.get(2);
        double v13 = top.get(3);

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

        double defRightCenterToRightBottomLengthX = defPoints.get(14) - defPoints.get(0);
        double ratio9 = defRightCenterToRightBottomLengthX / defTopLength;
        double v0 = v14 - ratio9 * topLength;

        double defRightCenterToRightBottomLengthY = defPoints.get(15) - defPoints.get(1);
        double ratio10 = defRightCenterToRightBottomLengthY / defTopLength;
        double v1 = v15 - ratio10 * topLength;

        return Arrays.asList(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15);
    }

    private double findChangeAngle(boolean left, double pivotX, double pivotY, List<Double> initialPoints, List<Double> endPoints) {
        double initVectorX = pivotX - initialPoints.get(0);
        double initVectorY = pivotY - initialPoints.get(1);
        double endVectorX = pivotX - endPoints.get(0);
        double endVectorY = pivotY - endPoints.get(1);

        double dotProduct = initVectorX * endVectorX + initVectorY * endVectorY;

        double initMagnitude = Math.sqrt(Math.pow(initVectorX, 2) + Math.pow(initVectorY, 2));
        double endMagnitude = Math.sqrt(Math.pow(endVectorX, 2) + Math.pow(endVectorY, 2));

        double angle = Math.acos(dotProduct / (initMagnitude * endMagnitude));

        Point2D leftPoint, rightPoint, middlePoint;
        if (left) {
            leftPoint = new Point2D(initialPoints.get(0), initialPoints.get(1));
            rightPoint = new Point2D(endPoints.get(0), endPoints.get(1));
        } else {
            rightPoint = new Point2D(initialPoints.get(0), initialPoints.get(1));
            leftPoint = new Point2D(endPoints.get(0), endPoints.get(1));
        }
        middlePoint = new Point2D(pivotX, pivotY);
        if (isReflex(rightPoint, middlePoint, leftPoint)) {
            angle = 2 * Math.PI - angle;
        }
        if (Double.isNaN(angle)) angle = Math.PI;

        return Math.toDegrees(angle);
    }

    private boolean changePanel() {
        if (step == 0) {
            tempRotationAngle = 0;
            rotate(180, pointer.getCenterX(), pointer.getCenterY());
            left = chooseDirection();
            List<Double> initPoints = getPoints();
            double percentage = h / maxH;
            Panel tempFlipperPanel;
            if (left) {
                tempFlipperPanel = currentPanel.getLeftPanel();
                pivotX = initPoints.get(8);
                pivotY = initPoints.get(9);
            } else {
                tempFlipperPanel = currentPanel.getRightPanel();
                pivotX = initPoints.get(12);
                pivotY = initPoints.get(13);
            }
            Flipper tempFlipper = new Flipper(tempFlipperPanel);
            tempFlipper.h = tempFlipperPanel.getLength() * percentage;
            tempFlipper.updatePoints();
            List<Double> endPoints = tempFlipper.getPoints();
            tempFlipper.destroy();
            fullRotationAngle = findChangeAngle(left, pivotX, pivotY, initPoints, endPoints);
            if (left) {
                fullRotationAngle *= -1;
            }
        } else {
            BetterPolygon temp = new BetterPolygon(getFlipperPoints());
            temp.rotate(180, pointer.getCenterX(), pointer.getCenterY());
            if (left) {
                pivotX = temp.getPoints().get(8);
                pivotY = temp.getPoints().get(9);
            } else {
                pivotX = temp.getPoints().get(12);
                pivotY = temp.getPoints().get(13);
            }
            tempRotationAngle += fullRotationAngle / 30;
            getPoints().setAll(temp.getPoints());
            rotate(tempRotationAngle, pivotX, pivotY);
        }
        step++;

        if (step == 31) {
            if (left) {
                currentPanel = currentPanel.getLeftPanel();
            } else {
                currentPanel = currentPanel.getRightPanel();
            }
            currentPanel.getFlippers().add(this);

            double percentage = h / maxH;
            maxH = currentPanel.getLength();
            h = percentage * maxH;

            double defFlipperPointerX = (defFlipper.getPoints().get(8) + defFlipper.getPoints().get(12)) / 2;
            double defFlipperPointerY = (defFlipper.getPoints().get(9) + defFlipper.getPoints().get(13)) / 2;
            defFlipper.rotate(180, defFlipperPointerX, defFlipperPointerY);
            double defFlipperPivotX = (left) ? defFlipper.getPoints().get(8) : defFlipper.getPoints().get(12);
            double defFlipperPivotY = (left) ? defFlipper.getPoints().get(9) : defFlipper.getPoints().get(13);
            defFlipper.rotate(fullRotationAngle, defFlipperPivotX, defFlipperPivotY);

            defPoints = new ArrayList<>(defFlipper.getPoints());

            pointer.moveTo((getPointsOnSides().get(0) + getPointsOnSides().get(2)) / 2, (getPointsOnSides().get(1) + getPointsOnSides().get(3)) / 2);
            if(Main.root.getChildren().contains(defFlipper)){
                Main.root.getChildren().remove(defFlipper);
            }
            defFlipper.setStroke(Color.RED);
            defFlipper.moveTo(200, 800);

            Main.root.getChildren().add(defFlipper);
            fullRotationAngle = 0;
            step = 0;

            return true;
        }
        return false;
    }

    private boolean isReflex(Point2D right, Point2D center, Point2D left) {
        Point2D middle = new Point2D((left.getX() + right.getX()) / 2, (left.getY() + right.getY()) / 2);
        if (left.getX() < right.getX()) {
            if (center.getX() > middle.getX() && center.getY() > middle.getY()) return true;
            else if (center.getX() < middle.getX() && center.getY() > middle.getY()) return true;
        } else if (left.getX() > right.getX()) {
            if (center.getX() > middle.getX() && center.getY() < middle.getY()) return true;
            else if (center.getX() < middle.getX() && center.getY() < middle.getY()) return true;
        }
        return false;
    }
}
