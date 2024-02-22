package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Particle;
import com.example.MotorolaScienceCup.Util;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import static com.example.MotorolaScienceCup.Tempest.Main.*;

public class Player extends BetterPolygon {
    private double xStepLeft, xStepRight;
    private double yStepLeft, yStepRight;
    private double leftRightDiffX, leftRightDiffY;
    private  static Panel currentPanel;


    public Player(List<Double> points, Panel currentPanel) {
        super(points);
        this.currentPanel = currentPanel;
        calculateSteps();
    }

    private void calculateSteps() {
        leftRightDiffX = currentPanel.getRightSide().getPoints().get(2) - currentPanel.getLeftSide().getPoints().get(2);
        leftRightDiffY = currentPanel.getRightSide().getPoints().get(3) - currentPanel.getLeftSide().getPoints().get(3);

        double POSITIONS_PER_SIDE = 6;
        xStepRight = leftRightDiffX / POSITIONS_PER_SIDE;
        yStepRight = leftRightDiffY / POSITIONS_PER_SIDE;
        xStepLeft = -leftRightDiffX / POSITIONS_PER_SIDE;
        yStepLeft = -leftRightDiffY / POSITIONS_PER_SIDE;
    }

    public void move(boolean left) {
        calculateSteps();
        if (left) {
            moveTo(getCenterX() + xStepLeft, getCenterY() + yStepLeft);
            if (leftRightDiffX > 0 && getCenterX() < currentPanel.getLeftSide().getPoints().get(2)) {
                moveTo(currentPanel.getLeftPanel().getRightSide().getPoints().get(2), currentPanel.getLeftPanel().getRightSide().getPoints().get(3));
                currentPanel = currentPanel.getLeftPanel();
            } else if (leftRightDiffX < 0 && getCenterX() > currentPanel.getLeftSide().getPoints().get(2)) {
                moveTo(currentPanel.getLeftPanel().getRightSide().getPoints().get(2), currentPanel.getLeftPanel().getRightSide().getPoints().get(3));
                currentPanel = currentPanel.getLeftPanel();
            } else if (leftRightDiffY > 0 && getCenterY() < currentPanel.getLeftSide().getPoints().get(3)) {
                moveTo(currentPanel.getLeftPanel().getRightSide().getPoints().get(2), currentPanel.getLeftPanel().getRightSide().getPoints().get(3));
                currentPanel = currentPanel.getLeftPanel();
            } else if (leftRightDiffY < 0 && getCenterY() > currentPanel.getLeftSide().getPoints().get(3)) {
                moveTo(currentPanel.getLeftPanel().getRightSide().getPoints().get(2), currentPanel.getLeftPanel().getRightSide().getPoints().get(3));
                currentPanel = currentPanel.getLeftPanel();
            }
        } else {
            moveTo(getCenterX() + xStepRight, getCenterY() + yStepRight);
            if (leftRightDiffX > 0 && getCenterX() > currentPanel.getRightSide().getPoints().get(2)) {
                moveTo(currentPanel.getRightPanel().getLeftSide().getPoints().get(2), currentPanel.getRightPanel().getLeftSide().getPoints().get(3));
                currentPanel = currentPanel.getRightPanel();
            } else if (leftRightDiffX < 0 && getCenterX() < currentPanel.getRightSide().getPoints().get(2)) {
                moveTo(currentPanel.getRightPanel().getLeftSide().getPoints().get(2), currentPanel.getRightPanel().getLeftSide().getPoints().get(3));
                currentPanel = currentPanel.getRightPanel();
            } else if (leftRightDiffY > 0 && getCenterY() > currentPanel.getRightSide().getPoints().get(3)) {
                moveTo(currentPanel.getRightPanel().getLeftSide().getPoints().get(2), currentPanel.getRightPanel().getLeftSide().getPoints().get(3));
                currentPanel = currentPanel.getRightPanel();
            } else if (leftRightDiffY < 0 && getCenterY() < currentPanel.getRightSide().getPoints().get(3)) {
                moveTo(currentPanel.getRightPanel().getLeftSide().getPoints().get(2), currentPanel.getRightPanel().getLeftSide().getPoints().get(3));
                currentPanel = currentPanel.getRightPanel();
            }
        }
        checkIfSideClose(left);
    }

    private void checkIfSideClose(boolean left) {
        if (left) {
            double xDiff = getCenterX() - currentPanel.getLeftSide().getPoints().get(2);
            double yDiff = getCenterY() - currentPanel.getLeftSide().getPoints().getLast();
            if (xDiff < 0.5 && xDiff > -0.5 && yDiff < 0.5 && yDiff > -0.5) {
                moveTo(currentPanel.getLeftSide().getPoints().get(2), currentPanel.getLeftSide().getPoints().get(3));
                currentPanel = currentPanel.getLeftPanel();
            }
        } else {
            double xDiff = getCenterX() - currentPanel.getRightSide().getPoints().get(2);
            double yDiff = getCenterY() - currentPanel.getRightSide().getPoints().getLast();
            if (xDiff < 0.5 && xDiff > -0.5 && yDiff < 0.5 && yDiff > -0.5) {
                moveTo(currentPanel.getRightSide().getPoints().get(2), currentPanel.getRightSide().getPoints().get(3));
                currentPanel = currentPanel.getRightPanel();
            }
        }
    }

    public void shoot() {
        double xBig = (currentPanel.getBigSide().getPoints().getFirst() + currentPanel.getBigSide().getPoints().get(2))/2;
        double yBig = (currentPanel.getBigSide().getPoints().get(1) + currentPanel.getBigSide().getPoints().getLast())/2;
        double xSmall = (currentPanel.getSmallSide().getPoints().getFirst() + currentPanel.getSmallSide().getPoints().get(2))/2;
        double ySmall = (currentPanel.getSmallSide().getPoints().get(1) + currentPanel.getSmallSide().getPoints().getLast())/2;

        double x = xBig - xSmall;
        double y = yBig - ySmall;

        double angle = Math.toDegrees(Math.atan(y/x));
        if(x >= 0) angle += 180;

        List<Double> points = Util.SVGconverter(bullet);    // Rectangle bullet
        Particle bullet = new Particle(points, angle, 0, BULLET_SPEED, 0);
        bullet.setFill(Color.RED);
        bullet.scale(10/bullet.getRadius());
        bullet.moveTo(xBig, yBig);
        currentPanel.addPlayerBullet(bullet);

        root.getChildren().add(bullet);
    }
    public static void collision() {
        List<Enemy> enemiesToRemove = new ArrayList<>();
        for (Panel panel: panels){
            for (Particle bullet: panel.getPlayerBullets()){
                for (Flipper flipper : panel.getFlippers()){
                    if (bullet.intersects(flipper.getLayoutBounds())){
                        enemiesToRemove.add(flipper);
                    }
                }
            }
        }
        for (Enemy enemy  :enemiesToRemove){

        }
    }

    public Panel getCurrentPanel() {
        return currentPanel;
    }
}