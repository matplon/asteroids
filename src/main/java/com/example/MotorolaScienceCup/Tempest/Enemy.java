package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Particle;
import com.example.MotorolaScienceCup.Vector;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.geometry.Point2D;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.*;

public class Enemy extends BetterPolygon {
    private static List<Panel> panelsWithSpiker = new ArrayList<>();
    protected static int FRAMES_PER_MOVE = 360;
    protected final int bulletCooldown = 90;
    private static final int seedCooldown = 20;
    protected final double radiusOffset = 5;
    protected double initVelocity = 0.1;
    protected static final List<Double> pointerPoints = Arrays.asList(0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 0.0, 1.0);
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
        double maxRadius = bigSideLength / 4 - radiusOffset;

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

    protected boolean chooseDirection() {
        if(currentPanel.getLeftPanel() == null){
            return false;
        }
        if(currentPanel.getRightPanel() == null){
            return true;
        }
        int distLeft = 0;
        Panel nextPanel = currentPanel.getLeftPanel();
        while (!nextPanel.equals(Main.player.getCurrentPanel())) {
            distLeft++;
            if(nextPanel.getLeftPanel() == null) return false;
            nextPanel = nextPanel.getLeftPanel();
        }
        int distRight = 0;
        nextPanel = currentPanel.getRightPanel();
        while (!nextPanel.equals(Main.player.getCurrentPanel())) {
            distRight++;
            if(nextPanel.getRightPanel() == null) return true;
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

    public static void spawnSeeds(int flippers, int tankers, int spikers, int fuseballs) {
        seedsDone = false;
        int flipperCounter = flippers;
        int tankerCounter = tankers;
        int spikerCounter = spikers;
        int fuseballCounter = fuseballs;

        List<Integer> possibilities = new ArrayList<>();

        Random random = new Random();

        while (flipperCounter > 0 || tankerCounter > 0 || spikerCounter > 0) {
            possibilities.clear();
            if (flipperCounter > 0) possibilities.add(0);
            if (tankerCounter > 0) possibilities.add(1);
            if (spikerCounter > 0) possibilities.add(2);
            if (fuseballCounter > 0) possibilities.add(3);

            int choiceIndex = random.nextInt(possibilities.size());
            int choice = possibilities.get(choiceIndex);
            Seed seed = new Seed(choice);
            seedList.add(seed);

            if (choice == 0) flipperCounter--;
            else if (choice == 1) tankerCounter--;
            else if (choice == 2) spikerCounter--;
            else if (choice == 3) fuseballCounter--;
        }
    }

    public static void updateSeeds() {
        List<Seed> seedsToRemove = new ArrayList<>();
        for (Seed topSeed : seedList) {
            topSeed.move();
            if (seedTimer <= 0) {
                seedTimer = seedCooldown;

                int flipperNumber = 0;
                int tankerNumber = 0;
                int spikerNumber = 0;
                int fuseballNumber = 0;

                for (Panel panel : Main.panels) {
                    flipperNumber += panel.getFlippers().size();
                    tankerNumber += panel.getTankers().size();
                    spikerNumber += panel.getSpikers().size();
                    fuseballNumber += panel.getFuseBalls().size();
                }

                if (topSeed.enemyType == 0 && flipperNumber < Main.maxFlipper) {
                    Flipper flipper = new Flipper(topSeed.chosenPanel);
                    Main.root.getChildren().add(flipper);
                    seedsToRemove.add(topSeed);
                } else if (topSeed.enemyType == 1 && tankerNumber < Main.maxTanker) {
                    Tanker tanker = new Tanker(topSeed.chosenPanel);
                    Main.root.getChildren().add(tanker);
                    seedsToRemove.add(topSeed);
                } else if (topSeed.enemyType == 2 && spikerNumber < Main.maxSpiker && topSeed.done) {
                    Spiker spiker = new Spiker(topSeed.chosenPanel);
                    Main.root.getChildren().add(spiker);
                    seedsToRemove.add(topSeed);
                } else if (topSeed.enemyType == 3 && fuseballNumber < Main.maxFuseball) {
                    Fuseball fuseball = new Fuseball(topSeed.chosenPanel);
                    Main.root.getChildren().add(fuseball);
                    seedsToRemove.add(topSeed);
                }
            }
        }
        for (Seed seed : seedsToRemove) {
            seed.remove();
        }
        if (seedList.isEmpty()) seedsDone = true;
        seedTimer--;
    }

    private static class Seed {
        public BetterPolygon seedPath;
        private int spikerTimer = 120;
        private Particle seed;
        private double speed;
        public boolean done = false;
        public Panel chosenPanel;
        private double distToCover;
        private double distCovered = 0;
        private int enemyType;
        private int pointOfPath;
        private Vector moveVector;

        public Seed(int enemyType) {
            this.enemyType = enemyType;
            choosePanel();
            Random random = new Random();
            seed = new Particle(Flipper.pointerPoints, 0, 0, 0, 0);
            if (enemyType == 0) seed.setStroke(Flipper.flipperColor);
            else if (enemyType == 1) seed.setStroke(Tanker.tankerColor);
            else if (enemyType == 2) {
                seed.setStroke(Spiker.spikerColor);
                seed.moveTo((chosenPanel.getSmallSide().getPoints().get(0) + chosenPanel.getSmallSide().getPoints().get(2)) / 2,
                        (chosenPanel.getSmallSide().getPoints().get(1) + chosenPanel.getSmallSide().getPoints().get(3)) / 2);
                spikerTimer += random.nextInt(120);
            } else if (enemyType == 3) {
                seed.setStroke(new Color(Math.random(), Math.random(), Math.random(), 1));
            }
            Main.root.getChildren().add(seed);
            if (enemyType != 2) {
                spawn();
            } else {
                seed.moveTo((chosenPanel.getSmallSide().getPoints().get(0) + chosenPanel.getSmallSide().getPoints().get(2)) / 2,
                        (chosenPanel.getSmallSide().getPoints().get(1) + chosenPanel.getSmallSide().getPoints().get(3)) / 2);
            }
        }

        private void spawn() {
            seedPath = new BetterPolygon(Graphics.mapShape.getPoints());


            Random random = new Random();
            double pathScale = random.nextDouble(0.05, 0.85);
            seedPath.scale(pathScale);

            pointOfPath = random.nextInt(seedPath.getPoints().size() / 2);
            seed.moveTo(seedPath.getPoints().get(pointOfPath * 2), seedPath.getPoints().get(pointOfPath * 2 + 1));

            double duration = random.nextDouble(2000, 4000);

            PathTransition pathTransition = new PathTransition();
            pathTransition.setNode(seed);
            pathTransition.setPath(seedPath);
            pathTransition.setCycleCount(Animation.INDEFINITE);
            pathTransition.setDuration(Duration.millis(duration));
            pathTransition.setDelay(Duration.ZERO);
            pathTransition.setInterpolator(Interpolator.LINEAR);
            pathTransition.play();
        }

        private void choosePanel() {
            Random random = new Random();
            int panelIndex = random.nextInt(Main.panels.size());
            chosenPanel = Main.panels.get(panelIndex);
            if (enemyType == 2) {
                if(panelsWithSpiker.size() == Main.panels.size()){
                    int index = random.nextInt(Main.panels.size());
                    chosenPanel = Main.panels.get(index);
                }
                else{
                    while (panelsWithSpiker.contains(chosenPanel)) {
                        panelIndex = random.nextInt(Main.panels.size());
                        chosenPanel = Main.panels.get(panelIndex);
                    }
                    panelsWithSpiker.add(chosenPanel);
                }
            }
        }

        public void move() {
            if (enemyType == 2) {
                spikerTimer--;
                if (spikerTimer <= 0) {
                    done = true;
                }
            }
        }

        public void remove() {
            seedList.remove(this);
            Main.root.getChildren().remove(seed);
        }
    }
}
