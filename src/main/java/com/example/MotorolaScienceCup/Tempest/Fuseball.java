package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.Util;
import com.example.MotorolaScienceCup.Vector;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Fuseball extends Enemy {
    private final String filepath = "fuseball.svg";
    private final int FRAMES_PER_SIDE_MOVE = 250;
    private int FRAMES_PER_MOVE_DOWN;
    private boolean leftSide;
    private boolean moveLeft;
    private boolean changingSide = false;
    private boolean movingDown = false;
    private boolean movingUp = true;
    private List<Double> currentSide = new ArrayList<>();
    private List<Double> nextSide;
    private double sideAngle;
    private int moveSidewaysTimer = FRAMES_PER_SIDE_MOVE;
    private int moveDownTimer = 0;
    Vector moveVector;

    public Fuseball(Panel startPanel) {
        super(startPanel);
        getPoints().setAll(Util.SVGconverter(filepath));
        currentPanel.getFuseBalls().add(this);

        Random random = new Random();
        int sideChooser = random.nextInt(2);
        leftSide = (sideChooser == 0);

        if (leftSide) {
            currentSide = currentPanel.getLeftSide().getPoints();
        } else {
            currentSide = currentPanel.getRightSide().getPoints();
        }
        moveTo(currentSide.get(0), currentSide.get(1));

        maxH = Math.sqrt(Math.pow(currentSide.get(2) - currentSide.get(0), 2) + Math.pow(currentSide.get(3) - currentSide.get(1), 2));
        sideAngle = Math.toDegrees(Math.atan2(currentSide.get(3) - currentSide.get(1), currentSide.get(2) - currentSide.get(0)));
        initVelocity = 3;
        setStroke(new Color(Math.random(), Math.random(), Math.random(), 1));
        setEffect(new Glow(Main.glowV));
    }

    public boolean move() {
        if(checkForCollision()) return false;
        if (!changingSide && !movingDown && !movingUp) {
            double moveChooser = Math.random();
            if (h < maxH) {
                if (moveChooser <= 0.8) movingUp = true;
                else changingSide = true;
            }
            else {
                if (moveChooser <= 0.7) changingSide = true;
                else movingDown = true;
            }
        } else if (movingUp) {
            moveUp();
        } else if (changingSide) {
            moveSideways();
        } else if (movingDown) {
            moveDown();
        }
        double rotationAngle = 90;
        if (Math.random() > 0.5) rotationAngle *= -1;
        rotate(rotationAngle);
        setStroke(new Color(Math.random(), Math.random(), Math.random(), 1));
        return true;
    }

    @Override
    protected void moveUp() {
        h += initVelocity;
        updatePoints();
        updateSize();
        if (h >= maxH) {
            reachedTheEdge = true;
            h = maxH;
            moveTo(currentSide.get(2), currentSide.get(3));
            movingUp = false;
        }
    }

    private void moveDown() {
        if (moveDownTimer == 0) {
            movingDown = true;
            Random random = new Random();
            double hPercentage = random.nextDouble(0.8);
            double sLength = Math.sqrt(Math.pow(currentSide.get(2) - currentSide.get(0), 2) + Math.pow(currentSide.get(3) - currentSide.get(1), 2));
            Vector vector = new Vector(sLength * hPercentage, sideAngle);
            double targetX = currentSide.get(0) + vector.getX();
            double targetY = currentSide.get(1) + vector.getY();
            double moveDownLength = Math.sqrt(Math.pow(targetX - getCenterX(), 2) + Math.pow(targetY - getCenterY(), 2));
            FRAMES_PER_MOVE_DOWN = (int) (moveDownLength / initVelocity);
            double moveAngle = Math.toDegrees(Math.atan2(targetY - getCenterY(), targetX - getCenterX()));
            moveVector = new Vector(initVelocity, moveAngle);
        }
        else if(moveDownTimer < FRAMES_PER_MOVE_DOWN){
            h -= initVelocity;
            moveTo(getCenterX() + moveVector.getX(), getCenterY() + moveVector.getY());
        }
        else{
            moveDownTimer = -1;
            movingDown = false;
        }
        updateSize();
        moveDownTimer++;
    }

    @Override
    protected void updatePoints() {
        Vector vector = new Vector(initVelocity, sideAngle);
        moveTo(getCenterX() + vector.getX(), getCenterY() + vector.getY());
    }

    private void moveSideways() {
        if (moveSidewaysTimer == FRAMES_PER_SIDE_MOVE) {
            moveLeft = chooseDirection();
            changingSide = true;
            if (moveLeft) {
                if (leftSide) {
                    nextSide = currentPanel.getLeftPanel().getLeftSide().getPoints();
                } else {
                    nextSide = currentPanel.getLeftSide().getPoints();
                }
            } else {
                if (leftSide) {
                    nextSide = currentPanel.getRightSide().getPoints();
                } else {
                    nextSide = currentPanel.getRightPanel().getRightSide().getPoints();
                }
            }
            double nextSideAngle = Math.toDegrees(Math.atan2(nextSide.get(3) - nextSide.get(1), nextSide.get(2) - nextSide.get(0)));
            double nextSideLength = Math.sqrt(Math.pow(nextSide.get(2) - nextSide.get(0), 2) + Math.pow(nextSide.get(3) - nextSide.get(1), 2));
            double hPercentage = h / maxH;
            Vector vector = new Vector(nextSideLength * hPercentage, nextSideAngle);
            double targetX = nextSide.get(0) + vector.getX();
            double targetY = nextSide.get(1) + vector.getY();
            double step = Math.sqrt(Math.pow(targetX - getCenterX(), 2) + Math.pow(targetY - getCenterY(), 2)) / FRAMES_PER_SIDE_MOVE;
            double moveAngle = Math.toDegrees(Math.atan2(targetY - getCenterY(), targetX - getCenterX()));
            moveVector = new Vector(step, moveAngle);
        } else if (moveSidewaysTimer >= 0) {
            moveTo(getCenterX() + moveVector.getX(), getCenterY() + moveVector.getY());
        } else {
            moveSidewaysTimer = FRAMES_PER_SIDE_MOVE + 1;
            changingSide = false;
            if (moveLeft) {
                if (leftSide) {
                    currentPanel = currentPanel.getLeftPanel();
                    currentPanel.getFuseBalls().add(this);
                    leftSide = false;
                } else {
                    leftSide = true;
                }
            } else {
                if (leftSide) {
                    leftSide = false;
                } else {
                    currentPanel = currentPanel.getRightPanel();
                    currentPanel.getFuseBalls().add(this);
                }
            }
            double sideLength = Math.sqrt(Math.pow(currentSide.get(2) - currentSide.get(0), 2) + Math.pow(currentSide.get(3) - currentSide.get(1), 2));
            double hPercentage = h / sideLength;
            currentSide = nextSide;
            sideLength = Math.sqrt(Math.pow(currentSide.get(2) - currentSide.get(0), 2) + Math.pow(currentSide.get(3) - currentSide.get(1), 2));
            h = sideLength * hPercentage;
            sideAngle = Math.toDegrees(Math.atan2(currentSide.get(3) - currentSide.get(1), currentSide.get(2) - currentSide.get(0)));
            maxH = Math.sqrt(Math.pow(currentSide.get(2) - currentSide.get(0), 2) + Math.pow(currentSide.get(3) - currentSide.get(1), 2));
        }
        moveSidewaysTimer--;
    }

    private boolean checkForCollision(){
        if(h == maxH){
            if(Main.player.getCurrentPanel() == currentPanel){
                return true;
            }
            else if(!leftSide && currentPanel.getRightPanel() == Main.player.getCurrentPanel()){
                return true;
            }
            else if(leftSide && currentPanel.getLeftPanel() == Main.player.getCurrentPanel()){
                return true;
            }
        }
        return false;
    }
}
