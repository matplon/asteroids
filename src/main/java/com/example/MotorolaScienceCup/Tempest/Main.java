package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Menu;
import com.example.MotorolaScienceCup.Util;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main {
    final static int WIDTH = Menu.WIDTH;
    final static int HEIGHT = Menu.HEIGHT;
    static Scene scene = Menu.scene;
    static AnchorPane root = Menu.root;
    static Stage stage = Menu.stage;
    static BetterPolygon base;
    static List<Polyline> bigShape;
    static List<Polyline> connectors;

    public static void init() {
        connectors = new ArrayList<>();
        Graphics.drawMap("mapa1.svg", Color.BLUE);
        double bigSideLength = Math.sqrt(Math.pow(bigShape.get(0).getPoints().get(2) - bigShape.get(0).getPoints().get(0), 2) +
                Math.pow(bigShape.get(0).getPoints().get(3) - bigShape.get(0).getPoints().get(1), 2));
        Player player = new Player(Util.SVGconverter("ship1.svg"), bigSideLength, 0, 7, 1);
        player.moveTo(bigShape.get(0).getPoints().get(0), bigShape.get(0).getPoints().get(1));
        player.setStroke(Color.RED);
        player.scale(20/player.getRadius());
        root.getChildren().add(player);
//        scene.setOnKeyPressed(keyEvent -> {
//            if (keyEvent.getCode() == KeyCode.RIGHT) player.move(false);   // Move right
//        });
    }

}
