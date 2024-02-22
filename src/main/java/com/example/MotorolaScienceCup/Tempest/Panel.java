package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Particle;
import com.example.MotorolaScienceCup.Vector;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;
import java.util.List;

import static com.example.MotorolaScienceCup.Tempest.Main.root;


public class Panel {
    private Polyline smallSide;
    private Polyline bigSide;
    private Polyline leftSide;
    private Polyline rightSide;
    private Panel leftPanel;
    private Panel rightPanel;

    private List<Particle> playerBullets = new ArrayList<>();
    private List<Enemy.Bullet> enemyBullets = new ArrayList<>();
    private List<Flipper> flippers = new ArrayList<>();
    private List<Tanker> tankers = new ArrayList<>();
    private List<Spiker> spikers = new ArrayList<>();

    private double length;
    private double angle;
    private Color color;


    public Panel() {
    }

    public void scalePanel(double scale){
        double angle = Math.toDegrees(Math.atan2((smallSide.getPoints().get(1) + smallSide.getPoints().get(3)) / 2 - Graphics.mapCenterY,
                (smallSide.getPoints().get(0) + smallSide.getPoints().get(2)) /2 - Graphics.mapCenterX));
        Vector vector = new Vector(scale, angle);
        for (int i = 0; i < smallSide.getPoints().size(); i+=2) {
            smallSide.getPoints().set(i, smallSide.getPoints().get(i) + vector.getX());
            smallSide.getPoints().set(i+1, smallSide.getPoints().get(i+1) + vector.getY());
        }

        angle = Math.toDegrees(Math.atan2((bigSide.getPoints().get(1) + bigSide.getPoints().get(3)) / 2 - Graphics.mapCenterY,
                (bigSide.getPoints().get(0) + bigSide.getPoints().get(2)) /2 - Graphics.mapCenterX));
        vector = new Vector(scale, angle);
        for (int i = 0; i < bigSide.getPoints().size(); i+=2) {
            bigSide.getPoints().set(i, bigSide.getPoints().get(i) + vector.getX());
            bigSide.getPoints().set(i+1, bigSide.getPoints().get(i+1) + vector.getY());
        }

        angle = Math.toDegrees(Math.atan2((rightSide.getPoints().get(1) + rightSide.getPoints().get(3)) / 2 - Graphics.mapCenterY,
                (rightSide.getPoints().get(0) + rightSide.getPoints().get(2)) /2 - Graphics.mapCenterX));
        vector = new Vector(scale, angle);
        for (int i = 0; i < rightSide.getPoints().size(); i+=2) {
            rightSide.getPoints().set(i, rightSide.getPoints().get(i) + vector.getX());
            rightSide.getPoints().set(i+1, rightSide.getPoints().get(i+1) + vector.getY());
        }

        angle = Math.toDegrees(Math.atan2((leftSide.getPoints().get(1) + leftSide.getPoints().get(3)) / 2 - Graphics.mapCenterY,
                (leftSide.getPoints().get(0) + leftSide.getPoints().get(2)) /2 - Graphics.mapCenterX));
        vector = new Vector(scale, angle);
        for (int i = 0; i < leftSide.getPoints().size(); i+=2) {
            leftSide.getPoints().set(i, leftSide.getPoints().get(i) + vector.getX());
            leftSide.getPoints().set(i+1, leftSide.getPoints().get(i+1) + vector.getY());
        }
    }

    private void updatePlayerBullets() {
        for (Particle bullet : playerBullets) {
            bullet.updatePosition();
        }
        double xBigSide = (bigSide.getPoints().getFirst() + bigSide.getPoints().get(2)) / 2;
        double yBigSide = (bigSide.getPoints().get(1) + bigSide.getPoints().getLast()) / 2;
        double xSmallSide = (smallSide.getPoints().getFirst() + smallSide.getPoints().get(2)) / 2;
        double ySmallSide = (smallSide.getPoints().get(1) + smallSide.getPoints().getLast()) / 2;

        double xDiff = xBigSide - xSmallSide;
        double yDiff = yBigSide - ySmallSide;

        List<Particle> bulletsToRemove = new ArrayList<>();

        for (Particle bullet : playerBullets) {
            if (xDiff > 0 && bullet.getCenterX() < xSmallSide) {
                bulletsToRemove.add(bullet);
            } else if (xDiff < 0 && bullet.getCenterX() > xSmallSide) {
                bulletsToRemove.add(bullet);
            } else if (yDiff > 0 && bullet.getCenterY() < ySmallSide) {
                bulletsToRemove.add(bullet);
            } else if (yDiff < 0 && bullet.getCenterY() > ySmallSide) {
                bulletsToRemove.add(bullet);
            }
        }
        for (Particle bullet : bulletsToRemove) {
            playerBullets.remove(bullet);
            root.getChildren().remove(bullet);
        }
    }

