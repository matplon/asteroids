package com.example.MotorolaScienceCup.Tempest;


import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Util;
import com.example.MotorolaScienceCup.Vector;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.MotorolaScienceCup.Tempest.Main.*;

public class Graphics {
    private static final int CONNECTORS_NUMBER = 16;
    private static double baseOffset = 20;
    private static double bigOffset = 0;
    private static List<Polyline> connectors = Main.connectors;
    static double mapCenterX;
    static double mapCenterY;
    static BetterPolygon mapShape;

    public static void drawMap(String filepath, Color color, double scale) {
        baseOffset = 20;
        bigOffset = 0;

        connectors = new ArrayList<>();
        panels = new ArrayList<>();

        double bigShapeRadius = HEIGHT / 2 - 30;

        List<Double> smallShapePoints = Util.getMapPoints(filepath);

        if(isMapOpen.get(maps.indexOf(filepath))){
            baseOffset = -80;
            bigOffset = 200;
        }

        BetterPolygon tempSmallShape = new BetterPolygon(smallShapePoints);
        tempSmallShape.scale((bigShapeRadius * scale / 10) / tempSmallShape.getRadius());
        tempSmallShape.moveTo(WIDTH / 2, HEIGHT / 2 + baseOffset);
        smallShapePoints = tempSmallShape.getPoints();
        mapCenterX = tempSmallShape.getCenterX();
        mapCenterY = tempSmallShape.getCenterY();
        mapShape = new BetterPolygon(tempSmallShape.getPoints());

        BetterPolygon tempPolygon = BetterPolygon.scale(tempSmallShape, 8.5);
        tempPolygon.moveTo(WIDTH / 2, HEIGHT / 2 + bigOffset);
        List<Double> bigShapePoints = tempPolygon.getPoints();
        drawConnectors(smallShapePoints, bigShapePoints, color, new Glow(Main.glowV), isMapOpen.get(maps.indexOf(filepath)));

        if(isMapOpen.get(maps.indexOf(filepath))){
            removePanel();
        }
    }

    private static void removePanel(){
        if(panels.get(0).getLeftPanel() == panels.getLast()) panels.get(0).leftPanel = null;
        else if(panels.get(0).getRightPanel() == panels.getLast()) panels.get(0).rightPanel = null;
        if(panels.get(panels.size()-2).getLeftPanel() == panels.getLast()) panels.get(panels.size()-2).leftPanel = null;
        else if(panels.get(panels.size()-2).getRightPanel() == panels.getLast()) panels.get(panels.size()-2).rightPanel = null;
    }

    private static void drawConnectors(List<Double> smallShapePoints, List<Double> bigShapePoints, Color color, Glow glow, boolean open) {
        for (int i = 0; i < smallShapePoints.size(); i += 2) {
            Polyline polyline = new Polyline(smallShapePoints.get(i), smallShapePoints.get(i + 1), bigShapePoints.get(i), bigShapePoints.get(i + 1));
            polyline.setFill(color);
            polyline.setStroke(color);
            polyline.setEffect(glow);
            connectors.add(polyline);
        }
        int n = CONNECTORS_NUMBER - smallShapePoints.size() / 2;
        if(!open) n /= (int) ((double) smallShapePoints.size() / 2);
        for (int i = 0; i < smallShapePoints.size(); i += 2) {
            double xDiffSmall, yDiffSmall, xDiffBig, yDiffBig;
            if (i + 2 >= smallShapePoints.size()) {
                xDiffSmall = smallShapePoints.get(0) - smallShapePoints.get(i);
                yDiffSmall = smallShapePoints.get(1) - smallShapePoints.get(i + 1);
                xDiffBig = bigShapePoints.get(0) - bigShapePoints.get(i);
                yDiffBig = bigShapePoints.get(1) - bigShapePoints.get(i + 1);
            } else {
                xDiffSmall = smallShapePoints.get(i + 2) - smallShapePoints.get(i);
                yDiffSmall = smallShapePoints.get(i + 3) - smallShapePoints.get(i + 1);
                xDiffBig = bigShapePoints.get(i + 2) - bigShapePoints.get(i);
                yDiffBig = bigShapePoints.get(i + 3) - bigShapePoints.get(i + 1);
            }
            xDiffSmall /= (n + 1);
            yDiffSmall /= (n + 1);
            xDiffBig /= (n + 1);
            yDiffBig /= (n + 1);
            for (int j = 1; j <= n; j++) {
                double xSmall = smallShapePoints.get(i) + xDiffSmall * j;
                double ySmall = smallShapePoints.get(i + 1) + yDiffSmall * j;
                double xBig = bigShapePoints.get(i) + xDiffBig * j;
                double yBig = bigShapePoints.get(i + 1) + yDiffBig * j;
                Polyline polyline = new Polyline(xSmall, ySmall, xBig, yBig);
                polyline.setStroke(color);
                polyline.setFill(color);
                polyline.setEffect(new Glow(glowV));
                connectors.add(polyline);
            }
        }
        createPanels(smallShapePoints.size() / 2, n, color, new Glow(Main.glowV));
    }

