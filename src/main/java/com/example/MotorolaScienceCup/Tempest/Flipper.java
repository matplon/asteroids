package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Particle;
import com.example.MotorolaScienceCup.Util;
import com.example.MotorolaScienceCup.Vector;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;

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
    private double rotationAngle;
    private double pivotX, pivotY;
    private int step;
    private boolean left;
    Circle circle1, circle2;
    BetterPolygon polygon = new BetterPolygon(null);

    public Flipper(Panel startPanel) {
        super(Main.panels.get(0));

        double panelToHorizontalAngle = Math.toDegrees(Math.atan((currentPanel.getSmallSide().getPoints().get(3) - currentPanel.getSmallSide().getPoints().get(1))
                / (currentPanel.getSmallSide().getPoints().get(2) - currentPanel.getSmallSide().getPoints().get(0))));
        if (Double.toString(panelToHorizontalAngle).equals("-0.0")) panelToHorizontalAngle = 180;
        else if (currentPanel.isBottomPanel()) {
            panelToHorizontalAngle += 180;
        }
        defFlipper.rotate(panelToHorizontalAngle);
//        defPoints = BetterPolygon.rotate(new BetterPolygon(defFlipper.getPoints()), panelToHorizontalAngle).getPoints();
        defPoints = defFlipper.getPoints();
        Main.root.getChildren().add(defFlipper);
        defFlipper.moveTo(300, 200);
        defFlipper.setStroke(Color.AQUA);

        double x = (currentPanel.getSmallSide().getPoints().get(0) + currentPanel.getSmallSide().getPoints().get(2)) / 2;
        double y = (currentPanel.getSmallSide().getPoints().get(1) + currentPanel.getSmallSide().getPoints().get(3)) / 2;
        pointer.moveTo(x, y);
        acceleration = getPointerAcceleration();
        getPoints().setAll(getFlipperPoints());

        currentPanel.addFlipper(this);
        setStroke(flipperColor);

        circle1 = new Circle(getPointsOnSides().get(0), getPointsOnSides().get(1), 5, Color.GOLD);
        Main.root.getChildren().add(circle1);
        circle2 = new Circle(getPointsOnSides().get(2), getPointsOnSides().get(3), 5, Color.GREEN);
        Main.root.getChildren().add(circle2);
        polygon.setStroke(Color.BLUEVIOLET);
    }

    public void move() {
        if (h < maxH) {
            moveUp();
        }
        if (!(Main.LEVEL == 0 && !reachedTheEdge)) changePanel();
        if (!reachedTheEdge) {
            bulletTimer--;
            if (bulletTimer <= 0) shoot();
        }
        circle1.setCenterX(getPointsOnSides().get(0));
        circle1.setCenterY(getPointsOnSides().get(1));
        circle2.setCenterX(getPointsOnSides().get(2));
        circle2.setCenterY(getPointsOnSides().get(3));
        currentPanel.changeColorSmallSide(Color.WHITE);
    }

    @Override
    protected void updatePoints() {
        getPoints().setAll(getFlipperPoints());
    }

    @Override
    protected void uniqueDestroyMethod() {
        Main.root.getChildren().remove(this);
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

    private double changePanelAngle(double triangleBaseXLeft, double triangleBaseYLeft, double triangleBaseXRight, double triangleBaseYRight, Point2D centerPoint) {
        double triangleBaseLengthX = triangleBaseXRight - triangleBaseXLeft;
        double triangleBaseLengthY = triangleBaseYRight - triangleBaseYLeft;
        double triangleBaseLength = Math.sqrt(Math.pow(triangleBaseLengthX, 2) + Math.pow(triangleBaseLengthY, 2));

        double triangleSideLength = Math.sqrt(Math.pow(centerPoint.getX() - triangleBaseXRight, 2) + Math.pow(centerPoint.getY() - triangleBaseYRight, 2));

        double angle = Math.toDegrees(Math.acos((Math.pow(triangleBaseLength, 2) - Math.pow(triangleSideLength, 2) - Math.pow(triangleSideLength, 2)) /
                (-2 * triangleSideLength * triangleSideLength)));
        if (triangleBaseXLeft == triangleBaseXRight || triangleBaseYLeft == triangleBaseYRight) {
            angle = 180;
        }
        if(isConcave(new Point2D(triangleBaseXLeft, triangleBaseYLeft), centerPoint, new Point2D(triangleBaseXRight, triangleBaseYRight))){
            System.out.println("yesyes");
            if(angle > 0){
                angle = 360 - angle;
            }
            else angle = -360 + angle;
        }
        if(Double.isNaN(angle)) angle = 180;
        return angle;
    }

    private boolean chooseDirection() {
        int distLeft = 0;
        Panel nextPanel = currentPanel.getLeftPanel();
        while (!nextPanel.equals(Main.player.getCurrentPanel())) {
            distLeft++;
            nextPanel = nextPanel.getLeftPanel();
        }
        int distRight = 0;
        nextPanel = currentPanel.getRightPanel();
        while (!nextPanel.equals(Main.player.getCurrentPanel())) {
            distRight++;
            nextPanel = nextPanel.getRightPanel();
        }
        if (distLeft < distRight) return true;
        else if (distRight < distLeft) return false;
        else {
            Random random = new Random();
            int dirChooser = random.nextInt(2);
            return (dirChooser == 0);
        }
    }

    private void changePanel() {
        if (step == 0) {
            rotate(180, pointer.getCenterX(), pointer.getCenterY());
            left = chooseDirection();
        } else {
            BetterPolygon temp = new BetterPolygon(getFlipperPoints());
            temp.rotate(180, pointer.getCenterX(), pointer.getCenterY());
            if (left) {
                double triangleBaseXLeft = currentPanel.getLeftPanel().getLeftSide().getPoints().get(2);
                double triangleBaseYLeft = currentPanel.getLeftPanel().getLeftSide().getPoints().get(3);
                double triangleBaseXRight = currentPanel.getRightSide().getPoints().get(2);
                double triangleBaseYRight = currentPanel.getRightSide().getPoints().get(3);

                double angle = changePanelAngle(triangleBaseXLeft, triangleBaseYLeft, triangleBaseXRight, triangleBaseYRight,
                        new Point2D(currentPanel.getLeftSide().getPoints().get(2), currentPanel.getLeftSide().getPoints().get(3)));
                angle /= 30;

                pivotX = temp.getPoints().get(8);
                pivotY = temp.getPoints().get(9);
                rotationAngle += angle;
            } else {
                double triangleBaseXLeft = currentPanel.getRightPanel().getRightSide().getPoints().get(2);
                double triangleBaseYLeft = currentPanel.getRightPanel().getRightSide().getPoints().get(3);
                double triangleBaseXRight = currentPanel.getLeftSide().getPoints().get(2);
                double triangleBaseYRight = currentPanel.getLeftSide().getPoints().get(3);

                double angle = changePanelAngle(triangleBaseXLeft, triangleBaseYLeft, triangleBaseXRight, triangleBaseYRight,
                        new Point2D(currentPanel.getRightSide().getPoints().get(2), currentPanel.getRightSide().getPoints().get(3)));
                angle /= 30;

                pivotX = temp.getPoints().get(12);
                pivotY = temp.getPoints().get(13);
                rotationAngle += angle;
            }
            getPoints().setAll(temp.getPoints());
            if (left) rotate(-rotationAngle, pivotX, pivotY);
            else rotate(rotationAngle, pivotX, pivotY);
        }
        step++;

        if (step == 31) {
            if (left) {
                currentPanel = currentPanel.getLeftPanel();
            } else {
                currentPanel = currentPanel.getRightPanel();
            }

            if (left) pointer.rotate(-rotationAngle, getPointsOnSides().get(0), getPointsOnSides().get(1));
            else pointer.rotate(rotationAngle, getPointsOnSides().get(2), getPointsOnSides().get(3));

            double percentage = h / maxH;
            maxH = currentPanel.getLength();
            h = percentage * maxH;

            double panelToHorizontalAngle = Math.toDegrees(Math.atan((currentPanel.getSmallSide().getPoints().get(3) - currentPanel.getSmallSide().getPoints().get(1))
                    / (currentPanel.getSmallSide().getPoints().get(2) - currentPanel.getSmallSide().getPoints().get(0))));
            if (Double.toString(panelToHorizontalAngle).equals("-0.0")) panelToHorizontalAngle = 180;
            else if (currentPanel.isBottomPanel()) {
                panelToHorizontalAngle += 180;
            }
//            System.out.println(currentPanel.getAngle());
//            defPoints = BetterPolygon.rotate(new BetterPolygon(defFlipper.getPoints()), panelToHorizontalAngle).getPoints();
//            BetterPolygon betterPolygon = new BetterPolygon(defPoints);
//            Main.root.getChildren().add(betterPolygon);
//            betterPolygon.scale(3);
//            betterPolygon.setStroke(Color.AQUA);
            if(left) defFlipper.rotate(rotationAngle, defPoints.get(8), defPoints.get(9));
            else defFlipper.rotate(-rotationAngle, defPoints.get(12), defPoints.get(13));
            defPoints = defFlipper.getPoints();
            System.out.println(rotationAngle+" chuj");

            getPoints().setAll(getFlipperPoints());

            rotationAngle = 0;
            step = 0;
        }
    }

    private boolean isConcave(Point2D right, Point2D center, Point2D left) {
        double xDiff = right.getX()-left.getX();
        double yDiff = right.getY()-left.getY();
        if(xDiff < 1.5 || yDiff < 1.5) return false;
        Point2D middle = new Point2D((left.getX() + right.getX()) / 2, (left.getY() + right.getY()) / 2);
        if(left.getX() < right.getX()){
            if(center.getX() > middle.getX() && center.getY() > middle.getY()) return true;
            else if(center.getX() < middle.getX() && center.getY() > middle.getY()) return true;
        }
        else if(left.getX() > right.getX()){
            if(center.getX() > middle.getX() && center.getY() < middle.getY()) return true;
            else if(center.getX() < middle.getX() && center.getY() < middle.getY()) return true;
        }
        return false;
    }
}
