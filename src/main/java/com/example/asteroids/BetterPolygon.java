package com.example.asteroids;

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




}