    private void updateEnemyBullets(){
        List<Enemy.Bullet> bulletsToDestroy = new ArrayList<>();
        for(Enemy.Bullet bullet : enemyBullets){
            bullet.move();
            if (bullet.checkIfOutisde()) {
                bulletsToDestroy.add(bullet);
            }
        }
        for (Enemy.Bullet bullet : bulletsToDestroy){
            bullet.remove();
            enemyBullets.remove(bullet);
        }
    }

    public void update(){
        List<Tanker> tankersToDestroy = new ArrayList<>();
        List<Spiker> spikersToDestroy = new ArrayList<>();
        for(Flipper flipper : flippers)
            flipper.move();
        for(Tanker tanker : tankers){
            if(!tanker.move()) tankersToDestroy.add(tanker);
        }
        for(Spiker spiker : spikers){
            if(!spiker.move()) spikersToDestroy.add(spiker);
        }
        updatePlayerBullets();
        updateEnemyBullets();
        for (Tanker tanker : tankersToDestroy){
            tanker.destroy();
        }
        for (Spiker spiker : spikersToDestroy){
            spiker.destroy();
        }
    }

    private void checkForHits() {
    }

    public void addFlipper(Flipper flipper) {
        flippers.add(flipper);
    }

    public void addTanker(Tanker tanker) {
        tankers.add(tanker);
    }

    public void addSPiker(Spiker spiker) {
        spikers.add(spiker);
    }

    public void addPlayerBullet(Particle bullet) {
        playerBullets.add(bullet);
    }

    public void addEnemyBullet(Enemy.Bullet bullet){
        enemyBullets.add(bullet);
    }

    public void changeColorSmallSide(Color color) {
        smallSide.setStroke(color);
        smallSide.setFill(color);
        this.color = Color.YELLOW;
    }

    public void changeColorBigSide(Color color) {
        bigSide.setStroke(color);
        bigSide.setFill(color);
        this.color = Color.YELLOW;
    }

    public void changeColorRightSide(Color color) {
        rightSide.setStroke(color);
        rightSide.setFill(color);
        this.color = Color.YELLOW;
    }

    public Color getColor() {
        return color;
    }

    public void changeColorLeftSide(Color color) {
        leftSide.setStroke(color);
        leftSide.setFill(color);
        this.color = Color.YELLOW;
    }

    public void changeColor(Color color) {
        changeColorBigSide(color);
        changeColorLeftSide(color);
        changeColorRightSide(color);
    }

    public boolean isBottomPanel() {
        double midYRight = (rightSide.getPoints().get(1) + rightSide.getPoints().get(3)) / 2;
        double midYLeft = (leftSide.getPoints().get(1) + leftSide.getPoints().get(3)) / 2;

        return (midYRight > Graphics.mapCenterY || midYLeft > Graphics.mapCenterY);
    }

    public void setSmallSide(Polyline smallSide) {
        this.smallSide = smallSide;
    }

    public void setBigSide(Polyline bigSide) {
        this.bigSide = bigSide;
    }

    public void setLeftSide(Polyline leftSide) {
        this.leftSide = leftSide;
    }

    public void setRightSide(Polyline rightSide) {
        this.rightSide = rightSide;
    }

    public void setLeftPanel(Panel leftPanel) {
        this.leftPanel = leftPanel;
    }

    public void setRightPanel(Panel rightPanel) {
        this.rightPanel = rightPanel;
    }

    public Polyline getSmallSide() {
        return smallSide;
    }

    public Polyline getBigSide() {
        return bigSide;
    }

    public Polyline getLeftSide() {
        return leftSide;
    }

    public Polyline getRightSide() {
        return rightSide;
    }

    public Panel getLeftPanel() {
        return leftPanel;
    }

    public Panel getRightPanel() {
        return rightPanel;
    }

    public List<Particle> getPlayerBullets() {
        return playerBullets;
    }

    public List<Flipper> getFlippers() {
        return flippers;
    }

    public List<Tanker> getTankers() {
        return tankers;
    }

    public List<Spiker> getSpikers() {
        return spikers;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "Panel{" +
                "smallSide=" + smallSide +
                ", bigSide=" + bigSide +
                ", leftSide=" + leftSide +
                ", rightSide=" + rightSide +
                ", leftPanel=" + Main.panels.indexOf(leftPanel) +
                ", rightPanel=" + Main.panels.indexOf(rightPanel) +
                '}';
    }
}