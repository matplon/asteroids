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
        private double maxOuterRadius;
        private double minOuterRadius;
        private double outerRadiusGrad;
        private final double speed = 8;
        private double h;
        private Panel panel;
        private Vector velocity;
        private final List<Double> defPoints = Arrays.asList(100.0, 0.0, 70.71, 70.71, 0.0, 100.0, -70.71, 70.71, -100.0, 0.0, -70.71, -70.71, 0.0, -100.0, 70.71, -70.71);
        private List<BetterPolygon> outerPoints;
        private List<BetterPolygon> innerPoints;
        public BetterPolygon outerSqr;
        private BetterPolygon innerSqr;

        private Bullet() {
            outerSqr = new BetterPolygon(defPoints);
            innerSqr = new BetterPolygon(defPoints);

            panel = currentPanel;

            maxOuterRadius = Math.sqrt(Math.pow(currentPanel.getBigSide().getPoints().get(0) - currentPanel.getBigSide().getPoints().get(2), 2) +
                    Math.pow(currentPanel.getBigSide().getPoints().get(1) - currentPanel.getBigSide().getPoints().get(3), 2)) / 10;
            minOuterRadius = Math.sqrt(Math.pow(currentPanel.getBigSide().getPoints().get(0) - currentPanel.getBigSide().getPoints().get(2), 2) +
                    Math.pow(currentPanel.getBigSide().getPoints().get(1) - currentPanel.getBigSide().getPoints().get(3), 2)) / 15;
            outerRadiusGrad = (maxOuterRadius - minOuterRadius) / panel.getLength();

            h = Enemy.this.h;

            outerSqr.scale(getOuterRadius() / outerSqr.getRadius());
            innerSqr.scale(outerSqr.getRadius() / 2 / innerSqr.getRadius());
            innerSqr.rotate(45);

            outerSqr.moveTo(pointer.getCenterX(), pointer.getCenterY());
            innerSqr.moveTo(pointer.getCenterX(), pointer.getCenterY());

            outerPoints = new ArrayList<>();
            innerPoints = new ArrayList<>();

            velocity = new Vector(speed, panel.getAngle());

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

        public boolean checkIfOutside(){
            return h >= panel.getLength();
        }

        private double getOuterRadius(){
            return minOuterRadius + outerRadiusGrad * h;
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
            outerSqr.moveTo(outerSqr.getCenterX() + velocity.getX(), outerSqr.getCenterY() + velocity.getY());
            innerSqr.moveTo(innerSqr.getCenterX() + velocity.getX(), innerSqr.getCenterY() + velocity.getY());
            outerSqr.rotate(600);
            innerSqr.rotate(700);
            outerSqr.scale(getOuterRadius() / outerSqr.getRadius());
            innerSqr.scale(getOuterRadius() / 2 / innerSqr.getRadius());
            h += speed;
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
