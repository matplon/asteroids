package com.example.MotorolaScienceCup;

import com.example.MotorolaScienceCup.Asteroids.Particle;
import javafx.scene.shape.Polygon;

import java.util.List;

public class BetterPolygon extends javafx.scene.shape.Polygon {

    public double getCenterX() {    // Mean average of the X coordinates
        double sum = 0;
        for (int i = 0; i < getPoints().size(); i += 2) {
            sum += getPoints().get(i);
        }
        return sum / ((double) getPoints().size() / 2);
    }

    public double getCenterY() {    // Mean average of the Y coordinates
        double sum = 0;
        for (int i = 1; i < getPoints().size(); i += 2) {
            sum += getPoints().get(i);
        }
        return sum / ((double) getPoints().size() / 2);
    }

    public double getRadius() { // Calculate radius by finding the furthest coordinate
        double maxOffset = 0;
        for (int i = 2; i < getPoints().size(); i += 2) {
            maxOffset = Math.max(maxOffset, Math.sqrt(Math.pow(getCenterX() - getPoints().get(i), 2) + Math.pow(getCenterY() - getPoints().get(i + 1), 2)));
        }
        return maxOffset;
    }

    public void scale(double scale) {
        double centerX = getCenterX();
        double centerY = getCenterY();
        for (int i = 0; i < getPoints().size(); i += 2) {
            double newX = scale * (getPoints().get(i) - centerX) + centerX;
            double newY = scale * (getPoints().get(i + 1) - centerY) + centerY;
            getPoints().set(i, newX);
            getPoints().set(i + 1, newY);
        }
    }

    public static BetterPolygon scale(BetterPolygon polygon, double scale){
        BetterPolygon tempPolygon = new BetterPolygon();
        tempPolygon.getPoints().setAll(polygon.getPoints());
        tempPolygon.scale(scale);
        return tempPolygon;
    }

    public void moveTo(double newCenterX, double newCenterY) {
        // Calculate new point coordinates
        double centerX = getCenterX();
        double centerY = getCenterY();
        for (int i = 0; i < getPoints().size(); i += 2) {
            double newX = newCenterX + getPoints().get(i) - centerX;
            double newY = newCenterY + getPoints().get(i + 1) - centerY;
            getPoints().set(i, newX);
            getPoints().set(i + 1, newY);
        }
    }

}
