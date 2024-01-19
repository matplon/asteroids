package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;

import java.util.List;

public class Player extends BetterPolygon {
    private double xStepLeft, xStepRight;
    private double yStepLeft, yStepRight;
    private double leftRightDiffX, leftRightDiffY;
    private Panel currentPanel;

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

    public Panel getCurrentPanel() {
        return currentPanel;
    }
}
