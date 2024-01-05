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

public class Menu extends Application {

    static Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    public  static int WIDTH = (int) screenBounds.getWidth();
    public static int HEIGHT = (int) screenBounds.getHeight();
    public static AnchorPane root;
    public static Stage stage = new Stage();
    public static Scene scene;
    @Override
    public void start(Stage stage1) throws Exception {
        root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        scene = new Scene(root, WIDTH,HEIGHT);
        stage = stage1;

        stage.setScene(scene);
        stage.show();
    }
}
