package com.example.MotorolaScienceCup.Tempest;


import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Util;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;
import java.util.List;

import static com.example.MotorolaScienceCup.Tempest.Main.WIDTH;
import static com.example.MotorolaScienceCup.Tempest.Main.HEIGHT;
import static com.example.MotorolaScienceCup.Tempest.Main.root;
import static com.example.MotorolaScienceCup.Tempest.Main.panels;

public class Graphics {
    private static final int CONNECTORS_NUMBER = 16;
    private final static double baseOffset = 20;
    private static List<Polyline> connectors = Main.connectors;

    public static void drawMap(String filepath, Color color) {
        List<Double> smallShapePoints = Util.SVGconverter(filepath);

        BetterPolygon tempSmallShape = new BetterPolygon(smallShapePoints);
        tempSmallShape.moveTo((double) WIDTH / 2, (double) HEIGHT / 2 + baseOffset);
        smallShapePoints = tempSmallShape.getPoints();

        BetterPolygon tempPolygon = BetterPolygon.scale(tempSmallShape, 8);
        tempPolygon.moveTo((double) WIDTH / 2, (double) HEIGHT / 2);
        List<Double> bigShapePoints = tempPolygon.getPoints();
        drawConnectors(smallShapePoints, bigShapePoints, color);
    }

    private static void drawConnectors(List<Double> smallShapePoints, List<Double> bigShapePoints, Color color) {
        for (int i = 0; i < smallShapePoints.size(); i += 2) {
            Polyline polyline = new Polyline(smallShapePoints.get(i), smallShapePoints.get(i + 1), bigShapePoints.get(i), bigShapePoints.get(i + 1));
            polyline.setFill(color);
            polyline.setStroke(Color.BLUE);
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
        createPanels(smallShapePoints.size() / 2, n, color);
    }

    private static void createPanels(int vertices, int connectorsBetweenVertices, Color color) {
        List<Polyline> newConnectors = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {    // Order connectors the correct way
            newConnectors.add(connectors.get(i));
            for (int j = 0; j < connectorsBetweenVertices; j++) {
                newConnectors.add(connectors.get(vertices + (connectorsBetweenVertices * i)));
            }
        }
        for (Polyline connector : connectors) {     // Draw connectors
            root.getChildren().add(connector);
        }
        connectors = newConnectors;
        for (int i = 0; i < CONNECTORS_NUMBER; i++) {   // Generate panels
            Panel panel = new Panel();
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
                smallSideX2 = connectors.getFirst().getPoints().getFirst();
                smallSideY2 = connectors.getFirst().getPoints().get(1);
                bigSideX2 = connectors.getFirst().getPoints().get(2);
                bigSideY2 = connectors.getFirst().getPoints().get(3);
                panel.setLeftSide(connectors.getFirst());
            }
            Polyline smallSide = new Polyline(smallSideX1, smallSideY1, smallSideX2, smallSideY2);
            smallSide.setFill(color);
            smallSide.setStroke(color);
            panel.setSmallSide(smallSide);
            root.getChildren().add(smallSide);

            Polyline bigSide = new Polyline(bigSideX1, bigSideY1, bigSideX2, bigSideY2);
            bigSide.setStroke(color);
            bigSide.setFill(color);
            panel.setBigSide(bigSide);
            root.getChildren().add(bigSide);

            panel.setRightSide(connectors.get(i));
            panels.add(panel);
        }

        for (int i = 0; i < panels.size(); i++) {
            if (i + 1 >= panels.size()) {
                panels.get(i).setLeftPanel(panels.getFirst());
            } else {
                panels.get(i).setLeftPanel(panels.get(i + 1));
            }
            if (i > 0) {
                panels.get(i).setRightPanel(panels.get(i - 1));
            } else {
                panels.get(i).setRightPanel(panels.getLast());
            }
        }
    }

}