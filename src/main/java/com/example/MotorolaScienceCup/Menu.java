package com.example.MotorolaScienceCup;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.sound.sampled.Clip;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Menu extends Application {

    static Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    public  static int WIDTH = (int) screenBounds.getWidth();
    public static int HEIGHT = (int) screenBounds.getHeight();
    public static final int FPS = 60;
    public static AnchorPane root;
    public static Stage stage;
    public static Scene scene;
    public static List<Clip> clips = new ArrayList();

    public static void resetMenu() throws IOException {
        root = FXMLLoader.load(Menu.class.getResource("Main.fxml"));
        scene = new Scene(root, WIDTH,HEIGHT);
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void start(Stage stage1) throws Exception {
        root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        System.out.println();
        scene = new Scene(root, WIDTH,HEIGHT);
        stage = stage1;
        stage1.setScene(scene);
        stage1.show();
        stage = stage1;
        Sound.loopPlay("8-bit-background-music-for-arcade-game-come-on-mario-164702.wav");

    }
}
