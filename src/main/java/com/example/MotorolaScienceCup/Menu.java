package com.example.MotorolaScienceCup;

import com.example.MotorolaScienceCup.Tempest.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Menu extends Application {

    static Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    public  static int WIDTH = (int) screenBounds.getWidth();
    public static int HEIGHT = (int) screenBounds.getHeight();
    public static AnchorPane root;
    public static Stage stage;
    public static Scene scene;

    public static void resetMenu() throws IOException {
        root = FXMLLoader.load(Menu.class.getResource("Main.fxml"));
        scene = new Scene(root, WIDTH,HEIGHT);
        System.out.println(Util.SVGconverter("testsquare.svg") +"lol");
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void start(Stage stage1) throws Exception {
        root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        scene = new Scene(root, WIDTH,HEIGHT);
        System.out.println(Util.SVGconverter("testsquare.svg") +"lol");
        stage = stage1;

        stage1.setScene(scene);
        stage1.show();
        stage = stage1;
    }
}
