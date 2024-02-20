package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Particle;
import com.example.MotorolaScienceCup.Util;
import com.example.MotorolaScienceCup.Vector;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.*;

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
    private final int bulletCooldown = 60;
    private static final String filepath = "flipper.svg";
    private static BetterPolygon defFlipper = BetterPolygon.rotate(new BetterPolygon(Util.SVGconverter(filepath)), 180);
    private static List<Double> defPoints = BetterPolygon.rotate(new BetterPolygon(Util.SVGconverter(filepath)), 180).getPoints();
    private static final List<Double> pointerPoints = Arrays.asList(0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 0.0, 1.0);
    private List<FlipperBullet> flipperBullets;
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
    private int bulletTimer;
    private static Queue<FlipperSeed> seedQueue = new LinkedList<>();
    private static List<FlipperSeed> seedList = new ArrayList<>();
    static boolean seedsDone = false;

    public Flipper(Panel startPanel) {
        super(null);
        pointer = new BetterPolygon(pointerPoints);

        currentPanel = startPanel;
        step = 0;
        h = 0;
        frameOfMovement = 0;
        maxH = currentPanel.getLength();
        flipperBullets = new ArrayList<>();
        bulletTimer = 0;

        double panelToHorizontalAngle = Math.toDegrees(Math.atan((startPanel.getSmallSide().getPoints().getLast() - startPanel.getSmallSide().getPoints().get(1))
                / (startPanel.getSmallSide().getPoints().get(2) - startPanel.getSmallSide().getPoints().getFirst())));
        if (Double.toString(panelToHorizontalAngle).equals("-0.0")) panelToHorizontalAngle = 180;
        defPoints = BetterPolygon.rotate(new BetterPolygon(defFlipper.getPoints()), panelToHorizontalAngle).getPoints();

        double x = (currentPanel.getSmallSide().getPoints().getFirst() + currentPanel.getSmallSide().getPoints().get(2)) / 2;
        double y = (currentPanel.getSmallSide().getPoints().get(1) + currentPanel.getSmallSide().getPoints().getLast()) / 2;
        pointer.moveTo(x, y);
        acceleration = pointerAcceleration();
        getPoints().setAll(getFlipperPoints());
    }

    public void move() {
        if (h < maxH) {
            moveUp();
        }
        if (!(Main.LEVEL == 0 && !reachedTheEdge)) changePanel();
        updateBullets();
        if (!reachedTheEdge) {
            bulletTimer--;
            if (bulletTimer <= 0) shoot();
        }
    }

    private void updatePointer() {
        double x = (currentPanel.getSmallSide().getPoints().getFirst() + currentPanel.getSmallSide().getPoints().get(2)) / 2;
        double y = (currentPanel.getSmallSide().getPoints().get(1) + currentPanel.getSmallSide().getPoints().getLast()) / 2;
        Vector tempVector = new Vector(h, currentPanel.getAngle());
        pointer.moveTo(x + tempVector.getX(), y + tempVector.getY());
    }

    private void moveUp() {
        updateH();
        updatePointer();
        getPoints().setAll(getFlipperPoints());
        frameOfMovement++;
    }

    private double pointerAcceleration() {
        double s = maxH;
        double t = FRAMES_PER_MOVE;
        double v0 = initVelocity;
        return (s - v0 * t) / (0.5 * t * t);
    }

    private void updateH() {
        h = (initVelocity * frameOfMovement) + (0.5 * acceleration * frameOfMovement * frameOfMovement);
        if (h >= maxH) {
            h = maxH;
            reachedTheEdge = true;
        }
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

            double panelToHorizontalAngle = Math.toDegrees(Math.atan((currentPanel.getSmallSide().getPoints().getLast() - currentPanel.getSmallSide().getPoints().get(1))
                    / (currentPanel.getSmallSide().getPoints().get(2) - currentPanel.getSmallSide().getPoints().getFirst())));
            if (Double.toString(panelToHorizontalAngle).equals("-0.0")) panelToHorizontalAngle = 180;
            defPoints = BetterPolygon.rotate(new BetterPolygon(defFlipper.getPoints()), panelToHorizontalAngle).getPoints();
            getPoints().setAll(getFlipperPoints());

            rotationAngle = 0;
            step = 0;
        }
    }

    private void shoot() {
        FlipperBullet flipperBullet = new FlipperBullet();
        flipperBullets.add(flipperBullet);
        bulletTimer = bulletCooldown;
    }

    private void updateBullets() {
        List<FlipperBullet> bulletsToRemove = new ArrayList<>();
        for (FlipperBullet bulletbullet : flipperBullets) {
            bulletbullet.move();
            if (bulletbullet.checkIfOutisde()) bulletsToRemove.add(bulletbullet);
        }
        for (FlipperBullet bulletBullet : bulletsToRemove) {
            bulletBullet.remove();
            flipperBullets.remove(bulletBullet);
        }
    }

    public static void spawnSeeds(double seedsNumber) {
        for (int i = 0; i < seedsNumber; i++) {
            FlipperSeed flipperSeed = new FlipperSeed();
            seedList.add(flipperSeed);
        }
    }

    public static void updateSeeds() {
        for (FlipperSeed seed : seedList) {
            if (!seed.done) seed.move();
            else seedQueue.add(seed);
        }
        FlipperSeed topSeed = seedQueue.poll();
        if (topSeed != null) {
            seedList.remove(topSeed);
            topSeed.remove();

            Flipper flipper = new Flipper(topSeed.chosenPanel);
            Main.flippers.add(flipper);
            Main.root.getChildren().add(flipper);
            flipper.setStroke(Color.RED);
        }
        if (seedList.isEmpty() && seedQueue.isEmpty()) seedsDone = true;
    }

    private static class FlipperSeed {
        private final double CENTER_X = Graphics.mapCenterX;
        private final double CENTER_Y = Graphics.mapCenterY;
        private double RADIUS = 0;
        private final double ANGULAR_SPEED = 0.2;
        private final double linearSpeed = 1.0;
        private double DEST_X;
        private double DEST_Y;
        private double ANGLE = 0;
        private double ANGLE_INC = 0.1;
        private double RADIUS_INC = 0.05;
        private Particle seed;
        private boolean spiral = true;
        private boolean done;
        private Panel chosenPanel;
        private double T;

        private FlipperSeed() {
            done = false;
            Circle bounds = new Circle(Graphics.mapCenterX, Graphics.mapCenterY, 15);
            Random random = new Random();
            double x = random.nextDouble(Main.WIDTH);
            double y = random.nextDouble(Main.HEIGHT);
            Point2D point2D = new Point2D(x, y);
            while (!bounds.contains(point2D)) {
                x = random.nextDouble(Main.WIDTH);
                y = random.nextDouble(Main.HEIGHT);
                point2D = new Point2D(x, y);
            }
            seed = new Particle(pointerPoints, 0, 0, 0, 0);
            seed.moveTo(x, y);
            seed.setStroke(Color.RED);
            Main.root.getChildren().add(seed);

            choosePanel();
        }

        private void choosePanel() {
            Random random = new Random();
            int panelIndex = random.nextInt(Main.panels.size());
            chosenPanel = Main.panels.get(panelIndex);
        }

        private void move() {
            if (!spiral) {
                Random random = new Random();
                double randomInt = random.nextInt(10);
                spiral = (randomInt <= 8);
                if (spiral) {
                    List<Double> points = chosenPanel.getSmallSide().getPoints();
                    double randomT = Math.random();
                    DEST_X = points.getFirst() + randomT * (points.get(2) - points.getFirst());
                    DEST_Y = points.get(1) + randomT * (points.getLast() - points.get(1));
                    seed.setVelocity(new Vector(linearSpeed, Math.atan2(DEST_Y - seed.getCenterY(), DEST_X - seed.getCenterX())));
                    seed.setAngle(Math.toDegrees(Math.atan2(DEST_Y - seed.getCenterY(), DEST_X - seed.getCenterX())));

                    double s = Math.sqrt(Math.pow(DEST_Y - seed.getCenterY(), 2) + Math.pow(DEST_X - seed.getCenterX(), 2));
                    T = s / linearSpeed;
                }
            }
            if (spiral) {
                double x = CENTER_X + RADIUS * Math.cos(ANGLE);
                double y = CENTER_Y + RADIUS * Math.sin(ANGLE);

                seed.moveTo(x, y);

                ANGLE += ANGLE_INC;
                RADIUS += RADIUS_INC;
            } else {
                seed.updatePosition();
                T--;
                if (T <= 0) done = true;
            }
        }

        private void remove() {
            Main.root.getChildren().remove(seed);
        }
    }

    private class FlipperBullet {
        private final double outerScale = 10;
        private final double innerScale = 5;
        private final double speed = 7;
        private List<BetterPolygon> outerPoints;
        private List<BetterPolygon> innerPoints;
        private Particle outerSqr;
        private Particle innerSqr;

        private FlipperBullet() {
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

                Main.root.getChildren().add(point);
                Main.root.getChildren().add(point2);
            }
        }

        private boolean checkIfOutisde() {
            double x1 = currentPanel.getRightSide().getPoints().get(2);
            double y1 = currentPanel.getRightSide().getPoints().get(3);

            double x2 = currentPanel.getLeftSide().getPoints().get(2);
            double y2 = currentPanel.getLeftSide().getPoints().get(3);

            if (x1 < x2) {
                if (y1 < y2) {
                    if ((innerSqr.getCenterX() > (x1 + x2) / 2) || innerSqr.getCenterY() < (y1 + y2) / 2) return true;
                } else if (y1 > y2) {
                    if ((innerSqr.getCenterX() < (x1 + x2) / 2) || (innerSqr.getCenterY() > (y1 + y2) / 2)) return true;
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