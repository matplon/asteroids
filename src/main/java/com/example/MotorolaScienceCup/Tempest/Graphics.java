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

public class Graphics {
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
        for (int i = 0; i < smallShapePoints.size(); i += 2) {
            Polyline polyline = new Polyline(smallShapePoints.get(i), smallShapePoints.get(i + 1), bigShapePoints.get(i), bigShapePoints.get(i + 1));
            polyline.setFill(color);
            polyline.setStroke(Color.BLUE);
            connectors.add(polyline);
            root.getChildren().add(polyline);
        }
        int i = 0;
        int connectionsPerSide = (16 - smallShapePoints.size() / 2) / bigShape.size();
        for (Polyline polyline : bigShape) {
            List<Double> points = polyline.getPoints();
            double bigXInterval = points.get(2) - points.get(0);
            double bigYInterval = points.get(3) - points.get(1);
            bigXInterval /= connectionsPerSide + 1;
            bigYInterval /= connectionsPerSide + 1;

            double smallXInterval, smallYInterval;

            if (i + 2 >= smallShapePoints.size()) {
                smallXInterval = smallShapePoints.get(0) - smallShapePoints.get(i);
                smallYInterval = smallShapePoints.get(1) - smallShapePoints.get(i + 1);
            } else {
                smallXInterval = smallShapePoints.get(i + 2) - smallShapePoints.get(i);
                smallYInterval = smallShapePoints.get(i + 3) - smallShapePoints.get(i + 1);
            }
            smallXInterval /= connectionsPerSide + 1;
            smallYInterval /= connectionsPerSide + 1;

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
