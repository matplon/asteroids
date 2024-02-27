package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Vector;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.MotorolaScienceCup.Tempest.Main.*;

public class Player extends BetterPolygon {
    private final int shotCooldown = 5;
    private double outerTriangleOffset = 30;
    private final double maxTriangleOffset = 30;
    private final double minTriangleOffset = 25;
    private double xStepLeft, xStepRight;
    private double yStepLeft, yStepRight;
    private double leftRightDiffX, leftRightDiffY;
    private double hStep = 2;
    public double h;
    private int panelIndex;
    private Circle pointer = new Circle(1);
    private static Panel currentPanel;
    public int shotTimer = 0;


    public Player(Panel startPanel) {
        super(null);
        currentPanel = startPanel;
        calculateSteps();
        h = currentPanel.getLength();
        panelIndex = currentPanel.getIndex();
        pointer.setCenterX((currentPanel.getBigSide().getPoints().get(0) + currentPanel.getBigSide().getPoints().get(2)) / 2);
        pointer.setCenterY((currentPanel.getBigSide().getPoints().get(1) + currentPanel.getBigSide().getPoints().get(3)) / 2);
        setStroke(Color.RED);
        setEffect(new Glow(glowV));
        updatePoints();
    }

    public void moveDown() {
        currentPanel = panels.get(panelIndex);
        h -= hStep;
        updatePointer();
        updatePoints();
//        outerTriangleOffset = getTriangleOffset();
        root.getChildren().add(this);
        if (!checkForSpiker() && h <= 0) {
            Main.temp();
        }
    }

    private boolean checkForSpiker() {
        for (Spiker spiker : currentPanel.getSpikers()) {
            if (spiker.intersects(this.getLayoutBounds())) {
                Main.gameOver();
                return true;
            }
        }
        return false;
    }

    private double getTriangleOffset() {
        double triangleOffsetGrad = (maxTriangleOffset - minTriangleOffset) / currentPanel.getLength();
        return minTriangleOffset + h * triangleOffsetGrad;
    }

    protected void updatePointer() {
        double x = (currentPanel.getSmallSide().getPoints().getFirst() + currentPanel.getSmallSide().getPoints().get(2)) / 2;
        double y = (currentPanel.getSmallSide().getPoints().get(1) + currentPanel.getSmallSide().getPoints().getLast()) / 2;
        Vector tempVector = new Vector(h, currentPanel.getAngle());
        pointer.setCenterX(x + tempVector.getX());
        pointer.setCenterY(y + tempVector.getY());
    }

    private void updatePoints() {
        Vector vector = new Vector(outerTriangleOffset, currentPanel.getAngle());
        double xTriangle1 = pointer.getCenterX() + vector.getX();
        double yTriangle1 = pointer.getCenterY() + vector.getY();
        double xTriangle2 = xTriangle1 + vector.getX();
        double yTriangle2 = yTriangle1 + vector.getY();
        List<Double> points = new ArrayList<>();
        points.add(xTriangle2);
        points.add(yTriangle2);
        points.add(getPointsOnSides().get(0));
        points.add(getPointsOnSides().get(1));
        points.add(xTriangle1);
        points.add(yTriangle1);
        points.add(getPointsOnSides().get(2));
        points.add(getPointsOnSides().get(3));
        getPoints().setAll(points);
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

        double gradX1 = (maxX1 - minX1) / currentPanel.getLength();
        double gradY1 = (maxY1 - minY1) / currentPanel.getLength();
        double gradX2 = (maxX2 - minX2) / currentPanel.getLength();
        double gradY2 = (maxY2 - minY2) / currentPanel.getLength();

        double x1 = minX1 + gradX1 * h;
        double y1 = minY1 + gradY1 * h;
        double x2 = minX2 + gradX2 * h;
        double y2 = minY2 + gradY2 * h;

        return Arrays.asList(x1, y1, x2, y2);
    }

    private void calculateSteps() {
        leftRightDiffX = getPointsOnSides().get(0) - getPointsOnSides().get(2);
        leftRightDiffY = getPointsOnSides().get(1) - getPointsOnSides().get(3);

        double POSITIONS_PER_SIDE = 6;
        xStepRight = leftRightDiffX / POSITIONS_PER_SIDE;
        yStepRight = leftRightDiffY / POSITIONS_PER_SIDE;
        xStepLeft = -leftRightDiffX / POSITIONS_PER_SIDE;
        yStepLeft = -leftRightDiffY / POSITIONS_PER_SIDE;
    }

