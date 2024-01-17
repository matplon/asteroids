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
        int n = 16 - smallShapePoints.size() / 2;
        n /= (smallShapePoints.size() / 2);
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
            System.out.println("Smol: "+ smallShapePoints.get(i) + " " + smallShapePoints.get(i+ 1) + " Big: " + bigShapePoints.get(i)+" "+bigShapePoints.get(i+1));
            System.out.println("Small " + xDiffSmall + " "+ yDiffSmall + " Big: " + xDiffBig + " "+ yDiffBig+"\n");
            for (int j = 1; j <= n; j++) {
                double xSmall = smallShapePoints.get(i) + xDiffSmall * j;
                double ySmall = smallShapePoints.get(i + 1) + yDiffSmall * j;
                double xBig = bigShapePoints.get(i) + xDiffBig * j;
                double yBig = bigShapePoints.get(i + 1) + yDiffBig * j;
                Polyline polyline = new Polyline(xSmall, ySmall, xBig, yBig);
                polyline.setStroke(color);
                polyline.setFill(color);
                connectors.add(polyline);
                root.getChildren().add(polyline);
            }
        }
    }

}
