package com.example.MotorolaScienceCup.Tempest;


import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Menu;
import com.example.MotorolaScienceCup.Util;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;
import java.util.List;

import static com.example.MotorolaScienceCup.Tempest.Main.base;
import static com.example.MotorolaScienceCup.Menu.WIDTH;
import static com.example.MotorolaScienceCup.Menu.HEIGHT;
import static com.example.MotorolaScienceCup.Menu.root;
import static com.example.MotorolaScienceCup.Tempest.Main.playingArea;

public class HUD {
    private final static double baseOffset = 20;
    public static void drawMap(String filepath, Color color){
        List<Double> points = Util.SVGconverter(filepath);

        base = new BetterPolygon();
        base.getPoints().setAll(points);
        base.setStroke(color);
        base.setFill(Color.TRANSPARENT);
        base.moveTo(WIDTH/2, HEIGHT/2 + baseOffset);
        root.getChildren().add(base);

        BetterPolygon tempPolygon = BetterPolygon.scale(base, 15);
        tempPolygon.moveTo(WIDTH/2, HEIGHT/2);
        points = tempPolygon.getPoints();

        playingArea = new ArrayList<>();

        for (int i = 0; i < points.size(); i+=2) {
            Polyline polyline = new Polyline();
            if(i+2 >= points.size()){
                polyline.getPoints().setAll(points.get(i), points.get(i+1), points.get(0), points.get(1));
            }
            else{
                polyline.getPoints().setAll(points.get(i), points.get(i+1), points.get(i+2), points.get(i+3));
            }
            polyline.setStroke(color);
            polyline.setFill(color);
            playingArea.add(polyline);
            root.getChildren().add(polyline);
        }
    }

    private void drawWeb(List<Double> smallPoints, List<Double> bigPoints){
        fori
    }
}
