package com.example.MotorolaScienceCup.Tempest;


import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Util;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;
import java.util.List;

import static com.example.MotorolaScienceCup.Tempest.Main.base;
import static com.example.MotorolaScienceCup.Menu.WIDTH;
import static com.example.MotorolaScienceCup.Menu.HEIGHT;
import static com.example.MotorolaScienceCup.Menu.root;
import static com.example.MotorolaScienceCup.Tempest.Main.bigShape;

public class HUD {
    private final static double baseOffset = 20;
    private static List<Polyline> connectors = Main.connectors;

    public static void drawMap(String filepath, Color color) {
        List<Double> basePoints = Util.SVGconverter(filepath);

        base = new BetterPolygon(basePoints);
        base.setStroke(color);
        base.setFill(Color.TRANSPARENT);
        base.moveTo((double) WIDTH / 2, (double) HEIGHT / 2 + baseOffset);
        root.getChildren().add(base);
        basePoints = base.getPoints();

        BetterPolygon tempPolygon = BetterPolygon.scale(base, 8);
        tempPolygon.moveTo((double) WIDTH / 2, (double) HEIGHT / 2);
        List<Double> bigShapePoints = tempPolygon.getPoints();

        bigShape = new ArrayList<>();

        for (int i = 0; i < bigShapePoints.size(); i += 2) {
            Polyline polyline = new Polyline();
            if (i + 2 >= bigShapePoints.size()) {
                polyline.getPoints().setAll(bigShapePoints.get(i), bigShapePoints.get(i + 1), bigShapePoints.get(0), bigShapePoints.get(1));
            } else {
                polyline.getPoints().setAll(bigShapePoints.get(i), bigShapePoints.get(i + 1), bigShapePoints.get(i + 2), bigShapePoints.get(i + 3));
            }
            polyline.setStroke(color);
            polyline.setFill(color);
            bigShape.add(polyline);
            root.getChildren().add(polyline);
        }
        drawConnectors(basePoints, bigShapePoints, color);
    }

    private static void drawConnectors(List<Double> smallShapePoints, List<Double> bigShapePoints, Color color) {
        boolean isRegular = true;
        double bigSideLength = Math.sqrt(Math.pow(bigShape.get(0).getPoints().get(2) - bigShape.get(0).getPoints().get(0), 2) +
                Math.pow(bigShape.get(0).getPoints().get(3) - bigShape.get(0).getPoints().get(1), 2));
        double smallSideLength = Math.sqrt(Math.pow(base.getPoints().get(2) - base.getPoints().get(0), 2) +
                Math.pow(base.getPoints().get(3) - base.getPoints().get(1), 2));
        for (Polyline value : bigShape) {
            if (Math.sqrt(Math.pow(value.getPoints().get(2) - value.getPoints().get(0), 2) +
                    Math.pow(value.getPoints().get(3) - value.getPoints().get(1), 2)) != bigSideLength) {
                isRegular = false;
                break;
            }
        }
        for (int i = 0; i < smallShapePoints.size(); i += 2) {
            Polyline polyline = new Polyline(smallShapePoints.get(i), smallShapePoints.get(i + 1), bigShapePoints.get(i), bigShapePoints.get(i + 1));
            polyline.setFill(color);
            polyline.setStroke(Color.BLUE);
            connectors.add(polyline);
            root.getChildren().add(polyline);
        }
        if (isRegular) {
            int i = 0;
            int connectionsPerSide = (16 - smallShapePoints.size() / 2) / bigShape.size();
            double bigLineInterval = bigSideLength / (connectionsPerSide + 1);
            double smallLineInterval = smallSideLength / (connectionsPerSide + 1);
            for (Polyline polyline : bigShape) {
                List<Double> points = polyline.getPoints();
                double gradient = (points.get(3) - points.get(1)) / (points.get(2) - points.get(0));
                double angle = Math.atan(gradient);
                double bigXInterval = bigLineInterval * Math.cos(angle);
                double bigYInterval = bigLineInterval * Math.sin(angle);
                double smallXInterval = smallLineInterval * Math.cos(angle);
                double smallYInterval = smallLineInterval * Math.sin(angle);

                if (points.get(0) > points.get(2)) {
                    bigXInterval *= -1;
                    smallXInterval *= -1;
                }
                if ((points.get(3) > points.get(1) && gradient < 0) || (gradient > 0 && points.get(3) < points.get(1))) {
                    bigYInterval *= -1;
                    smallYInterval *= -1;
                }

                for (int j = 1; j <= connectionsPerSide; j++) {
                    Polyline connection = new Polyline(0, 0, 0, 0);
                    connection.getPoints().set(0, points.get(0) + j * bigXInterval);
                    connection.getPoints().set(1, points.get(1) + j * bigYInterval);
                    connection.getPoints().set(2, smallShapePoints.get(i) + j * smallXInterval);
                    connection.getPoints().set(3, smallShapePoints.get(i + 1) + j * smallYInterval);
                    connection.setStroke(color);
                    connection.setFill(color);
                    connectors.add(connection);
                    root.getChildren().add(connection);
                }
                i += 2;
            }
        }
    }

}
