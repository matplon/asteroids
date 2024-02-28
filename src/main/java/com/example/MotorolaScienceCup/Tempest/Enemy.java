package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Particle;
import com.example.MotorolaScienceCup.Vector;
import javafx.geometry.Point2D;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.*;

public class Enemy extends BetterPolygon {
    private static List<Panel> panelsWithSpiker = new ArrayList<>();
    protected static final int FRAMES_PER_MOVE = 360;
    protected final int bulletCooldown = 90;
    private static final int seedCooldown = 8;
    protected final double radiusOffset = 5;
    protected double initVelocity = 0.1;
    protected static final List<Double> pointerPoints = Arrays.asList(0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 0.0, 1.0);
    private static Queue<Seed> seedQueue = new LinkedList<>();
    private static List<Seed> seedList = new ArrayList<>();
    private static int seedTimer = 0;
    static boolean seedsDone = false;
    protected Panel currentPanel;
    protected BetterPolygon pointer;
    protected double h;
    protected double maxH;
    protected double frameOfMovement;
    protected double acceleration;
    protected boolean reachedTheEdge = false;
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
        double x = (currentPanel.getSmallSide().getPoints().get(0) + currentPanel.getSmallSide().getPoints().get(2)) / 2;
        double y = (currentPanel.getSmallSide().getPoints().get(1) + currentPanel.getSmallSide().getPoints().get(3)) / 2;
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
        private final double speed = 12;
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
                    Math.pow(currentPanel.getBigSide().getPoints().get(1) - currentPanel.getBigSide().getPoints().get(3), 2)) / 19;
            minOuterRadius = Math.sqrt(Math.pow(currentPanel.getBigSide().getPoints().get(0) - currentPanel.getBigSide().getPoints().get(2), 2) +
                    Math.pow(currentPanel.getBigSide().getPoints().get(1) - currentPanel.getBigSide().getPoints().get(3), 2)) / 18;
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

        public boolean checkIfOutside() {
            return h >= panel.getLength();
        }

        private double getOuterRadius() {
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

    public static void spawnSeeds(int flippers, int tankers, int spikers) {
        int flipperCounter = flippers;
        int tankerCounter = tankers;
        int spikerCounter = spikers;

        List<Integer> possibilities = new ArrayList<>();

        Random random = new Random();

        while (flipperCounter > 0 || tankerCounter > 0 || spikerCounter > 0) {
            possibilities.clear();
            if (flipperCounter > 0) possibilities.add(0);
            if (tankerCounter > 0) possibilities.add(1);
            if (spikerCounter > 0) possibilities.add(2);

            int choiceIndex = random.nextInt(possibilities.size());
            int choice = possibilities.get(choiceIndex);
            Seed seed = new Seed(choice);
            seedList.add(seed);

            if (choice == 0) flipperCounter--;
            else if (choice == 1) tankerCounter--;
            else if (choice == 2) spikerCounter--;
        }
    }

    public static void updateSeeds() {
        List<Seed> seedsToRemove = new ArrayList<>();
        for (Seed seed : seedList) {
            if (!seed.done) seed.move();
            else {
                seedQueue.add(seed);
                seedsToRemove.add(seed);
            }
        }
        for (Seed seed : seedsToRemove) {
            seedList.remove(seed);
        }
        if (seedTimer <= 0) {
            seedTimer = seedCooldown;
            Seed topSeed = seedQueue.poll();
            if (topSeed != null) {
                seedList.remove(topSeed);
                topSeed.remove();

                if (topSeed.enemyType == 0) {
                    Flipper flipper = new Flipper(topSeed.chosenPanel);
                    Main.root.getChildren().add(flipper);
                } else if (topSeed.enemyType == 1) {
                    Tanker tanker = new Tanker(topSeed.chosenPanel);
                    Main.root.getChildren().add(tanker);
                } else if (topSeed.enemyType == 2) {
                    Spiker spiker = new Spiker(topSeed.chosenPanel);
                    Main.root.getChildren().add(spiker);
                }
            }
        }
        if (seedList.isEmpty() && seedQueue.isEmpty()) seedsDone = true;
        seedTimer--;
    }

    private static class Seed {
        private final int spiralCooldown = 20;
        private final double CENTER_X = Graphics.mapCenterX;
        private final double CENTER_Y = Graphics.mapCenterY;
        private double RADIUS = 0;
        private final double linearSpeed = 0.5;
        private double DEST_X;
        private double DEST_Y;
        private double ANGLE = 0;
        private double ANGLE_INC;
        private double RADIUS_INC;
        private int spiralTimer = 20;
        private int spikerTimer = 120;
        private Particle seed;
        private boolean spiral = true;
        public boolean done = false;
        public Panel chosenPanel;
        private double T;
        private int enemyType;

        public Seed(int enemyType) {
            this.enemyType = enemyType;
            choosePanel();
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
            seed = new Particle(Flipper.pointerPoints, 0, 0, 0, 0);
            seed.moveTo(x, y);
            if (enemyType == 0) seed.setStroke(Flipper.flipperColor);
            else if (enemyType == 1) seed.setStroke(Tanker.tankerColor);
            else if (enemyType == 2) {
                seed.setStroke(Spiker.spikerColor);
                seed.moveTo((chosenPanel.getSmallSide().getPoints().get(0) + chosenPanel.getSmallSide().getPoints().get(2)) / 2,
                        (chosenPanel.getSmallSide().getPoints().get(1) + chosenPanel.getSmallSide().getPoints().get(3)) / 2);
                spikerTimer += random.nextInt(120);
            }
            Main.root.getChildren().add(seed);

            RADIUS_INC = random.nextDouble(0.1, 0.2);
            ANGLE_INC = random.nextDouble(0.02, 0.1);
            int negativeAngle = random.nextInt(2);
            if (negativeAngle == 0) ANGLE_INC *= -1;
        }

        private void choosePanel() {
            Random random = new Random();
            int panelIndex = random.nextInt(Main.panels.size());
            chosenPanel = Main.panels.get(panelIndex);
            if(enemyType == 2){
                while(panelsWithSpiker.contains(chosenPanel)){
                    panelIndex = random.nextInt(Main.panels.size());
                    chosenPanel = Main.panels.get(panelIndex);
                }
                panelsWithSpiker.add(chosenPanel);
            }
        }

        public void move() {
            if(enemyType == 2){
                spikerTimer--;
                if(spikerTimer <= 0){
                    done = true;
                }
                return;
            }
            if (spiral && spiralTimer <= 0) {
                Random random = new Random();
                double randomDouble = Math.random();
                spiral = (randomDouble < 0.86);
                if (!spiral) {
                    List<Double> points = chosenPanel.getSmallSide().getPoints();
                    double randomT = Math.random();
                    DEST_X = points.get(0) + randomT * (points.get(2) - points.get(0));
                    DEST_Y = points.get(1) + randomT * (points.get(3) - points.get(1));
                    double angle = Math.toDegrees(Math.atan2(DEST_Y - seed.getCenterY(), DEST_X - seed.getCenterX()));
                    seed.setVelocity(new Vector(linearSpeed, angle));
                    seed.setAngle(angle);

                    double s = Math.sqrt(Math.pow(DEST_Y - seed.getCenterY(), 2) + Math.pow(DEST_X - seed.getCenterX(), 2));
                    T = s / linearSpeed;
                } else {
                    spiralTimer = spiralCooldown;
                }
            }
            if (spiral) {
                double x = CENTER_X + RADIUS * Math.cos(ANGLE);
                double y = CENTER_Y + RADIUS * Math.sin(ANGLE);

                seed.moveTo(x, y);

                ANGLE += ANGLE_INC;
                RADIUS += RADIUS_INC;

                spiralTimer--;
            } else {
                seed.updatePosition();
                T--;
                if (T <= 0) done = true;
            }
        }

        public void remove() {
            Main.root.getChildren().remove(seed);
        }
    }
}