    private static void createPanels(int vertices, int connectorsBetweenVertices, Color color, Glow glow) {
        List<Polyline> newConnectors = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {    // Order connectors the correct way
            newConnectors.add(connectors.get(i));
            for (int j = 0; j < connectorsBetweenVertices; j++) {
                newConnectors.add(connectors.get(vertices + (connectorsBetweenVertices * i) + j));
            }
        }
        for (Polyline connector : connectors) {     // Draw connectors
            root.getChildren().add(connector);
        }
        connectors = newConnectors;
        for (int i = 0; i < CONNECTORS_NUMBER; i++) {   // Generate panels
            Panel panel = new Panel();
            panel.setIndex(i);
            double smallSideX1 = connectors.get(i).getPoints().get(0);
            double smallSideY1 = connectors.get(i).getPoints().get(1);
            double bigSideX1 = connectors.get(i).getPoints().get(2);
            double bigSideY1 = connectors.get(i).getPoints().get(3);
            double smallSideX2, smallSideY2, bigSideX2, bigSideY2;
            if (i + 1 < CONNECTORS_NUMBER) {
                smallSideX2 = connectors.get(i + 1).getPoints().get(0);
                smallSideY2 = connectors.get(i + 1).getPoints().get(1);
                bigSideX2 = connectors.get(i + 1).getPoints().get(2);
                bigSideY2 = connectors.get(i + 1).getPoints().get(3);
                panel.setLeftSide(connectors.get(i + 1));
            } else {
                smallSideX2 = connectors.get(0).getPoints().get(0);
                smallSideY2 = connectors.get(0).getPoints().get(1);
                bigSideX2 = connectors.get(0).getPoints().get(2);
                bigSideY2 = connectors.get(0).getPoints().get(3);
                panel.setLeftSide(connectors.get(0));
            }
            Polyline smallSide = new Polyline(smallSideX1, smallSideY1, smallSideX2, smallSideY2);
            smallSide.setFill(color);
            smallSide.setStroke(color);
            smallSide.setEffect(glow);
            panel.setSmallSide(smallSide);
            root.getChildren().add(smallSide);

            Polyline bigSide = new Polyline(bigSideX1, bigSideY1, bigSideX2, bigSideY2);
            bigSide.setStroke(color);
            bigSide.setFill(color);
            bigSide.setEffect(glow);
            panel.setBigSide(bigSide);
            root.getChildren().add(bigSide);

            panel.setRightSide(connectors.get(i));
            panels.add(panel);

            double x1 = (smallSideX1 + smallSideX2) / 2;
            double y1 = (smallSideY1 + smallSideY2) / 2;
            double x2 = (bigSideX1 + bigSideX2) / 2;
            double y2 = (bigSideY1 + bigSideY2) / 2;

            double xLength = x2 - x1;
            double yLength = y2 - y1;
            double length = Math.sqrt(xLength * xLength + yLength * yLength);
            panel.setLength(length);

            panel.setAngle(Math.toDegrees(Math.atan2(y2 - y1, x2 - x1)));
        }

        for (int i = 0; i < panels.size(); i++) {
            if (i + 1 >= panels.size()) {
                panels.get(i).setLeftPanel(panels.get(0));
            } else {
                panels.get(i).setLeftPanel(panels.get(i + 1));
            }
            if (i > 0) {
                panels.get(i).setRightPanel(panels.get(i - 1));
            } else {
                panels.get(i).setRightPanel(panels.get(panels.size() - 1));
            }
        }
    }

    private static void drawSpikerLine(Panel oldPanel, Panel newPanel) {
        double lengthPercentage = newPanel.getLength() / oldPanel.getLength();
        double oldLineLength = Math.sqrt(Math.pow(oldPanel.spikerLine.getPoints().get(0) - oldPanel.spikerLine.getPoints().get(2), 2) +
                Math.pow(oldPanel.spikerLine.getPoints().get(1) - oldPanel.spikerLine.getPoints().get(3), 2));
        double newLineLength = oldLineLength * lengthPercentage;

        Polyline newLine = new Polyline();
        newLine.getPoints().add((newPanel.getSmallSide().getPoints().get(0) + newPanel.getSmallSide().getPoints().get(2)) / 2);
        newLine.getPoints().add((newPanel.getSmallSide().getPoints().get(1) + newPanel.getSmallSide().getPoints().get(3)) / 2);
        Vector vector = new Vector(newLineLength, newPanel.getAngle());
        newLine.getPoints().add(newLine.getPoints().get(0) + vector.getX());
        newLine.getPoints().add(newLine.getPoints().get(1) + vector.getY());
        newLine.setEffect(new Glow(Main.glowV));
        newLine.setStroke(Spiker.spikerColor);
        newPanel.spikerLine = newLine;
    }

    private static void retrieveBullets(Panel oldPanel, Panel newPanel){
        newPanel.playerBullets = new ArrayList<>();
        for(Player.Bullet bullet1 : oldPanel.playerBullets){
            if(bullet1.h > 0) newPanel.playerBullets.add(bullet1);
        }
    }
}