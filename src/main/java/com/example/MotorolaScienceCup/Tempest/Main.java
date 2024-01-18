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
    static Scene scene;
    static AnchorPane root;
    static Stage stage = Menu.stage;
    static List<Polyline> smallShape;
    static List<Polyline> bigShape;
    static List<Polyline> connectors;
    static List<Panel> panels;
    static String testMap1 = "testoctagon.svg";
    static String testMap2 = "testsquareKTORYDZIALA.svg";
    static String testMap3 = "mapa 3.svg";
    static String testShip = "ship1.svg";

    public static void init() {
        connectors = new ArrayList<>();
        bigShape = new ArrayList<>();
        smallShape = new ArrayList<>();
        panels = new ArrayList<>();
        root = new AnchorPane();
        scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);


        Graphics.drawMap(testMap2, Color.BLUE);

        Player player = new Player(Util.SVGconverter(testShip), panels.getFirst());
        player.scale(20/player.getRadius());
        player.setStroke(Color.RED);
        player.setFill(Color.RED);
        player.moveTo(panels.getFirst().getRightSide().getPoints().get(2), panels.getFirst().getRightSide().getPoints().get(3));
        root.getChildren().add(player);
        System.out.println(player.getCenterX()+" "+player.getCenterY());

        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.RIGHT) player.move(false);   // Rotate right
            if (keyEvent.getCode() == KeyCode.LEFT) player.move(true); // Rotate left
        });

    }

}