    public void move(boolean left) {
        double hPercentage = h / currentPanel.getLength();
        calculateSteps();
        List<Double> points = getPointsOnSides();
        if (left) {
            pointer.setCenterX(pointer.getCenterX() + xStepLeft);
            pointer.setCenterY(pointer.getCenterY() + yStepLeft);
            if (leftRightDiffX > 0 && pointer.getCenterX() < points.get(2)) {
                pointer.setCenterX(points.get(2));
                pointer.setCenterY(points.get(3));
                currentPanel = currentPanel.getLeftPanel();
            } else if (leftRightDiffX < 0 && pointer.getCenterX() > points.get(2)) {
                pointer.setCenterX(points.get(2));
                pointer.setCenterY(points.get(3));
                currentPanel = currentPanel.getLeftPanel();
            } else if (leftRightDiffY > 0 && pointer.getCenterY() < points.get(3)) {
                pointer.setCenterX(points.get(2));
                pointer.setCenterY(points.get(3));
                currentPanel = currentPanel.getLeftPanel();
            } else if (leftRightDiffY < 0 && pointer.getCenterY() > points.get(3)) {
                pointer.setCenterX(points.get(2));
                pointer.setCenterY(points.get(3));
                currentPanel = currentPanel.getLeftPanel();
            }
        } else {
            pointer.setCenterX(pointer.getCenterX() + xStepRight);
            pointer.setCenterY(pointer.getCenterY() + yStepRight);
            if (leftRightDiffX > 0 && getCenterX() > points.get(0)) {
                pointer.setCenterX(points.get(0));
                pointer.setCenterY(points.get(1));
                currentPanel = currentPanel.getRightPanel();
            } else if (leftRightDiffX < 0 && getCenterX() < points.get(0)) {
                pointer.setCenterX(points.get(0));
                pointer.setCenterY(points.get(1));
                currentPanel = currentPanel.getRightPanel();
            } else if (leftRightDiffY > 0 && getCenterY() > points.get(1)) {
                pointer.setCenterX(points.get(0));
                pointer.setCenterY(points.get(1));
                currentPanel = currentPanel.getRightPanel();
            } else if (leftRightDiffY < 0 && getCenterY() < points.get(1)) {
                pointer.setCenterX(points.get(0));
                pointer.setCenterY(points.get(1));
                currentPanel = currentPanel.getRightPanel();
            }
        }
        panelIndex = currentPanel.getIndex();
        currentPanel = panels.get(panelIndex);
        checkIfSideClose(left);
        h = currentPanel.getLength() * hPercentage;
        updatePoints();
    }

    private void checkIfSideClose(boolean left) {
        if (left) {
            double xDiff = pointer.getCenterX() - currentPanel.getLeftSide().getPoints().get(2);
            double yDiff = pointer.getCenterY() - currentPanel.getLeftSide().getPoints().getLast();
            if (xDiff < 0.5 && xDiff > -0.5 && yDiff < 0.5 && yDiff > -0.5) {
                pointer.setCenterX(currentPanel.getLeftSide().getPoints().get(2));
                pointer.setCenterY(currentPanel.getLeftSide().getPoints().get(3));
                currentPanel = currentPanel.getLeftPanel();
            }
        } else {
            double xDiff = pointer.getCenterX() - currentPanel.getRightSide().getPoints().get(2);
            double yDiff = pointer.getCenterY() - currentPanel.getRightSide().getPoints().getLast();
            if (xDiff < 0.5 && xDiff > -0.5 && yDiff < 0.5 && yDiff > -0.5) {
                pointer.setCenterX(currentPanel.getRightSide().getPoints().get(2));
                pointer.setCenterY(currentPanel.getRightSide().getPoints().get(3));
                currentPanel = currentPanel.getRightPanel();
            }
        }
    }

    public void shoot() {
        if (shotTimer <= 0) {
            System.out.println("ui");
            Bullet bullet = new Bullet();
            currentPanel.addPlayerBullet(bullet);
            root.getChildren().add(bullet);
            shotTimer = shotCooldown;
        }
    }

    public Panel getCurrentPanel() {
        return currentPanel;
    }

    public class Bullet extends Circle {
        private final double normalSpeed = 10;
        private final double nextLevelSpeed = 20;
        private double maxRadius;
        private double minRadius;
        private double grad;
        private Vector velocity;
        public double h = 0;
        public Panel panel;

        private Bullet() {
            super(0);
            h = player.h;
            maxRadius = Math.sqrt(Math.pow(currentPanel.getBigSide().getPoints().get(0) - currentPanel.getBigSide().getPoints().get(2), 2) +
                    Math.pow(currentPanel.getBigSide().getPoints().get(1) - currentPanel.getBigSide().getPoints().get(3), 2)) / 20;
            minRadius = Math.sqrt(Math.pow(currentPanel.getSmallSide().getPoints().get(0) - currentPanel.getSmallSide().getPoints().get(2), 2) +
                    Math.pow(currentPanel.getSmallSide().getPoints().get(1) - currentPanel.getSmallSide().getPoints().get(3), 2)) / 20;
            this.setRadius(getBulletRadius());
            this.setCenterX(pointer.getCenterX());
            this.setCenterY(pointer.getCenterY());
            panel = currentPanel;
            grad = (maxRadius - minRadius) / currentPanel.getLength();
            this.setCenterX((panel.getBigSide().getPoints().get(0) + panel.getBigSide().getPoints().get(2)) / 2);
            this.setCenterY((panel.getBigSide().getPoints().get(1) + panel.getBigSide().getPoints().get(3)) / 2);
            velocity = new Vector(normalSpeed, 180 + currentPanel.getAngle());
            updatePoints();
            this.setStroke(Color.MAGENTA);
            this.setEffect(new Glow(glowV));
        }

        public boolean move(boolean nextLevel) {
            if (!nextLevel) h -= normalSpeed;
            else h -= 5;
            updatePoints();
            return (h > 0);
        }

        private void updatePoints() {
            if (h <= 0) {
                this.setCenterX((panel.getSmallSide().getPoints().get(0) + panel.getSmallSide().getPoints().get(2)) / 2);
                this.setCenterY((panel.getSmallSide().getPoints().get(1) + panel.getSmallSide().getPoints().get(3)) / 2);
            } else {
                this.setCenterX(this.getCenterX() + velocity.getX());
                this.setCenterY(this.getCenterY() + velocity.getY());
            }
            this.setRadius(getBulletRadius());
        }

        private double getBulletRadius() {
            return minRadius + grad * h;
        }

        public void remove() {
            root.getChildren().remove(this);
            panel.getPlayerBullets().remove(this);
        }
    }
}