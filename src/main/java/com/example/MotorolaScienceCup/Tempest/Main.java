package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Menu;
import com.example.MotorolaScienceCup.Util;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main {

    static Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    final static int WIDTH = Menu.WIDTH;
    final static int HEIGHT = Menu.HEIGHT;
    static Scene scene = Menu.scene;
    static AnchorPane root = Menu.root;
    static Stage stage = Menu.stage;

    public static void init() {
    }

}
