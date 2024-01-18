package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

public class Player extends BetterPolygon {
    private final double MOVE_SIZE = 1; // pixel
    private double xStepLeft, xStepRight;
    private double yStepLeft, yStepRight;
    private int sideIndex;
    private Panel currentPanel;

    public Player(List<Double> points, Panel currentPanel){
        super(points);
        this.currentPanel = currentPanel;

    }
    
    public void move(boolean left){
        double leftRightDiffX = currentPanel.getRightSide().getPoints().get(2) - currentPanel.getLeftSide().getPoints().get(2);
        double leftRightDiffY = currentPanel.getRightSide().getPoints().get(3) - currentPanel.getLeftSide().getPoints().get(3);

        if(left){
            moveTo(getCenterX() + xStepLeft, getCenterY() + yStepLeft);
            if(leftRightDiffX > 0){
                if(getCenterX() < currentPanel.getLeftSide().getPoints().get(2)){
                    moveTo(currentPanel.getLeftPanel().getRightSide().getPoints().get(2), currentPanel.getLeftPanel().getRightSide().getPoints().get(3));
                    currentPanel = currentPanel.getLeftPanel();
                }
            }
            else if(leftRightDiffX < 0){
                if(getCenterX() > currentPanel.getLeftSide().getPoints().get(2)){
                    moveTo(currentPanel.getLeftPanel().getRightSide().getPoints().get(2), currentPanel.getLeftPanel().getRightSide().getPoints().get(3));
                    currentPanel = currentPanel.getLeftPanel();
                }
            }
            if(leftRightDiffY > 0){
                if(getCenterY() < currentPanel.getLeftSide().getPoints().get(3)){
                    moveTo(currentPanel.getLeftPanel().getRightSide().getPoints().get(2), currentPanel.getLeftPanel().getRightSide().getPoints().get(3));
                    currentPanel = currentPanel.getLeftPanel();
                }
            }
            else if(leftRightDiffY < 0){
                if(getCenterY() > currentPanel.getLeftSide().getPoints().get(3)){
                    moveTo(currentPanel.getLeftPanel().getRightSide().getPoints().get(2), currentPanel.getLeftPanel().getRightSide().getPoints().get(3));
                    currentPanel = currentPanel.getLeftPanel();
                }
            }
        }
        else{
            moveTo(getCenterX() + xStepRight, getCenterY() + yStepRight);
            if(leftRightDiffX > 0){
                if(getCenterX() > currentPanel.getRightSide().getPoints().get(2)){
                    moveTo(currentPanel.getRightPanel().getLeftSide().getPoints().get(2), currentPanel.getRightPanel().getLeftSide().getPoints().get(3));
                    currentPanel = currentPanel.getRightPanel();
                }
            }
            else if(leftRightDiffX < 0){
                if(getCenterX() < currentPanel.getRightSide().getPoints().get(2)){
                    moveTo(currentPanel.getRightPanel().getLeftSide().getPoints().get(2), currentPanel.getRightPanel().getLeftSide().getPoints().get(3));
                    currentPanel = currentPanel.getRightPanel();
                }
            }
            if(leftRightDiffY > 0){
                if(getCenterX() > currentPanel.getRightSide().getPoints().get(3)){
                    moveTo(currentPanel.getRightPanel().getLeftSide().getPoints().get(2), currentPanel.getRightPanel().getLeftSide().getPoints().get(3));
                    currentPanel = currentPanel.getRightPanel();
                }
            }
            else if(leftRightDiffY < 0){
                if(getCenterX() < currentPanel.getRightSide().getPoints().get(3)){
                    moveTo(currentPanel.getRightPanel().getLeftSide().getPoints().get(2), currentPanel.getRightPanel().getLeftSide().getPoints().get(3));
                    currentPanel = currentPanel.getRightPanel();
                }
            }
        }
        System.out.println(getCenterX()+" "+getCenterY());
    }
}
