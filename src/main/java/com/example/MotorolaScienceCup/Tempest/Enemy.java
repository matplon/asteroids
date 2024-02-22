package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Particle;
import com.example.MotorolaScienceCup.Vector;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Enemy extends BetterPolygon {
    protected static final int FRAMES_PER_MOVE = 360;
    protected final int bulletCooldown = 60;
    protected final double radiusOffset = 5;
    protected double initVelocity = 0.1;
    protected static final List<Double> pointerPoints = Arrays.asList(0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 0.0, 1.0);
    protected Panel currentPanel;
    protected BetterPolygon pointer;
    protected double h;
    protected double maxH;
    protected double frameOfMovement;
    protected double acceleration;
    protected boolean reachedTheEdge;
    protected boolean destroyed = false;
    protected int bulletTimer;

    public Enemy(Panel startPanel) {
        super(null);
        pointer = new BetterPolygon(pointerPoints);

        currentPanel = startPanel;
        h = 0;
        frameOfMovement = 0;
        maxH = currentPanel.getLength();
        acceleration = getPointerAcceleration();

        Glow glow = new Glow(Main.glowV);
        setEffect(glow);
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

    protected void updatePoints() {
        updateSize();
        moveTo(pointer.getCenterX(), pointer.getCenterY());
    }

    protected void updateSize() {
        double smallSideLength = Math.sqrt(Math.pow(currentPanel.getSmallSide().getPoints().get(0) - currentPanel.getSmallSide().getPoints().get(2), 2) +
                Math.pow(currentPanel.getSmallSide().getPoints().get(1) - currentPanel.getSmallSide().getPoints().get(3), 2));
        double minRadius = smallSideLength / 2 - radiusOffset;

        double bigSideLength = Math.sqrt(Math.pow(currentPanel.getBigSide().getPoints().get(0) - currentPanel.getBigSide().getPoints().get(2), 2) +
                Math.pow(currentPanel.getBigSide().getPoints().get(1) - currentPanel.getBigSide().getPoints().get(3), 2));
        double maxRadius = bigSideLength / 2 - radiusOffset;

        double radiusGrad = (maxRadius - minRadius) / maxH;

        scale((minRadius + radiusGrad * h) / getRadius());
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

    public void destroy() {
        if (Main.root.getChildren().contains(this)) Main.root.getChildren().remove(this);
        destroyed = true;
        uniqueDestroyMethod();
    }

    protected void uniqueDestroyMethod() {
    }

    protected void shoot() {
        if (!destroyed) {
            Bullet bullet = new Bullet();
            currentPanel.addEnemyBullet(bullet);
            bulletTimer = bulletCooldown;
        }
    }

    public class Bullet {
        private final double outerScale = 10;
        private final double innerScale = 5;
        private final double speed = 7;
        private List<BetterPolygon> outerPoints;
        private List<BetterPolygon> innerPoints;
        private Particle outerSqr;
        private Particle innerSqr;

        private Bullet() {
            outerSqr = new Particle(pointerPoints, currentPanel.getAngle(), 120, speed, 0);
            innerSqr = new Particle(pointerPoints, currentPanel.getAngle(), 120, speed, 0);

            outerSqr.scale(outerScale);
            innerSqr.scale(innerScale);
            innerSqr.rotate(45);

            outerSqr.moveTo(pointer.getCenterX(), pointer.getCenterY());
            innerSqr.moveTo(pointer.getCenterX(), pointer.getCenterY());

            outerPoints = new ArrayList<>();
            innerPoints = new ArrayList<>();

            spawn();
            updatePoints();
        }

        private void spawn() {
            for (int i = 0; i < outerSqr.getPoints().size(); i += 2) {
                BetterPolygon point = new BetterPolygon(pointerPoints);
                BetterPolygon point2 = new BetterPolygon(pointerPoints);
                point2.scale(2);
                outerPoints.add(point2);
                innerPoints.add(point);

                point.setStroke(Color.RED);
                point2.setStroke(Color.WHITE);
                point.setEffect(new Glow(Main.glowV));
                point2.setEffect(new Glow(Main.glowV));

                Main.root.getChildren().add(point);
                Main.root.getChildren().add(point2);
            }
        }

        public boolean checkIfOutisde() {
            double x1 = currentPanel.getRightSide().getPoints().get(2);
            double y1 = currentPanel.getRightSide().getPoints().get(3);

            double x2 = currentPanel.getLeftSide().getPoints().get(2);
            double y2 = currentPanel.getLeftSide().getPoints().get(3);

            if (x1 < x2) {
                if (y1 < y2) {
                    if ((innerSqr.getCenterX() > (x1 + x2) / 2) || innerSqr.getCenterY() < (y1 + y2) / 2) return true;
                } else if (y1 > y2) {
                    if ((innerSqr.getCenterX() < (x1 + x2) / 2) || (innerSqr.getCenterY() < (y1 + y2) / 2)) return true;
                } else if (innerSqr.getCenterY() < (y1 + y2) / 2) return true;
            } else if (x1 > x2) {
                if (y1 < y2) {
                    if ((innerSqr.getCenterY() > (x1 + x2) / 2) || (innerSqr.getCenterY() > (y1 + y2) / 2)) return true;
                } else if (y1 > y2) {
                    if ((innerSqr.getCenterX() < (x1 + x2) / 2) || (innerSqr.getCenterY() > (y1 + y2) / 2)) return true;
                } else if (innerSqr.getCenterY() > (y1 + y2) / 2) return true;
            } else if (y1 < y2) {
                if (innerSqr.getCenterX() > (x1 + x2) / 2) return true;
            } else if (y1 > y2) {
                if (innerSqr.getCenterX() < (x1 + x2) / 2) return true;
            }
            return false;
        }

        private void updatePoints() {
            int index = 0;
            for (int i = 0; i < outerSqr.getPoints().size(); i += 2) {
                outerPoints.get(index).moveTo(outerSqr.getPoints().get(i), outerSqr.getPoints().get(i + 1));
                innerPoints.get(index).moveTo(innerSqr.getPoints().get(i), innerSqr.getPoints().get(i + 1));
                index++;
            }
        }

        public void move() {
            outerSqr.updatePosition();
            innerSqr.updatePosition();
            updatePoints();
        }

        public void remove() {
            for (int i = 0; i < outerPoints.size(); i++) {
                Main.root.getChildren().remove(outerPoints.get(i));
                Main.root.getChildren().remove(innerPoints.get(i));
            }
        }
    }
}
