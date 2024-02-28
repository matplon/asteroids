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
    private final static double baseOffset = 20;
    private static List<Polyline> connectors = Main.connectors;
    static double mapCenterX;
    static double mapCenterY;

    public static void drawMap(String filepath, Color color, double scale) {
        connectors = new ArrayList<>();
        List<Panel> oldPanels = new ArrayList<>(panels);
        panels = new ArrayList<>();

        List<Double> smallShapePoints = Util.getMapPoints(filepath);

        BetterPolygon tempSmallShape = new BetterPolygon(smallShapePoints);
        tempSmallShape.setStroke(Color.GREEN);
        tempSmallShape.scale(scale);
        tempSmallShape.moveTo((double) WIDTH / 2, (double) HEIGHT / 2 + baseOffset);
        smallShapePoints = tempSmallShape.getPoints();
        mapCenterX = tempSmallShape.getCenterX();
        mapCenterY = tempSmallShape.getCenterY();

        BetterPolygon tempPolygon = BetterPolygon.scale(tempSmallShape, 10 + scale);
        tempPolygon.moveTo((double) WIDTH / 2, (double) HEIGHT / 2);
        List<Double> bigShapePoints = tempPolygon.getPoints();
        drawConnectors(oldPanels, smallShapePoints, bigShapePoints, color, new Glow(Main.glowV));
    }

    private static void drawConnectors(List<Panel> oldPanels, List<Double> smallShapePoints, List<Double> bigShapePoints, Color color, Glow glow) {
        for (int i = 0; i < smallShapePoints.size(); i += 2) {
            Polyline polyline = new Polyline(smallShapePoints.get(i), smallShapePoints.get(i + 1), bigShapePoints.get(i), bigShapePoints.get(i + 1));
            polyline.setFill(color);
            polyline.setStroke(Color.BLUE);
            polyline.setEffect(glow);
            connectors.add(polyline);
        }
        int n = CONNECTORS_NUMBER - smallShapePoints.size() / 2;
        n /= (int) ((double) smallShapePoints.size() / 2);
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
                connectors.add(polyline);
            }
        }
        createPanels(oldPanels, smallShapePoints.size() / 2, n, color, new Glow(Main.glowV));
    }

    private static void createPanels(List<Panel> oldPanels, int vertices, int connectorsBetweenVertices, Color color, Glow glow) {
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

            if(!oldPanels.isEmpty()){
                if(oldPanels.get(i).spikerLine != null) drawSpikerLine(oldPanels.get(i), panel);
                if(!oldPanels.get(i).getPlayerBullets().isEmpty()) retrieveBullets(oldPanels.get(i), panel);
            }

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