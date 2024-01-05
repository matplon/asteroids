package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Menu;
import com.example.MotorolaScienceCup.Util;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.List;

public class Main {

    static Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    final static int WIDTH = Menu.WIDTH;
    final static int HEIGHT = Menu.HEIGHT;
    static Scene scene = Menu.scene;
    static AnchorPane root = Menu.root;
    static Stage stage = Menu.stage;
    static BetterPolygon base;
    static List<Polyline> playingArea;

    public static void init() {
        HUD.drawMap("testsquare.svg", Color.BLUE);
    }
}
