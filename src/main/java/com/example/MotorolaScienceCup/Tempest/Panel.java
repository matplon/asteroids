package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Particle;
import com.example.MotorolaScienceCup.Util;
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

    private List<Particle> bullets = new ArrayList<>();
    private List<Flipper> flippers = new ArrayList<>();

    private double length;
    private double angle;

    public Panel() {
    }

    public void updateBullets() {
        for (Particle bullet : bullets) {
            bullet.updatePosition();
        }
        double xBigSide = (bigSide.getPoints().getFirst() + bigSide.getPoints().get(2)) / 2;
        double yBigSide = (bigSide.getPoints().get(1) + bigSide.getPoints().getLast()) / 2;
        double xSmallSide = (smallSide.getPoints().getFirst() + smallSide.getPoints().get(2)) / 2;
        double ySmallSide = (smallSide.getPoints().get(1) + smallSide.getPoints().getLast()) / 2;

        double xDiff = xBigSide - xSmallSide;
        double yDiff = yBigSide - ySmallSide;

        List<Particle> bulletsToRemove = new ArrayList<>();

        for (Particle bullet : bullets) {
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
            bullets.remove(bullet);
            root.getChildren().remove(bullet);
        }
    }

    private void checkForHits() {
    }

    public void addFlipper(Flipper flipper){
        flippers.add(flipper);
    }

    public void addBullet(Particle bullet){bullets.add(bullet);}

    public void changeColorSmallSide(Color color) {
        smallSide.setStroke(color);
        smallSide.setFill(color);
    }

    public void changeColorBigSide(Color color) {
        bigSide.setStroke(color);
        bigSide.setFill(color);
    }

    public void changeColorRightSide(Color color) {
        rightSide.setStroke(color);
        rightSide.setFill(color);
    }

    public void changeColorLeftSide(Color color) {
        leftSide.setStroke(color);
        leftSide.setFill(color);
    }

    public void changeColor(Color color) {
        changeColorBigSide(color);
        changeColorLeftSide(color);
        changeColorRightSide(color);
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

    public List<Particle> getBullets() {
        return bullets;
    }

    public List<Flipper> getFlippers() {
        return flippers;
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